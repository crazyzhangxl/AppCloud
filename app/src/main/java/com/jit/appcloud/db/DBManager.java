package com.jit.appcloud.db;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.db_model.AgDeviceBean;
import com.jit.appcloud.db.db_model.AgDeviceDetailBean;
import com.jit.appcloud.db.db_model.AgPondBean;
import com.jit.appcloud.db.db_model.CityWeatherBean;
import com.jit.appcloud.db.db_model.FarmerNameBean;
import com.jit.appcloud.db.db_model.FeedMeal;
import com.jit.appcloud.db.db_model.Friend;
import com.jit.appcloud.db.db_model.GroupMember;
import com.jit.appcloud.db.db_model.Groups;
import com.jit.appcloud.db.db_model.MyPbNewsBean;
import com.jit.appcloud.db.db_model.PondBean;
import com.jit.appcloud.db.db_model.SeedMeal;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserOtherCache;
import com.jit.appcloud.model.response.FriendAddGetResponse;
import com.jit.appcloud.model.response.GroupInfoByIdResponse;
import com.jit.appcloud.model.response.GroupMbQyResponse;
import com.jit.appcloud.model.response.GroupsQyResponse;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.NetUtils;
import com.jit.appcloud.util.PinyinUtils;
import com.jit.appcloud.util.RongGenerate;
import com.jit.appcloud.util.UIUtils;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.model.UserInfo;

/**
 *
 * @author 张先磊
 * @date 2018/4/26
 */

public class DBManager {

    private static DBManager mInstance;
    private boolean mHasFetchedFriends = false;
    private boolean mHasFetchedGroups = false;
    private boolean mHasFetchedGroupMembers = false;
    private LinkedHashMap<String, UserInfo> mUserInfoCache;
    private List<Groups> mGroupsList;
    private DBManager(){
        mUserInfoCache = new LinkedHashMap<>();
    }
    public static DBManager getInstance(){
        if (mInstance == null){
            synchronized (DBManager.class){
                if (mInstance == null) {
                    mInstance = new DBManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 清除所有用户数据
     */
    public void deleteAllUserInfo(){
        DataSupport.deleteAll(FarmerNameBean.class);
        DataSupport.deleteAll(PondBean.class);
        DataSupport.deleteAll(CityWeatherBean.class);
        DataSupport.deleteAll(Friend.class);
        DataSupport.deleteAll(GroupMember.class);
        DataSupport.deleteAll(Groups.class);
        DataSupport.deleteAll(FeedMeal.class);
        DataSupport.deleteAll(SeedMeal.class);
        DataSupport.deleteAll(AgPondBean.class);
        DataSupport.deleteAll(AgDeviceBean.class);
        DataSupport.deleteAll(AgDeviceDetailBean.class);
        DataSupport.deleteAll(MyPbNewsBean.class);
    }

    /**
     * 登录时同步好友，群组，群组成员，黑名单数据
     */
    public void getAllUserInfo() {
        if (!NetUtils.isNetworkAvailable(UIUtils.getContext())) {
            return;
        }
        fetchFriends();
        fetchGroups();
    }

    /**
     * 同步朋友信息
     */
    private void fetchFriends() {
        mHasFetchedFriends = false;
        ApiRetrofit.getInstance().getAllUserRelationship(UserCache.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userRelationshipResponse -> {
                    if (userRelationshipResponse != null && userRelationshipResponse.getCode() == 1) {
                        List<FriendAddGetResponse.DataBean> list = userRelationshipResponse.getData();
                        if (list != null && list.size() > 0) {
                            LogUtils.e("好友","拉取好友中说明是有的");
                            deleteFriends();
                            saveFriends(list);
                        }
                        mHasFetchedFriends = true;
                        checkFetchComplete();
                    } else {
                        mHasFetchedFriends = true;
                        checkFetchComplete();
                    }
                }, this::fetchFriendError);
    }

    /**
     * 同步群组信息
     */
    private void fetchGroups() {
        mHasFetchedGroups = false;
        ApiRetrofit.getInstance().queryAllGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(getGroupResponse -> {
                    if (getGroupResponse != null && getGroupResponse.getCode() == 1) {
                        List<GroupsQyResponse.DataBean> list = getGroupResponse.getData();
                        if (list != null && list.size() > 0) {
                            DBManager.this.deleteGroups();
                            saveGroups(list);
                            //同步群组成员信息
                            DBManager.this.fetchGroupMembers();
                        } else {
                            mHasFetchedGroupMembers = true;
                        }
                        mHasFetchedGroups = true;
                        DBManager.this.checkFetchComplete();
                    } else {
                        mHasFetchedGroups = true;
                        mHasFetchedGroupMembers = true;
                        DBManager.this.checkFetchComplete();
                    }
                }, DBManager.this::fetchGroupsError);
    }

    private synchronized void saveGroups(List<GroupsQyResponse.DataBean> list) {
        if (list != null && list.size() > 0) {
            mGroupsList = new ArrayList<>();
            for (GroupsQyResponse.DataBean groups : list) {
                String portrait = groups.getImage();
                if (TextUtils.isEmpty(portrait)) {
                    portrait = RongGenerate.generateDefaultAvatar(groups.getGroupname(), String.valueOf(groups.getId()));
                }
                LogUtils.e("群组同步",groups.getGroupname() +"  "+groups.getId());
                mGroupsList.add(new Groups(String.valueOf(groups.getId()),
                        groups.getGroupname(),
                        portrait,
                        String.valueOf(groups.getAdmin())));
            }
        }
        if (mGroupsList.size() > 0) {
            Log.e("群组同步",mGroupsList.size()+" ");
            DataSupport.saveAll(mGroupsList);
        }
    }

    /**
     * 同步群组成员信息
     */
    private synchronized void fetchGroupMembers() {
        mHasFetchedGroupMembers = false;
        Observable.fromArray(getGroups().toArray())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(o -> {
                    Groups group = (Groups)o;
                    ApiRetrofit.getInstance().queryGpMbById(Integer.parseInt(group.getGroupId()))
                            .subscribe(getGroupMemberResponse -> {
                                if (getGroupMemberResponse != null && getGroupMemberResponse.getCode() == 1) {
                                    List<GroupMbQyResponse.DataBean> list = getGroupMemberResponse.getData();
                                    if (list != null && list.size() > 0) {
                                        DBManager.this.deleteGroupMembersByGroupId(group.getGroupId());
                                        DBManager.this.saveGroupMembers(list, group.getGroupId());
                                    }
                                    mHasFetchedGroupMembers = true;
                                    DBManager.this.checkFetchComplete();
                                } else {
                                    mHasFetchedGroupMembers = true;
                                    DBManager.this.checkFetchComplete();
                                }
                            }, DBManager.this::fetchGroupMembersError);
                });
    }


    /**
     * 保存GroupID 对应的GroupMembers成员们
     * @param list
     * @param groupId
     */
    public   synchronized void saveGroupMembers(List<GroupMbQyResponse.DataBean> list, String groupId) {
        if (list != null && list.size() > 0) {
            List<GroupMember> groupMembers = setCreatedToTop(list, groupId);
            if (groupMembers != null && groupMembers.size() > 0) {
                for (GroupMember groupMember : groupMembers) {
                    if (groupMember != null && TextUtils.isEmpty(groupMember.getPortraitUri())) {
                        String portrait =  RongGenerate.generateDefaultAvatar(groupMember.getName(),groupMember.getUserId());
                        groupMember.setPortraitUri(portrait);
                    }
                }
                if (groupMembers.size() > 0) {
                    for (GroupMember groupMember : groupMembers) {
                        saveOrUpdateGroupMember(groupMember);
                    }
                }
            }
        }
    }

    private synchronized  List<GroupMember> setCreatedToTop(List<GroupMbQyResponse.DataBean> list, String groupId) {
        List<GroupMember> newList = new ArrayList<>();
        GroupMember created = null;
        for (GroupMbQyResponse.DataBean group : list) {
            String groupName = null;
            String groupPortraitUri = null;
            Groups groups = getGroupsById(groupId);
            if (groups != null) {
                groupName = groups.getName();
                groupPortraitUri = groups.getPortraitUri();
            }
            GroupMember newMember = new GroupMember(groupId,
                    String.valueOf(group.getUser_id()),
                    group.getUsername(),
                    group.getImage(),
                    group.getNickname(),
                    groupName,
                    groupPortraitUri,
                    group.getIs_show() != 0);
            if (group.getAdmin() == 1) {
                created = newMember;
            } else {
                newList.add(newMember);
            }
        }
        if (created != null) {
            newList.add(created);
        }
        // 哈哈 这样创建者就在最上面了
        Collections.reverse(newList);
        return newList;
    }


    private synchronized void fetchFriendError(Throwable throwable) {
        mHasFetchedFriends = true;
        checkFetchComplete();
    }

    private synchronized void fetchGroupsError(Throwable throwable) {
        LogUtils.e("fetchGroupsError",throwable.getLocalizedMessage());
        mHasFetchedGroups = true;
        mHasFetchedGroupMembers = true;
        checkFetchComplete();
    }

    private void fetchGroupMembersError(Throwable throwable) {
        LogUtils.e("fetchGroupMembersError",throwable.getLocalizedMessage());
        mHasFetchedGroupMembers = true;
        checkFetchComplete();
    }

    private void checkFetchComplete() {
        if (mHasFetchedFriends && mHasFetchedGroups && mHasFetchedGroupMembers) {
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.FETCH_COMPLETE);
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_FRIEND);
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_GROUP);
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
            LogUtils.e("好友+群成员","==================================拉去结束啦！！！！！！");
        }
    }

    /**
     * 缺少通过群id查询群信息 =======
     * @param groupId
     */
    public void getGroups(String groupId) {
        if (!mHasFetchedGroups) {
            fetchGroups();
        } else {
            ApiRetrofit.getInstance().queryGpInfoById(Integer.parseInt(groupId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(groupInfoByIdResponse -> {
                        if (groupInfoByIdResponse != null && groupInfoByIdResponse.getCode() == 1){
                            GroupInfoByIdResponse.DataBean data = groupInfoByIdResponse.getData();
                            if (data != null) {
                                String role = data.getCreateId() == Integer.parseInt(UserCache.getId()) ? "1" : "0";
                                saveOrUpdateGroup(new Groups(groupId, data.getGroupname(), data.getImage(), role));
                            }
                        }

                    }, throwable -> LogUtils.e("失败",throwable.getLocalizedMessage()));
        }
    }


    public void getGroupMember(String groupId) {
        if (!mHasFetchedGroupMembers) {
            deleteGroupMembers();
            mGroupsList = getGroups();
            fetchGroupMembers();
        } else {
            ApiRetrofit.getInstance().queryGpMbById(Integer.parseInt(groupId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(getGroupMemberResponse -> {
                        if (getGroupMemberResponse != null && getGroupMemberResponse.getCode() == 1) {
                            List<GroupMbQyResponse.DataBean> list = getGroupMemberResponse.getData();
                            if (list != null && list.size() > 0) {
                                deleteGroupMembersByGroupId(groupId);
                                saveGroupMembers(list, groupId);
                                BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_GROUP_MEMBER, groupId);
                                BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                            }
                        }
                    }, this::loadError);
        }
    }
    public synchronized GroupMember getGroupMemberById(String groupID,String userID){
        if (!TextUtils.isEmpty(userID)) {
            List<GroupMember> groupMembers = DataSupport.where("groupid = ? and userid = ?", groupID,userID).find(GroupMember.class);
            if (groupMembers != null && groupMembers.size() > 0) {
                return groupMembers.get(0);
            }
        }
        return null;
    }

    public  synchronized List<GroupMember> queryGroupMembers(String groupID){
        return DataSupport.where("groupid = ?",groupID).find(GroupMember.class);
    }


    private void loadError(Throwable throwable) {
        LogUtils.e("数据库错误",throwable.getLocalizedMessage());
    }

    /**
     * 查询本地用户信息
     * 1、查缓存
     * 2、查Friend表
     * 3、查GroupMember表
     *
     * @param userid
     * @return
     */
    public UserInfo getUserInfo(String userid) {
        if (TextUtils.isEmpty(userid)) {
            return null;
        }

        if (mUserInfoCache != null) {
            UserInfo userInfo = mUserInfoCache.get(userid);
            if (userInfo != null) {
                return userInfo;
            }
        }

        UserInfo userInfo;
        Friend friend = getFriendById(userid);
        if (friend != null) {
            String name = friend.getName();
            if (friend.isExitsDisplayName()) {
                name = friend.getDisplayName();
            }
            userInfo = new UserInfo(friend.getUserId(), name, Uri.parse(friend.getPortraitUri()));
            return userInfo;
        }

        List<GroupMember> groupMembers = getGroupMembersWithUserId(userid);
        if (groupMembers != null && groupMembers.size() > 0) {
            GroupMember groupMember = groupMembers.get(0);
            userInfo = new UserInfo(groupMember.getUserId(), groupMember.getName(), Uri.parse(groupMember.getPortraitUri()));
            return userInfo;
        }

        return null;
    }


    public boolean isMyFriend(String userid) {
        Friend friend = getFriendById(userid);
        if (friend != null) {
            return true;
        }
        return false;
    }

    public boolean isMe(String userid) {
        if (UserCache.getId().equalsIgnoreCase(userid)) {
            return true;
        }
        return false;
    }

    public boolean isInThisGroup(String groupId) {
        Groups groups = getGroupsById(groupId);
        return groups != null;
    }
    /*================== Friend begin ==================*/

    public synchronized void saveOrUpdateFriend(Friend friend) {
        if (friend != null) {
            String portrait = friend.getPortraitUri();
            if (TextUtils.isEmpty(portrait)) {
                portrait = RongGenerate.generateDefaultAvatar(friend.getName(), friend.getUserId());
                friend.setPortraitUri(portrait);
            }
            friend.saveOrUpdate("userid = ?", friend.getUserId());
            //更新过本地好友数据后，清空内存中对应用户信息缓存
            if (mUserInfoCache != null && mUserInfoCache.containsKey(friend.getUserId())) {
                mUserInfoCache.remove(friend.getUserId());
                LogUtils.e("纠正","未执行");
            }
            LogUtils.e("纠正","save成功了啊");
        }
    }

    public synchronized void deleteFriend(Friend friend) {
        DataSupport.deleteAll(Friend.class, "userid = ?", friend.getUserId());
    }

    public synchronized String getFriendNameByID(String id){
        if (id == null) {
            return null;
        }
        List<Friend> friendList = DataSupport.where("userid = ?", id).find(Friend.class);
        if (friendList !=null && friendList.size()!=0) {
            return friendList.get(0).getName();
        }
        return null;
    }

    public synchronized Friend getFriendById(String userid) {
        if (!TextUtils.isEmpty(userid)) {
            List<Friend> friends = DataSupport.where("userid = ?", userid).find(Friend.class);
            if (friends != null && friends.size() > 0) {
                return friends.get(0);
            }
        }
        return null;
    }

    public synchronized String getFriendDisplayName(String userid){
        if (!TextUtils.isEmpty(userid)) {
            List<Friend> friends = DataSupport.where("userid = ?", userid).find(Friend.class);
            if (friends != null && friends.size() > 0) {
                return friends.get(0).getDisplayName();
            }
        }
        return null;
    }

    // 返回朋友不包括自己
    public synchronized List<Friend> getFriends() {
        return DataSupport.where("userid != ?", UserCache.getId()).find(Friend.class);
    }


    /* ------------ 保存所有的好友信息(已经是好友了)-------------- 进行转换*/

    public synchronized void saveFriends(List<FriendAddGetResponse.DataBean> list) {
        LogUtils.e("好友+","1");

        List<Friend> friends = new ArrayList<>();
        for (FriendAddGetResponse.DataBean entity : list) {
            if (entity.getStatus() == 1) {
                Friend friend = new Friend(
                        String.valueOf(entity.getId()),
                        entity.getFriendname(),
                        entity.getImage() == null ? "":entity.getImage(),
                        TextUtils.isEmpty(entity.getNickname()) ? entity.getNickname() : entity.getFriendname(),
                        null, null, null, null,
                        PinyinUtils.getPinyin(entity.getFriendname()),
                        PinyinUtils.getPinyin(TextUtils.isEmpty(entity.getNickname()) ? entity.getNickname() : entity.getFriendname())
                );
                if (TextUtils.isEmpty(friend.getPortraitUri())) {
                    friend.setPortraitUri(getPortrait(friend));
                }
                LogUtils.e("ID","好友姓名 = "+friend.getName() +"  ID = "+friend.getUserId());
                friends.add(friend);
            }
        }
        if (friends != null && friends.size() > 0) {
            DataSupport.saveAll(friends);
        }
        LogUtils.e("好友","保存了的啊!!");
    }

    public synchronized void deleteFriends() {
        List<Friend> friends = getFriends();
        for (Friend friend : friends) {
            friend.delete();
        }
    }

    public synchronized void deleteFriendById(String friendId) {
        DataSupport.deleteAll(Friend.class, "userid = ?", friendId);
    }

    /*==================Friend end ==================*/

    /*================== Groups start ==================*/

    /**
     * 保存或者更新群组
     * @param groups
     */
    public synchronized void saveOrUpdateGroup(Groups groups) {
        if (groups != null) {
            String portrait = groups.getPortraitUri();
            if (TextUtils.isEmpty(portrait)) {
                portrait = RongGenerate.generateDefaultAvatar(groups.getName(), groups.getGroupId());
                groups.setPortraitUri(portrait);
            }
            groups.saveOrUpdate("groupid = ?", groups.getGroupId());
        }
    }

    public synchronized void deleteGroup(Groups groups) {
        DataSupport.deleteAll(Groups.class, "groupid = ?", groups.getGroupId());
    }

    /**
     * 通过群聊号查询群聊的细节信息
     * @param groupId 群聊的ID
     * @return
     */
    public synchronized Groups getGroupsById(String groupId) {
        if (!TextUtils.isEmpty(groupId)) {
            List<Groups> groupses = DataSupport.where("groupid = ?", groupId).find(Groups.class);
            if (groupses != null && groupses.size() > 0) {
                return groupses.get(0);
            }
        }
        return null;
    }

    public synchronized List<Groups> getGroups() {
        return DataSupport.findAll(Groups.class);
    }




    /**
     * 同步之前 - 删除掉所有的群组
     */
    public synchronized void deleteGroups() {
        DataSupport.deleteAll(Groups.class);
    }

    public synchronized void deleteGroupsById(String groupId) {
        DataSupport.deleteAll(Groups.class, "groupid = ?", groupId);
    }

    /*================== Groups end ==================*/
    /*================== GroupMember start ==================*/
    public synchronized void saveOrUpdateGroupMember(GroupMember groupMember) {
        if (groupMember != null) {
            String portrait = groupMember.getPortraitUri();
            if (TextUtils.isEmpty(portrait)) {
                portrait = RongGenerate.generateDefaultAvatar(groupMember.getName(), groupMember.getUserId());
                groupMember.setPortraitUri(portrait);
            }
            groupMember.saveOrUpdate("groupid = ? and userid = ?", groupMember.getGroupId(), groupMember.getUserId());
        }
    }

    public synchronized void updateGroupMemberPortraitUri(String userId, String portraitUri) {
        if (TextUtils.isEmpty(portraitUri)) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put("portraituri", portraitUri);
        DataSupport.updateAll(GroupMember.class, values, "userid = ?", userId);
    }

    public synchronized List<GroupMember> getGroupMembers(String groupId) {
        return DataSupport.where("groupid = ?", groupId).find(GroupMember.class);
    }

    public synchronized List<GroupMember> getGroupMembersWithUserId(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        return DataSupport.where("userid = ?", userId).find(GroupMember.class);
    }



    public synchronized void updateGroupsName(String groupId, String groupName) {
        Groups groups = getGroupsById(groupId);
        if (groups != null) {
            groups.setName(groupName);
            saveOrUpdateGroup(groups);
        }
    }

    public synchronized void deleteGroupMembers() {
        DataSupport.deleteAll(GroupMember.class);
    }

    public synchronized void deleteGroupMembers(String groupId, List<String> kickedUserIds) {
        if (kickedUserIds != null && kickedUserIds.size() > 0) {
            for (String userId : kickedUserIds) {
                DataSupport.deleteAll(GroupMember.class, "groupid = ? and userid = ?", groupId, userId);
            }
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_GROUP_MEMBER, groupId);
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.REFRESH_CURRENT_SESSION);
        }
    }

    public synchronized void deleteGroupMembersByGroupId(String groupId) {
        DataSupport.deleteAll(GroupMember.class, "groupid = ?", groupId);
    }


    /*================== GroupMember end ==================*/

    /**
     * app中获取用户头像的接口
     * 这个方法不涉及读数据库,头像空时直接生成默认头像
     */
    public String getPortraitUri(UserInfo userInfo) {
        if (userInfo != null) {
            if (userInfo.getPortraitUri() != null) {
                if (TextUtils.isEmpty(userInfo.getPortraitUri().toString())) {
                    if (userInfo.getName() != null) {
                        return RongGenerate.generateDefaultAvatar(userInfo);
                    } else {
                        return null;
                    }
                } else {
                    return userInfo.getPortraitUri().toString();
                }
            } else {
                if (userInfo.getName() != null) {
                    return RongGenerate.generateDefaultAvatar(userInfo);
                } else {
                    return null;
                }
            }

        }
        return null;
    }

    public String getPortraitUri(String name, String userId) {
        return RongGenerate.generateDefaultAvatar(name, userId);
    }


    /**
     * 获取用户头像,头像为空时会生成默认的头像,此默认头像可能已经存在数据库中,不重新生成
     * 先从缓存读,再从数据库读
     */
    private String getPortrait(Friend friend) {
        if (friend != null) {
            if (TextUtils.isEmpty(friend.getPortraitUri().toString())) {
                if (TextUtils.isEmpty(friend.getUserId())) {
                    return null;
                } else {
                    UserInfo userInfo = mUserInfoCache.get(friend.getUserId());
                    if (userInfo != null) {
                        if (!TextUtils.isEmpty(userInfo.getPortraitUri().toString())) {
                            return userInfo.getPortraitUri().toString();
                        } else {
                            mUserInfoCache.remove(friend.getUserId());
                        }
                    }
                    String portrait = RongGenerate.generateDefaultAvatar(friend.getName(), friend.getUserId());
                    //缓存信息kit会使用,备注名存在时需要缓存displayName
                    String name = friend.getName();
                    if (friend.isExitsDisplayName()) {
                        name = friend.getDisplayName();
                    }
                    userInfo = new UserInfo(friend.getUserId(), name, Uri.parse(portrait));
                    mUserInfoCache.put(friend.getUserId(), userInfo);
                    return portrait;
                }
            } else {
                return friend.getPortraitUri().toString();
            }
        }
        return null;
    }

    private String getPortrait(GroupMember groupMember) {
        if (groupMember != null) {
            if (TextUtils.isEmpty(groupMember.getPortraitUri().toString())) {
                if (TextUtils.isEmpty(groupMember.getUserId())) {
                    return null;
                } else {
                    UserInfo userInfo = mUserInfoCache.get(groupMember.getUserId());
                    if (userInfo != null) {
                        if (!TextUtils.isEmpty(userInfo.getPortraitUri().toString())) {
                            return userInfo.getPortraitUri().toString();
                        } else {
                            mUserInfoCache.remove(groupMember.getUserId());
                        }
                    }
                    Friend friend = getFriendById(groupMember.getUserId());
                    if (friend != null) {
                        if (!TextUtils.isEmpty(friend.getPortraitUri().toString())) {
                            return friend.getPortraitUri().toString();
                        }
                    }
                    List<GroupMember> groupMemberList = getGroupMembersWithUserId(groupMember.getUserId());
                    if (groupMemberList != null && groupMemberList.size() > 0) {
                        GroupMember member = groupMemberList.get(0);
                        if (!TextUtils.isEmpty(member.getPortraitUri().toString())) {
                            return member.getPortraitUri().toString();
                        }
                    }
                    String portrait = RongGenerate.generateDefaultAvatar(groupMember.getName(), groupMember.getUserId());
                    userInfo = new UserInfo(groupMember.getUserId(), groupMember.getName(), Uri.parse(portrait));
                    mUserInfoCache.put(groupMember.getUserId(), userInfo);
                    return portrait;
                }
            } else {
                return groupMember.getPortraitUri().toString();
            }
        }
        return null;
    }






    /*  =========  城市相关的 开始 ================= */
    public synchronized List<CityWeatherBean> queryAllCity(){
        return DataSupport.findAll(CityWeatherBean.class);

    }

    public synchronized void saveOrUpdateCity(CityWeatherBean cityWeatherBean){
        if (cityWeatherBean != null) {
            cityWeatherBean.saveOrUpdate("cityid = ?", cityWeatherBean.getCityId());
        }
    }

    public synchronized void deleteCityById(String id){
        DataSupport.deleteAll(CityWeatherBean.class,"cityid = ?",id);

    }
     /* ========== 城市相关的 结束 =================== */



     /* =========  养殖户姓名开始 开始 =================*/
     public synchronized List<String> queryFarmName(){
         List<FarmerNameBean> nameBeans = DataSupport.findAll(FarmerNameBean.class);
         List<String> mResult = new ArrayList<>();
         for (FarmerNameBean bean:nameBeans){
             mResult.add(bean.getFarmName());
         }
         return mResult;
     }

     public synchronized void saveOrUpdateName(FarmerNameBean farmerNameBean){
         if (farmerNameBean != null){
             farmerNameBean.saveOrUpdate("farmname = ?",farmerNameBean.getFarmName());
         }
     }

     public synchronized void deleleFarmNameByName(String name){
         DataSupport.deleteAll(FarmerNameBean.class,"farmname = ?",name);
     }

     /* ========== 养殖户姓名 结束 ===================*/

     /* ==========   塘口信息开始  ================*/

     public synchronized List<String> queryPondName(){
         List<PondBean> pondBeanList = DataSupport.findAll(PondBean.class);
         List<String> mResultList = new ArrayList<>();
         for (PondBean bean:pondBeanList){
             mResultList.add(bean.getPondName());
         }
         return mResultList;
     }

    public synchronized List<PondBean> queryAllPond(){

        return DataSupport.findAll(PondBean.class);
    }

     public synchronized int queryPondIdByName(String name){
         List<PondBean> pondBeanList = DataSupport.where("pondname = ?", name).find(PondBean.class);
         if (pondBeanList !=null && pondBeanList.size()!=0) {
             return pondBeanList.get(0).getPondId();
         }
         return -1;
     }

    public synchronized String queryPondNameByID(int pondId){
        List<PondBean> pondBeanList = DataSupport.where("pondid = ?", String.valueOf(pondId)).find(PondBean.class);
        if (pondBeanList !=null && pondBeanList.size()!=0) {
            return pondBeanList.get(0).getPondName();
        }
        return null;
    }

     public synchronized void savePond(PondBean pondBean){
         if (pondBean != null){
             pondBean.save();
         }
     }

    public synchronized void deletePondById(String id){
        DataSupport.deleteAll(PondBean.class,"pondid = ?",id);

    }

     public synchronized void deleteAllPond(){
         DataSupport.deleteAll(PondBean.class);
     }

     /* ==========   塘口信息结束  ================*/


     /* ==================  投放餐数  ============= */
     /*保存套餐*/
    public synchronized void saveFeedMeal(FeedMeal feedMeal){
        if (feedMeal != null){
            feedMeal.save();
        }
    }

    /*  查询全部套餐*/
    public synchronized List<FeedMeal> queryFeedMeal(){
        List<FeedMeal> meal = DataSupport.findAll(FeedMeal.class);
        return meal;
    }

    /* 通过套餐名删除*/
    public synchronized void deleteFeedMealByName(String mealName){
        DataSupport.deleteAll(FeedMeal.class,"mealname = ?",mealName);
    }

    public synchronized void saveOrUpdateFeedMeal(FeedMeal feedMeal){
        if (feedMeal != null) {
            feedMeal.saveOrUpdate("mealname = ?", feedMeal.getMealName());
        }
    }

    public synchronized void saveOrUpdateFeedMeal(FeedMeal feedMeal,String mOriginName){
        if (feedMeal != null) {
            feedMeal.saveOrUpdate("mealname = ?",mOriginName);
        }
    }

    public synchronized FeedMeal getFeedMealByName(String name) {
        if (!TextUtils.isEmpty(name)) {
            List<FeedMeal> meals = DataSupport.where("mealname = ?", name).find(FeedMeal.class);
            if (meals != null && meals.size() > 0) {
                return meals.get(0);
            }
        }
        return null;
    }

    public boolean isHaveMeal(String meal) {
        FeedMeal feedMeal = getFeedMealByName(meal);
        if (feedMeal != null) {
            return true;
        }
        return false;
    }

    public boolean isHaveMeal(String meal,String mealName) {
        FeedMeal feedMeal = getFeedMealByName(meal);
        if (feedMeal != null && !feedMeal.getMealName().equals(mealName)) {
            return true;
        }
        return false;
    }


    /*保存套餐*/
    public synchronized void saveSeedMeal(SeedMeal seedMeal){
        if (seedMeal != null){
            seedMeal.save();
        }
    }

    /*  查询全部套餐*/
    public synchronized List<SeedMeal> querySeedMeal(){
        List<SeedMeal> meal = DataSupport.findAll(SeedMeal.class);
        return meal;
    }

    /* 通过套餐名删除*/
    public synchronized void deleteSeedMealByName(String mealName){
        DataSupport.deleteAll(SeedMeal.class,"mealname = ?",mealName);
    }

    public synchronized void saveOrUpdateSeedMeal(SeedMeal seedMeal){
        if (seedMeal != null) {
            seedMeal.saveOrUpdate("mealname = ?", seedMeal.getMealName());
        }
    }

    public synchronized void saveOrUpdateSeedMeal(SeedMeal seedMeal,String mOriginName){
        if (seedMeal != null) {
            seedMeal.saveOrUpdate("mealname = ?",mOriginName);
        }
    }

    public synchronized SeedMeal getSeedMealByName(String name) {
        if (!TextUtils.isEmpty(name)) {
            List<SeedMeal> meals = DataSupport.where("mealname = ?", name).find(SeedMeal.class);
            if (meals != null && meals.size() > 0) {
                return meals.get(0);
            }
        }
        return null;
    }

    public boolean isHaveSeedMeal(String meal) {
        SeedMeal feedMeal = getSeedMealByName(meal);
        if (feedMeal != null) {
            return true;
        }
        return false;
    }

    public boolean isHaveSeedMeal(String meal,String mealName) {
        SeedMeal seedMeal = getSeedMealByName(meal);
        if (seedMeal != null && !seedMeal.getMealName().equals(mealName)) {
            return true;
        }
        return false;
    }

      /* ==================  投放餐数  ============= */


      /* ================== 经销商塘口开始 =============== */
      public synchronized void saveAgPond (AgPondBean agPondBean){
          if (agPondBean != null){
              agPondBean.save();
          }
      }

      public synchronized void updateAgPond(AgPondBean agPondBean,int agId){
          if (agPondBean != null){
              agPondBean.update(agId);
          }
      }


    public synchronized List<AgPondBean> queryAllAgPond(){
        return DataSupport.findAll(AgPondBean.class);
    }

    /* 删除对应塘口的id*/
    public synchronized void deleteAgPond(int agId){
        DataSupport.delete(AgPondBean.class,agId);
    }

    public synchronized void clearAllAgPond(){
        DataSupport.deleteAll(AgPondBean.class);
    }

    public synchronized List<AgPondBean> queryAllAgPondsId(){
        return DataSupport.findAll(AgPondBean.class);
    }

      /* ================== 经销商塘口结束 =============== */


    /* ================== 经销商设备开始 =============== */
    public synchronized void saveAgDevice (AgDeviceBean agDeviceBean){
        if (agDeviceBean != null){
            agDeviceBean.save();
        }
    }

    public synchronized void updateAgDevice(AgDeviceBean agDeviceBean,int agId){
        if (agDeviceBean != null){
            agDeviceBean.update(agId);
        }
    }

    public synchronized List<AgDeviceBean> queryAllAgDevice(){
        return DataSupport.findAll(AgDeviceBean.class);
    }


    /* 删除对应塘口的id*/
    public synchronized void deleteAgDevice(int agId){
        DataSupport.delete(AgDeviceBean.class,agId);
    }

    public synchronized void clearAllAgDevice(){
        DataSupport.deleteAll(AgDeviceBean.class);
    }

    /* ================== 经销商设备结束 =============== */


    /* ================== 经销商设备详细开始 =============== */

    public synchronized void saveAgDeviceDetail (AgDeviceDetailBean agDeviceBean){
        if (agDeviceBean != null){
            agDeviceBean.save();
        }
    }

    public synchronized void updateAgDeviceDetail(AgDeviceDetailBean agDeviceBean,int agId){
        if (agDeviceBean != null){
            agDeviceBean.update(agId);
        }
    }

    public synchronized void deleteDvDtByDeviceId(String deviceId){
        DataSupport.deleteAll(AgDeviceDetailBean.class,"deviceid = ?",deviceId);
    }

    /* 删除对应塘口的id*/
    public synchronized void deleteAgDvDtId(int agId){
        DataSupport.delete(AgDeviceDetailBean.class,agId);
    }


    public synchronized List<AgDeviceDetailBean> queryDvDtByDeviceId(String deviceId) {
        if (!TextUtils.isEmpty(deviceId)) {
            return DataSupport.where("deviceid = ?", deviceId).find(AgDeviceDetailBean.class);
        }
        return null;
    }

    public synchronized void clearAllAgDeviceDt(){
        DataSupport.deleteAll(AgDeviceDetailBean.class);
    }
    /* ================== 经销商设备详细结束 =============== */


    /* 持久化 ------------------------------ 我的发布 */

    public synchronized void saveNewsMyPb (MyPbNewsBean  newsBean){
        // 将 id赋值给imageID,不然id会被覆盖的!
        if (newsBean != null){
            newsBean.save();
        }
    }


    public synchronized void saveOrUpdateNewsPb(MyPbNewsBean  newsBean){
        if (newsBean != null){
            newsBean.saveOrUpdate("newsid = ?",String.valueOf(newsBean.getNewsId()));
        }
    }


    public synchronized List<MyPbNewsBean> queryAllNewsPb(){
        return DataSupport.findAll(MyPbNewsBean.class);
    }


    public synchronized void deleteNewsPbByNewsId(int newsId){
        LogUtils.e("删除",String.valueOf(newsId));
        int i = DataSupport.deleteAll(MyPbNewsBean.class, "newsid = ?", String.valueOf(newsId));
        LogUtils.e("删除","这里执行了"+i);
    }

    /* 这是一个很耗时的操作 ---- 得放子线程中吧*/
    public synchronized List<MyPbNewsBean> queryAllNewsPbSorted(){
        String[] newsNumbers = UserOtherCache.getNewsPbSort().split(",");
        List<MyPbNewsBean> mListNews = new ArrayList<>();
        for (String newsId:newsNumbers){
            List<MyPbNewsBean> newsAll = DataSupport.where("newsid = ?",newsId).find(MyPbNewsBean.class);
            mListNews.add(newsAll.get(0));
        }
        return mListNews;
    }


    public synchronized List<MyPbNewsBean> queryAllNewsNormal(){
        List<MyPbNewsBean> myPbNewsBeans = DataSupport.findAll(MyPbNewsBean.class);
        for (MyPbNewsBean myPbNewsBean : myPbNewsBeans){
            LogUtils.e("删除-排查"," "+myPbNewsBean.getNewsId());
        }
        return DataSupport.findAll(MyPbNewsBean.class);
    }

    public synchronized List<String> queryOriginStrList(){
        List<String> mOriginList = new ArrayList<>();
        List<MyPbNewsBean> beans = DataSupport.findAll(MyPbNewsBean.class);
        for (MyPbNewsBean  bean:beans){
            mOriginList.add(String.valueOf(bean.getNewsId()));
        }
        return mOriginList;
    }
}

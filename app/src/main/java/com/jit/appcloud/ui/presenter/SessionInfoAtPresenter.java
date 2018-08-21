package com.jit.appcloud.ui.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.transition.Scene;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.GroupMember;
import com.jit.appcloud.db.db_model.Groups;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.manager.JsonMananger;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.message.GroupNotificationMessageData;
import com.jit.appcloud.model.response.NormalResponse;
import com.jit.appcloud.ui.activity.message.CreateGroupActivity;
import com.jit.appcloud.ui.activity.message.RemoveGpMemberActivity;
import com.jit.appcloud.ui.activity.message.SessionInfoActivity;
import com.jit.appcloud.ui.activity.message.UserInfoBySearchActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.ISessionInfoAtView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.PinyinUtils;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.GroupNotificationMessage;

import static com.jit.appcloud.commom.AppConst.EXTRA_FRIEND_INFO;
import static com.jit.appcloud.commom.AppConst.SESSION_TYPE_GROUP;
import static com.jit.appcloud.commom.AppConst.SESSION_TYPE_PRIVATE;

/**
 * @author zxl on 2018/7/24.
 *         discription: 用户会话用户信息的P层
 *         包括 由单聊转入 也包括由群聊转入。
 */

public class SessionInfoAtPresenter extends BasePresenter<ISessionInfoAtView> {
    private Conversation.ConversationType mConversationType;
    private String mSessionId;
    private List<GroupMember> mData = new ArrayList<>();
    private BaseQuickAdapter<GroupMember,BaseViewHolder> mAdapter;
    private boolean mIsManager = false;
    public boolean mIsCreateNewGroup = false;
    public String mDisplayName = "";
    private CustomDialog mSetDisplayNameDialog;
    private Groups mGroups;
    private Observable<NormalResponse> quitGroupResponseObservable = null;
    private int mAddWithMinusNum = 2;
    private int mAddOnlyNum = 1;
    private GroupMember mGroupMember;

    public SessionInfoAtPresenter(BaseActivity context, String sessionId, Conversation.ConversationType conversationType) {
        super(context);
        // 获得当前用户的 sessionId和聊天的类型
        mSessionId = sessionId;
        mConversationType = conversationType;
    }

    public void loadMembers() {
        loadData();
        setAdapter();
    }

    private void setAdapter() {
        if (mAdapter == null){
            getView().getRvMember().setLayoutManager(new GridLayoutManager(mContext,5,LinearLayoutManager.VERTICAL,false));
            mAdapter = new BaseQuickAdapter<GroupMember, BaseViewHolder>(R.layout.item_member_info,mData) {
                @Override
                protected void convert(BaseViewHolder helper, GroupMember item) {
                    ImageView ivHeader = helper.getView(R.id.ivHeader);
                    if (mIsManager && helper.getAdapterPosition() >= mData.size() -mAddWithMinusNum){
                        if (helper.getAdapterPosition() == mData.size() - mAddWithMinusNum){
                            ivHeader.setImageResource(R.mipmap.ic_remove_team_member);
                        }else {
                            ivHeader.setImageResource(R.mipmap.ic_add_team_member);
                        }
                    }else if (!mIsManager && helper.getAdapterPosition() >= mData.size() - mAddOnlyNum){
                        ivHeader.setImageResource(R.mipmap.ic_add_team_member);
                    }else {
                        Glide.with(mContext).load(item.getPortraitUri()).apply(new RequestOptions().placeholder(R.mipmap.default_header)).into(ivHeader);
                        if (mConversationType == Conversation.ConversationType.PRIVATE) {
                            helper.setText(R.id.tvName, item.getName());
                        }else {
                            helper.setText(R.id.tvName, item.getDisplayName());
                        }
                    }
                }
            };

            /*
             * 条目点击事件
             */
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                if (mIsManager && position >= (mData.size() - mAddWithMinusNum)){
                    if (position == (mData.size() - mAddWithMinusNum)){
                        removeMember();
                    }else {
                        addMember(mConversationType == Conversation.ConversationType.GROUP);
                    }
                }else if (!mIsManager && position >= (mData.size() - mAddOnlyNum)) {//+
                    addMember(mConversationType == Conversation.ConversationType.GROUP);
                } else {
                    seeUserInfo(DBManager.getInstance().getUserInfo(mData.get(position).getUserId()));
                }
            });
            getView().getRvMember().setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 查看用户信息
     * @param userInfo
     */
    private void seeUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            Intent intent = new Intent(mContext, UserInfoBySearchActivity.class);
            intent.putExtra(EXTRA_FRIEND_INFO, userInfo);
             mContext.jumpToActivity(intent);
        }
    }

    /**
     * 添加成员
     *    分为从单聊 ---> 建立群聊
     *       从群聊 ---> 添加成员
     * @param isGroup
     */
    private void addMember(boolean isGroup) {
        Intent intent = new Intent(mContext, CreateGroupActivity.class);
        ArrayList<String> selectedTeamMemberAccounts = new ArrayList<>();
        if (isGroup){
            for (int i = 0; i < mData.size(); i++) {
                selectedTeamMemberAccounts.add(mData.get(i).getUserId());
            }
            intent.putExtra("selectedMember", selectedTeamMemberAccounts);
            intent.putExtra(AppConst.EXTRA_FLAG_GROUP,AppConst.GROUP_FLAG_UPDATE);
        }else {
            selectedTeamMemberAccounts.add(mSessionId);
            intent.putExtra("selectedMember", selectedTeamMemberAccounts);
            intent.putExtra(AppConst.EXTRA_FLAG_GROUP,AppConst.GROUP_FLAG_CREATE);

        }
        mContext.startActivityForResult(intent, SessionInfoActivity.REQ_ADD_MEMBERS);
    }

    /**
     * 明显 已经是在一个群了,需要将好友移除出群聊
     * 且只要管理员可以删除 --------
     */
    private void removeMember() {
        Intent intent = new Intent(mContext, RemoveGpMemberActivity.class);
        intent.putExtra("sessionId", mSessionId);
        mContext.startActivityForResult(intent, SessionInfoActivity.REQ_REMOVE_MEMBERS);
    }
    /**
     * 删除群从成员
     * 执行删除成功之后会进行广播通知的 ----
     * @param selectedIds
     */
    public void deleteGroupMembers(ArrayList<String> selectedIds) {
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<selectedIds.size();i++){
            sb.append(selectedIds.get(i)).append("-");
        }
        ApiRetrofit.getInstance().removeGroupMb(Integer.parseInt(mSessionId), sb.substring(0,sb.lastIndexOf("-")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteGroupMemberResponse -> {
                    List<String> mTargetName = new ArrayList<>();
                    List<String> mTargetIDS = new ArrayList<>();
                    if (deleteGroupMemberResponse != null && deleteGroupMemberResponse.getCode() == 1) {
                        for (int i = 0; i < mData.size(); i++) {
                            GroupMember member = mData.get(i);
                            if (selectedIds.contains(member.getUserId())) {
                                mTargetIDS.add(member.getUserId());
                                // 很好 ----
                                mTargetName.add(member.getDisplayName());
                                member.delete();
                                mData.remove(i);
                                i--;
                            }
                        }
                        // 管理员移除出群聊
                        GroupNotificationMessageData messageData = new GroupNotificationMessageData();
                        messageData.setOperatorNickname(DBManager.getInstance().getGroupMemberById(mSessionId,UserCache.getId()).getDisplayName());
                        messageData.setTargetUserIds(mTargetIDS);
                        GroupNotificationMessage requestMessage = GroupNotificationMessage.
                                obtain(UserCache.getId(),
                                        GroupNotificationMessage.GROUP_OPERATION_KICKED,
                                        JsonMananger.beanToJson(messageData), "");
                        RongIMClient.getInstance().sendMessage(Message.obtain(mSessionId,
                                Conversation.ConversationType.GROUP, requestMessage),
                                "RC:GrpNtf", "", new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {

                            }

                            @Override
                            public void onSuccess(Message message) {
                                LogUtils.e("群组消息的通知","----------发送成功了");
                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                LogUtils.e("群组消息的通知","----------发送失败了");
                            }
                        });
                        mContext.hideWaitingDialog();
                        setAdapter();
                        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.REFRESH_CURRENT_SESSION);
                        UIUtils.showToast(UIUtils.getString(R.string.del_member_success));
                    } else {
                        mContext.hideWaitingDialog();
                        UIUtils.showToast(UIUtils.getString(R.string.del_member_fail));
                    }
                }, this::delMembersError);
    }


    private void delMembersError(Throwable throwable) {
        LogUtils.e("删除群成员",throwable.getLocalizedMessage());
        mContext.hideWaitingDialog();
        UIUtils.showToast(UIUtils.getString(R.string.del_member_fail));
    }


    /**
    * 依据单群聊分类加载数据
    * */
    private void loadData() {
        if (mConversationType == Conversation.ConversationType.PRIVATE){
            UserInfo userInfo = DBManager.getInstance().getUserInfo(mSessionId);
            if (userInfo != null){
                // 用户id,用户姓名,用户头像
                GroupMember newNumber =
                        new GroupMember(mSessionId,userInfo.getName(),userInfo.getPortraitUri().toString());
                mData.add(newNumber);
                // 显示的的 +号
                mData.add(new GroupMember("","",""));
            }
        }else {
            List<GroupMember> groupMembers = DBManager.getInstance().getGroupMembers(mSessionId);
            if (groupMembers != null && groupMembers.size() > 0) {
                Groups groupsById = DBManager.getInstance().getGroupsById(mSessionId);
                if (groupsById != null && groupsById.getRole().equals(AppConst.GROUP_ROLE_CREATE)) {
                    mIsManager = true;
                }
                mData.clear();
                mData.addAll(groupMembers);
                mData.add(new GroupMember("", "", ""));
                // 是管理员 则可以减少
                if (mIsManager) {
                    mData.add(new GroupMember("", "", ""));
                }
            }
            mIsCreateNewGroup = false;
        }
        setAdapter();
    }

    /**
     * 加载其他数据
     * @param sessionType 聊天类型
     * @param sessionId   聊天的标识id
     */
    public void loadOtherInfo(int sessionType, String sessionId) {
        switch (sessionType){
            case SESSION_TYPE_PRIVATE:
                break;
            case SESSION_TYPE_GROUP:
                // 设置关于群的一些信息
                // 设置群聊的名称
                setShowNickState();
                Observable.just(DBManager.getInstance().getGroupsById(sessionId))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(groups -> {
                            if (groups == null) {
                                return;
                            }
                            mGroups = groups;
                            //设置群信息
                            getView().getOivGroupName().setRightText(groups.getName());
                            mDisplayName = mGroupMember.getDisplayName();
                            getView().getOivNickNameInGroup().setRightText(mDisplayName);
                            getView().getBtnQuit().setText(groups.getRole().equals(AppConst.GROUP_ROLE_CREATE) ? UIUtils.getString(R.string.dismiss_this_group) :
                                    UIUtils.getString(R.string.delete_and_exit));
                        }, this::loadOtherError);
                break;
            default:
                break;
        }
    }



    private void loadOtherError(Throwable throwable) {
        LogUtils.e("加载其他",throwable.getLocalizedMessage());
    }


    /**
     * 添加群成员
     * 并将广播发出
     *         先看下效果吧
     * @param selectedIds
     */
    public void addGroupMember(ArrayList<String> selectedIds) {
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        StringBuilder sb = new StringBuilder();
        for (String strID : selectedIds){
            sb.append(strID).append("-");
        }
        ApiRetrofit.getInstance().addGroupMb(Integer.parseInt(mSessionId),sb.substring(0,sb.lastIndexOf("-")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getCode() == 1){
                        Groups groups = DBManager.getInstance().getGroupsById(mSessionId);
                        List<String> mTargetName = new ArrayList<>();
                        List<String> mTargetIDS = new ArrayList<>();
                        for (String groupMemberId : selectedIds) {
                            UserInfo userInfo = DBManager.getInstance().getUserInfo(groupMemberId);
                            if (userInfo != null) {
                                GroupMember newMember = new GroupMember(mSessionId,
                                        userInfo.getUserId(),
                                        userInfo.getName(),
                                        userInfo.getPortraitUri().toString(),
                                        userInfo.getName(),
                                        groups.getName(),
                                        groups.getPortraitUri());
                                mTargetName.add(userInfo.getName());
                                mTargetIDS.add(userInfo.getUserId());
                                DBManager.getInstance().saveOrUpdateGroupMember(newMember);
                            }
                        }

                        // 发送了添加群聊的通知
                        GroupNotificationMessageData messageData = new GroupNotificationMessageData();
                        messageData.setOperatorNickname(DBManager.getInstance().getGroupMemberById(mSessionId,UserCache.getId()).getDisplayName());
                        messageData.setTargetUserDisplayNames(mTargetName);
                        messageData.setTargetUserIds(mTargetIDS);
                        GroupNotificationMessage requestMessage = GroupNotificationMessage.
                                obtain(UserCache.getId(),
                                        GroupNotificationMessage.GROUP_OPERATION_ADD,
                                        JsonMananger.beanToJson(messageData), "");
                        RongIMClient.getInstance().sendMessage(Message.obtain(mSessionId, Conversation.ConversationType.GROUP, requestMessage), "RC:GrpNtf", "", new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {

                            }

                            @Override
                            public void onSuccess(Message message) {
                                LogUtils.e("群组消息的通知","----------发送成功了");
                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                LogUtils.e("群组消息的通知","----------发送失败了");
                            }
                        });
                        mContext.hideWaitingDialog();
                        loadData();
                        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.REFRESH_CURRENT_SESSION);
                        UIUtils.showToast(UIUtils.getString(R.string.add_member_success));
                    }else {
                        mContext.hideWaitingDialog();
                        UIUtils.showToast(UIUtils.getString(R.string.add_member_fail));
                    }
                }, throwable -> {
                    mContext.hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }




    /**
     * 退出群聊
     *     普通身份: 退出群聊 --
     *     管理身份: 解散群聊 --
     */
    public void quit() {
        if (mGroups == null){
            return;
        }

        String tip;
        if (mGroups.getRole().equalsIgnoreCase(AppConst.GROUP_ROLE_CREATE)){
            tip = UIUtils.getString(R.string.are_you_sure_to_dismiss_this_group);
            quitGroupResponseObservable = ApiRetrofit.getInstance().dismissGroup(Integer.parseInt(mSessionId));
        } else {
            tip = UIUtils.getString(R.string.you_will_never_receive_any_msg_after_quit);
            quitGroupResponseObservable = ApiRetrofit.getInstance().quitGroup(Integer.parseInt(mSessionId));
        }

        mContext.showMaterialDialog(null, tip, UIUtils.getString(R.string.sure), UIUtils.getString(R.string.cancel), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                List<String> mTargetName = new ArrayList<>();
                List<String> mTargetIDS = new ArrayList<>();
                mTargetIDS.add(UserCache.getId());
                mTargetName.add(UserCache.getName());
                GroupNotificationMessageData messageData = new GroupNotificationMessageData();
                messageData.setOperatorNickname(DBManager.getInstance().getGroupMemberById(mSessionId,UserCache.getId()).getDisplayName());
                messageData.setTargetUserDisplayNames(mTargetName);
                messageData.setTargetUserIds(mTargetIDS);
                GroupNotificationMessage requestMessage;
                if (!mGroups.getRole().equalsIgnoreCase(AppConst.GROUP_ROLE_CREATE)){
                    // 退出群聊
                    requestMessage = GroupNotificationMessage.
                            obtain(UserCache.getId(),
                                    GroupNotificationMessage.GROUP_OPERATION_QUIT,
                                    JsonMananger.beanToJson(messageData), "");
                }else {
                    // --------- 解散的通知也是要发的吖
                    requestMessage = GroupNotificationMessage.
                            obtain(UserCache.getId(),
                                    GroupNotificationMessage.GROUP_OPERATION_DISMISS,
                                    JsonMananger.beanToJson(messageData), "");
                }
                RongIMClient.getInstance().sendMessage(Message.obtain(mSessionId,
                        Conversation.ConversationType.GROUP, requestMessage),
                        "RC:GrpNtf", "", new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {

                            }

                            @Override
                            public void onSuccess(Message message) {
                                LogUtils.e("群组消息的通知","----------发送成功了");
                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                LogUtils.e("群组消息的通知","----------发送失败了");
                            }
                        });
                quitGroupResponseObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            mContext.hideMaterialDialog();
                            if (response != null && response.getCode() == 1) {
                                RongIMClient.getInstance().getConversation(mConversationType, mSessionId,
                                        new RongIMClient.ResultCallback<Conversation>() {
                                    @Override
                                    public void onSuccess(Conversation conversation) {
                                        RongIMClient.getInstance().clearMessages(Conversation.ConversationType.GROUP, mSessionId,
                                                new RongIMClient.ResultCallback<Boolean>() {
                                            @Override
                                            public void onSuccess(Boolean aBoolean) {
                                                RongIMClient.getInstance().removeConversation(mConversationType, mSessionId, null);
                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode errorCode) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                                /*  假如是退出群聊的话,那么应当要发送广播的*/
                                DBManager.getInstance().deleteGroupMembersByGroupId(mSessionId);
                                DBManager.getInstance().deleteGroupsById(mSessionId);
                                BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                                BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_GROUP);
                                BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.CLOSE_CURRENT_SESSION);
                                mContext.finish();
                            } else {
                                UIUtils.showToast(UIUtils.getString(R.string.exit_group_fail));
                            }
                        }, throwable -> UIUtils.showToast(UIUtils.getString(R.string.exit_group_fail)));
            }
        }, (dialog, which) -> {

        });
    }

    /**
     * 清除聊天记录
     */
    public void clearConversationMsg() {


    }

    /**
     * 设置群聊显示的名字
     */
    public void setDisplayName() {
        View view = View.inflate(mContext, R.layout.dialog_group_display_name_change, null);
        mSetDisplayNameDialog = new CustomDialog(mContext, view, R.style.MyDialog);
        EditText etName = (EditText) view.findViewById(R.id.etName);
        etName.setText(mDisplayName);
        etName.setSelection(mDisplayName.length());
        view.findViewById(R.id.tvCancle).setOnClickListener(v -> mSetDisplayNameDialog.dismiss());
        view.findViewById(R.id.tvOk).setOnClickListener(v -> {
            String displayName = etName.getText().toString().trim();
            if (!TextUtils.isEmpty(displayName)) {
                ApiRetrofit.getInstance().updateNickNameInGroup(Integer.parseInt(mSessionId),mGroupMember.getUserId(), displayName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(setGroupDisplayNameResponse -> {
                            if (setGroupDisplayNameResponse != null && setGroupDisplayNameResponse.getCode() == 1) {
                                if (mGroupMember != null) {
                                    mGroupMember.setDisplayName(displayName);
                                    mGroupMember.saveOrUpdate("groupid = ? and userid = ?",mGroupMember.getGroupId(),mGroupMember.getUserId());
                                    mDisplayName = displayName;
                                    getView().getOivNickNameInGroup().setRightText(mDisplayName);
                                }
                                UIUtils.showToast(UIUtils.getString(R.string.change_success));
                                // 重新加载一下数据 -------
                                loadMembers();

                                /* 修改昵称 -----------*/
                            } else {
                                UIUtils.showToast(UIUtils.getString(R.string.change_fail));
                            }
                            mSetDisplayNameDialog.dismiss();
                        }, this::setDisplayNameError);
            }
        });
        mSetDisplayNameDialog.show();
    }


    private void setDisplayNameError(Throwable throwable) {
        LogUtils.e("修改昵称",throwable.getLocalizedMessage());
        UIUtils.showToast(UIUtils.getString(R.string.change_fail));
        mSetDisplayNameDialog.dismiss();
    }

    /**
     * 改变数据库的状态-----
     * @param isChecked
     */
    public void changeShowNickName(boolean isChecked) {
        ApiRetrofit.getInstance().updateShowNickStatus(Integer.parseInt(mSessionId),
                mGroupMember.getUserId(),isChecked ? "1":"0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getCode() == 1){
                        mGroupMember.setShowNickName(isChecked);
                        mGroupMember.saveOrUpdate("groupid = ? and userid = ?",mGroupMember.getGroupId(),mGroupMember.getUserId());
                        BroadcastManager.getInstance(mContext)
                                .sendBroadcast(AppConst.REFRESH_CURRENT_SESSION);
                        if (isChecked) {
                            UIUtils.showToast(UIUtils.getString(R.string.success_show_group_nick_name));
                        }else {
                            UIUtils.showToast(UIUtils.getString(R.string.success_unshow_group_nick_name));
                        }
                    }else {
                        if (isChecked) {
                            UIUtils.getString(R.string.fail_show_group_nick_name);
                        }else {
                            UIUtils.showToast(UIUtils.getString(R.string.fail_unshow_group_nick_name));
                        }
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));


    }

    /**
     * 初次加载是否显示昵称
     */
    private void setShowNickState() {
        mGroupMember =  DBManager.getInstance().getGroupMemberById(mSessionId,UserCache.getId());
        getView().getSbShowNickView().setChecked(mGroupMember.isShowNickName());
    }
}

package com.jit.appcloud.ui.presenter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.Friend;
import com.jit.appcloud.db.db_model.Groups;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.manager.JsonMananger;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.message.GroupNotificationMessageData;
import com.jit.appcloud.model.response.GroupCreateResponse;
import com.jit.appcloud.model.response.GroupMbQyResponse;
import com.jit.appcloud.ui.activity.message.CreateGroupActivity;
import com.jit.appcloud.ui.activity.message.SessionActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.ICreateGroupAtView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.SortUtils;
import com.jit.appcloud.util.UIUtils;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRHeaderAndFooterAdapter;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.GroupNotificationMessage;

import static com.jit.appcloud.commom.AppConst.SESSION_TYPE_GROUP;

/**
 * @author zxl on 2018/7/23.
 *         discription:
 */

public class CreateGroupAtPresenter extends BasePresenter<ICreateGroupAtView> {
    private List<Friend> mData = new ArrayList<>();
    private List<Friend> mSelectedData = new ArrayList<>();
    private LQRHeaderAndFooterAdapter mAdapter;
    private int mGroupID;

    public CreateGroupAtPresenter(BaseActivity context) {
        super(context);
    }

    public void loadContacts() {
        loadData();
        setAdapter();
    }



    private void setAdapter() {
        if (mAdapter == null) {
            LQRAdapterForRecyclerView adapter = new LQRAdapterForRecyclerView<Friend>(mContext, mData, R.layout.item_contact) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, Friend item, int position) {
                    helper.setText(R.id.tvName, item.getDisplayName()).setViewVisibility(R.id.cb, View.VISIBLE);
                    ImageView ivHeader = helper.getView(R.id.ivHeader);
                    Glide.with(mContext).load(item.getPortraitUri()).into(ivHeader);
                    CheckBox cb = helper.getView(R.id.cb);

                    //如果添加群成员的话，需要判断是否已经在群中
                    if (((CreateGroupActivity) mContext).mSelectedTeamMemberAccounts != null &&
                            ((CreateGroupActivity) mContext).mSelectedTeamMemberAccounts.contains(item.getUserId())) {
                        cb.setChecked(true);
                        helper.setEnabled(R.id.cb, false).setEnabled(R.id.root, false);
                    } else {
                        helper.setEnabled(R.id.cb, true).setEnabled(R.id.root, true);
                        //没有在已有群中的联系人，根据当前的选中结果判断
                        cb.setChecked(mSelectedData.contains(item));
                    }
                }
            };
            mAdapter = adapter.getHeaderAndFooterAdapter();
            getView().getRvContacts().setAdapter(mAdapter);

            ((LQRAdapterForRecyclerView) mAdapter.getInnerAdapter()).setOnItemClickListener((lqrViewHolder, viewGroup, view, i) -> {
                //选中或反选
                Friend friend = mData.get(i);
                if (mSelectedData.contains(friend)) {
                    mSelectedData.remove(friend);
                } else {
                    mSelectedData.add(friend);
                }

                mAdapter.notifyDataSetChanged();
            });
        }
    }

    private void loadData() {
        Observable.just(DBManager.getInstance().getFriends())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(friends -> {
                    if (friends != null && friends.size() > 0) {
                        mData.clear();
                        mData.addAll(friends);
                        //整理排序
                        SortUtils.sortContacts(mData);
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }, this::loadError);
    }


    private void loadError(Throwable throwable) {
        UIUtils.showToast(throwable.getLocalizedMessage());
        mContext.hideWaitingDialog();
    }


    /**
     * 返回群成员对象 --------
     */
    public void addGroupMembers() {
        if (mSelectedData.size() == 0){
            UIUtils.showToast("客官请选择群聊成员!");
            return;
        }

        ArrayList<String> selectedIds = new ArrayList<>(mSelectedData.size());
        for (int i = 0; i < mSelectedData.size(); i++) {
            Friend friend = mSelectedData.get(i);
            selectedIds.add(friend.getUserId());
        }
        Intent data = new Intent();
        data.putStringArrayListExtra("selectedIds", selectedIds);
        mContext.setResult(Activity.RESULT_OK, data);
        mContext.finish();
    }

    /**
     * 创建群聊并发出群聊的通知
     *
     * 解决BUG -- 单纯的创建群聊是OK的,但是通过好友聊天创建群聊是失败的
     * @param groupName
     * @param imagePath
     */
    public void createGroup(String groupName, String imagePath) {
        // 把自己也加入进去
        ArrayList<String> memberAccounts = ((CreateGroupActivity) mContext).mSelectedTeamMemberAccounts;
        if ( memberAccounts!= null && memberAccounts.size() == 1 ){
            mSelectedData.add(DBManager.getInstance().getFriendById(memberAccounts.get(0)));
        }
        int size = mSelectedData.size();
        LogUtils.e("创建群聊","已经有的人数:"+size);
        if ( (memberAccounts != null) && size == 1 ){
            // 创建群聊 ---- 聊天界面转入
            UIUtils.showToast("客官请选择群聊成员!");
            return;
        }else if (memberAccounts == null && size ==0){
            // 普通 创建
            UIUtils.showToast("客官请选择群聊成员!");
            return;
        }else if (memberAccounts == null && size == 1){
            // 这是普通 创建群聊时的入口
            Intent intent = new Intent(mContext,SessionActivity.class);
            intent.putExtra("sessionId", mSelectedData.get(0).getUserId());
            intent.putExtra("sessionType", AppConst.SESSION_TYPE_PRIVATE);
            mContext.jumpToActivity(intent);
            mContext.finish();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mSelectedData.size(); i++) {
            Friend friend = mSelectedData.get(i);
            sb.append(friend.getUserId()).append("-");
        }
        LogUtils.e("创建群聊",sb.toString());
        String groupMembers =  sb.substring(0, sb.lastIndexOf("-"));
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().createGroup(groupName,groupMembers,new File(imagePath))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(groupCreateResponse -> {
                    if (groupCreateResponse != null && groupCreateResponse.getCode() == 1){
                        GroupCreateResponse.DataBean data = groupCreateResponse.getData();
                        mGroupID = data.getId();
                        DBManager.getInstance().saveOrUpdateGroup(new Groups(String.valueOf(mGroupID),
                                data.getGroupname(), data.getImage(),
                                String.valueOf(1)));
                        return ApiRetrofit.getInstance().queryGpMbById(data.getId());
                    }

                    return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(groupMbQyResponse -> {
                    mContext.hideWaitingDialog();
                    if (groupMbQyResponse != null && groupMbQyResponse.getCode() == 1) {
                        UIUtils.showToast(UIUtils.getString(R.string.create_group_success));
                        List<String> mTargetIDS = new ArrayList<>();
                        List<String> mTargetName = new ArrayList<>();
                        List<GroupMbQyResponse.DataBean> list = groupMbQyResponse.getData();
                        if (list != null && list.size() > 0) {
                            DBManager.getInstance().saveGroupMembers(list, String.valueOf(mGroupID));
                        }
                        for (GroupMbQyResponse.DataBean bean : list){
                            mTargetIDS.add(String.valueOf(bean.getUser_id()));
                            mTargetName.add(bean.getUsername());
                        }
                        // 发送了创建群聊的通知
                        GroupNotificationMessageData messageData = new GroupNotificationMessageData();
                        messageData.setOperatorNickname(DBManager.getInstance().getGroupMemberById(String.valueOf(mGroupID),UserCache.getId()).getDisplayName());
                        messageData.setTargetUserDisplayNames(mTargetName);
                        messageData.setTargetUserIds(mTargetIDS);
                        GroupNotificationMessage requestMessage = GroupNotificationMessage.
                                obtain(UserCache.getId(),
                                        GroupNotificationMessage.GROUP_OPERATION_CREATE,
                                        JsonMananger.beanToJson(messageData),
                                        "哈哈");
                        RongIMClient.getInstance().sendMessage(Message.obtain(String.valueOf(mGroupID),
                                Conversation.ConversationType.GROUP, requestMessage),
                                "RC:GrpNtf",
                                "",
                                new IRongCallback.ISendMessageCallback() {
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

                        Intent intent = new Intent(mContext, SessionActivity.class);
                        intent.putExtra("sessionId", String.valueOf(mGroupID));
                        intent.putExtra("sessionType", SESSION_TYPE_GROUP);
                        mContext.jumpToActivity(intent);
                        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.GROUP_LIST_UPDATE);
                        mContext.finish();
                    }else {
                        UIUtils.showToast(UIUtils.getString(R.string.create_group_fail));
                    }
                }, throwable -> {
                    mContext.hideWaitingDialog();
                    loadError(throwable);
                });
    }
}

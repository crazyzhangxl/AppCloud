package com.jit.appcloud.ui.presenter;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.Friend;
import com.jit.appcloud.db.db_model.GroupMember;
import com.jit.appcloud.db.db_model.Groups;
import com.jit.appcloud.manager.JsonMananger;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.message.GroupNotificationMessageData;
import com.jit.appcloud.ui.activity.MainActivity;
import com.jit.appcloud.ui.activity.message.SessionActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IMsgRecentView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.MediaFileUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;

import com.jit.appcloud.widget.CustomDialog;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.emoji.MoonUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.FileMessage;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import retrofit2.HttpException;

/**
 *
 * @author zxl
 * @date 2018/4/27
 *    description 最近消息的Fragment
 */

public class MsgRecentFgPresenter extends BasePresenter<IMsgRecentView> {
    private List<Conversation> mData = new ArrayList<>();
    private LQRAdapterForRecyclerView<Conversation> mAdapter;
    private int mUnreadCountTotal = 0;
    private final RequestOptions mOptions;
    private CustomDialog mConversationMenuDialog;
    @SuppressLint("CheckResult")
    public MsgRecentFgPresenter(BaseActivity context) {
        super(context);
        mOptions = new RequestOptions();
        mOptions.centerCrop()
                .placeholder(R.mipmap.default_header);
    }

    // 现在初次加载就获得了消息列表
    public void getConversations(){
        LogUtils.e("置顶操作","刷新咯 哈哈哈哈------------------------");
        loadData();
        setAdapter();
    }

    private void loadData() {
        LogUtils.e("融云","loadData");
        // 获得会话的列表,
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations !=null && conversations.size()>0){
                    mData.clear();
                    mData.addAll(conversations);
                    LogUtils.e("查看多少条-----","未筛选:   "+conversations.size());
                    filterData(mData);
                }else {
                    mData.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtils.e("融云","加载最近会话失败"+errorCode);
            }
        });
    }

    /**
     * 删选数据 ---- 筛选单聊个群聊信息
     * @param conversations
     */
    private void filterData(List<Conversation> conversations) {
        for (int i = 0; i < conversations.size(); i++) {
            Conversation item = conversations.get(i);
            //其他消息会话不显示（比如：系统消息）
            if (!(item.getConversationType() == Conversation.ConversationType.PRIVATE
                    || item.getConversationType() == Conversation.ConversationType.GROUP)) {
                conversations.remove(i);
                i--;
                continue;
            }

            if (item.getConversationType() == Conversation.ConversationType.GROUP) {
                /* 查询群成员 ----  */
                Log.e("群组同步","最新消息------"+item.getTargetId());
                List<GroupMember> groupMembers = DBManager.getInstance().getGroupMembers(item.getTargetId());
                if (groupMembers == null || groupMembers.size() == 0) {
                    Log.e("群组同步","我曹 已经删除了啊");
                    DBManager.getInstance().deleteGroupsById(item.getTargetId());
                    //删除没有群成员的群
                    conversations.remove(i);
                    i--;
                }
            } else if (item.getConversationType() == Conversation.ConversationType.PRIVATE) {
                if (!DBManager.getInstance().isMyFriend(item.getTargetId())) {
                    conversations.remove(i);
                    i--;
                }
            }
        }

        // 未读的消息总数(这里可以设置给下面的消息按钮)
        mUnreadCountTotal = 0;
        for (Conversation conversation : conversations) {
            mUnreadCountTotal += conversation.getUnreadMessageCount();
        }
        updateTotalUnreadView();
        LogUtils.e("查看多少条-----",conversations.size()+" ");
        if (mAdapter != null) {
            mAdapter.notifyDataSetChangedWrapper();
        }
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new LQRAdapterForRecyclerView<Conversation>(mContext, mData, R.layout.item_recent_message) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, Conversation item, int position) {
                    if (item.getConversationType() == Conversation.ConversationType.PRIVATE) {
                        ImageView ivHeader = helper.getView(R.id.ivHeader);
                        Friend friend = DBManager.getInstance().getFriendById(String.valueOf(item.getTargetId()));
                        if (friend != null) {
                            Glide.with(mContext).load(friend.getPortraitUri()).apply(mOptions).into(ivHeader);
                            helper.setText(R.id.tvName, friend.getDisplayName())
                                    .setText(R.id.tvTime, TimeUtil.getMsgFormatTime(item.getSentTime()))
                                    .setViewVisibility(R.id.ngiv, View.GONE)
                                    .setViewVisibility(R.id.ivHeader, View.VISIBLE);
                        }
                        // ====== 这里用户名未显示 得研究下
                    } else {
                        ImageView ivHeader = helper.getView(R.id.ivHeader);
                        Groups groups = DBManager.getInstance().getGroupsById(item.getTargetId());
                        if (groups != null) {
                            Glide.with(mContext).load(groups.getPortraitUri()).apply(mOptions).into(ivHeader);
                            helper.setText(R.id.tvName, groups.getName())
                                    .setText(R.id.tvTime, TimeUtil.getMsgFormatTime(item.getReceivedTime()))
                                    .setViewVisibility(R.id.ngiv, View.GONE)
                                    .setViewVisibility(R.id.ivHeader, View.VISIBLE);
                        }

                    }
                    //设置的背景暂时删除
                    //setBackgroundColor(R.id.flRoot, item.isTop() ? R.color.gray8 : android.R.color.white)
                    helper.setText(R.id.tvCount, item.getUnreadMessageCount() + "")
                            .setViewVisibility(R.id.tvCount, item.getUnreadMessageCount() > 0 ? View.VISIBLE : View.GONE);
                    TextView tvContent = helper.getView(R.id.tvContent);
                    if (!TextUtils.isEmpty(item.getDraft())) {
                        MoonUtils.identifyFaceExpression(mContext, tvContent, item.getDraft(), ImageSpan.ALIGN_BOTTOM);
                        helper.setViewVisibility(R.id.tvDraft, View.VISIBLE);
                        return;
                    } else {
                        helper.setViewVisibility(R.id.tvDraft, View.GONE);
                        if (item.getSentStatus() == Message.SentStatus.FAILED) {
                            helper.setViewVisibility(R.id.ivError, View.VISIBLE);
                        } else {
                            helper.setViewVisibility(R.id.ivError, View.GONE);
                        }
                    }

                    if (item.getLatestMessage() instanceof TextMessage) {
                        MoonUtils.identifyFaceExpression(mContext, tvContent, ((TextMessage) item.getLatestMessage()).getContent(), ImageSpan.ALIGN_BOTTOM);
                    } else if (item.getLatestMessage() instanceof ImageMessage) {
                        tvContent.setText("[" + UIUtils.getString(R.string.picture) + "]");
                    } else if (item.getLatestMessage() instanceof VoiceMessage) {
                        tvContent.setText("[" + UIUtils.getString(R.string.voice) + "]");
                    } else if (item.getLatestMessage() instanceof FileMessage) {
                        FileMessage fileMessage = (FileMessage) item.getLatestMessage();
                        if (MediaFileUtils.isImageFileType(fileMessage.getName())) {
                            tvContent.setText("[" + UIUtils.getString(R.string.sticker) + "]");
                        } else if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
                            tvContent.setText("[" + UIUtils.getString(R.string.video) + "]");
                        }
                    }else if (item.getLatestMessage() instanceof GroupNotificationMessage) {
                        // 人为设置 --- 哈哈
                        helper.setViewVisibility(R.id.ivError, View.GONE);
                        GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) item.getLatestMessage();
                        try {
                            UserInfo curUserInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
                            GroupNotificationMessageData data = JsonMananger.jsonToBean(
                                    groupNotificationMessage.getData(),
                                    GroupNotificationMessageData.class);
                            if (data == null) {
                                return;
                            }
                            String operation = groupNotificationMessage.getOperation();
                            String notification = "";
                            String operatorName = data.getOperatorNickname().equals(curUserInfo.getName()) ? UIUtils.getString(R.string.you) : data.getOperatorNickname();
                            String targetUserDisplayNames = "";
                            List<String> targetUserDisplayNameList = data.getTargetUserDisplayNames();
                            for (String name : targetUserDisplayNameList) {
                                targetUserDisplayNames += name.equals(curUserInfo.getName()) ? UIUtils.getString(R.string.you) : name;
                            }
                            if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_CREATE)) {
                                notification = UIUtils.getString(R.string.created_group, operatorName);
                            } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_DISMISS)) {
                                notification = operatorName + UIUtils.getString(R.string.dismiss_groups);
                            } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_KICKED)) {
                                if (operatorName.contains(UIUtils.getString(R.string.you))) {
                                    notification = UIUtils.getString(R.string.remove_group_member, operatorName, targetUserDisplayNames);
                                } else {
                                    notification = UIUtils.getString(R.string.remove_self, targetUserDisplayNames, operatorName);
                                }
                            } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_ADD)) {
                                notification = UIUtils.getString(R.string.invitation, operatorName, targetUserDisplayNames);
                            } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_QUIT)) {
                                notification = operatorName + UIUtils.getString(R.string.quit_groups);
                            } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_RENAME)) {
                                notification = UIUtils.getString(R.string.change_group_name, operatorName, data.getTargetGroupName());
                            }
                            tvContent.setText(notification);
                        } catch (HttpException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };
            /* 设置短按进入具体消息 */
            mAdapter.setOnItemClickListener((helper, parent, itemView, position) -> {
                Intent intent = new Intent(mContext, SessionActivity.class);
                Conversation item = mData.get(position);
                intent.putExtra("sessionId", item.getTargetId());
                if (item.getConversationType() == Conversation.ConversationType.PRIVATE) {
                    intent.putExtra("sessionType", AppConst.SESSION_TYPE_PRIVATE);
                } else {
                    intent.putExtra("sessionType", AppConst.SESSION_TYPE_GROUP);
                }
                mContext.jumpToActivity(intent);
            });

            /* 设置长按删除或者置顶消息*/
            mAdapter.setOnItemLongClickListener((helper, parent, itemView, position) -> {
                Conversation item = mData.get(position);
                View conversationMenuView = View.inflate(mContext, R.layout.dialog_conversation_menu, null);
                mConversationMenuDialog = new CustomDialog(mContext, conversationMenuView, R.style.MyDialog);
                TextView tvSetConversationToTop = (TextView) conversationMenuView.findViewById(R.id.tvSetConversationToTop);
                tvSetConversationToTop.setText(item.isTop() ? UIUtils.getString(R.string.cancel_conversation_to_top) : UIUtils.getString(R.string.set_conversation_to_top));
                conversationMenuView.findViewById(R.id.tvSetConversationToTop).setOnClickListener(v -> RongIMClient.getInstance().setConversationToTop(item.getConversationType(), item.getTargetId(), !item.isTop(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        loadData();
                        mConversationMenuDialog.dismiss();
                        mConversationMenuDialog = null;
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                }));
                conversationMenuView.findViewById(R.id.tvDeleteConversation).setOnClickListener(v -> {
                    RongIMClient.getInstance().removeConversation(item.getConversationType(), item.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            LogUtils.e("删除聊天", "成功没啊！");
                            loadData();
                            mConversationMenuDialog.dismiss();
                            mConversationMenuDialog = null;
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                });
                mConversationMenuDialog.show();
                return true;
            });
            getView().getRvRecentMessage().setAdapter(mAdapter);

        }
    }


    private void updateTotalUnreadView() {
        LogUtils.e("融云","未读取的消息 ===="+mUnreadCountTotal);
        ((MainActivity) mContext).setUnReadCont(mUnreadCountTotal);
    }

}

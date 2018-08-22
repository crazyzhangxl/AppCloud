package com.jit.appcloud.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.TimeUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.Friend;
import com.jit.appcloud.db.db_model.GroupMember;
import com.jit.appcloud.manager.JsonMananger;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.message.GroupNotificationMessageData;
import com.jit.appcloud.ui.activity.message.SessionActivity;
import com.jit.appcloud.ui.activity.message.UserInfoBySearchActivity;
import com.jit.appcloud.ui.presenter.SessionAtPresenter;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.MediaFileUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.util.VideoThumbLoader;
import com.jit.appcloud.widget.BubbleImageView;
import com.jit.appcloud.widget.CircularProgressBar;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.emoji.MoonUtils;

import org.joda.time.DateTime;

import java.io.File;
import java.util.Date;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.FileMessage;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import retrofit2.HttpException;

/**
 * 会话界面的消息列表适配器
 */
public class SessionAdapter extends LQRAdapterForRecyclerView<Message> {

    private Context mContext;
    private List<Message> mData;
    private SessionAtPresenter mPresenter;
    private String groupID;

    private static final int SEND_TEXT = R.layout.item_text_send;
    private static final int RECEIVE_TEXT = R.layout.item_text_receive;
    private static final int SEND_IMAGE = R.layout.item_image_send;
    private static final int RECEIVE_IMAGE = R.layout.item_image_receive;
    private static final int SEND_STICKER = R.layout.item_sticker_send;
    private static final int RECEIVE_STICKER = R.layout.item_sticker_receive;
    private static final int SEND_VIDEO = R.layout.item_video_send;
    private static final int RECEIVE_VIDEO = R.layout.item_video_receive;
    private static final int RECEIVE_VOICE = R.layout.item_audio_receive;
    private static final int SEND_VOICE = R.layout.item_audio_send;
    private static final int UNDEFINE_MSG = R.layout.item_no_support_msg_type;
    private static final int RECEIVE_NOTIFICATION = R.layout.item_notification;
    public  boolean isPushNow = false;
    private GroupMember mMyMember;

    public SessionAdapter(Context context, List<Message> data, SessionAtPresenter presenter,String groupID) {
        super(context, data);
        mContext = context;
        mData = data;
        mPresenter = presenter;
        this.groupID = groupID;
    }

    @Override
    public void convert(LQRViewHolderForRecyclerView helper, Message item, int position) {
        setTime(helper, item, position);
        setView(helper, item, position);
        if (!(item.getContent() instanceof GroupNotificationMessage) && !(item.getContent() instanceof RecallNotificationMessage) && (getItemViewType(position) != UNDEFINE_MSG)) {
            initMember();
            setAvatar(helper, item, position);
            setName(helper, item, position);
            setStatus(helper, item, position);
            setOnClick(helper, item, position);
        }
    }

    private void initMember() {
        mMyMember = DBManager.getInstance().getGroupMemberById(groupID, UserCache.getId());
    }

    private void setView(LQRViewHolderForRecyclerView helper, Message item, int position) {
        //根据消息类型设置消息显示内容
        // 目前我只需要做:图片和文字信息
        ImageView iv = helper.getView(R.id.ivAvatar);
        MessageContent msgContent = item.getContent();
        if (msgContent instanceof TextMessage) {
            MoonUtils.identifyFaceExpression(mContext, helper.getView(R.id.tvText), ((TextMessage) msgContent).getContent(), ImageSpan.ALIGN_BOTTOM);
        } else if (msgContent instanceof ImageMessage) {
            ImageMessage imageMessage = (ImageMessage) msgContent;
            BubbleImageView bivPic = helper.getView(R.id.bivPic);
            Glide.with(mContext).load(imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri()).apply(
                    new RequestOptions().error(R.mipmap.default_img_failed).override(UIUtils.dip2Px(80), UIUtils.dip2Px(150)).centerCrop()
            ).into(bivPic);
        } else if (msgContent instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) msgContent;
            if (MediaFileUtils.isImageFileType(fileMessage.getName())) {
                ImageView ivPic = helper.getView(R.id.ivSticker);
                Glide.with(mContext).load(fileMessage.getLocalPath() == null ? fileMessage.getMediaUrl() :
                        fileMessage.getLocalPath()).apply(new RequestOptions().placeholder(R.mipmap.default_img).error(R.mipmap.default_img_failed).centerCrop()).into(ivPic);
            } else if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
                BubbleImageView bivPic = helper.getView(R.id.bivPic);
                if (fileMessage.getLocalPath() != null && new File(fileMessage.getLocalPath().getPath()).exists()) {
                    VideoThumbLoader.getInstance().showThumb(fileMessage.getLocalPath().getPath(), bivPic, 92, 140);
                } else {
                    bivPic.setImageResource(R.mipmap.img_video_default);
                }
                // 假设只在这里进行刷新
            }
        }else if (msgContent instanceof VoiceMessage) {
            VoiceMessage voiceMessage = (VoiceMessage) msgContent;
            int increment = (int) (UIUtils.getDisplayWidth() / 2 / AppConst.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND * voiceMessage.getDuration());
            RelativeLayout rlAudio = helper.setText(R.id.tvDuration, voiceMessage.getDuration() + "''").getView(R.id.rlAudio);
            ViewGroup.LayoutParams params = rlAudio.getLayoutParams();
            params.width = UIUtils.dip2Px(65) + UIUtils.dip2Px(increment);
            rlAudio.setLayoutParams(params);
        }  else if (msgContent instanceof GroupNotificationMessage) {
            LogUtils.e("群消息","-----sessionAdapter = 是有通知的 -----");
            GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) msgContent;
            try {
                UserInfo curUserInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
                GroupNotificationMessageData data =
                        JsonMananger.jsonToBean(groupNotificationMessage.getData(),
                                GroupNotificationMessageData.class);
                String operation = groupNotificationMessage.getOperation();
                String notification = "";
                String operatorName = data.getOperatorNickname().equals(curUserInfo.getName())
                        ? UIUtils.getString(R.string.you) : data.getOperatorNickname();
                String targetUserDisplayNames = "";
                List<String> targetUserDisplayNameList =
                        data.getTargetUserDisplayNames();
                for (String name : targetUserDisplayNameList) {
                    targetUserDisplayNames += name.equals(curUserInfo.getName()) ? UIUtils.getString(R.string.you) : name+" ";
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
                helper.setText(R.id.tvNotification, notification);
            } catch (HttpException e) {
                e.printStackTrace();
            }
        }
    }

    private void setOnClick(LQRViewHolderForRecyclerView helper, Message item, int position) {
        // 在这边设置了 出错和头像的点击事件

        helper.getView(R.id.llError).setOnClickListener(v ->
                RongIMClient.getInstance().deleteMessages(new int[]{item.getMessageId()}, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        mData.remove(position);
                        mPresenter.setAdapter();
                        MessageContent content = item.getContent();
                        if (content instanceof TextMessage) {
                            mPresenter.sendTextMsg(((TextMessage) content).getContent());
                        } else if (content instanceof ImageMessage) {
                            mPresenter.sendImgMsg(((ImageMessage) content).getThumUri(), ((ImageMessage) content).getLocalUri());
                        } else if (content instanceof FileMessage) {
                            mPresenter.sendFileMsg(new File(((FileMessage) content).getLocalPath().getPath()));
                        } else if (content instanceof VoiceMessage) {
                            VoiceMessage voiceMessage = (VoiceMessage) content;
                            mPresenter.sendAudioFile(voiceMessage.getUri(), voiceMessage.getDuration());
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                })
        );

        helper.getView(R.id.ivAvatar).setOnClickListener(v -> {
            /* 如果是好友的 ======= */
            if (!item.getSenderUserId().equals(UserCache.getId())) {
                Friend friend = DBManager.getInstance().getFriendById(item.getSenderUserId());
                UserInfo userInfo = new UserInfo(friend.getUserId(),friend.getDisplayName(),Uri.parse(friend.getPortraitUri()));
                ApiRetrofit.getInstance().searchFriend(UserCache.getToken(),friend.getName())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchFriendResponse -> {
                            if (searchFriendResponse.getCode() == 1){
                                Intent intent = new Intent(mContext,UserInfoBySearchActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(AppConst.EXTRA_FRIEND_INFO,userInfo);
                                bundle.putString(AppConst.EXTRA_SIGNATURE,searchFriendResponse.getData().getSign() == null ? "":searchFriendResponse.getData().getSign());
                                bundle.putString(AppConst.EXTRA_AREA,searchFriendResponse.getData().getProvince() == null ? "":
                                        String.format(UIUtils.getString(R.string.str_format_city_bond),searchFriendResponse.getData().getProvince()
                                                ,searchFriendResponse.getData().getCity()
                                                ,searchFriendResponse.getData().getCountry()
                                        ));
                                intent.putExtras(bundle);
                                ((SessionActivity) mContext).jumpToActivity(intent);
                            }
                        }, throwable -> {
                            Intent intent = new Intent(mContext,UserInfoBySearchActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(AppConst.EXTRA_FRIEND_INFO,userInfo);
                            intent.putExtras(bundle);
                            ((SessionActivity) mContext).jumpToActivity(intent);
                        });
            }
        });
    }

    private void setStatus(LQRViewHolderForRecyclerView helper, Message item, int position) {
        MessageContent msgContent = item.getContent();
        if (msgContent instanceof TextMessage || msgContent instanceof LocationMessage || msgContent instanceof VoiceMessage) {
            //只需要设置自己发送的状态
            Message.SentStatus sentStatus = item.getSentStatus();
            if (sentStatus == Message.SentStatus.SENDING) {
                helper.setViewVisibility(R.id.pbSending, View.VISIBLE).setViewVisibility(R.id.llError, View.GONE);
            } else if (sentStatus == Message.SentStatus.FAILED) {
                helper.setViewVisibility(R.id.pbSending, View.GONE).setViewVisibility(R.id.llError, View.VISIBLE);
            } else if (sentStatus == Message.SentStatus.SENT) {
                helper.setViewVisibility(R.id.pbSending, View.GONE).setViewVisibility(R.id.llError, View.GONE);
            }
        } else if (msgContent instanceof ImageMessage) {
            ImageMessage imageMessage = (ImageMessage) msgContent;
            BubbleImageView bivPic = helper.getView(R.id.bivPic);
            boolean isSend = item.getMessageDirection() == Message.MessageDirection.SEND;
            if (isSend) {
                Message.SentStatus sentStatus = item.getSentStatus();
                if (sentStatus == Message.SentStatus.SENDING) {
                    bivPic.setProgressVisible(true);
                    if (!TextUtils.isEmpty(item.getExtra())) {
                        bivPic.setPercent(Integer.valueOf(item.getExtra()));
                    }
                    bivPic.showShadow(true);
                    helper.setViewVisibility(R.id.llError, View.GONE);
                } else if (sentStatus == Message.SentStatus.FAILED) {
                    bivPic.setProgressVisible(false);
                    bivPic.showShadow(false);
                    helper.setViewVisibility(R.id.llError, View.VISIBLE);
                } else if (sentStatus == Message.SentStatus.SENT) {
                    bivPic.setProgressVisible(false);
                    bivPic.showShadow(false);
                    helper.setViewVisibility(R.id.llError, View.GONE);
                }
            } else {
                Message.ReceivedStatus receivedStatus = item.getReceivedStatus();
                bivPic.setProgressVisible(false);
                bivPic.showShadow(false);
                helper.setViewVisibility(R.id.llError, View.GONE);
            }
        } else if (msgContent instanceof FileMessage) {
            BubbleImageView bivPic = helper.getView(R.id.bivPic);
            FileMessage fileMessage = (FileMessage) msgContent;
            boolean isSend = item.getMessageDirection() == Message.MessageDirection.SEND;
            if (MediaFileUtils.isImageFileType(fileMessage.getName())) {
                if (isSend) {
                    Message.SentStatus sentStatus = item.getSentStatus();
                    if (sentStatus == Message.SentStatus.SENDING) {
                    } else if (sentStatus == Message.SentStatus.FAILED) {
                        helper.setViewVisibility(R.id.llError, View.VISIBLE);
                    } else if (sentStatus == Message.SentStatus.SENT) {
                        helper.setViewVisibility(R.id.llError, View.GONE);
                    }
                } else {
                    if (bivPic != null) {
                        bivPic.setProgressVisible(false);
                        bivPic.showShadow(false);
                    }
                    helper.setViewVisibility(R.id.llError, View.GONE);
                }
            } else if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
                CircularProgressBar cpbLoading = helper.getView(R.id.cpbLoading);
                if (isSend) {
                    Message.SentStatus sentStatus = item.getSentStatus();
                    if (sentStatus == Message.SentStatus.SENDING || fileMessage.getLocalPath() == null || (fileMessage.getLocalPath() != null && !new File(fileMessage.getLocalPath().getPath()).exists())) {
                        if (!TextUtils.isEmpty(item.getExtra())) {
                            cpbLoading.setMax(100);
                            cpbLoading.setProgress(Integer.valueOf(item.getExtra()));
                        } else {
                            cpbLoading.setMax(100);
                            cpbLoading.setProgress(0);
                        }
                        helper.setViewVisibility(R.id.llError, View.GONE)
                                .setViewVisibility(R.id.cpbLoading, View.VISIBLE)
                                .setViewVisibility(R.id.ivPlay,View.GONE);
                        bivPic.showShadow(true);
                    } else if (sentStatus == Message.SentStatus.FAILED) {
                        helper.setViewVisibility(R.id.llError, View.VISIBLE)
                                .setViewVisibility(R.id.cpbLoading, View.GONE)
                                .setViewVisibility(R.id.ivPlay,View.GONE);
                        bivPic.showShadow(false);
                    } else if (sentStatus == Message.SentStatus.SENT) {
                        helper.setViewVisibility(R.id.llError, View.GONE)
                                .setViewVisibility(R.id.cpbLoading, View.GONE)
                                .setViewVisibility(R.id.ivPlay,View.VISIBLE);
                        bivPic.showShadow(false);
                    }
                } else {
                    Message.ReceivedStatus receivedStatus = item.getReceivedStatus();
                    if (receivedStatus.isDownload() || fileMessage.getLocalPath() != null) {
                        helper.setViewVisibility(R.id.llError, View.GONE)
                                .setViewVisibility(R.id.cpbLoading, View.GONE)
                                .setViewVisibility(R.id.ivPlay,View.VISIBLE);
                        bivPic.showShadow(false);
                    } else {
                        if (!TextUtils.isEmpty(item.getExtra())) {
                            cpbLoading.setMax(100);
                            cpbLoading.setProgress(Integer.valueOf(item.getExtra()));
                        } else {
                            cpbLoading.setMax(100);
                            cpbLoading.setProgress(0);
                        }
                        helper.setViewVisibility(R.id.llError, View.GONE)
                                .setViewVisibility(R.id.cpbLoading, View.VISIBLE)
                                .setViewVisibility(R.id.ivPlay,View.GONE);
                        bivPic.showShadow(true);
                    }
                }
            }
        }
    }

    private void setAvatar(LQRViewHolderForRecyclerView helper, Message item, int position) {
        ImageView ivAvatar = helper.getView(R.id.ivAvatar);
        // 不管了,这里就默认的设置头像吧 ====== 后面再进行修改
       // Glide.with(mContext).load(UIUtils.getDrawable(R.mipmap.default_header)).into(ivAvatar);
        // 这里做了优化,只在不是视频上传时
        if (!isPushNow) {
            Uri image = null;
            if (UserCache.getId().equals(item.getSenderUserId())) {
                image = UserCache.getHeadImage() == null ? null : Uri.parse(UserCache.getHeadImage());
            } else {
                image = DBManager.getInstance().getUserInfo(item.getSenderUserId()).getPortraitUri();
            }
            Glide.with(mContext).load(image)
                    .apply(new RequestOptions()
                            .error(R.mipmap.default_header)
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(ivAvatar);
        }
    }

    private void setName(LQRViewHolderForRecyclerView helper, Message item, int position) {
        LogUtils.e("session","setName得到了执行");
        // 单聊
        if (item.getConversationType() == Conversation.ConversationType.PRIVATE) {
            helper.setViewVisibility(R.id.tvName, View.GONE);
        } else {
            // 群聊消息
            // 如果是本人发送的 那么不显示
            if (UserCache.getId().equals(item.getSenderUserId())){
                helper.setViewVisibility(R.id.tvName, View.GONE);
            }else {
                if (mMyMember != null &&  mMyMember.isShowNickName()) {
                    helper.setViewVisibility(R.id.tvName, View.VISIBLE)
                            .setText(R.id.tvName, DBManager.getInstance().getGroupMemberById(groupID, item.getSenderUserId()).getName());
                }else {
                    helper.setViewVisibility(R.id.tvName, View.GONE);
                }
            }
        }
    }

    private void setTime(LQRViewHolderForRecyclerView helper, Message item, int position) {
        LogUtils.e("时间哦","发送时间: "+ TimeUtil.date2String(new Date(item.getSentTime()),"MM-dd HH:mm")
                +"  接收时间"+TimeUtil.date2String(new Date(item.getReceivedTime()),"MM-dd HH:mm"));
        /* 这里还是值得商榷的 ========================= */
            long msgTime = item.getSentTime();
            if (position > 0) {
                Message preMsg = mData.get(position - 1);
                long preMsgTime = preMsg.getSentTime();
                if (msgTime - preMsgTime > (5 * 60 * 1000)) {
                    helper.setViewVisibility(R.id.tvTime, View.VISIBLE).setText(R.id.tvTime, TimeUtil.getMsgFormatTime(msgTime));
                } else {
                    helper.setViewVisibility(R.id.tvTime, View.GONE);
                }
            } else {
                helper.setViewVisibility(R.id.tvTime, View.VISIBLE).setText(R.id.tvTime, TimeUtil.getMsgFormatTime(msgTime));
            }
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = mData.get(position);
        boolean isSend = msg.getMessageDirection() == Message.MessageDirection.SEND;

        MessageContent msgContent = msg.getContent();
        if (msgContent instanceof TextMessage) {
            return isSend ? SEND_TEXT : RECEIVE_TEXT;
        }
        if (msgContent instanceof ImageMessage) {
            return isSend ? SEND_IMAGE : RECEIVE_IMAGE;
        }

        if (msgContent instanceof GroupNotificationMessage) {
            return RECEIVE_NOTIFICATION;
        }

        if (msgContent instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) msgContent;
            if (MediaFileUtils.isImageFileType(fileMessage.getName())) {
                return isSend ? SEND_STICKER : RECEIVE_STICKER;
            } else if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
                return isSend ? SEND_VIDEO : RECEIVE_VIDEO;
            }
        }

        if (msgContent instanceof VoiceMessage) {
            return isSend ? SEND_VOICE : RECEIVE_VOICE;
        }
        return UNDEFINE_MSG;
    }
}

package com.jit.appcloud.ui.presenter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.jit.appcloud.R;
import com.jit.appcloud.ui.activity.message.SessionActivity;
import com.jit.appcloud.ui.activity.message.ShowBigImageActivity;
import com.jit.appcloud.ui.adapter.SessionAdapter;
import com.jit.appcloud.ui.base.BaseFragmentActivity;
import com.jit.appcloud.ui.base.BaseFragmentPresenter;
import com.jit.appcloud.ui.view.ISessionAtView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.MediaFileUtils;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.CustomDialog;
import com.lqr.audio.AudioPlayManager;
import com.lqr.audio.IAudioPlayListener;
import com.luck.picture.lib.PictureSelector;
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
import io.rong.imlib.model.MessageContent;
import io.rong.message.FileMessage;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
/**
 * Created by 张先磊 on 2018/5/2.
 */

public class SessionAtPresenter extends BaseFragmentPresenter<ISessionAtView> {
    public Conversation.ConversationType mConversationType;
    private String mSessionId;
    private String mPushCotent = "";//接收方离线时需要显示的push消息内容。
    private String mPushData = "";//接收方离线时需要在push消息中携带的非显示内容。
    private int mMessageCount = 5;//一次获取历史消息的最大数量

    private List<Message> mData = new ArrayList<>();
    private CustomDialog mSessionMenuDialog;
    public SessionAdapter mAdapter;
    public boolean isTop = false;
    public SessionAtPresenter(BaseFragmentActivity context, String sessionId, Conversation.ConversationType conversationType) {
        super(context);
        mSessionId = sessionId;
        mConversationType = conversationType;
    }

    public void loadMessage() {
        defIsTop();
        loadData();
        setAdapter();
    }

    private void defIsTop() {
        RongIMClient.getInstance().getConversation(mConversationType, mSessionId, new RongIMClient.ResultCallback<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                if (conversation != null) {
                    isTop = conversation.isTop();
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void loadData() {
        getLocalHistoryMessage();
        setAdapter();
    }

    public void setAdapter() {
        if (mAdapter == null){
            mAdapter = new SessionAdapter(mContext, mData, this,mSessionId);
            getView().getRvMsg().setAdapter(mAdapter);
            // ======= 需要设置点击事件 ========
            mAdapter.setOnItemClickListener((helper, parent, itemView, position) -> {
                Message message = mData.get(position);
                MessageContent content = message.getContent();
                if (content instanceof ImageMessage){
                    ImageMessage imageMessage = (ImageMessage) content;
                    View view = helper.getView(R.id.bivPic);
                    ShowBigImageActivity.startAction(mContext,view,
                            imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri().toString() : imageMessage.getLocalUri().toString());
                }else if (content instanceof FileMessage){
                    FileMessage fileMessage = (FileMessage) content;
                    if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
                        ImageView ivPlayer = helper.getView(R.id.ivPlay);
                        helper.getView(R.id.bivPic).setOnClickListener(v -> {
                            boolean isSend = message.getMessageDirection() == Message.MessageDirection.SEND;
                            if (isSend) {
                                if (ivPlayer.getVisibility() == View.VISIBLE) {
                                    // 当播放按钮可点击时才 可进入分支
                                    // 判断本地是否还有，有的话就直接打开，否则就要下载了
                                    if (fileMessage.getLocalPath() != null && new File(fileMessage.getLocalPath().getPath()).exists()) {
                                        PictureSelector.create(mContext).externalPictureVideo(fileMessage.getLocalPath().getPath());
                                    } else {
                                        downloadMediaMessage(message);
                                    }
                                }
                            } else {
                                if (ivPlayer.getVisibility() == View.VISIBLE) {
                                    if (fileMessage.getLocalPath() != null) {
                                        PictureSelector.create(mContext).externalPictureVideo(fileMessage.getLocalPath().getPath());
                                    } else {
                                        UIUtils.showToast(UIUtils.getString(R.string.file_out_of_date));
                                    }
                                } else {
                                    downloadMediaMessage(message);
                                }
                            }
                        });
                    }
                }else if (content instanceof VoiceMessage){
                    VoiceMessage voiceMessage = (VoiceMessage)content;
                    ImageView ivAudio = helper.getView(R.id.ivAudio);
                    AudioPlayManager.getInstance().startPlay(mContext, voiceMessage.getUri(), new IAudioPlayListener() {
                        @Override
                        public void onStart(Uri var1) {
                            if (ivAudio != null && ivAudio.getBackground() instanceof AnimationDrawable) {
                                AnimationDrawable animation = (AnimationDrawable) ivAudio.getBackground();
                                animation.start();
                            }
                        }
                        @Override
                        public void onStop(Uri var1) {
                            if (ivAudio != null && ivAudio.getBackground() instanceof AnimationDrawable) {
                                AnimationDrawable animation = (AnimationDrawable) ivAudio.getBackground();
                                animation.stop();
                                animation.selectDrawable(0);
                            }

                        }

                        @Override
                        public void onComplete(Uri var1) {
                            if (ivAudio != null && ivAudio.getBackground() instanceof AnimationDrawable) {
                                AnimationDrawable animation = (AnimationDrawable) ivAudio.getBackground();
                                animation.stop();
                                animation.selectDrawable(0);
                            }
                        }
                    });
                }
            });

            mAdapter.setOnItemLongClickListener((helper, parent, itemView, position) -> {
                View sessionMenuView = View.inflate(mContext, R.layout.dialog_session_menu, null);
                mSessionMenuDialog = new CustomDialog(mContext, sessionMenuView, R.style.MyDialog);
                TextView tvRetry = (TextView) sessionMenuView.findViewById(R.id.tvRetry);
                TextView tvDelete = (TextView) sessionMenuView.findViewById(R.id.tvDelete);
                tvRetry.setVisibility(View.GONE);
                //根据消息类型控制显隐
                Message message = mData.get(position);
                MessageContent content = message.getContent();
                // 其他的消息就直接返回了
                if (content instanceof GroupNotificationMessage || content instanceof RecallNotificationMessage) {
                    return false;
                }

                if (message.getSentStatus() == Message.SentStatus.FAILED ){
                    tvRetry.setVisibility(View.VISIBLE);
                }

                tvRetry.setOnClickListener(v -> RongIMClient.getInstance().deleteMessages(new int[]{message.getMessageId()}, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        UIUtils.postTaskSafely(() -> {
                            mSessionMenuDialog.dismiss();
                            mSessionMenuDialog = null;
                            mData.remove(position);
                            setAdapter();
                            if (content instanceof TextMessage) {
                                sendTextMsg(((TextMessage) content).getContent());
                            } else if (content instanceof ImageMessage) {
                                sendImgMsg(((ImageMessage) content).getThumUri(), ((ImageMessage) content).getLocalUri());
                            } else if (content instanceof FileMessage) {
                                sendFileMsg(new File(((FileMessage) content).getLocalPath().getPath()));
                            } else if (content instanceof VoiceMessage) {
                                VoiceMessage voiceMessage = (VoiceMessage) content;
                                sendAudioFile(voiceMessage.getUri(), voiceMessage.getDuration());
                            }
                        });
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                }));

                tvDelete.setOnClickListener(v -> RongIMClient.getInstance().deleteMessages(new int[]{message.getMessageId()}, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        // 直接在主线程中执行
                        UIUtils.postTaskSafely(() -> {
                            mSessionMenuDialog.dismiss();
                            mSessionMenuDialog = null;
                            mData.remove(position);
                            mAdapter.notifyDataSetChangedWrapper();
                            UIUtils.showToast(UIUtils.getString(R.string.delete_success));
                        });
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        UIUtils.postTaskSafely(() -> {
                            mSessionMenuDialog.dismiss();
                            mSessionMenuDialog = null;
                            UIUtils.showToast(UIUtils.getString(R.string.delete_fail) + ":" + errorCode.getValue());
                        });
                    }
                }));
                mSessionMenuDialog.show();
                return false;
            });

            if (!((SessionActivity)mContext).isScrolled ) {
                UIUtils.postTaskDelay(() -> getView().getRvMsg().smoothMoveToPosition(mData.size() - 1), 200);
            }

        }else {
            mAdapter.notifyDataSetChangedWrapper();
            if (getView() != null && getView().getRvMsg() != null) {
                rvMoveToBottom();
            }
        }
    }

    public void downloadMediaMessage(Message message) {
        RongIMClient.getInstance().downloadMediaMessage(message, new IRongCallback.IDownloadMediaMessageCallback() {
            @Override
            public void onSuccess(Message message) {
                updateMessageStatus(message);
                message.getReceivedStatus().setDownload();
                message.getReceivedStatus().setRetrieved();
                message.getReceivedStatus().setMultipleReceive();

                mAdapter.isPushNow = false;
            }

            @Override
            public void onProgress(Message message, int progress) {
                //发送进度
                //发送进度
                mAdapter.isPushNow = true;
                message.setExtra(progress + "");
                updateMessageStatus(message);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                updateMessageStatus(message);
                mAdapter.isPushNow = false;
            }

            @Override
            public void onCanceled(Message message) {
                updateMessageStatus(message);
                mAdapter.isPushNow = false;
            }
        });
    }

    public void loadMore(){
        getLocalHistoryMessage();
        mAdapter.notifyDataSetChangedWrapper();
    }


    public void receiveNewMessage(Message message) {
        // 接收到新消息啦
        mData.add(message);
        setAdapter();
        //设置会话已读
        RongIMClient.getInstance().clearMessagesUnreadStatus(mConversationType, mSessionId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                LogUtils.e("融云","设置会话已读"+aBoolean);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }



    private void getLocalHistoryMessage() {
        LogUtils.e("融云","设置数据======获得历史数据咯!");
        // 没有消息第一次调用应该设置为 -1
        int messageId = -1;
        if (mData.size() >0 ){
            messageId = mData.get(0).getMessageId();
        }else {
            messageId = -1;
        }

        // 获得历史的消息
        RongIMClient.getInstance().getHistoryMessages(mConversationType, mSessionId, messageId, mMessageCount, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                LogUtils.e("融云","获得消息成功啦++开始加载数据 ==========");
                getView().getRefreshLayout().endRefreshing();
                // 如果历史数据为空 那么要从远端获取,否则就是保存
                if (messages == null || messages.size() == 0) {
                    getRemoteHistoryMessages();
                } else {
                    saveHistoryMsg(messages);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                getView().getRefreshLayout().endRefreshing();
                loadMessageError(errorCode);
            }
        });
    }

    //单聊、群聊、讨论组、客服的历史消息从远端获取

    public void getRemoteHistoryMessages() {
        //消息中的 sentTime；第一次可传 0，获取最新 count 条。
        long dateTime = 0;
        if (mData.size() > 0) {
            dateTime = mData.get(0).getSentTime();
        } else {
            dateTime = 0;
        }

        RongIMClient.getInstance().getRemoteHistoryMessages(mConversationType, mSessionId, dateTime, mMessageCount,
                new RongIMClient.ResultCallback<List<Message>>() {
                    @Override
                    public void onSuccess(List<Message> messages) {
                        saveHistoryMsg(messages);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        loadMessageError(errorCode);
                    }
                });
    }

    private void saveHistoryMsg(List<Message> messages) {
        //messages的时间顺序从新到旧排列，所以必须反过来加入到mData中

        /* 只需要对数据进行筛选就能达到要求*/
        if (messages != null && messages.size() > 0) {
            for (Message msg : messages) {
                if (msg.getContent() instanceof TextMessage
                        || msg.getContent() instanceof FileMessage
                        || msg.getContent() instanceof VoiceMessage
                        || msg.getContent() instanceof ImageMessage
                        || msg.getContent() instanceof GroupNotificationMessage) {
                    mData.add(0, msg);
                }
            }
            getView().getRvMsg().moveToPosition(messages.size() - 1);
        }
    }

    private void loadMessageError(RongIMClient.ErrorCode errorCode) {
        LogUtils.e("融云","拉取历史消息失败，errorCode = " + errorCode);
    }

    /*========================   发送各种数据开始  ================================*/

    public void sendTextMsg(){
        sendTextMsg(getView().getEtContent().getText().toString());
        getView().getEtContent().setText("");
    }

    /**
     * 发送文字消息基本没什么问题
     * @param content
     */
    public void sendTextMsg(String content) {
        RongIMClient.getInstance().sendMessage(mConversationType, mSessionId, TextMessage.obtain(content), mPushCotent, mPushData,
                new RongIMClient.SendMessageCallback() {// 发送消息的回调
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        updateMessageStatus(integer);
                        LogUtils.e("群组",errorCode.getMessage()+" "+errorCode.getValue());
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        updateMessageStatus(integer);
                    }
                }, new RongIMClient.ResultCallback<Message>() {//消息存库的回调，可用于获取消息实体
                    @Override
                    public void onSuccess(Message message) {
                        // 发送成功 关闭软键盘
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        // 隐藏软键盘
                        imm.hideSoftInputFromWindow(mContext.getWindow().getDecorView().getWindowToken(), 0);
                        mAdapter.addLastItem(message);
                        rvMoveToBottom();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        LogUtils.e("群组",errorCode.getMessage()+" "+errorCode.getValue());
                    }
                });

/*        RongIMClient.getInstance().sendMessage(mConversationType, mSessionId, TextMessage.obtain(content),
                mPushCotent, mPushData,
                new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {

                    }

                    @Override
                    public void onSuccess(Message message) {
                        // 发送成功 关闭软键盘
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        // 隐藏软键盘
                        imm.hideSoftInputFromWindow(mContext.getWindow().getDecorView().getWindowToken(), 0);
                        mAdapter.addLastItem(message);
                        rvMoveToBottom();
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        updateMessageStatus(message.getMessageId());
                    }
                });*/

    }

    // 发送文件
    public void sendFileMsg(File file) {
        Message fileMessage = Message.obtain(mSessionId, mConversationType, FileMessage.obtain(Uri.fromFile(file)));
        RongIMClient.getInstance().sendMediaMessage(fileMessage, mPushCotent, mPushData, new IRongCallback.ISendMediaMessageCallback() {
            @Override
            public void onProgress(Message message, int progress) {
                //发送进度
                mAdapter.isPushNow = true;
                message.setExtra(String.valueOf(progress));
                updateMessageStatus(message);
            }

            @Override
            public void onCanceled(Message message) {

            }

            @Override
            public void onAttached(Message message) {
                //保存数据库成功
                mContext.hideWaitingDialog();
                mAdapter.addLastItem(message);
                rvMoveToBottom();
            }

            @Override
            public void onSuccess(Message message) {
                //发送成功
                updateMessageStatus(message);
                mAdapter.isPushNow = false;
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //发送失败
                updateMessageStatus(message);
                mAdapter.isPushNow = false;
            }
        });
    }

    //发送视频
    public void sendAudioFile(Uri audioPath, int duration) {
        if (audioPath != null) {
            File file = new File(audioPath.getPath());
            if (!file.exists() || file.length() == 0L) {
                LogUtils.e("融云_发送音频",UIUtils.getString(R.string.send_audio_fail));
                return;
            }
            VoiceMessage voiceMessage = VoiceMessage.obtain(audioPath, duration);
            RongIMClient.getInstance().sendMessage(Message.obtain(mSessionId, mConversationType, voiceMessage), mPushCotent, mPushData, new IRongCallback.ISendMessageCallback() {
                @Override
                public void onAttached(Message message) {
                    mAdapter.isPushNow = true;
                    //保存数据库成功
                    mAdapter.addLastItem(message);
                    rvMoveToBottom();
                }

                @Override
                public void onSuccess(Message message) {
                    //发送成功
                    updateMessageStatus(message);
                    mAdapter.isPushNow = false;
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                    //发送失败
                    updateMessageStatus(message);
                    mAdapter.isPushNow = false;
                }
            });
        }
    }

    // 发送图片 ======== =====

    public void sendImgMsg(File imageFileThumb, File imageFileSource) {
        Uri imageFileThumbUri = Uri.fromFile(imageFileThumb);
        Uri imageFileSourceUri = Uri.fromFile(imageFileSource);
        sendImgMsg(imageFileThumbUri, imageFileSourceUri);
    }

    public void sendImgMsg(Uri imageFileThumbUri, Uri imageFileSourceUri) {
        ImageMessage imgMsg = ImageMessage.obtain(imageFileThumbUri, imageFileSourceUri);
        // 这边方法都是在 主线程中执行的
        RongIMClient.getInstance().sendImageMessage(mConversationType, mSessionId, imgMsg, mPushCotent, mPushData,
                new RongIMClient.SendImageMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        mContext.hideWaitingDialog();
                        //保存数据库成功
                        mAdapter.addLastItem(message);
                        rvMoveToBottom();
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        //发送失败
                        updateMessageStatus(message);
                        mAdapter.isPushNow = false;
                    }

                    @Override
                    public void onSuccess(Message message) {
                        //发送成功
                        updateMessageStatus(message);
                        mAdapter.isPushNow = false;
                    }

                    @Override
                    public void onProgress(Message message, int progress) {
                        //发送进度
                        mAdapter.isPushNow = true;
                        message.setExtra(progress + "");
                        updateMessageStatus(message);

                    }
                });
    }
    /*========================   发送各种数据结束  ================================*/


    public void saveDraft() {
        String draft = getView().getEtContent().getText().toString();
        if (!TextUtils.isEmpty(draft)) {
            RongIMClient.getInstance().saveTextMessageDraft(mConversationType, mSessionId, draft, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    public void resetDraft() {
        // 很好这里进行了判空ing
        Observable.just(RongIMClient.getInstance().getTextMessageDraft(mConversationType, mSessionId) == null?"":RongIMClient.getInstance().getTextMessageDraft(mConversationType, mSessionId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (!TextUtils.isEmpty(s)) {
                        SessionAtPresenter.this.getView().getEtContent().setText(s);
                        SessionAtPresenter.this.getView().getEtContent().setSelection(s.length());
                        RongIMClient.getInstance().clearTextMessageDraft(mConversationType, mSessionId);
                    }
                }, this::loadError);

    }

    private void loadError(Throwable throwable) {
        LogUtils.e("融云",throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
    }

    // 更新消息的状态
    private void updateMessageStatus(int messageId) {
        RongIMClient.getInstance().getMessage(messageId, new RongIMClient.ResultCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                for (int i = 0; i < mData.size(); i++) {
                    Message msg = mData.get(i);
                    if (msg.getMessageId() == message.getMessageId()) {
                        mData.remove(i);
                        mData.add(i, message);
                        mAdapter.notifyItemChangedWrapper(i);
                        break;
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void updateMessageStatus(Message message) {
        for (int i = 0; i < mData.size(); i++) {
            Message msg = mData.get(i);
            if (msg.getMessageId() == message.getMessageId()) {
                mData.remove(i);
                mData.add(i, message);
                mAdapter.notifyItemChangedWrapper(i);
                break;
            }
        }
    }
    private void rvMoveToBottom() {
        getView().getRvMsg().smoothMoveToPosition(mData.size() - 1);
    }

}

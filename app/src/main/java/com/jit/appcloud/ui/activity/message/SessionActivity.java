package com.jit.appcloud.ui.activity.message;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.app.MyApp;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.Friend;
import com.jit.appcloud.db.db_model.Groups;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.ui.base.BaseFragmentActivity;
import com.jit.appcloud.ui.presenter.SessionAtPresenter;
import com.jit.appcloud.ui.view.ISessionAtView;
import com.jit.appcloud.util.ImageUtils;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.NotificationUtils;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.MyKeyBoardLinearLayout;
import com.lqr.audio.AudioRecordManager;
import com.lqr.audio.IAudioRecordListener;
import com.lqr.emoji.EmotionKeyboard;
import com.lqr.emoji.EmotionLayout;
import com.lqr.emoji.IEmotionSelectedListener;
import com.lqr.recyclerview.LQRRecyclerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.jit.appcloud.commom.AppConst.SESSION_TYPE_GROUP;
import static com.jit.appcloud.commom.AppConst.SESSION_TYPE_PRIVATE;

/**
 * @author zxl
 *         discription: 单聊 --- 拓展群聊(7/25)
 */
@RuntimePermissions
public class SessionActivity extends BaseFragmentActivity<ISessionAtView, SessionAtPresenter> implements BGARefreshLayout.BGARefreshLayoutDelegate, ISessionAtView, IEmotionSelectedListener {
    public static final int REQUEST_IMAGE_PICKER = 1000;
    public final static int REQUEST_TAKE_PHOTO = 1001;
    public final static int REQUEST_MY_LOCATION = 1002;

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.llToolbarTitle)
    LinearLayout mLlToolbarTitle;
    @BindView(R.id.flToolBar)
    FrameLayout mFlToolBar;
    @BindView(R.id.rvMsg)
    LQRRecyclerView mRvMsg;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.ivAudio)
    ImageView mIvAudio;
    @BindView(R.id.etContent)
    EditText mEtContent;
    @BindView(R.id.btnAudio)
    Button mBtnAudio;
    @BindView(R.id.ivEmo)
    ImageView mIvEmo;
    @BindView(R.id.ivMore)
    ImageView mIvMore;
    @BindView(R.id.btnSend)
    Button mBtnSend;
    @BindView(R.id.llContent)
    LinearLayout mLlContent;
    @BindView(R.id.elEmotion)
    EmotionLayout mElEmotion;
    @BindView(R.id.ivAlbum)
    ImageView mIvAlbum;
    @BindView(R.id.rlAlbum)
    RelativeLayout mRlAlbum;
    @BindView(R.id.ivShot)
    ImageView mIvShot;
    @BindView(R.id.rlTakePhoto)
    RelativeLayout mRlTakePhoto;
    @BindView(R.id.flEmotionView)
    FrameLayout mFlEmotionView;
    @BindView(R.id.llMore)
    LinearLayout mLlMore;
    @BindView(R.id.llRoot)
    MyKeyBoardLinearLayout mLlRoot;
    @BindView(R.id.ll_content_key)
    LinearLayout mLlContentKey;
    @BindView(R.id.rlTakeVideo)
    RelativeLayout mRlTakeVideo;
    @BindView(R.id.ibToolbarMore)
    ImageButton mIbToolbarMore;


    private String mSessionId = "";
    private Conversation.ConversationType mConversationType = Conversation.ConversationType.PRIVATE;
    private EmotionKeyboard mEmotionKeyboard;
    public boolean isScrolled = false; // 记录rev是否已经滑动过,滑过
    private boolean isFromEmo = false; // 是否来自表情
    // 图片选择路径

    private List<LocalMedia> mCameraList;       // 相机
    private List<LocalMedia> mPictureSelectList;// 图片
    private List<LocalMedia> mVideoSelectList;  // 视频
    @Override
    protected void init() {
        Intent intent = getIntent();
        mSessionId = intent.getStringExtra("sessionId");
        LogUtils.e("身份","   群号ID或者好友ID"+mSessionId);
        int sessionType = intent.getIntExtra("sessionType", SESSION_TYPE_PRIVATE);
        LogUtils.e("身份","   类型"+sessionType);
        // 直接就清除通知就得了
        new NotificationUtils.Build(mContext).cancelTargetNf(mSessionId);
        switch (sessionType) {
            case SESSION_TYPE_PRIVATE:
                mConversationType = Conversation.ConversationType.PRIVATE;
                break;
            case SESSION_TYPE_GROUP:
                mConversationType = Conversation.ConversationType.GROUP;
                break;
            default:
                break;
        }

        initAudioRecordManager();
        // 设置会话已读
        RongIMClient.getInstance().clearMessagesUnreadStatus(mConversationType, mSessionId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                LogUtils.e("融云", "设置会话已读" + aBoolean);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
        // 注册广播
        registerBR();
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        mIbToolbarMore.setVisibility(View.VISIBLE);
        ((SimpleItemAnimator) mRvMsg.getItemAnimator()).setSupportsChangeAnimations(false);
        mElEmotion.attachEditText(mEtContent);
        initEmotionKeyboard();
        initRefreshLayout();
        setTitle();
    }


    @Override
    protected void initData() {
        // 加载数据
        mPresenter.loadMessage();
    }

    // 可见的包括title部分 root的话是中间的 ==》 700 1280/3= 420*2
    // 426 = 1/3 1280;1232  ===> 745;697    500 1280
    public boolean isSoftKeyboardShow(View rootView) {
        int screenHeight;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        int visibleBottom = rect.bottom;
        return visibleBottom < screenHeight * 2 / 3;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Android避免进入页面EditText自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resetDraft();
    }

    // 按返回键 这是退出栈的方法

    @Override
    public void onBackPressed() {
        if (mElEmotion.isShown() || mLlMore.isShown()) {
            mEmotionKeyboard.interceptBackPress();
            mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        isFromEmo = false;
        return super.dispatchKeyEvent(event);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.saveDraft();
    }

    @Override
    protected void onDestroy() {
        unRegisterBR();
        super.onDestroy();
    }


    /**
     * 注册各种广播
     */
    private void registerBR() {
        // 接收到新的消息啦
        BroadcastManager.getInstance(this).register(AppConst.UPDATE_CURRENT_SESSION, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Message message = intent.getParcelableExtra("result");
                if (message != null) {
                    if (message.getTargetId().equals(mSessionId)) {
                        mPresenter.receiveNewMessage(message);
                    }
                }
            }
        });

        // 监听到刷新会话时
        BroadcastManager.getInstance(this).register(AppConst.REFRESH_CURRENT_SESSION, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LogUtils.e("会话","---------- 执行了刷新会话 --------------");
                mPresenter.loadMessage();
            }
        });

        // 监听到对方修改了姓名时
        BroadcastManager.getInstance(this).register(AppConst.UPDATE_CURRENT_SESSION_NAME, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setTitle();
            }
        });
        // 监听到关闭对话
        BroadcastManager.getInstance(this).register(AppConst.CLOSE_CURRENT_SESSION, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        });

        // 输入状态改变时
        RongIMClient.setTypingStatusListener((type, targetId, typingStatusSet) -> {
            //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
            if (type.equals(mConversationType) && targetId.equals(mSessionId)) {
                //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示
                int count = typingStatusSet.size();
                if (count > 0) {
                    Iterator<TypingStatus> iterator = typingStatusSet.iterator();
                    TypingStatus status = iterator.next();
                    String objectName = status.getTypingContentType();

                    MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                    MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                    //匹配对方正在输入的是文本消息还是语音消息
                    if (objectName.equals(textTag.value())) {
                        //显示“对方正在输入”
                        setToolbarTitle(UIUtils.getString(R.string.SET_TEXT_TYPING_TITLE));
                    } else if (objectName.equals(voiceTag.value())) {
                        //显示“对方正在讲话”
                        setToolbarTitle(UIUtils.getString(R.string.SET_VOICE_TYPING_TITLE));
                    }
                } else {
                    //当前会话没有用户正在输入，标题栏仍显示原来标题
                    setTitle();
                }
            }
        });
    }

    private void unRegisterBR() {
        BroadcastManager.getInstance(this).unregister(AppConst.UPDATE_CURRENT_SESSION);
        BroadcastManager.getInstance(this).unregister(AppConst.REFRESH_CURRENT_SESSION);
        BroadcastManager.getInstance(this).unregister(AppConst.UPDATE_CURRENT_SESSION_NAME);
        BroadcastManager.getInstance(this).unregister(AppConst.CLOSE_CURRENT_SESSION);
    }

    public void setToolbarTitle(String title) {
        mTvToolbarTitle.setText(title);
    }

    @Override
    protected void initListener() {
        // 软键盘的弹出事件
        mLlRoot.setSoftKeyboardListener(() -> {
            boolean isShow = isSoftKeyboardShow(mLlRoot);
            if (isShow) {
                if (!isScrolled && !isFromEmo) {
                    isScrolled = true;
                    if (mRvMsg.getAdapter().getItemCount() != 0) {
                        UIUtils.postTaskDelay(() -> mRvMsg.smoothMoveToPosition(mRvMsg.getAdapter().getItemCount() - 1), 50);
                    }
                }
            } else {
                isScrolled = false;
                isFromEmo = false;
            }
        });

        setClickMoreListener();

        mIvToolbarNavigation.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            // 隐藏软键盘
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            // 延迟个0.1秒是因为 可能推出太快导致软键盘刚刚被回收，前面的界面出现断层
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();

        });

        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtContent.getText().toString().trim().length() > 0) {
                    // 如果输入框中有内容 那么进行更多的切换
                    mBtnSend.setVisibility(View.VISIBLE);
                    mIvMore.setVisibility(View.GONE);
                    // 这个表示用户正在输入 ====
                    RongIMClient.getInstance().sendTypingStatus(mConversationType, mSessionId, TextMessage.class.getAnnotation(MessageTag.class).value());
                } else {
                    mBtnSend.setVisibility(View.GONE);
                    mIvMore.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 发送消息啦
        mBtnSend.setOnClickListener(v -> mPresenter.sendTextMsg());

        /* =================      表情选择    ============= */
        // 设置表情的点击事件
        mElEmotion.setEmotionSelectedListener(this);

        /* =================   话筒的点击事件  =================*/
        mIvAudio.setOnClickListener(v -> SessionActivityPermissionsDispatcher.micNeedsPSWithCheck(SessionActivity.this));

        setAudioOnTouchListener();

        /*  下面的相册 拍照的点击事件*/
        mRlAlbum.setOnClickListener((View view) -> {
            // 虽然没有从根本上解决Bug,但如果这样可以的话也是不错的选择
            // view的
            mIvAudio.callOnClick();
            hideAudioButton();
            choosePictureEvent();

        });

        mRlTakePhoto.setOnClickListener(view ->
        {
            mIvAudio.callOnClick();
            hideAudioButton();
            takePhotoEvent();
        });

        mRlTakeVideo.setOnClickListener(v -> {
            mIvAudio.callOnClick();
            hideAudioButton();
            takeVideo();
        });
    }

    private void setAudioOnTouchListener() {
        mBtnAudio.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    AudioRecordManager.getInstance(SessionActivity.this).startRecord();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isCancelled(v, event)) {
                        AudioRecordManager.getInstance(SessionActivity.this).willCancelRecord();
                    } else {
                        AudioRecordManager.getInstance(SessionActivity.this).continueRecord();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    AudioRecordManager.getInstance(SessionActivity.this).stopRecord();
                    AudioRecordManager.getInstance(SessionActivity.this).destroyRecord();
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    /**
     * 右上角的更多 -> 好友和群聊明细的界面
     */
    private void setClickMoreListener() {
        mIbToolbarMore.setOnClickListener(v -> {
            Intent intent = new Intent(SessionActivity.this, SessionInfoActivity.class);
            intent.putExtra("sessionId", mSessionId);
            intent.putExtra("sessionType", mConversationType == Conversation.ConversationType.PRIVATE ? SESSION_TYPE_PRIVATE : SESSION_TYPE_GROUP);
            intent.putExtra(AppConst.CONVERSATION_IS_TOP,mPresenter.isTop);
            jumpToActivity(intent);
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                UIUtils.hideKeyboard(ev, mLlContentKey, SessionActivity.this);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setDelegate(this);
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, false);
        refreshViewHolder.setRefreshingText("");
        refreshViewHolder.setPullDownRefreshText("");
        refreshViewHolder.setReleaseRefreshText("");
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    private void initEmotionKeyboard() {
        mEmotionKeyboard = EmotionKeyboard.with(this);
        mEmotionKeyboard.bindToEditText(mEtContent);
        mEmotionKeyboard.bindToContent(mLlContent);
        mEmotionKeyboard.setEmotionLayout(mFlEmotionView);
        mEmotionKeyboard.bindToEmotionButton(mIvEmo, mIvMore);
        mEmotionKeyboard.setOnEmotionButtonOnClickListener(view -> {
            if (!isScrolled && !isFromEmo) {
                if (mRvMsg.getAdapter().getItemCount() != 0) {
                    UIUtils.postTaskDelay(() -> mRvMsg.smoothMoveToPosition(mRvMsg.getAdapter().getItemCount() - 1), 50);
                }
            }
            isFromEmo = true;
            switch (view.getId()) {
                case R.id.ivEmo:
                    // 这里的作用是将列表布局定位到倒数第二个
                    if (!mElEmotion.isShown()) {
                        if (mLlMore.isShown()) {
                            showEmotionLayout();
                            hideMoreLayout();
                            hideAudioButton();
                            return true;
                        }
                    } else if (mElEmotion.isShown() && !mLlMore.isShown()) {
                        mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);
                        return false;
                    }
                    showEmotionLayout();
                    hideMoreLayout();
                    hideAudioButton();
                    break;
                case R.id.ivMore:
                    if (!mLlMore.isShown()) {
                        if (mElEmotion.isShown()) {
                            showMoreLayout();
                            hideEmotionLayout();
                            hideAudioButton();
                            return true;
                        }
                    }
                    showMoreLayout();
                    hideEmotionLayout();
                    hideAudioButton();
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    private void showEmotionLayout() {
        mElEmotion.setVisibility(View.VISIBLE);
        mIvEmo.setImageResource(R.mipmap.ic_cheat_keyboard);
    }


    public void hideEmotionLayout() {
        mElEmotion.setVisibility(View.GONE);
        mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);
    }

    private void showMoreLayout() {
        mLlMore.setVisibility(View.VISIBLE);

    }

    public void hideMoreLayout() {
        //就是那个文件
        mLlMore.setVisibility(View.GONE);
    }


    private void showAudioButton() {
        mBtnAudio.setVisibility(View.VISIBLE);
        mEtContent.setVisibility(View.GONE);
        mIvAudio.setImageResource(R.mipmap.ic_cheat_keyboard);

        if (mFlEmotionView.isShown()) {
            if (mEmotionKeyboard != null) {
                mEmotionKeyboard.interceptBackPress();
            }
        } else {
            if (mEmotionKeyboard != null) {
                mEmotionKeyboard.hideSoftInput();
            }
        }
    }

    private void hideAudioButton() {
        mBtnAudio.setVisibility(View.GONE);
        mEtContent.setVisibility(View.VISIBLE);
        mIvAudio.setImageResource(R.mipmap.ic_cheat_voice);
    }


    private void setTitle() {
        if (mConversationType == Conversation.ConversationType.PRIVATE) {
            Friend friend = DBManager.getInstance().getFriendById(mSessionId);
            if (friend != null) {
                setToolbarTitle(friend.getDisplayName());
            }
        } else if (mConversationType == Conversation.ConversationType.GROUP) {
            Groups groups = DBManager.getInstance().getGroupsById(mSessionId);
            if (groups != null) {
                setToolbarTitle(groups.getName());
            }
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_session;
    }


    @Override
    protected SessionAtPresenter createPresenter() {
        return new SessionAtPresenter(this, mSessionId, mConversationType);
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mPresenter.loadMore();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public BGARefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    @Override
    public LQRRecyclerView getRvMsg() {
        return mRvMsg;
    }

    @Override
    public EditText getEtContent() {
        return mEtContent;
    }


    /*  =================    表情回调事件   ==================*/
    @Override
    public void onEmojiSelected(String key) {

    }

    @Override
    public void onStickerSelected(String categoryName, String stickerName, String stickerBitmapPath) {
        mPresenter.sendFileMsg(new File(stickerBitmapPath));
        mIvAudio.callOnClick();
        hideAudioButton();
    }


    /* ==================== 音频相关开始========================*/

    /**
     * 初始化音频----
     */
    private void initAudioRecordManager() {
        AudioRecordManager.getInstance(this).setMaxVoiceDuration(AppConst.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND);
        File audioDir = new File(AppConst.AUDIO_SAVE_DIR);
        if (!audioDir.exists()) {
            audioDir.mkdirs();
        }
        AudioRecordManager.getInstance(this).setAudioSavePath(audioDir.getAbsolutePath());
        AudioRecordManager.getInstance(this).setAudioRecordListener(new IAudioRecordListener() {

            private TextView mTimerTV;
            private TextView mStateTV;
            private ImageView mStateIV;
            private PopupWindow mRecordWindow;

            @Override
            public void initTipView() {
                View view = View.inflate(SessionActivity.this, R.layout.popup_audio_wi_vo, null);
                mStateIV = view.findViewById(R.id.rc_audio_state_image);
                mStateTV = view.findViewById(R.id.rc_audio_state_text);
                mTimerTV = view.findViewById(R.id.rc_audio_timer);
                mRecordWindow = new PopupWindow(view, -1, -1);
                mRecordWindow.showAtLocation(mLlRoot, 17, 0, 0);
                mRecordWindow.setFocusable(true);
                mRecordWindow.setOutsideTouchable(false);
                mRecordWindow.setTouchable(false);
            }

            @Override
            public void setTimeoutTipView(int counter) {
                if (this.mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.GONE);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setText(String.format("%s", new Object[]{Integer.valueOf(counter)}));
                    this.mTimerTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void setRecordingTipView() {
                if (this.mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void setAudioShortTipView() {
                if (this.mRecordWindow != null) {
                    mStateIV.setImageResource(R.mipmap.ic_volume_wraning);
                    mStateTV.setText(R.string.voice_short);
                }
            }

            @Override
            public void setCancelTipView() {
                if (this.mRecordWindow != null) {
                    this.mTimerTV.setVisibility(View.GONE);
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.mipmap.ic_volume_cancel);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_cancel);
                    this.mStateTV.setBackgroundResource(R.drawable.corner_voice_style);
                }
            }

            @Override
            public void destroyTipView() {
                if (this.mRecordWindow != null) {
                    this.mRecordWindow.dismiss();
                    this.mRecordWindow = null;
                    this.mStateIV = null;
                    this.mStateTV = null;
                    this.mTimerTV = null;
                }
            }

            @Override
            public void onStartRecord() {
                RongIMClient.getInstance().sendTypingStatus(mConversationType, mSessionId, VoiceMessage.class.getAnnotation(MessageTag.class).value());
            }

            @Override
            public void onFinish(Uri audioPath, int duration) {
                //发送文件
                File file = new File(audioPath.getPath());
                if (file.exists()) {
                    mPresenter.sendAudioFile(audioPath, duration);
                }
            }

            @Override
            public void onAudioDBChanged(int db) {
                switch (db / 5) {
                    case 0:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                        break;
                    case 1:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_2);
                        break;
                    case 2:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_3);
                        break;
                    case 3:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_4);
                        break;
                    case 4:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_5);
                        break;
                    case 5:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_6);
                        break;
                    case 6:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_7);
                        break;
                    default:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_8);
                }
            }
        });
    }

    private boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - 40;

    }


    /* ==================== 音频相关结束========================*/


    /*  =============== 图片视频选择开始 ========================*/

    /**
     * 图片选择----
     */
    public void choosePictureEvent() {
        PictureSelector.create(SessionActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(3)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(false)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(false)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(true)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(mPictureSelectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    public void takePhotoEvent() {
        PictureSelector.create(SessionActivity.this)
                .openCamera(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(5)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(false)// 是否显示拍照按钮
                .enableCrop(false)// 是否裁剪
                .compress(false)// 是否压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(true)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(mCameraList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.TYPE_CAMERA);//结果回调onActivityResult code
    }


    public void takeVideo() {
        PictureSelector.create(SessionActivity.this)
                .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(5)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(false)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .enableCrop(false)// 是否裁剪
                .compress(false)// 是否压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(mVideoSelectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.TYPE_VIDEO);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            showWaitingDialog("请稍等...");
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    mPictureSelectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : mPictureSelectList) {
                        // 应该在子线程中更新操作
                        new Thread(
                                () -> mPresenter.sendImgMsg(ImageUtils.genThumbImgFile(media.getPath()), new File(media.getPath()))
                        ).start();
                    }
                    mPictureSelectList.clear();
                    break;
                case PictureConfig.TYPE_CAMERA:
                    mCameraList = PictureSelector.obtainMultipleResult(data);
                    LocalMedia cameraMedia = mCameraList.get(0);
                    new Thread(
                            () -> mPresenter.sendImgMsg(ImageUtils.genThumbImgFile(cameraMedia.getPath()), new File(cameraMedia.getPath()))
                    ).start();
                    mCameraList.clear();
                    break;
                case PictureConfig.TYPE_VIDEO:
                    mVideoSelectList = PictureSelector.obtainMultipleResult(data);
                    LocalMedia videoMedia = mVideoSelectList.get(0);
                    new Thread(() -> mPresenter.sendFileMsg(new File(videoMedia.getPath()))).start();
                    mVideoSelectList.clear();
                    break;
                default:
                    break;

            }

        }
    }

    /*  =============== 图片视频选择结束 ========================*/


    /* ============================ 音频运行时权限申请开始 ================================*/
    /**
     * 权限
     */
    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    void micNeedsPS() {
        if (mBtnAudio.isShown()) {
            hideAudioButton();
            mEtContent.requestFocus();
            if (mEmotionKeyboard != null) {
                mEmotionKeyboard.showSoftInput();
            }

        } else {
            showAudioButton();
            hideEmotionLayout();
            hideMoreLayout();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SessionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    void micShowRT(final PermissionRequest request) {
        showMaterialDialog(null, "有部分权限需要你的授权", UIUtils.getString(R.string.str_ensure), UIUtils.getString(R.string.str_cancel),
                (dialog, which) -> request.proceed(), (dialog, which) -> request.cancel());
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    void micPSDenied() {
        showMaterialDialog("权限说明", "语音录入需要打开语音的权限，如果不授予可能会影响正常使用!", "赋予权限", "退出",
                (dialog, which) -> SessionActivityPermissionsDispatcher.micNeedsPSWithCheck(SessionActivity.this),
                (dialog, which) -> dialog.dismiss());
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    void micNeverAsk() {
        UIUtils.showToast("不在询问!");
        getAppDetailSettingIntent();
    }
}

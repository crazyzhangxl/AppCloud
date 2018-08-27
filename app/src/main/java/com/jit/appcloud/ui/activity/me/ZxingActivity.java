package com.jit.appcloud.ui.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.manager.JsonMananger;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.message.GroupNotificationMessageData;
import com.jit.appcloud.model.response.GroupMbQyResponse;
import com.jit.appcloud.model.response.NormalResponse;
import com.jit.appcloud.ui.activity.message.PostScriptActivity;
import com.jit.appcloud.ui.activity.message.SessionActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.Check;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.GroupNotificationMessage;

import static com.jit.appcloud.commom.AppConst.SESSION_TYPE_GROUP;

/**
 * @author zxl
 *  description: 二维码扫描界面; 采用QRCodeView进行二维码扫描
 *               支持基本扫描以及照片扫描; 二维码扫描需要一定的权限的，这个得注意下
 *               支持通过扫描 --》 添加好友;加入群聊等操作
 */
public class ZxingActivity extends BaseActivity implements QRCodeView.Delegate {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.ivLight)
    ImageView mIvLight;
    @BindView(R.id.zxingview)
    ZXingView mZxingview;
    @BindView(R.id.ivScan)
    ImageView mIvScan;
    @BindView(R.id.ivGallery)
    ImageView mIvGallery;
    @BindView(R.id.ivOpenGall)
    RelativeLayout mIvOpenGall;
    private List<LocalMedia> selectList = new ArrayList<>();
    private boolean isLightOn = false;
    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(view -> finish());
        mIvOpenGall.setOnClickListener(view -> choosePictureEvent());
        mIvLight.setOnClickListener(v -> {
            if (!isLightOn){
                mZxingview.openFlashlight();
                mIvLight.setImageResource(R.mipmap.ic_light_on);
            }else {
                mZxingview.closeFlashlight();
                mIvLight.setImageResource(R.mipmap.ic_light_off);
            }
            isLightOn = !isLightOn;
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.str_title_zxing);
        mIvLight.setVisibility(View.VISIBLE);
        mZxingview.setDelegate(this);

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_zxing;
    }

    @Override
    protected void init() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    /**
     * 针对二维码结果-进行处理
     *    1. 添加好友 --
     *    2. 进入群聊 --
     * @param result  二位码扫到的结果
     */
    public void handleResult(String result) {
        LogUtils.e("扫描", result);
        mZxingview.startSpot(); // 延迟1.5秒后开始识别
        vibrate();
        // 这里还要进行处理呢别急哦
        if (result.startsWith(AppConst.QrCodeCommon.ADD)) {
            // 判断
            // 首先查询联系人是否已经是好友,是好友的话那么就返回了,否则就进行加入了
            // finish();
            String spString = result.substring(AppConst.QrCodeCommon.ADD.length());
            String[] split = spString.split(";");
            String friendID = split[0];
            String friendName = split[1];
            // 判断是否是好友
            if (UserCache.getId().equals(friendID)){
                UIUtils.showToast("扫到自己咯,哈哈!");
                return;
            }else if (DBManager.getInstance().isMyFriend(friendID)){
                UIUtils.showToast(getString(R.string.this_account_was_your_friend));
                return;
            }
            Intent intent = new Intent(ZxingActivity.this, PostScriptActivity.class);
            intent.putExtra(AppConst.EXTRA_FRIEND_NAME,friendName);
            intent.putExtra(AppConst.EXTRA_FRIEND_ID,friendID);
            jumpToActivity(intent);
            finish();
        }else  if (result.startsWith(AppConst.QrCodeCommon.JOIN)){
            // 进群 -----------------
            String groupId = result.substring(AppConst.QrCodeCommon.JOIN.length());
            showWaitingDialog(getString(R.string.str_please_waiting));
            if (DBManager.getInstance().isInThisGroup(groupId)){
                UIUtils.showToast(UIUtils.getString(R.string.you_already_in_this_group));
                return;
            }else {
                // 申请入群 ----------- 然后进入群聊信息 -------
                ApiRetrofit.getInstance().joinGroup(Integer.parseInt(groupId))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response != null && response.getCode() == 1){
                                // 同步组信息 ----------------
                                DBManager.getInstance().getGroups(groupId);
                                DBManager.getInstance().getGroupMember(groupId);
                                // 延迟1秒执执行操作
                                // 发送了添加群聊的通知
                                List<String> name = new ArrayList<>();
                                List<String> IDS = new ArrayList<>();
                                name.add(UserCache.getName());
                                IDS.add(UserCache.getId());
                                GroupNotificationMessageData messageData = new GroupNotificationMessageData();
                                messageData.setOperatorNickname("通过二维码");
                                messageData.setTargetUserDisplayNames(name);
                                messageData.setTargetUserIds(IDS);
                                GroupNotificationMessage requestMessage = GroupNotificationMessage.
                                        obtain(UserCache.getId(),
                                                GroupNotificationMessage.GROUP_OPERATION_ADD,
                                                JsonMananger.beanToJson(messageData), "");
                                RongIMClient.getInstance().sendMessage(Message.obtain(groupId,
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

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideWaitingDialog();
                                        Intent intent = new Intent(ZxingActivity.this, SessionActivity.class);
                                        intent.putExtra("sessionId", groupId);
                                        intent.putExtra("sessionType", SESSION_TYPE_GROUP);
                                        jumpToActivity(intent);
                                        finish();
                                    }
                                },1000);

                            }else {
                                UIUtils.showToast(response.getMsg());
                            }
                        }, throwable -> {
                            hideWaitingDialog();
                            UIUtils.showToast(throwable.getLocalizedMessage());
                        });
            }


        }else {
            UIUtils.showToast("扫描结果:"+result);
        }

    }

    /**
     * 震动手机
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    /**
     * 扫描二维码成功回调
     */

    @Override
    public void onScanQRCodeSuccess(String result) {
        handleResult(result);
    }


    /**
     * 扫描二维码失败回调
     */

    @Override
    public void onScanQRCodeOpenCameraError() {
        UIUtils.showToast(getString(R.string.str_toast_open_camera_error));
    }

    /*===============================打开相册开始============================*/

    public void choosePictureEvent() {
        PictureSelector.create(ZxingActivity.this)
                .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
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
                .selectionMedia(selectList)// 是否传入已选图片
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    /* for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }*/
                    String path = selectList.get(0).getPath();
                    String spotResult = QRCodeDecoder.syncDecodeQRCode(path);
                    if (Check.isNull(spotResult)) {
                        UIUtils.showToast("未发现二维码!");
                    } else {
                        handleResult(spotResult);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /*==============================打开相册回调结束了============================*/


    /**
     * 生命周期开启和关闭
     */
    @Override
    protected void onStart() {
        super.onStart();
        mZxingview.startCamera();
        // 开始扫描并展示扫描框
        mZxingview.startSpotAndShowRect();

    }

    @Override
    protected void onStop() {
        mZxingview.stopCamera();
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        mZxingview.onDestroy();
        super.onDestroy();
    }
}

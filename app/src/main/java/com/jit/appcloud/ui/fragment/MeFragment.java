package com.jit.appcloud.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.ui.activity.ChangePwdActivity;
import com.jit.appcloud.ui.activity.MainActivity;
import com.jit.appcloud.ui.activity.me.CalendarActivity;
import com.jit.appcloud.ui.activity.me.EditSignatureActivity;
import com.jit.appcloud.ui.activity.me.FmDataManageActivity;
import com.jit.appcloud.ui.activity.me.FmDeviceManageActivity;
import com.jit.appcloud.ui.activity.me.FmPondManageActivity;
import com.jit.appcloud.ui.activity.me.GalleryNiceActivity;
import com.jit.appcloud.ui.activity.me.SettingActivity;
import com.jit.appcloud.ui.activity.me.ViceUserMgActivity;
import com.jit.appcloud.ui.activity.me.WeatherActivity;
import com.jit.appcloud.ui.activity.me.ZxingActivity;
import com.jit.appcloud.ui.base.BaseFragment;
import com.jit.appcloud.ui.presenter.MeFgPresenter;
import com.jit.appcloud.ui.view.IMeFgView;
import com.jit.appcloud.util.GlideLoaderUtils;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.CustomDialog;
import com.jit.appcloud.widget.ManInfoPpWindow;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 *
 * @author 张先磊
 * @date 2018/4/17
 */
@RuntimePermissions
public class MeFragment extends BaseFragment<IMeFgView, MeFgPresenter> {
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
    @BindView(R.id.ivHeader)
    CircleImageView mIvHeader;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvSignature)
    TextView mTvSignature;
    @BindView(R.id.ivQRCordCard)
    ImageView mIvQRCordCard;
    @BindView(R.id.llMyInfo)
    LinearLayout mLlMyInfo;
    @BindView(R.id.stv_me_calendar)
    SuperTextView mStvMeCalendar;
    @BindView(R.id.stv_me_weather)
    SuperTextView mStvMeWeather;
    @BindView(R.id.stv_me_zxing)
    SuperTextView mStvMeZxing;
    @BindView(R.id.stvAlPond)
    SuperTextView mStvAlPond;
    @BindView(R.id.stv_me_setting)
    SuperTextView mStvMeSetting;
    @BindView(R.id.stv_change_pwd)
    SuperTextView mStvChangePwd;
    @BindView(R.id.stv_register)
    SuperTextView mStvRegister;
    @BindView(R.id.stvAlData)
    SuperTextView mStvAlData;
    @BindView(R.id.stvAlDevice)
    SuperTextView mStvAlDevice;
    @BindView(R.id.stv_me_gallery)
    SuperTextView mStvMeGallery;
    @BindView(R.id.ivEditor)
    ImageView mIvEditorSign;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.stvDn)
    SuperTextView mStvDn;
    @BindView(R.id.stvBank)
    SuperTextView mStvBank;
    @BindView(R.id.rlInfo)
    RelativeLayout mRlInfo;
    private CustomDialog mQrCardDialog;
    private MainActivity mActivity;
    public List<LocalMedia> mSingleSelectList = new ArrayList<>();
    private ManInfoPpWindow mManInfoPpWindow;
    private String mSign;
    private String mImage;
    private String mRealName;

    @Override
    public void init() {
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void initView(View rootView) {
        mIvToolbarNavigation.setVisibility(View.GONE);
        mVToolbarDivision.setVisibility(View.INVISIBLE);
        mTvToolbarTitle.setText(R.string.title_personal_info);
        /* ======== 根据用户的身份进行显示与影藏操作 ========= */
        if (AppConst.ROLE_FARMER.equals(UserCache.getRole())) {
            mStvAlPond.setVisibility(View.VISIBLE);
            mStvAlDevice.setVisibility(View.VISIBLE);
            mStvRegister.setVisibility(View.GONE);
        }else if (AppConst.ROLE_GENERAL_AGENCY.equals(UserCache.getRole())){
            mStvDn.setVisibility(View.VISIBLE);
            mStvBank.setVisibility(View.VISIBLE);
        }else if (AppConst.ROLE_VICE_MANAGER.equals(UserCache.getRole())
                || AppConst.ROLE_VICE_AGENT.equals(UserCache.getRole())
                || AppConst.ROLE_VICE_ADMIN.equals(UserCache.getRole())){
            mStvMeGallery.setVisibility(View.GONE);
            mStvChangePwd.setVisibility(View.GONE);
            mStvRegister.setVisibility(View.GONE);
            mRlInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().getUserInfoByName(UserCache.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInfoResponse -> {
                    hideWaitingDialog();
                    if (userInfoResponse.getCode() == 1) {
                        mRealName = userInfoResponse.getData().getRealname();
                        mSign = userInfoResponse.getData().getSign();
                        mImage = userInfoResponse.getData().getImage();
                        updateViews();// 更新View
                    } else {
                        UIUtils.showToast(userInfoResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    private void queryPhotosAndShow() {
        ApiRetrofit.getInstance().queryAllPhotos(UserCache.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photoResponse -> {
                    if (photoResponse.getCode() == 1) {
                        mManInfoPpWindow.setHeadImage(mImage);
                        mManInfoPpWindow.setSignature(mSign);
                        mManInfoPpWindow.setListGallery(photoResponse.getData());
                        mManInfoPpWindow.showAsDropDown(mAppBarLayout);
                    } else {
                        UIUtils.showToast(photoResponse.getMsg());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

    private void updateViews() {
        Glide.with(mActivity).load(mImage)
                .apply(new RequestOptions().placeholder(R.mipmap.default_header)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)).into(mIvHeader);
        mTvName.setText(mRealName);
        if (!TextUtils.isEmpty(mSign)) {
            mTvSignature.setText(mSign);
        }
        mManInfoPpWindow = new ManInfoPpWindow(getActivity());
        mManInfoPpWindow.setUserName(mRealName);
    }
    @Override
    public void initListener() {
        mLlMyInfo.setOnClickListener(view -> {
            if (UserCache.getRole().equals(AppConst.ROLE_VICE_AGENT)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_ADMIN)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_MANAGER)){
                UIUtils.showToast(getString(R.string.please_permission_denied));
                return;
            }
            if (mManInfoPpWindow != null && mManInfoPpWindow.isShowing()) {
                mManInfoPpWindow.dismiss();
            } else {
                if (mManInfoPpWindow != null) {
                    queryPhotosAndShow();
                }
            }
        });

        mIvQRCordCard.setOnClickListener(view -> {
            if (UserCache.getRole().equals(AppConst.ROLE_VICE_AGENT)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_ADMIN)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_MANAGER)){
                UIUtils.showToast(getString(R.string.please_permission_denied));
                return;
            }
            // 生成二维码
            showQRDialog();
        });

        mIvHeader.setOnClickListener(v -> {
            if (UserCache.getRole().equals(AppConst.ROLE_VICE_AGENT)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_ADMIN)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_MANAGER)){
                UIUtils.showToast(getString(R.string.please_permission_denied));
                return;
            }
            mSingleSelectList.clear();
            chooseSinglePictureEvent();
        });


        mStvMeCalendar.setOnSuperTextViewClickListener(superTextView -> startActivity(new Intent(getActivity(), CalendarActivity.class)));

        mStvMeWeather.setOnSuperTextViewClickListener(superTextView -> startActivity(new Intent(getActivity(), WeatherActivity.class)));

        mStvMeZxing.setOnSuperTextViewClickListener(superTextView -> {
            // 这里需要来时申请权限了 申请完了才能进入
            MeFragmentPermissionsDispatcher.goToZxingAtWithCheck(this);
        });

        mStvMeSetting.setOnSuperTextViewClickListener(superTextView -> jumpToActivity(SettingActivity.class));

        mStvChangePwd.setOnSuperTextViewClickListener(superTextView -> jumpToActivity(ChangePwdActivity.class));

        mStvRegister.setOnSuperTextViewClickListener(superTextView -> jumpToActivity(ViceUserMgActivity.class));
        /* 养殖户塘口管理 */
        mStvAlPond.setOnSuperTextViewClickListener(superTextView -> MeFragment.this.jumpToActivity(FmPondManageActivity.class));

        mStvAlData.setOnSuperTextViewClickListener(superTextView -> jumpToActivity(FmDataManageActivity.class));

        mStvAlDevice.setOnSuperTextViewClickListener(superTextView -> jumpToActivity(FmDeviceManageActivity.class));

        mStvMeGallery.setOnSuperTextViewClickListener(superTextView -> MeFragment.this.jumpToActivity(GalleryNiceActivity.class));

        mIvEditorSign.setOnClickListener(v -> {
            // 编辑信息
            if (UserCache.getRole().equals(AppConst.ROLE_VICE_AGENT)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_ADMIN)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_MANAGER)){
                UIUtils.showToast(getString(R.string.please_permission_denied));
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.CONTENT_FROM_SIGNATURE, mTvSignature.getText().toString().trim());
            jumpToActivityForResult(EditSignatureActivity.class, bundle, AppConst.RECODE_EDIT_FROM_SIGNATURE);
        });

        mStvBank.setOnSuperTextViewClickListener(superTextView -> UIUtils.showToast(getString(R.string.please_no_provide)));

        mStvDn.setOnSuperTextViewClickListener(superTextView -> UIUtils.showToast(getString(R.string.please_no_provide)));

    }

    @Override
    protected MeFgPresenter createPresenter() {
        return new MeFgPresenter((MainActivity) getActivity());
    }

    private void showQRDialog() {
        // 不为空就加载就好了,免得多次生成啊
        if (mQrCardDialog == null) {
            View qrCardView = View.inflate(getActivity(), R.layout.include_qrcode_card, null);
            ImageView ivHeader = (ImageView) qrCardView.findViewById(R.id.ivHeader);
            TextView tvName = (TextView) qrCardView.findViewById(R.id.tvName);
            ImageView ivCard = (ImageView) qrCardView.findViewById(R.id.ivCard);
            TextView tvTip = (TextView) qrCardView.findViewById(R.id.tvTip);
            tvTip.setText(UIUtils.getString(R.string.qr_code_card_tip));
            // 获取用户信息
            tvName.setText(UserCache.getName());
            GlideLoaderUtils.displayRound(getActivity(),ivHeader,mImage);
            Observable.just(QRCodeEncoder.syncEncodeQRCode(AppConst.QrCodeCommon.ADD + UserCache.getId()+";"+UserCache.getName(), UIUtils.dip2Px(100), R.color.black))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Bitmap>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Bitmap bitmap) {
                            ivCard.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.e("二维码生成", e.getLocalizedMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            /*360 540*/
            mQrCardDialog = new CustomDialog(getActivity(), 300, 400, qrCardView, R.style.MyDialog);
        }
        mQrCardDialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mQrCardDialog != null) {
            mQrCardDialog = null;
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_me;
    }

    //============= 获得二维码开始权限 ============

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void goToZxingAt() {
        startActivity(new Intent(getActivity(), ZxingActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MeFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationale(final PermissionRequest request) {
        showMaterialDialog(null, "有部分权限需要你的授权", "确定", "取消",
                (dialog, which) -> request.proceed(), (dialog, which) -> request.cancel());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void permissionDenied() {
        showMaterialDialog("权限说明", "扫描二维码需要打开相机和散光灯的权限，如果不授予可能会影响正常使用！", "赋予权限", "退出",
                (dialog, which) -> MeFragmentPermissionsDispatcher.goToZxingAtWithCheck(this),
                (dialog, which) -> dialog.dismiss());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void neverAskAgain() {
        Toast.makeText(_mActivity, "不再询问授权", Toast.LENGTH_SHORT).show();
        getAppDetailSettingIntent();
    }

    //============= 获得二维码权限结束 ============


    private void chooseSinglePictureEvent() {
        PictureSelector.create(MeFragment.this)
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
                .selectionMedia(mSingleSelectList)// 是否传入已选图片
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
                .forResult(PictureConfig.SINGLE);//结果回调onActivityResult code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.SINGLE:
                    mSingleSelectList = PictureSelector.obtainMultipleResult(data);
                    String filePath = mSingleSelectList.get(0).getPath();
                    mActivity.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
                    ApiRetrofit.getInstance().postHeadImage(UserCache.getToken(), new File(filePath))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(userHeadPostResponse -> {
                                mActivity.hideWaitingDialog();
                                if (userHeadPostResponse.getCode() == 1) {
                                    Glide.with(mActivity).load(filePath)
                                            .apply(new RequestOptions().error(R.mipmap.default_header)
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                                            .into(mIvHeader);
                                    mImage = filePath;
                                    // 通知群聊和聊天 -- 个人信息修改
                                    UserCache.setHeadImage(userHeadPostResponse.getData().getImage());
                                    BroadcastManager.getInstance(getActivity()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                                    BroadcastManager.getInstance(getActivity()).sendBroadcast(AppConst.UPDATE_GROUP);
                                } else {
                                    UIUtils.showToast(userHeadPostResponse.getMsg());
                                }
                            }, throwable -> {
                                mActivity.hideWaitingDialog();
                                UIUtils.showToast(throwable.getLocalizedMessage());
                            });
                    break;
                case AppConst.RECODE_EDIT_FROM_SIGNATURE:
                    mSign = data.getStringExtra(AppConst.RESULT_FROM_EDIT);
                    mTvSignature.setText(mSign);
                    break;
                default:
                    break;
            }
        }
    }
}

package com.jit.appcloud.ui.activity.message;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.ui.activity.me.ZxingActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author zxl on 2018/07/24.
 *         discription: 二维码图片的滑动
 */
@RuntimePermissions
public class QRCodeCardActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.ivHeader)
    CircleImageView mIvHeader;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.ivCard)
    ImageView mIvCard;
    @BindView(R.id.ibToolbarMore)
    ImageButton mIbToolbarMore;
    @BindView(R.id.tvSaveToPhone)
    TextView mTvSaveToPhone;
    @BindView(R.id.openQrcode)
    TextView mOpenQrcode;
    @BindView(R.id.rlMenu)
    RelativeLayout mRlMenu;
    @BindView(R.id.svMenu)
    ScrollView mSvMenu;
    @BindView(R.id.llContent)
    LinearLayout mLlContent;
    private String mGroupID;
    private String mGroupsName;

    @Override
    protected void init() {
        mGroupID = getIntent().getStringExtra(AppConst.EXTRA_GROUP_ID);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_qrcode_card;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mTvToolbarTitle.setText(R.string.title_group_qrcode);
        mIbToolbarMore.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        Observable.just(DBManager.getInstance().getGroupsById(mGroupID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(groups -> {
                    if (groups != null) {
                        String portraitUri = groups.getPortraitUri();
                        setQRCode(AppConst.QrCodeCommon.JOIN+mGroupID, portraitUri);
                        Glide.with(this).load(portraitUri).apply(new RequestOptions().placeholder(R.mipmap.default_header))
                                .into(mIvHeader);
                        mGroupsName = groups.getName();
                        mTvName.setText(mGroupsName);
                    }
                }, throwable -> LogUtils.e("错误",throwable.getLocalizedMessage()));


    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mRlMenu.setOnClickListener(v -> hideMenu());
        mIbToolbarMore.setOnClickListener(v -> showMenu());
        mTvSaveToPhone.setOnClickListener(v -> hideShootAndSavePhone());
        mOpenQrcode.setOnClickListener(v -> {
            hideMenu();
            QRCodeCardActivityPermissionsDispatcher.needsPermissionWithCheck(this);
        });
    }

    private void hideShootAndSavePhone() {
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mRlMenu.setVisibility(View.GONE);
                shootWithSave();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ta.setDuration(200);
        mSvMenu.startAnimation(ta);
    }

    private void shootWithSave() {
        Bitmap bitmap = loadBitmapFromView(mLlContent);
        Observable.just(bitmap).flatMap(bitmap1 -> {
            if (bitmap1 == null) {
                return null;
            }

            File appDir = new File(Environment.getExternalStorageDirectory(), AppConst.PS_SAVE_DIR);
            if (!appDir.exists()) {
                appDir.mkdir();
            }

            String fileName = TimeUtil.date2String(new Date(), mGroupsName+"-截图") + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                assert bitmap1 != null;
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            Uri uri = Uri.fromFile(file);
                /* t通知图库更新*/
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            mContext.sendBroadcast(scannerIntent);
            return Observable.just(uri.getPath());
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (null != s){
                        String format = String.format(UIUtils.getString(R.string.picture_has_save_to), s);
                        UIUtils.showToast(format);
                    }else {
                        UIUtils.showToast(getString(R.string.picture_save_error));
                    }
                }, throwable -> {
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    /**
     * 对单独某个View进行截图
     *
     * @param v
     * @return
     */
    private Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        c.translate(-v.getScrollX(), -v.getScrollY());
        v.draw(c);
        return screenshot;
    }


    /**
     * 展示菜单
     */
    private void showMenu() {
        mRlMenu.setVisibility(View.VISIBLE);
        TranslateAnimation ta =
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 1,
                        Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(200);
        mSvMenu.startAnimation(ta);
    }




    /**
     * 隐藏菜单
     */
    private void hideMenu() {
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mRlMenu.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ta.setDuration(200);
        mSvMenu.startAnimation(ta);
    }

    private void setQRCode(String content, String portraitUri) {
        Observable.just(QRCodeEncoder.syncEncodeQRCode(
                content
                , UIUtils.dip2Px(100)
                , R.color.black))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        mIvCard.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("二维码生成", e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void needsPermission() {
        jumpToActivity(ZxingActivity.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        QRCodeCardActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationale(final PermissionRequest request) {
        showMaterialDialog(null, "有部分权限需要你的授权", "确定", "取消",
                (dialog, which) -> request.proceed(), (dialog, which) -> request.cancel());
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void permissionDenied() {
        showMaterialDialog("权限说明", "扫描二维码需要打开相机和散光灯的权限，如果不授予可能会影响正常使用！", "赋予权限", "退出",
                (dialog, which) -> QRCodeCardActivityPermissionsDispatcher.needsPermissionWithCheck(this),
                (dialog, which) -> dialog.dismiss());
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void neverAsk() {
        Toast.makeText(this, "不再询问授权", Toast.LENGTH_SHORT).show();
        getAppDetailSettingIntent();
    }


    //============= 获得二维码权限结束 ============


}

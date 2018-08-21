package com.jit.appcloud.ui.activity.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.RxAppcloud;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.StatusBarCompat;
import com.luck.picture.lib.photoview.PhotoView;
import java.io.File;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author 张先磊
 */
public class ShowBigImageActivity extends BaseActivity {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.iv_save)
    ImageView mIvSave;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.preview_image)
    PhotoView mPreviewImage;
    private boolean mIsHidden = false;
    private String mUrl;

    /**
     * 实现的过度动画哦
     * @param mContext
     * @param view
     * @param imgUrl
     */
    public static void startAction(Context mContext, View view,String imgUrl) {
        Intent intent = new Intent(mContext, ShowBigImageActivity.class);
        intent.putExtra(AppConst.SHOW_IMAGE_URL, imgUrl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity) mContext,view, AppConst.TRANSITION_ANIMATION_NEWS_PHOTOS);
            ActivityCompat.startActivity(mContext,intent, options.toBundle());
        } else {

            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        // 那我就设置状态栏颜色
        StatusBarCompat.setStatusBarColor(this,UIUtils.getColor(R.color.black));
        mTvToolbarTitle.setText("大图查看");
        /* ======== 关联 =======*/
        ViewCompat.setTransitionName(mPreviewImage, AppConst.TRANSITION_ANIMATION_NEWS_PHOTOS);
        mIvSave.setVisibility(View.VISIBLE);
        mPreviewImage.setZoomable(true);
        setAppBarAlpha(0.7f);
        Glide.with(this).load(mUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(mPreviewImage);
        setupPhotoAttacher();

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mIvShare.setOnClickListener(v -> {

        });

        mIvSave.setOnClickListener(v -> saveImageToGallery());
    }

    private void setupPhotoAttacher() {
        mPreviewImage.setOnClickListener(v -> hideOrShowToolbar());
        mPreviewImage.setOnLongClickListener(v -> {

            showMaterialDialog(null, "保存到手机?", "确定", "取消",
                    (dialog, which) -> {
                        saveImageToGallery();
                        dialog.dismiss();
                    }, (dialog, which) -> dialog.dismiss());
            return true;
        });
    }

    private void saveImageToGallery() {
        RxAppcloud.saveImageAndGetPathObservable(mContext,mUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uri -> {
                    File appDir = new File(Environment.getExternalStorageDirectory(), AppConst.PS_SAVE_DIR);
                    String format = String.format(UIUtils.getString(R.string.picture_has_save_to), appDir.getAbsoluteFile());
                    UIUtils.showToast(format);
                }, throwable -> UIUtils.showToast(throwable.getMessage()+"\n再试试..."));
    }


    @Override
    protected int provideContentViewId() {
        return R.layout.activity_show_big_image;
    }

    @Override
    protected void init() {
        mUrl = getIntent().getStringExtra(AppConst.SHOW_IMAGE_URL);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    private void setAppBarAlpha(float alpha) {
        mAppBarLayout.setAlpha(alpha);
    }

    private void hideOrShowToolbar() {
        mAppBarLayout.animate()
                .translationY(mIsHidden ? 0 : -mAppBarLayout.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

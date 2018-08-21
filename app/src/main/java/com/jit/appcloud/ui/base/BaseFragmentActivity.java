package com.jit.appcloud.ui.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jit.appcloud.app.MyApp;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 张先磊 on 2018/5/2.
 */

public abstract class BaseFragmentActivity<V,T extends BaseFragmentPresenter<V>> extends FragmentActivity{

    protected T mPresenter;
    protected Context mContext;
    private MaterialDialog mMaterialDialog;
    private MaterialDialog mWaitMaterialDialog;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.activities.add(this);
        mContext = this;
        init();
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);//因为之后所有的子类都要实现对应的View接口
        }
        setContentView(provideContentViewId());
        mUnbinder = ButterKnife.bind(this);
        initView(savedInstanceState);
        initData();
        initListener();
    }

    //在setContentView()调用之前调用，可以设置WindowFeature(如：this.requestWindowFeature(Window.FEATURE_NO_TITLE);)
    protected abstract void init();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void initListener();


    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    public void jumpToActivity(Intent intent) {
        startActivity(intent);
    }

    public void jumpToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }



    public void showWaitingDialog(String tip) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(tip)
                .progress(true, 0)
                .progressIndeterminateStyle(false);
        mWaitMaterialDialog = builder.build();
        mWaitMaterialDialog.setCancelable(false);
        mWaitMaterialDialog.show();
    }



    public void hideWaitingDialog() {
        if (mWaitMaterialDialog != null) {
            mWaitMaterialDialog.dismiss();
            mWaitMaterialDialog = null;
        }
    }



    /**
     * 显示MaterialDialog
     */
    public MaterialDialog showMaterialDialog(String title, String message, String positiveText, String negativeText, MaterialDialog.SingleButtonCallback positiveCallBack,MaterialDialog.SingleButtonCallback negativeCallBack) {
        hideMaterialDialog();
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        if (!TextUtils.isEmpty(title)) {
            builder.title(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.content(message);
        }
        if (!TextUtils.isEmpty(positiveText)) {
            builder.positiveText(positiveText).onPositive(positiveCallBack);
        }
        if (!TextUtils.isEmpty(negativeText)) {
            builder.negativeText(negativeText).onNegative(negativeCallBack);
        }
        mMaterialDialog = builder.build();
        mMaterialDialog.show();
        return mMaterialDialog;
    }

    /**
     * 隐藏MaterialDialog
     */
    public void hideMaterialDialog() {
        if (mMaterialDialog != null) {
            mMaterialDialog.dismiss();
            mMaterialDialog = null;
        }
    }

    /**
     * 跳转到设置权限的界面
     * */
    public void getAppDetailSettingIntent() {
        // 可以看下这篇文章
        // https://blog.csdn.net/cbbbc/article/details/60148864
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        MyApp.activities.remove(this);
        if (mUnbinder != null){
            mUnbinder.unbind();
        }
    }

}

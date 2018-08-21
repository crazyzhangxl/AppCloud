package com.jit.appcloud.ui.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by 张先磊 on 2018/4/17.
 */

public abstract class BaseFragment<V,T extends BasePresenter<V>> extends SupportFragment {
    protected T mPresenter;
    private MaterialDialog mMaterialDialog;
    private MaterialDialog mWaitMaterialDialog;
    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        //判断是否使用MVP模式
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);//因为之后所有的子类都要实现对应的View接口
        }
    }

    // 先
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(provideContentViewId(),container,false);
        mUnbinder = ButterKnife.bind(this, rootView);
        initView(rootView);
        return rootView;//还是挺不错的
    }


    /**
     * 后
     * 这个可能在初始化的时候不会得到执行
     * @param savedInstanceState 维系的参数
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null){
            mUnbinder.unbind();
        }
    }

    public abstract void init();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    public abstract void initView(View rootView) ;

    public abstract void initData();

    public abstract void initListener();


    public void jumpToActivity(Intent intent) {
        startActivity(intent);
    }

    public void jumpToActivity(Class activity) {
        Intent intent = new Intent(getActivity(), activity);
        startActivity(intent);
    }

    /**
     *  含有bundle跳转
     *  */
    public void jumpToActivity(Class<?> cls,Bundle bundle){
        Intent intent = new Intent(getActivity(),cls);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void jumpToActivityForResult(Class<?> cls,int requestCode){
        jumpToActivityForResult(cls,null,requestCode);
    }

    public void jumpToActivityForResult(Class<?> cls,Bundle bundle,int requestCode){
        Intent intent = new Intent(getActivity(),cls);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }

    public void jumpToActivityAndClearTask(Class activity) {
        Intent intent = new Intent(getActivity(), activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }



    public void showWaitingDialog(String tip) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
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
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
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
            intent.setData(Uri.fromParts("package",getActivity().getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getActivity().getPackageName());
        }
        startActivity(intent);
    }

}

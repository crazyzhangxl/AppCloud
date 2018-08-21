package com.jit.appcloud.ui.activity.me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.ViceMgAtPresenter;
import com.jit.appcloud.ui.view.IViceMGAtView;
import butterknife.BindView;

/**
 * @author zxl on 2018/07/16.
 *         discription: 平行用户的管理
 */
public class ViceUserMgActivity extends BaseActivity<IViceMGAtView, ViceMgAtPresenter> implements IViceMGAtView {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tvViceAdd)
    TextView mTvViceAdd;
    @BindView(R.id.tvViceEditor)
    TextView mTvViceEditor;
    @BindView(R.id.tvViceDel)
    TextView mTvViceDel;
    @BindView(R.id.rvViceMg)
    RecyclerView mRvViceMg;

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_vice_user_mg;
    }

    @Override
    protected ViceMgAtPresenter createPresenter() {
        return new ViceMgAtPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.title_vice_manage);
        mPresenter.initViceInfo();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mTvViceAdd.setOnClickListener(v -> mPresenter.addViceUser());

        mTvViceDel.setOnClickListener(v -> mPresenter.delViceUser());

        mTvViceEditor.setOnClickListener(v -> mPresenter.editorViceUser());
    }

    @Override
    public RecyclerView getRvViceMG() {
        return mRvViceMg;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(this).register(AppConst.UPDATE_VICE_INFO, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.refreshViceData();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(this).unregister(AppConst.UPDATE_VICE_INFO);
    }
}

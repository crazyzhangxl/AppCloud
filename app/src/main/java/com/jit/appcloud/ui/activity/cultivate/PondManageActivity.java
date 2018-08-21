package com.jit.appcloud.ui.activity.cultivate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserOtherCache;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.PondManageAtPresenter;
import com.jit.appcloud.ui.view.IPondManageView;

import butterknife.BindView;

/**
 * @author zxl on 2018/05/23.
 *         discription: 塘口管理的活动
 *         <p>
 *         提供给管理员，删除，修改，增加塘口的权限
 *
 *         //  稍等下吧，这里还是单个农户的塘口比较好吧,后面总体的再添加吧
 *
 */
public class PondManageActivity extends BaseActivity<IPondManageView, PondManageAtPresenter> implements IPondManageView {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.lqrPondManage)
    RecyclerView mRVPondManage;
    @BindView(R.id.btnAddPond)
    Button mBtnAddPond;
    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_pond_manage;
    }

    @Override
    protected PondManageAtPresenter createPresenter() {
        return new PondManageAtPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.str_title_manage_pond);
        mPresenter.setAdapterWithData(UserOtherCache.getFarmSelectedName());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> finish());
        mBtnAddPond.setOnClickListener(v -> {
            /* 看有没有必要传递责任人过去,还是怎么说 */
            jumpToActivity(PondAdByMGActivity.class);
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(mContext).register(AppConst.UPDATE_POND_MG, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.refreshData();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(mContext).unregister(AppConst.UPDATE_POND_MG);
    }

    @Override
    public RecyclerView getPondManageRV() {
        return mRVPondManage;
    }
}

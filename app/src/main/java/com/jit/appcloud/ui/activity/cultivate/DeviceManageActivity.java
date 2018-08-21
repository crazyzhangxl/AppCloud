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
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.DeviceManageAtPresenter;
import com.jit.appcloud.ui.view.IDeviceManageView;
import butterknife.BindView;
/**
 * @author zxl on 2018/06/05.
 *         discription: 设备管理活动
 *         auth: Manage
 */
public class DeviceManageActivity extends BaseActivity<IDeviceManageView, DeviceManageAtPresenter> implements IDeviceManageView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.rcvDeviceManage)
    RecyclerView mRcvDeviceManage;
    @BindView(R.id.btnAddDevice)
    Button mBtnAddDevice;

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_device_manage;
    }

    @Override
    protected DeviceManageAtPresenter createPresenter() {
        return new DeviceManageAtPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("设备管理");
        mPresenter.setAdapterWithData();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mBtnAddDevice.setOnClickListener(v -> jumpToActivity(DeviceAdByMGActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(mContext).register(AppConst.DEVICE_UPDATE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.refreshData();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(mContext).unregister(AppConst.DEVICE_UPDATE);
    }

    @Override
    public RecyclerView getDeviceManageRV() {
        return mRcvDeviceManage;
    }


}

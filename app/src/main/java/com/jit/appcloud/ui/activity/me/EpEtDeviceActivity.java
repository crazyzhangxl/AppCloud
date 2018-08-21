package com.jit.appcloud.ui.activity.me;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.db_model.AgDeviceDetailBean;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.request.EpDeviceRequest;
import com.jit.appcloud.model.response.EpDeviceResponse;
import com.jit.appcloud.model.response.PondGetByMGResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/07/13.
 *         discription: 养殖户设备编辑活动
 */
public class EpEtDeviceActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.etDeviceId)
    EditText mEtDeviceId;
    @BindView(R.id.tvSearchDecId)
    TextView mTvSearchDecId;
    @BindView(R.id.etMacIP)
    EditText mEtMacIP;
    @BindView(R.id.tvSearchMacIP)
    TextView mTvSearchMacIP;
    @BindView(R.id.mSPPond)
    MaterialSpinner mMSPPond;
    @BindView(R.id.etWorkAddress)
    EditText mEtWorkAddress;
    @BindView(R.id.tvAddFunc)
    TextView mTvAddFunc;
    @BindView(R.id.tvDeviceNum)
    TextView mTvDeviceNum;
    @BindView(R.id.llIncrease)
    LinearLayout mLlIncrease;
    private List<PondGetByMGResponse.DataBean> mMEpPondList;
    private List<String> mPondStrList = new ArrayList<>();
    private EpDeviceResponse.DataBean mDeviceBean;

    @Override
    protected void init() {
        if (getIntent() != null){
            mDeviceBean = (EpDeviceResponse.DataBean) getIntent().getExtras().getSerializable(AppConst.EXTRA_SER_AG_DEVICE_EDITOR);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_ag_device_editor;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(getString(R.string.str_title_save));
        mTvToolbarTitle.setText("编辑设备");
        initNormal();
        //initFun();
    }

    private void initNormal() {
        if (mDeviceBean != null) {
            mEtDeviceId.setText(String.valueOf(mDeviceBean.getDevice_no()));
            mEtMacIP.setText(mDeviceBean.getMac_ip());
            mEtWorkAddress.setText(mDeviceBean.getAddress());
        }
    }

    private void initFun() {
        List<String> mFunList = Arrays.asList(AppConst.LIST_DEVICE_CH);
        //动了手脚了
        List<AgDeviceDetailBean> agDeviceDetailBeans = null;
        if (agDeviceDetailBeans != null){
            for (int i=0;i<agDeviceDetailBeans.size();i++){
                AgDeviceDetailBean agDeviceDetailBean = agDeviceDetailBeans.get(i);
                /* 增加下面的功能*/
                View funcView = LayoutInflater.from(mContext).inflate(R.layout.item_device_func_add, null);
                MaterialSpinner mSpFun = funcView.findViewById(R.id.spDeviceFun);
                mSpFun.setItems(AppConst.LIST_DEVICE_CH);
                mSpFun.setSelectedIndex(mFunList.indexOf(agDeviceDetailBean.getFunctionName()));

                EditText etY1 =  funcView.findViewById(R.id.etYellow1);
                EditText etY2 =  funcView.findViewById(R.id.etYellow2);
                EditText etY3 =  funcView.findViewById(R.id.etYellow3);
                EditText etY4 =  funcView.findViewById(R.id.etYellow4);
                EditText etG1 =  funcView.findViewById(R.id.etGreen1);
                EditText etG2 =  funcView.findViewById(R.id.etGreen2);
                etY1.setText(String.valueOf(agDeviceDetailBean.getYellow1()));
                etY2.setText(String.valueOf(agDeviceDetailBean.getYellow2()));
                etY3.setText(String.valueOf(agDeviceDetailBean.getYellow3()));
                etY4.setText(String.valueOf(agDeviceDetailBean.getYellow4()));
                etG1.setText(String.valueOf(agDeviceDetailBean.getGreen1()));
                etG2.setText(String.valueOf(agDeviceDetailBean.getGreen2()));
                TextView tvFuncCount = funcView.findViewById(R.id.tvFuncCount);
                tvFuncCount.setText(String.valueOf(mLlIncrease.getChildCount()+1));
                funcView.findViewById(R.id.ivDelFun).setOnClickListener(v -> {
                    mLlIncrease.removeView(funcView);
                    refreshFunNum();
                    mTvDeviceNum.setText(String.format("记录数:%s",mLlIncrease.getChildCount()));
                });

                mLlIncrease.addView(funcView);
        /* 显示记录数 */
                mTvDeviceNum.setText(String.format("记录数:%s",mLlIncrease.getChildCount()));

            }

        }

    }

    /**
     * 重新编号---
     * */
    private void refreshFunNum() {
        for (int i=0;i<mLlIncrease.getChildCount();i++){
            View view = mLlIncrease.getChildAt(i);
            TextView tvNum =  view.findViewById(R.id.tvFuncCount);
            tvNum.setText(String.valueOf(i+1));
        }

    }

    @Override
    protected void initData() {
        showWaitingDialog(getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().getPondByEp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pondGetByMGResponse -> {
                    if (pondGetByMGResponse != null && pondGetByMGResponse.getCode() == 1){
                        hideWaitingDialog();
                        mMEpPondList = pondGetByMGResponse.getData();
                        mPondStrList.clear();
                        for (PondGetByMGResponse.DataBean bean:mMEpPondList){
                            mPondStrList.add(bean.getNumber());
                        }
                        mMSPPond.setItems(mPondStrList);
                        // 设置换种的Id即可
                        refreshMSP();
                    }else {
                        UIUtils.showToast(pondGetByMGResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    /**
     * 重新刷新MaterialSpinner
     * */
    private void refreshMSP() {
        if (mDeviceBean != null){
            int id = mDeviceBean.getPound_id();
            int selectPosition = -1;
            for (int i=0;i<mMEpPondList.size();i++){
                PondGetByMGResponse.DataBean pondBean = mMEpPondList.get(i);
                if (id == pondBean.getId()){
                    selectPosition = i;
                    break;
                }
            }
            mMSPPond.setSelectedIndex(selectPosition);
        }
    }

    private void updateDevice() {
        String deviceId = mEtDeviceId.getText().toString();
        if (TextUtils.isEmpty(deviceId)){
            UIUtils.showToast("请输入设备ID");
            return;
        }
        String macIp =  mEtMacIP.getText().toString();
        if (TextUtils.isEmpty(macIp)){
            UIUtils.showToast("请输入设备ID");
            return;
        }

        if (mLlIncrease.getChildCount() == 0){
            UIUtils.showToast("请增加设备功能");
            return;
        }

        int mPondId =  mMEpPondList.get(mMSPPond.getSelectedIndex()).getId();
        String mWorkAddress = mEtWorkAddress.getText().toString();
        EpDeviceRequest request = new EpDeviceRequest();
        request.setDevice_no(Integer.parseInt(deviceId));
        request.setMac_ip(macIp);
        request.setPound_id(mPondId);
        request.setAddress(mWorkAddress);
        ApiRetrofit.getInstance().epUpdateDevice(mDeviceBean.getId(),request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getCode() == 1){
                        BroadcastManager.getInstance(this).sendBroadcast(AppConst.UPDATE_EP_DEVICE);
                        finish();
                    }else {
                        UIUtils.showToast(response.getMsg());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));


    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());


        mTvPublishNow.setOnClickListener(v -> updateDevice());

        mTvAddFunc.setOnClickListener(v -> addFunc());
    }

    private void addFunc() {
        View funcView = LayoutInflater.from(mContext).inflate(R.layout.item_device_func_add, null);
        MaterialSpinner mSpFun = funcView.findViewById(R.id.spDeviceFun);
        mSpFun.setItems(AppConst.LIST_DEVICE_CH);
        mSpFun.setSelectedIndex(0);

        TextView tvFuncCount = funcView.findViewById(R.id.tvFuncCount);
        tvFuncCount.setText(String.valueOf(mLlIncrease.getChildCount()+1));
        funcView.findViewById(R.id.ivDelFun).setOnClickListener(v -> {
            mLlIncrease.removeView(funcView);
            refreshFunNum();
            mTvDeviceNum.setText(String.format("记录数:%s",mLlIncrease.getChildCount()));
        });

        mLlIncrease.addView(funcView);
        /* 显示记录数 */
        mTvDeviceNum.setText(String.format("记录数:%s",mLlIncrease.getChildCount()));
    }
}

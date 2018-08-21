package com.jit.appcloud.ui.activity.me;

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
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.request.EpDeviceRequest;
import com.jit.appcloud.model.response.PondGetByMGResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/07/13.
 *         discription:
 */
public class EpAddDeviceActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.llToolbarMyAnno)
    LinearLayout mLlToolbarMyAnno;
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
    @BindView(R.id.llIncrease)
    LinearLayout mLlIncrease;
    @BindView(R.id.tvAddFunc)
    TextView mTvAddFunc;
    @BindView(R.id.tvDeviceNum)
    TextView mTvDeviceNum;
    private List<PondGetByMGResponse.DataBean> mMEpPondList;
    private List<String> mPondStrList = new ArrayList<>();

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_ag_device_add;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(getString(R.string.title_add));
        mTvToolbarTitle.setText(R.string.title_add_device);
    }

    /**
     * 首先加载塘口
     * */
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
                        if (mPondStrList.size() != 0){
                            mMSPPond.setSelectedIndex(0);
                        }
                    }else {
                        UIUtils.showToast(pondGetByMGResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mTvAddFunc.setOnClickListener(v -> addFunc());

        mTvPublishNow.setOnClickListener(v -> addDevice());
    }

    private void addDevice() {
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
        showWaitingDialog(getString(R.string.str_please_waiting));
        int mPondId = mMEpPondList.get(mMSPPond.getSelectedIndex()).getId();
        String mWorkAddress = mEtWorkAddress.getText().toString();
        EpDeviceRequest request = new EpDeviceRequest();
        request.setDevice_no(Integer.parseInt(deviceId));
        request.setMac_ip(macIp);
        request.setPound_id(mPondId);
        request.setAddress(mWorkAddress);
        ApiRetrofit.getInstance().epInsertDevice(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    hideWaitingDialog();
                    if (response.getCode() == 1){
                        BroadcastManager.getInstance(EpAddDeviceActivity.this).sendBroadcast(AppConst.UPDATE_EP_DEVICE);
                        finish();
                    }else {
                        UIUtils.showToast(response.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
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

    private void refreshFunNum() {
        for (int i=0;i<mLlIncrease.getChildCount();i++){
            View view = mLlIncrease.getChildAt(i);
            TextView tvNum =  view.findViewById(R.id.tvFuncCount);
            tvNum.setText(String.valueOf(i+1));
        }
    }
}

package com.jit.appcloud.ui.activity.cultivate;

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
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.AgDeviceBean;
import com.jit.appcloud.db.db_model.AgDeviceDetailBean;
import com.jit.appcloud.db.db_model.AgPondBean;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
/**
 * @author zxl on 2018/06/25.
 *         discription: 经销商身份增加设备
 */
public class AgDeviceAddActivity extends BaseActivity {
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
    private List<String> mPondsList = new ArrayList<>();
    private List<AgPondBean> mAgPondBeans;

    @Override
    protected void init() {
        mAgPondBeans = DBManager.getInstance().queryAllAgPondsId();
        for (AgPondBean bean: mAgPondBeans){
            mPondsList.add(bean.getPondId());
            LogUtils.e("设备--号码",bean.getPondPosition()+" ");
        }
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
        mTvToolbarTitle.setText("添加设备");
        mMSPPond.setItems(mPondsList);
        mMSPPond.setSelectedIndex(0);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTvAddFunc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFunc();
            }
        });


        mTvPublishNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDevice();
            }
        });
    }

    private void addDevice() {
        String deviceId = mEtDeviceId.getText().toString();
        if (TextUtils.isEmpty(deviceId)){
            UIUtils.showToast("请输入设备ID");
            return;
        }
        String macIp =  mEtMacIP.getText().toString();
        if (TextUtils.isEmpty(macIp)){
            UIUtils.showToast("请输入Mac地址");
            return;
        }

        if (mLlIncrease.getChildCount() == 0){
            UIUtils.showToast("请增加设备功能");
            return;
        }

        String mPondName =   mAgPondBeans.get(mMSPPond.getSelectedIndex()).getPondId();
        int mPondId = mAgPondBeans.get(mMSPPond.getSelectedIndex()).getPondPosition();
        String mWorkAddress = mEtWorkAddress.getText().toString();

        StringBuilder sbFun = new StringBuilder();
        for (int i=0;i<mLlIncrease.getChildCount();i++){
            View deviceView = mLlIncrease.getChildAt(i);
            MaterialSpinner spFun = deviceView.findViewById(R.id.spDeviceFun);
            /* 下面的6个数据========= */
            EditText etY1 =  deviceView.findViewById(R.id.etYellow1);
            EditText etY2 =  deviceView.findViewById(R.id.etYellow2);
            EditText etY3 =  deviceView.findViewById(R.id.etYellow3);
            EditText etY4 =  deviceView.findViewById(R.id.etYellow4);
            EditText etG1 =  deviceView.findViewById(R.id.etGreen1);
            EditText etG2 =  deviceView.findViewById(R.id.etGreen2);
            AgDeviceDetailBean agDeviceDetailBean = new AgDeviceDetailBean();

            agDeviceDetailBean.setDeviceId(deviceId);
            agDeviceDetailBean.setMacAddress(macIp);
            agDeviceDetailBean.setPondName(mPondName);
            agDeviceDetailBean.setWorkAddress(mWorkAddress);
            agDeviceDetailBean.setFunctionName(AppConst.LIST_DEVICE_CH[spFun.getSelectedIndex()]);
            if (!TextUtils.isEmpty(etY1.getText().toString())){
                agDeviceDetailBean.setYellow1(Float.parseFloat(etY1.getText().toString()));
            }

            if (!TextUtils.isEmpty(etY2.getText().toString())){
                agDeviceDetailBean.setYellow2(Float.parseFloat(etY2.getText().toString()));

            }

            if (!TextUtils.isEmpty(etY3.getText().toString())){
                agDeviceDetailBean.setYellow3(Float.parseFloat(etY3.getText().toString()));

            }

            if (!TextUtils.isEmpty(etY4.getText().toString())){
                agDeviceDetailBean.setYellow4(Float.parseFloat(etY4.getText().toString()));

            }

            if (!TextUtils.isEmpty(etG1.getText().toString())){
                agDeviceDetailBean.setGreen1(Float.parseFloat(etG1.getText().toString()));

            }

            if (!TextUtils.isEmpty(etG2.getText().toString())){
                agDeviceDetailBean.setGreen2(Float.parseFloat(etG2.getText().toString()));

            }

            if (i == 0) {
                sbFun.append(AppConst.LIST_DEVICE_CH[spFun.getSelectedIndex()]);
            } else {
                sbFun.append(",").append(AppConst.LIST_DEVICE_CH[spFun.getSelectedIndex()]);
            }
            /* ============= 保存设备详细信息 =============*/
            DBManager.getInstance().saveAgDeviceDetail(agDeviceDetailBean);
        }

        AgDeviceBean agDeviceBean = new AgDeviceBean();
        agDeviceBean.setDeviceID(deviceId);
        agDeviceBean.setMacAddress(macIp);
        agDeviceBean.setPondName(mPondName);
        agDeviceBean.setWorkAddress(mWorkAddress);
        agDeviceBean.setFunctionName(sbFun.toString());
        agDeviceBean.setPondId(mPondId);
        DBManager.getInstance().saveAgDevice(agDeviceBean);
        setResult(RESULT_OK);
        finish();
    }

    /* LayoutInflater有空还是需要进一步掌握的撒 ++++++*/
    private void addFunc() {
        View funcView = LayoutInflater.from(mContext).inflate(R.layout.item_device_func_add, null);
        MaterialSpinner mSpFun = funcView.findViewById(R.id.spDeviceFun);
        mSpFun.setItems(AppConst.LIST_DEVICE_CH);
        mSpFun.setSelectedIndex(0);

        TextView tvFuncCount = funcView.findViewById(R.id.tvFuncCount);
        tvFuncCount.setText(String.valueOf(mLlIncrease.getChildCount()+1));
        funcView.findViewById(R.id.ivDelFun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlIncrease.removeView(funcView);
                refreshFunNum();
                mTvDeviceNum.setText(String.format("记录数:%s",mLlIncrease.getChildCount()));
            }
        });

        mLlIncrease.addView(funcView);
        /* 显示记录数 */
        mTvDeviceNum.setText(String.format("记录数:%s",mLlIncrease.getChildCount()));
    }

    /* 重新刷新编号*/
    private void refreshFunNum() {
        for (int i=0;i<mLlIncrease.getChildCount();i++){
            View view = mLlIncrease.getChildAt(i);
            TextView tvNum =  view.findViewById(R.id.tvFuncCount);
            tvNum.setText(String.valueOf(i+1));
        }
    }
}

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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.IFwLogCallback;

/**
 * @author zxl on 2018/06/25.
 *         discription:
 */
public class AgDeviceEditorActivity extends BaseActivity {
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
    private AgDeviceBean mAgDeviceBean;
    private List<String> mPondsList = new ArrayList<>();
    private List<AgPondBean> mAgPondBeans;
    @BindView(R.id.mSpCg)
    MaterialSpinner mMSpCg;
    @Override
    protected void init() {
        mAgDeviceBean = (AgDeviceBean) getIntent().getSerializableExtra(AppConst.EXTRA_SER_AG_DEVICE_EDITOR);
        mAgPondBeans = DBManager.getInstance().queryAllAgPondsId();
        for (AgPondBean bean: mAgPondBeans){
            mPondsList.add(bean.getPondId());
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
        mMSpCg.setItems(Arrays.asList(AppConst.DEVICE_KIND));
        initNormal();
        initPondSp();
        initFun();
    }

    private void initNormal() {
        mEtDeviceId.setText(mAgDeviceBean.getDeviceID());
        //mEtMacIP.setText(mAgDeviceBean.getMacAddress());
        //mEtWorkAddress.setText(mAgDeviceBean.getWorkAddress());
        mMSpCg.setSelectedIndex(mAgDeviceBean.getType());
    }

    private void initFun() {
        List<String> mFunList = Arrays.asList(AppConst.LIST_DEVICE_CH);
        List<AgDeviceDetailBean> agDeviceDetailBeans = DBManager.getInstance().queryDvDtByDeviceId(mAgDeviceBean.getDeviceID());
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
                funcView.findViewById(R.id.ivDelFun).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /* 删除数据对应的数据*/
                        mLlIncrease.removeView(funcView);
                        refreshFunNum();
                        mTvDeviceNum.setText(String.format("记录数:%s",mLlIncrease.getChildCount()));

                        /* 删除数据库中该数据;这个就没必要了吧*/
                    }
                });

                mLlIncrease.addView(funcView);
        /* 显示记录数 */
                mTvDeviceNum.setText(String.format("记录数:%s",mLlIncrease.getChildCount()));

            }

        }

    }

    /* 重新刷新编号*/
    private void refreshFunNum() {
        for (int i=0;i<mLlIncrease.getChildCount();i++){
            View view = mLlIncrease.getChildAt(i);
            TextView tvNum =  view.findViewById(R.id.tvFuncCount);
            tvNum.setText(String.valueOf(i+1));
        }

    }

    private void initPondSp() {
        mMSPPond.setItems(mPondsList);
        mMSPPond.setSelectedIndex(mPondsList.indexOf(mAgDeviceBean.getPondName()));
    }

    @Override
    protected void initData() {

    }

    private void updateDevice() {
        String deviceId = mEtDeviceId.getText().toString();
        if (TextUtils.isEmpty(deviceId)){
            UIUtils.showToast("请输入设备ID");
            return;
        }


        String mPondName =  mPondsList.get(mMSPPond.getSelectedIndex());
        int mPondId =  mAgPondBeans.get(mMSPPond.getSelectedIndex()).getPondPosition();
        //String mWorkAddress = mEtWorkAddress.getText().toString();

        DBManager.getInstance().deleteDvDtByDeviceId(mAgDeviceBean.getDeviceID());/* 删除对应设备的详细信息*/

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
            //agDeviceDetailBean.setMacAddress(macIp);
            agDeviceDetailBean.setPondName(mPondName);
            //agDeviceDetailBean.setWorkAddress(mWorkAddress);
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
            }
            else {
                sbFun.append(",").append(AppConst.LIST_DEVICE_CH[spFun.getSelectedIndex()]);
            }

            /* ============= 保存设备详细信息 =============*/
            DBManager.getInstance().saveAgDeviceDetail(agDeviceDetailBean);
        }

        AgDeviceBean agDeviceBean = new AgDeviceBean();
        agDeviceBean.setDeviceID(deviceId);
        //agDeviceBean.setMacAddress(macIp);
        agDeviceBean.setPondName(mPondName);
        agDeviceBean.setPondId(mPondId);
        //agDeviceBean.setWorkAddress(mWorkAddress);
        agDeviceBean.setFunctionName(sbFun.toString());
        agDeviceBean.setType(mMSpCg.getSelectedIndex());
        /* 更新了View*/
        DBManager.getInstance().updateAgDevice(agDeviceBean,mAgDeviceBean.getId());
        setResult(RESULT_OK);
        finish();

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mTvPublishNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDevice();
            }
        });

        mTvAddFunc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFunc();
            }
        });
    }

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
}

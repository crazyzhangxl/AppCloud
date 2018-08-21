package com.jit.appcloud.ui.activity.cultivate;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.bean.PersonalBean;
import com.jit.appcloud.model.bean.SensorNormalBean;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.FarmDeviceResponse;
import com.jit.appcloud.model.response.SensorNmResponse;
import com.jit.appcloud.model.response.UserBBAMResponse;
import com.jit.appcloud.model.response.UserBdAmResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * description: 设备列表的信息,用于展示实时的设备信息
 * 这里待商榷,展示的是设备界面
 *
 * @author zxl
 */
public class FarmDeviceActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.lqr_agencyList)
    RecyclerView mLqrAgencyList;
    @BindView(R.id.spFarm)
    MaterialSpinner mSpFarm;
    @BindView(R.id.tvTimeOne)
    TextView mTvTimeOne;
    @BindView(R.id.ivTimeOne)
    ImageView mIvTimeOne;
    @BindView(R.id.llTime)
    LinearLayout mLlTime;
    @BindView(R.id.tvDeviceTotal)
    TextView mTvDetailTotal;
    @BindView(R.id.ivLight)
    ImageView mIvTable;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private List<String> farmList = new ArrayList<>();
    private List<SensorNormalBean> mDeviceList = new ArrayList<>();
    private BaseQuickAdapter<SensorNormalBean,BaseViewHolder> mAdapter;
    private String mUserName;
    private List<PersonalBean> mDataBeanList;
    /**
     * 用来表示选中的数量
     */
    private int selectIndex = 0;
    @Override
    protected void init() {
        if (getIntent() != null) {
            mUserName = getIntent().getStringExtra(AppConst.AGENCY_NAME);
        }
    }

    private void initMSpinner(){
                /*加载数据*/
        showWaitingDialog(getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().getMMFMInfo(UserCache.getToken(), mUserName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userBBAMResponse -> {
                    hideWaitingDialog();
                    if (userBBAMResponse.getCode() == 1) {
                        mDataBeanList = userBBAMResponse.getData();
                        filterAndUpdateMS();
                    } else {
                        UIUtils.showToast(userBBAMResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    private void filterAndUpdateMS() {
        if (mDataBeanList != null && mDataBeanList.size() != 0){
            for (PersonalBean bean : mDataBeanList){
                farmList.add(bean.getRealname());
            }
            mSpFarm.setItems(farmList);
            mSpFarm.setSelectedIndex(selectIndex);
            refreshSensorRvByName(mDataBeanList.get(selectIndex).getUsername());
        }

    }

    /**
     * 更具养殖户名来刷新信息
     * @param username
     */
    private void refreshSensorRvByName(String username) {
        Observable<SensorNmResponse> observable = null;
        // 平行总代 --------
        if (UserCache.getRole().equals(AppConst.ROLE_VICE_AGENT)
                || UserCache.getRole().equals(AppConst.ROLE_AGENT)){
            observable =  ApiRetrofit.getInstance().querySensorLatestByUName(username);
        }else {
            // 这个是总部?
            observable =  ApiRetrofit.getInstance().queryFarmSsLatestByUName(username);
        }
                observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sensorNmResponse -> {
                    if (sensorNmResponse != null && sensorNmResponse.getCode() == 1){
                        mDeviceList.clear();
                        mDeviceList.addAll(sensorNmResponse.getData());
                        mAdapter.notifyDataSetChanged();
                        updateFmTotal();
                    }else {
                        UIUtils.showToast("实时设备信息获取失败!");
                    }

                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

    private void updateFmTotal() {
        int totalCount = mDeviceList.size();
        int countOn = 0;
        for (SensorNormalBean bean : mDeviceList){
            if ("1".equals(bean.getDevice_status())){
                countOn ++;
            }
        }
        refreshFmTotal(totalCount,countOn,totalCount -countOn);
    }

    private void refreshFmTotal(int totalCount, int countOn, int i) {
        mTvDetailTotal.setText(
                String.format(UIUtils.getString(R.string.format_device_total),
                        String.valueOf(totalCount),String.valueOf(countOn),String.valueOf(i)));
    }


    @Override
    protected int provideContentViewId() {
        return R.layout.activity_farm_device;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mIvTable.setVisibility(View.VISIBLE);
        mIvTable.setImageResource(R.mipmap.ic_table);
        mTvToolbarTitle.setText(R.string.title_intelligent_detection);
        initAdapter();
        mTvTimeOne.setText(TimeUtil.getPastDate(0));
    }

    private void initAdapter() {
        initListData();
        setAdapter();
    }

    private void setAdapter() {
        mLqrAgencyList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mAdapter = new BaseQuickAdapter<SensorNormalBean, BaseViewHolder>(R.layout.item_pond_info,mDeviceList) {
            @Override
            protected void convert(BaseViewHolder helper, SensorNormalBean item) {
                // 设备名称(塘口名-设备名)
                helper.setText(R.id.deviceName,String.format("%s-%s",item.getPound_name(),item.getDevice_no()));
                // 设备状态
                helper.setText(R.id.tvDeviceState,item.getDevice_status().equals("0") ? "离线":"在线");
                //溶解氧
                helper.setText(R.id.tvO2Value,String.format("%smg/L",String.valueOf(item.getOxygen())));
                // PH值
                helper.setText(R.id.tvPHValue,String.valueOf(item.getPh()));
                // 温度值
                helper.setText(R.id.tvTmpValue,String.format("%s℃",String.valueOf(item.getTemperature())));
                // 时间
                helper.setText(R.id.tvRefreshTime,item.getTime());
            }
        };
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, NormalChartActivity.class);
            // 塘口ID传递过去了
            intent.putExtra(AppConst.POND_ID_SELECTED,mDeviceList.get(position).getPound_id());
            intent.putExtra(AppConst.DEVICE_ID,mDeviceList.get(position).getDevice_id());
            jumpToActivity(intent);
        });
        mLqrAgencyList.setAdapter(mAdapter);
    }

    private void initListData() {
        setAdapter();
    }

    public void showDataOne(String[] timeArray) {
        MaterialDialog dialogOne = new MaterialDialog.Builder(this)
                .title(getString(R.string.title_time_pick))
                .customView(R.layout.dialog_datepicker, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .showListener(dialog -> ObjectAnimator.ofFloat(mIvTimeOne, View.ROTATION.getName(), 180).start())
                .dismissListener(dialog -> ObjectAnimator.ofFloat(mIvTimeOne, View.ROTATION.getName(), -180, 0).start())
                .onPositive((dialog, which) -> {
                    DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                    mTvTimeOne.setText(String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));

                })
                .build();
        DatePicker datePicker = (DatePicker) dialogOne.findViewById(R.id.datePicker);
        datePicker.init(Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]) - 1, Integer.parseInt(timeArray[2]), null);
        dialogOne.show();
    }

    @Override
    protected void initData() {
        // 根据用户名去查询 ================================
        initMSpinner();
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mLlTime.setOnClickListener(v -> {
            String[] timeArray = mTvTimeOne.getText().toString().split("-");
            showDataOne(timeArray);
        });
        mSpFarm.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectIndex = position;
                refreshSensorRvByName(mDataBeanList.get(selectIndex).getUsername());
            }
        });

        /**
         * 执行的刷新操作 -----------
         */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCustom();
            }
        });


        mIvTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RTimeMonitorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AppConst.K_SELECTED_INDEX,selectIndex);
                bundle.putSerializable(AppConst.FM_SERIALIZABLE_ARRAY,(Serializable) mDataBeanList);
                bundle.putString(AppConst.EXTRA_TIME,mTvTimeOne.getText().toString().trim());
                intent.putExtras(bundle);
                jumpToActivity(intent);
            }
        });
    }

    public void refreshCustom() {
        if (mDataBeanList == null || mDataBeanList.size() == 0){
            mSwipeRefreshLayout.setRefreshing(false);
            UIUtils.showToast("暂无设备信息可获得!");
            return;
        }

        new Handler().postDelayed(() -> {
            String username = mDataBeanList.get(selectIndex).getUsername();
            Observable<SensorNmResponse> observable = null;
            // 平行总代 --------
            if (UserCache.getRole().equals(AppConst.ROLE_VICE_AGENT)
                    || UserCache.getRole().equals(AppConst.ROLE_AGENT)){
                observable =  ApiRetrofit.getInstance().querySensorLatestByUName(username);
            }else {
                // 这个是总部?
                observable =  ApiRetrofit.getInstance().queryFarmSsLatestByUName(username);
            }
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(sensorNmResponse -> {
                        if (sensorNmResponse != null && sensorNmResponse.getCode() == 1){
                            mDeviceList.clear();
                            mDeviceList.addAll(sensorNmResponse.getData());
                            mAdapter.notifyDataSetChanged();
                            updateFmTotal();
                        }else {
                            UIUtils.showToast("实时设备信息获取失败!");
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }, throwable -> {
                        mSwipeRefreshLayout.setRefreshing(false);
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    });

        },1000);
    }
}

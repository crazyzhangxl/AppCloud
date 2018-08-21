package com.jit.appcloud.ui.activity.cultivate;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TimeUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.bean.PersonalBean;
import com.jit.appcloud.model.response.FarmDeviceResponse;
import com.jit.appcloud.model.response.PondGetByMGResponse;
import com.jit.appcloud.model.response.UserBdAmResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.RTimeMonitorAtPresenter;
import com.jit.appcloud.ui.view.IRTimeMonitorAtView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zxl on 2018/08/09.
 *         discription: 实时监控数据的活动
 */
public class RTimeMonitorActivity extends BaseActivity<IRTimeMonitorAtView, RTimeMonitorAtPresenter> implements IRTimeMonitorAtView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.rvRTMonitor)
    RecyclerView mRvRTMonitor;
    @BindView(R.id.tvSpTitle)
    TextView mTvSpTitle;
    @BindView(R.id.spinner)
    MaterialSpinner mSpinner;
    @BindView(R.id.tvDeviceTime)
    TextView mTvDeviceTime;
    @BindView(R.id.ivTimeTwo)
    ImageView mIvTimeTwo;
    @BindView(R.id.llDeviceTime)
    LinearLayout mLlDeviceTime;
    private List<PondGetByMGResponse.DataBean> mEpPondList = new ArrayList<>();
    private List<String> mPondStrList = new ArrayList<>();
    /**
     * 标识身份:
     * 目前区分为:塘口和用户的--------
     */

    private int deatRole = -1;
    /**
     * 标识spinner的选择ID
     */
    private int mSpSelectIndex = 0;
    private List<PersonalBean> mFMDeviceList = new ArrayList<>();
    private List<String> mEmployeeList = new ArrayList<>();
    private String mTimeFromBefore;

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        // 传值这里还是比较关键的撒
        if (getIntent() != null){
            if (getIntent().hasExtra(AppConst.POND_SERIALIZABLE_ARRAY)) {
                mEpPondList = (List<PondGetByMGResponse.DataBean>)
                        getIntent().getSerializableExtra(AppConst.POND_SERIALIZABLE_ARRAY);
                deatRole = 0;
            }else if (getIntent().hasExtra(AppConst.FM_SERIALIZABLE_ARRAY)){
                mFMDeviceList = (List<PersonalBean>)
                        getIntent().getSerializableExtra(AppConst.FM_SERIALIZABLE_ARRAY);
                deatRole = 1;
            }
            mTimeFromBefore = getIntent().getStringExtra(AppConst.EXTRA_TIME);
            mSpSelectIndex = getIntent().getIntExtra(AppConst.K_SELECTED_INDEX, 0);
            LogUtils.e("检测","ID = "+mSpSelectIndex);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_rtime_monitor;
    }

    @Override
    protected RTimeMonitorAtPresenter createPresenter() {
        return new RTimeMonitorAtPresenter(this,deatRole);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        // 设置时间
        mTvToolbarTitle.setText("实时监护");
        initTimer();
        initAdapter();
        initSpinner();
    }

    private void initAdapter() {
        mPresenter.setDeviceAdapter();
    }

    private void initTimer() {
        mTvDeviceTime.setText(mTimeFromBefore);
    }

    /**
     * 展示时间
     * @param timeArray
     */
    private void showTimeDevice(String[] timeArray) {
        @SuppressLint("DefaultLocale") MaterialDialog dialogOne = new MaterialDialog.Builder(this)
                .title(getString(R.string.title_time_pick))
                .customView(R.layout.dialog_datepicker, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .showListener(dialog -> ObjectAnimator.ofFloat(mIvTimeTwo, View.ROTATION.getName(), 180).start())
                .dismissListener(dialog -> ObjectAnimator.ofFloat(mIvTimeTwo, View.ROTATION.getName(), -180, 0).start())
                .onPositive((dialog, which) -> {
                    DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                    mTvDeviceTime.setText(String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                    if (deatRole == 0){
                        mPresenter.queryPondDateAndRf(mEpPondList.get(mSpSelectIndex).getId());
                    }else if (deatRole == 1){
                        mPresenter.queryFmDateAndRf(mFMDeviceList.get(mSpSelectIndex).getUsername());
                    }
                })
                .build();
        DatePicker datePicker = (DatePicker) dialogOne.findViewById(R.id.datePicker);
        datePicker.init(Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]) - 1, Integer.parseInt(timeArray[2]), null);
        dialogOne.show();

    }

    /**
     * 筛选塘口数据
     */
    private void filterPondList() {
        for (PondGetByMGResponse.DataBean bean:mEpPondList){
            mPondStrList.add(bean.getNumber());
        }
        if (mPondStrList != null && mPondStrList.size() != 0) {
            mSpinner.setItems(mPondStrList);
            mSpinner.setSelectedIndex(mSpSelectIndex);
            mPresenter.queryPondDateAndRf(mEpPondList.get(mSpSelectIndex).getId());
        }
    }

    /**
     * 筛选养殖户数据
     */
    private void filterFmPondList(){
        for (PersonalBean bean:mFMDeviceList){
            mEmployeeList.add(bean.getRealname());
        }

        if (mEmployeeList != null && mEmployeeList.size() != 0){
            mSpinner.setItems(mEmployeeList);
            mSpinner.setSelectedIndex(mSpSelectIndex);
            mPresenter.queryFmDateAndRf(mFMDeviceList.get(mSpSelectIndex).getUsername());
        }

    }

    private void initSpinner() {
        if (deatRole == 0){
            mTvSpTitle.setText("养殖塘:");
            filterPondList();
        }else if (deatRole ==1){
            mTvSpTitle.setText("养殖户:");
            filterFmPondList();
        }
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mLlDeviceTime.setOnClickListener(v -> {
            String[] timeArray = mTvDeviceTime.getText().toString().split("-");
            showTimeDevice(timeArray);
        });

        mSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            mSpSelectIndex = position;
            if (deatRole == 0){
                mPresenter.queryPondDateAndRf(mEpPondList.get(mSpSelectIndex).getId());
            }else if (deatRole == 1){
                mPresenter.queryFmDateAndRf(mFMDeviceList.get(mSpSelectIndex).getUsername());
            }
        });
    }


    @Override
    public RecyclerView mRvTimeMonitor() {
        return mRvRTMonitor;
    }

    @Override
    public TextView mTvTime() {
        return mTvDeviceTime;
    }
}

package com.jit.appcloud.ui.activity.cultivate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.LinearChartManager;
import com.jit.appcloud.model.bean.IncomeBean;
import com.jit.appcloud.model.bean.SensorDtBean;
import com.jit.appcloud.model.response.SensorDfInfoResponse;
import com.jit.appcloud.ui.activity.FarmLogActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/08/08.
 *         discription: 普通设备的实时数据;该界面采用折线图的形式来完成绘制
 */
public class NormalChartActivity extends BaseActivity {


    private static final String[] TITLE_DAYS = {"今天", "昨天", "前天", "十天"};
    @BindView(R.id.tvToday)
    TextView mTvToday;
    @BindView(R.id.tvYesterday)
    TextView mTvYesterday;
    @BindView(R.id.tvBeforeDay)
    TextView mTvBeforeDay;
    @BindView(R.id.tvTenDay)
    TextView mTvTenDay;
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.llTable)
    LinearLayout mLlTable;
    @BindView(R.id.chartPH)
    LineChart mChartPH;
    @BindView(R.id.chartO2)
    LineChart mChartO2;
    @BindView(R.id.chartTmp)
    LineChart mChartTmp;
    private List<String> mDataList = new ArrayList<>();
    private int deviceID = -1;
    private int pondID = -1;

    private int prePosition = -1;
    private LinearChartManager mChartManagerPH;
    private LinearChartManager mChartManagerO2;
    private LinearChartManager mChartManagerTmp;
    private List<IncomeBean> mDataPH;
    private List<IncomeBean> mDataO2;
    private List<IncomeBean> mDataTmp;
    private Observable<SensorDfInfoResponse> mDeviceDFDay;

    @Override
    protected void init() {
        deviceID = getIntent().getIntExtra(AppConst.DEVICE_ID,-1);
        pondID = getIntent().getIntExtra(AppConst.POND_ID_SELECTED,-1);
        mDataList = Arrays.asList(TITLE_DAYS);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_normal_chart;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("图表数据");
        mLlTable.setVisibility(View.VISIBLE);
        initThreeChart();
    }

    private void initThreeChart() {
        mChartManagerPH = new LinearChartManager(mChartPH);
        mChartManagerO2 = new LinearChartManager(mChartO2);
        mChartManagerTmp = new LinearChartManager(mChartTmp);
    }

    private void refreshThreeChart(){
        if (mDataPH.size() != 0) {
            mChartManagerPH.showLineChart(mDataPH, "PH值", Color.BLUE);
            //设置曲线填充色 以及 MarkerView
            Drawable drawable = getResources().getDrawable(R.drawable.fade_blue);
            mChartManagerPH.setChartFillDrawable(drawable);
            mChartManagerPH.setMarkerView(this);
        }else {
            mChartPH.clear();
        }

        if (mDataO2.size() != 0) {
            mChartManagerO2.showLineChart(mDataO2, "溶解氧", Color.BLUE);
            //设置曲线填充色 以及 MarkerView
            Drawable drawable1 = getResources().getDrawable(R.drawable.fade_blue);
            mChartManagerO2.setChartFillDrawable(drawable1);
            mChartManagerO2.setMarkerView(this);
        }else {
            mChartO2.clear();
        }

        if (mDataTmp.size() != 0) {
            mChartManagerTmp.showLineChart(mDataTmp, "温度", Color.BLUE);
            //设置曲线填充色 以及 MarkerView
            Drawable drawable2 = getResources().getDrawable(R.drawable.fade_blue);
            mChartManagerTmp.setChartFillDrawable(drawable2);
            mChartManagerTmp.setMarkerView(this);
        }else {
            mChartTmp.clear();
        }
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mLlTable.setOnClickListener(v -> {
            // 跳转向养殖记录表 ----------- 和塘口ID有很大的关系
            Intent intent = new Intent(NormalChartActivity.this, FarmLogActivity.class);
            intent.putExtra(AppConst.POND_ID_SELECTED,pondID);
            jumpToActivity(intent);
        });

        mTvToday.setOnClickListener(v -> setTitleSelected(prePosition, 0));

        mTvYesterday.setOnClickListener(v -> setTitleSelected(prePosition, 1));

        mTvBeforeDay.setOnClickListener(v -> setTitleSelected(prePosition, 2));

        mTvTenDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitleSelected(prePosition, 3);
            }
        });


        mTvToday.callOnClick();
    }

    private void setTitleSelected(int prePosition, int nowPosition) {
        if (prePosition == nowPosition) {
            return;
        }

        switch (prePosition) {
            case 0:
                mTvToday.setTextColor(Color.BLACK);
                mTvToday.setBackground(null);
                break;
            case 1:
                mTvYesterday.setTextColor(Color.BLACK);
                mTvYesterday.setBackground(null);
                break;
            case 2:
                mTvBeforeDay.setTextColor(Color.BLACK);
                mTvBeforeDay.setBackground(null);
                break;
            case 3:
                mTvTenDay.setTextColor(Color.BLACK);
                mTvTenDay.setBackground(null);
                break;
            default:
                break;
        }

        switch (nowPosition) {
            case 0:
                mTvToday.setTextColor(Color.WHITE);
                mTvToday.setBackgroundColor(UIUtils.getColor(R.color.bg_title_selector));
                break;
            case 1:
                mTvYesterday.setTextColor(Color.WHITE);
                mTvYesterday.setBackgroundColor(UIUtils.getColor(R.color.bg_title_selector));
                break;
            case 2:
                mTvBeforeDay.setTextColor(Color.WHITE);
                mTvBeforeDay.setBackgroundColor(UIUtils.getColor(R.color.bg_title_selector));
                break;
            case 3:
                mTvTenDay.setTextColor(Color.WHITE);
                mTvTenDay.setBackgroundColor(UIUtils.getColor(R.color.bg_title_selector));
                break;
            default:
                break;
        }

        this.prePosition = nowPosition;
        if (deviceID != -1) {
            String date= "";
            switch (nowPosition) {
                case 0:
                    date = TimeUtil.getPastDate(0);
                    mDeviceDFDay = ApiRetrofit.getInstance().getDeviceDFDay(deviceID, date);
                    break;
                case 1:
                    date = TimeUtil.getPastDate(1);
                    mDeviceDFDay = ApiRetrofit.getInstance().getDeviceDFDay(deviceID, date);
                    break;
                case 2:
                    date = TimeUtil.getPastDate(2);
                    mDeviceDFDay = ApiRetrofit.getInstance().getDeviceDFDay(deviceID, date);
                    break;
                case 3:
                    date = TimeUtil.getPastDate(10);
                    mDeviceDFDay = ApiRetrofit.getInstance().getDeviceDFByTime
                            (deviceID,date,TimeUtil.getPastDate(0));
                    break;
                default:
                    break;
            }
            showWaitingDialog(getString(R.string.str_please_waiting));
            if (!TextUtils.isEmpty(date) ) {
                        mDeviceDFDay
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            hideWaitingDialog();
                            if (response != null && response.getCode() == 1){
                                filterDateAndRefresh(response.getData());
                            }else {
                                LogUtils.e("图表错误",response.getMsg());
                                UIUtils.showToast(response.getMsg());
                            }
                        }, throwable -> {
                            hideWaitingDialog();
                            LogUtils.e("图表错误",throwable.getLocalizedMessage());
                            UIUtils.showToast(throwable.getLocalizedMessage());
                        });

            }
        }
    }

    private void filterDateAndRefresh(List<SensorDtBean> data) {
        if (mDataO2 == null){
            mDataO2 = new ArrayList<>();
        }

        if (mDataPH == null){
            mDataPH = new ArrayList<>();
        }

        if (mDataTmp == null){
            mDataTmp = new ArrayList<>();
        }
        mDataO2.clear();
        mDataPH.clear();
        mDataTmp.clear();

        for (int i=0;i<data.size();i++){
            SensorDtBean bean = data.get(i);
            mDataO2.add(new IncomeBean(bean.getTime(),bean.getOxygen()));
            mDataPH.add(new IncomeBean(bean.getTime(),bean.getPh()));
            mDataTmp.add(new IncomeBean(bean.getTime(),bean.getTemperature()));
        }
        refreshThreeChart();
    }

}

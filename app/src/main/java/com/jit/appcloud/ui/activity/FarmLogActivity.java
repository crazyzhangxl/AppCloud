package com.jit.appcloud.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.DiaryResponse;
import com.jit.appcloud.model.response.SinglePondResponse;
import com.jit.appcloud.ui.activity.me.LogDdListActivity;
import com.jit.appcloud.ui.activity.me.LogFileCsActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.FarmLogAtPresenter;
import com.jit.appcloud.ui.view.IFarmLogAtView;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.MyRecyclerView;
import com.luck.picture.lib.decoration.RecycleViewDivider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/07/03.
 *         discription: 养殖记录表活动
 */
public class FarmLogActivity extends BaseActivity<IFarmLogAtView, FarmLogAtPresenter> implements IFarmLogAtView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.llToolbarTitle)
    LinearLayout mLlToolbarTitle;
    @BindView(R.id.llDownload)
    LinearLayout mLlDownload;
    @BindView(R.id.ivDnLog)
    ImageView mIvDnLog;
    @BindView(R.id.flToolBar)
    FrameLayout mFlToolBar;
    @BindView(R.id.tvPondName)
    TextView mTvPondName;
    @BindView(R.id.tvPv)
    TextView mTvPv;
    @BindView(R.id.tvPondBrand)
    TextView mTvPondBrand;
    @BindView(R.id.tvBreedNum)
    TextView mTvBreedNum;
    @BindView(R.id.tvBreedTime)
    TextView mTvBreedTime;
    @BindView(R.id.tvArea)
    TextView mTvArea;
    @BindView(R.id.tvLiaoName)
    TextView mTvLiaoName;
    @BindView(R.id.tvLiaoType)
    TextView mTvLiaoType;
    @BindView(R.id.tvCusName)
    TextView mTvCusName;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.tvAddress)
    TextView mTvAddress;
    @BindView(R.id.tvTimeRang)
    TextView mTvTimeRang;
    @BindView(R.id.tvTimeOne)
    TextView mTvTimeOne;
    @BindView(R.id.ivTimeOne)
    ImageView mIvTimeOne;
    @BindView(R.id.llTimeOne)
    LinearLayout mLlTimeOne;
    @BindView(R.id.tvTimeTwo)
    TextView mTvTimeTwo;
    @BindView(R.id.ivTimeTwo)
    ImageView mIvTimeTwo;
    @BindView(R.id.llTimeTwo)
    LinearLayout mLlTimeTwo;
    @BindView(R.id.llTitle)
    LinearLayout mLlTitle;

    @BindView(R.id.rvContent)
    MyRecyclerView mRvContent;

    @BindView(R.id.llLogAll)
    LinearLayout mLlLogAll;
    @BindView(R.id.tvRecordNum)
    TextView mTvRecordNum;
    private List<DiaryResponse.DataBean> mBeanList = new ArrayList<>();
    private BaseQuickAdapter<DiaryResponse.DataBean, BaseViewHolder> mAdapter;
    private int  mPondId = -1;
    private SinglePondResponse.DataBean mSinglePdBean;
    private String mPoundName;

    @Override
    protected void init() {
        if (getIntent() != null){
            mPondId = getIntent().getIntExtra(AppConst.POND_ID_SELECTED, -1);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_farm_log;
    }

    @Override
    protected FarmLogAtPresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (mPondId == -1){
            finish();
            return;
        }
        mTvToolbarTitle.setText(R.string.title_farm_log_table);
        mLlLogAll.setVisibility(View.VISIBLE);
        initTime();
        initTitleLog();
        initAdapter();
        /* 互相联动 =========== */
    }

    private void initTime() {
        mTvTimeOne.setText(TimeUtil.getPastDate(7));
        mTvTimeTwo.setText(TimeUtil.getPastDate(0));
    }

    private void initTitleLog() {
        View viewHead = LayoutInflater.from(mContext).inflate(R.layout.head_log_title, null);
        mLlTitle.addView(viewHead);
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setSmoothScrollbarEnabled(true);
        manager.setAutoMeasureEnabled(true);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvContent.addItemDecoration(new RecycleViewDivider(
                mContext, LinearLayoutManager.HORIZONTAL, UIUtils.dip2px(mContext, 0.5f), UIUtils.getColor(R.color.bg_line_2)));
        mRvContent.setLayoutManager(manager);
        mAdapter = new BaseQuickAdapter<DiaryResponse.DataBean, BaseViewHolder>(R.layout.item_total_log, mBeanList) {
            @Override
            protected void convert(BaseViewHolder helper, DiaryResponse.DataBean item) {
                helper.setText(R.id.tvData,TimeUtil.getMyTimeDay(item.getDate()));
                helper.setText(R.id.tvWeather,item.getWeather());
                helper.setText(R.id.tvFeedOne,String.valueOf(item.getCount1()));
                helper.setText(R.id.tvFeedTwo,String.valueOf(item.getCount2()));
                helper.setText(R.id.tvFeedThree,String.valueOf(item.getCount3()));
                helper.setText(R.id.tvFeedFour,String.valueOf(item.getCount4()));
                helper.setText(R.id.tvFeedFive,String.valueOf(item.getCount5()));
                helper.setText(R.id.tvFeedSix,String.valueOf(item.getCount6()));
                helper.setText(R.id.tvFeedTotal,String.valueOf(item.getCount_total()));
                helper.setText(R.id.tvPHMin,String.valueOf(item.getPh_min()));
                helper.setText(R.id.tvPhMax,String.valueOf(item.getPh_max()));
                helper.setText(R.id.tvPhRange,String.valueOf(item.getPh_fluctuate()));
                helper.setText(R.id.tvO2Min,String.valueOf(item.getOxygen_min()));
                helper.setText(R.id.tvO2Max,String.valueOf(item.getOxygen_max()));
                helper.setText(R.id.tvO2Range,String.valueOf(item.getOxygen_fluctuate()));
                helper.setText(R.id.tvTempMin,String.valueOf(item.getTemperature_min()));
                helper.setText(R.id.tvTempMax,String.valueOf(item.getTemperature_max()));
                helper.setText(R.id.tvTempRange,String.valueOf(item.getTemperature_fluctuate()));
                if (!TextUtils.isEmpty(item.getAmmo_nitro())) {
                    helper.setText(R.id.tvAnDAn, item.getAmmo_nitro());}
                if (!TextUtils.isEmpty(item.getNano2())) {
                    helper.setText(R.id.tvXiaoSuan, item.getNano2());}
                if (!TextUtils.isEmpty(item.getAlkali())){
                    helper.setText(R.id.tvJianDu,item.getAlkali());}
                helper.setText(R.id.tvMedicine,item.getMedicine());
                helper.setText(R.id.tvRemark,item.getRemark());
            }
        };
        mRvContent.setHasFixedSize(true); //*
        mRvContent.setNestedScrollingEnabled(false); //*
        mRvContent.setAdapter(mAdapter);
    }

    private void refreshNum(){
        mTvRecordNum.setText(String.format("记录数: %s",String.valueOf(mBeanList.size())));
    }

    /**
     *  刷新了根据时间来选择的时间
     * */
    private void refreshData(){
        ApiRetrofit.getInstance().getDiaryInfo(mPondId,mTvTimeOne.getText().toString(),mTvTimeTwo.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diaryResponse -> {
                    if (diaryResponse.getCode() == 1) {
                        mBeanList.clear();
                        mBeanList.addAll(diaryResponse.getData());
                        mAdapter.notifyDataSetChanged();
                        refreshNum();
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

    @Override
    protected void initData() {
        if (mPondId != -1) {
            ApiRetrofit.getInstance().getSinglePondInfo(mPondId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .flatMap(singlePondResponse -> {
                        if (singlePondResponse != null && singlePondResponse.getCode() ==1){
                            mSinglePdBean = singlePondResponse.getData();
                            return ApiRetrofit.getInstance().getDiaryInfo(mPondId,mTvTimeOne.getText().toString(),mTvTimeTwo.getText().toString());
                        }
                        return null;
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diaryResponse -> {
                        if (diaryResponse != null && diaryResponse.getCode() == 1){
                            refreshNormalViews();
                            mBeanList.clear();
                            mBeanList.addAll(diaryResponse.getData());
                            mAdapter.notifyDataSetChanged();
                            refreshNum();
                        }
                    }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
        }
    }

    private void refreshNormalViews() {
        mPoundName = mSinglePdBean.getNumber();
        mTvPondName.setText(mPoundName);
        mTvPv.setText(mSinglePdBean.getSeed_type());
        mTvPondBrand.setText(mSinglePdBean.getSeed_brand());
        mTvBreedNum.setText(String.valueOf(mSinglePdBean.getSeed_number()));
        mTvBreedTime.setText(mSinglePdBean.getSeed_time());
        mTvArea.setText(String.valueOf(mSinglePdBean.getSquare()));
        mTvLiaoName.setText(mSinglePdBean.getFeed_brand());
        mTvLiaoType.setText(mSinglePdBean.getFeed_type());
        mTvCusName.setText(mSinglePdBean.getRealname());
        // 电话和地址 ================
        mTvPhone.setText(mSinglePdBean.getTel());
        mTvAddress.setText(mSinglePdBean.getAddress());
    }


    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mLlDownload.setOnClickListener(v -> {
            if (UserCache.getRole().equals(AppConst.ROLE_VICE_AGENT)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_ADMIN)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_MANAGER)){
                UIUtils.showToast(getString(R.string.please_permission_denied));
                return;
            }
            if (mBeanList.size() == 0) {
                UIUtils.showToast("客官,暂无数据可供下载ing!");
            }else {
                Bundle bundle = new Bundle();
                bundle.putString(AppConst.EXTRA_DOWNLOAD_NAME,mPoundName+TimeUtil.getPastDate(0));
                bundle.putSerializable(AppConst.EXTRA_DOWNLOAD_LIST, (Serializable) mBeanList);
                FarmLogActivity.this.jumpToActivity(LogFileCsActivity.class, bundle);
            }
        });

        mIvDnLog.setOnClickListener(v -> {
            if (UserCache.getRole().equals(AppConst.ROLE_VICE_AGENT)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_ADMIN)
                    || UserCache.getRole().equals(AppConst.ROLE_VICE_MANAGER)){
                UIUtils.showToast(getString(R.string.please_permission_denied));
                return;
            }
            FarmLogActivity.this.jumpToActivity(LogDdListActivity.class);
        });
        mLlTimeOne.setOnClickListener(v -> {
            String[] timeArray = mTvTimeOne.getText().toString().split("-");
            showDataOne(timeArray);
        });

        mLlTimeTwo.setOnClickListener(v -> {
            String[] timeArray = mTvTimeTwo.getText().toString().split("-");
            showDataTwo(timeArray);
        });
    }

    public void showDataOne(String[] timeArray) {
        MaterialDialog dialogOne = new MaterialDialog.Builder(this)
                .title(getString(R.string.title_time_pick))
                .customView(R.layout.dialog_datepicker, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .showListener(dialog -> ObjectAnimator.ofFloat(mIvTimeOne, View.ROTATION.getName(), 180).start())
                .dismissListener(dialog -> {
                    ObjectAnimator.ofFloat(mIvTimeOne, View.ROTATION.getName(), -180, 0).start();
                    refreshData();
                })
                .onPositive((dialog, which) -> {
                    DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                    mTvTimeOne.setText(String.format("%d-%02d-%d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                })
                .build();
        DatePicker datePicker = (DatePicker) dialogOne.findViewById(R.id.datePicker);
        datePicker.init(Integer.parseInt(timeArray[0]),Integer.parseInt(timeArray[1])-1,Integer.parseInt(timeArray[2]),null);
        dialogOne.show();

    }
    public void showDataTwo(String[] timeArray) {
        MaterialDialog pickTwo = new MaterialDialog.Builder(this)
                .title(getString(R.string.title_time_pick))
                .customView(R.layout.dialog_datepicker, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .showListener(dialog -> ObjectAnimator.ofFloat(mIvTimeTwo, View.ROTATION.getName(), 180).start())
                .dismissListener(dialog -> {
                    ObjectAnimator.ofFloat(mIvTimeTwo, View.ROTATION.getName(), -180, 0).start();
                    refreshData();
                })
                .onPositive((dialog, which) -> {
                    DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                    mTvTimeTwo.setText(String.format("%d-%02d-%d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                })
                .build();
        DatePicker datePicker = (DatePicker) pickTwo.findViewById(R.id.datePicker);
        datePicker.init(Integer.parseInt(timeArray[0]),Integer.parseInt(timeArray[1])-1,Integer.parseInt(timeArray[2]),null);
        pickTwo.show();
    }
}

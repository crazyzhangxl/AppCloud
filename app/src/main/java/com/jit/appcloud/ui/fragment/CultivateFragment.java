package com.jit.appcloud.ui.fragment;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.ui.activity.MainActivity;
import com.jit.appcloud.ui.base.BaseFragment;
import com.jit.appcloud.ui.presenter.CultivateFgPresenter;
import com.jit.appcloud.ui.view.ICultivateFgView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author 张先磊
 * @date 2018/4/17
 * @description: 该fragment为养殖界面的重点，需要依据用户的权限做不同的显示，
 * 这里东西还是挺多的
 */

public class CultivateFragment extends BaseFragment<ICultivateFgView, CultivateFgPresenter> implements ICultivateFgView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.rvGeneralAgList)
    RecyclerView mRvGeneralAgList;
    @BindView(R.id.ll_generalAgency)
    LinearLayout mLlGeneralAgency;
    @BindView(R.id.spFarm)
    MaterialSpinner mSpFarm;
    @BindView(R.id.llCus)
    LinearLayout mLlCus;
    @BindView(R.id.tvTimeOne)
    TextView mTvTimeOne;
    @BindView(R.id.ivTimeOne)
    ImageView mIvTimeOne;
    @BindView(R.id.llTime)
    LinearLayout mLlTime;
    @BindView(R.id.tvDeviceState)
    TextView mTvDeviceState;
    @BindView(R.id.rvDevice)
    RecyclerView mRvDevice;
    @BindView(R.id.ll_agency)
    LinearLayout mLlAgency;
    @BindView(R.id.spPond)
    MaterialSpinner mSpPond;
    @BindView(R.id.llPond)
    LinearLayout mLlPond;
    @BindView(R.id.tvTimeTwo)
    TextView mTvTimeTwo;
    @BindView(R.id.ivTimeTwo)
    ImageView mIvTimeTwo;
    @BindView(R.id.llPondTime)
    LinearLayout mLlPondTime;
    @BindView(R.id.tvFarmDeviceSt)
    TextView mTvFarmDeviceSt;
    @BindView(R.id.rvFarmDevice)
    RecyclerView mRvFarmDevice;
    @BindView(R.id.ll_farmer)
    LinearLayout mLlFarmer;
    @BindView(R.id.swipeAg)
    SwipeRefreshLayout mSwipeAg;
    @BindView(R.id.swipeFm)
    SwipeRefreshLayout mSwipeFm;

    @BindView(R.id.ivRealTable)
    ImageView mIVRealTimeTable;
    @Override
    public void init() {

    }

    @Override
    public void initView(View rootView) {
        LogUtils.e("数值", "initView---------------------------------");
        mIvToolbarNavigation.setVisibility(View.GONE);
        mVToolbarDivision.setVisibility(View.INVISIBLE);
        /* 这里需要进行判断 ok的*/
        if (UserCache.getRole().equals(AppConst.ROLE_FARMER)) {
            mTvToolbarTitle.setText(UIUtils.getString(R.string.title_intelligent_monitoring));
            mLlFarmer.setVisibility(View.VISIBLE);
            mPresenter.getLogManageList();
            mIVRealTimeTable.setVisibility(View.VISIBLE);
            mTvTimeTwo.setText(TimeUtil.getPastDate(0)); // 时间
        } else if (UserCache.getRole().equals(AppConst.ROLE_AGENCY)
                || UserCache.getRole().equals(AppConst.ROLE_VICE_MANAGER)) {
            // 经销商
            mIVRealTimeTable.setVisibility(View.VISIBLE);
            mTvToolbarTitle.setText(UIUtils.getString(R.string.title_intelligent_detection));
            mLlAgency.setVisibility(View.VISIBLE);
            mPresenter.getAgencyDvData();
            mTvTimeOne.setText(TimeUtil.getPastDate(0)); // 时间
        } else if (UserCache.getRole().equals(AppConst.ROLE_GENERAL_AGENCY)
                || UserCache.getRole().equals(AppConst.ROLE_AGENT)
                || UserCache.getRole().equals(AppConst.ROLE_VICE_ADMIN)
                || UserCache.getRole().equals(AppConst.ROLE_VICE_AGENT)
                ) {
            mTvToolbarTitle.setText(R.string.title_custom_list);
            mIVRealTimeTable.setVisibility(View.GONE);
            mLlGeneralAgency.setVisibility(View.VISIBLE);
            // 加载数据用的
            mPresenter.getAgencyList();
        }
    }

    @Override
    public void initData() {

    }


    @Override
    public void initListener() {
        // 时间选择
        mLlTime.setOnClickListener(v -> {
            String[] timeArray = mTvTimeOne.getText().toString().split("-");
            showDataOne(timeArray);
        });

        mLlPondTime.setOnClickListener(v -> {
            String[] timeArray = mTvTimeTwo.getText().toString().split("-");
            showDataTwo(timeArray);
        });

        mSpFarm.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                mPresenter.materialSpSelected(AppConst.SP_TYPE_EMPLOYEE, position);
            }
        });


        mSpPond.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                mPresenter.materialSpSelected(AppConst.SP_TYPE_POND, position);
            }
        });

        mIVRealTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserCache.getRole().equals(AppConst.ROLE_FARMER)){
                    mPresenter.jumpMiniChartFromPond();
                }else {
                    mPresenter.jumpMiniChartFromEpy();
                }
            }
        });

        mSwipeAg.setOnRefreshListener(() -> mPresenter.refreshAgency());

        mSwipeFm.setOnRefreshListener(() -> mPresenter.refreshFarm());
    }

    private void showDataTwo(String[] timeArray) {
        MaterialDialog dialogOne = new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.title_time_pick))
                .customView(R.layout.dialog_datepicker, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .showListener(dialog -> ObjectAnimator.ofFloat(mIvTimeOne, View.ROTATION.getName(), 180).start())
                .dismissListener(dialog -> ObjectAnimator.ofFloat(mIvTimeOne, View.ROTATION.getName(), -180, 0).start())
                .onPositive((dialog, which) -> {
                    DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                    mTvTimeTwo.setText(String.format("%d-%02d-%d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                })
                .build();
        DatePicker datePicker = (DatePicker) dialogOne.findViewById(R.id.datePicker);
        datePicker.init(Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]) - 1, Integer.parseInt(timeArray[2]), null);
        dialogOne.show();

    }

    public void showDataOne(String[] timeArray) {
        MaterialDialog dialogOne = new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.title_time_pick))
                .customView(R.layout.dialog_datepicker, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .showListener(dialog -> ObjectAnimator.ofFloat(mIvTimeOne, View.ROTATION.getName(), 180).start())
                .dismissListener(dialog -> ObjectAnimator.ofFloat(mIvTimeOne, View.ROTATION.getName(), -180, 0).start())
                .onPositive((dialog, which) -> {
                    DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                    mTvTimeOne.setText(String.format("%d-%02d-%d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                })
                .build();
        DatePicker datePicker = (DatePicker) dialogOne.findViewById(R.id.datePicker);
        datePicker.init(Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]) - 1, Integer.parseInt(timeArray[2]), null);
        dialogOne.show();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(getActivity()).register(AppConst.UPDATE_CUL_CUSTOM, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.updateAgencyList(); // 跟新客户列表
            }
        });

        BroadcastManager.getInstance(getActivity()).register(AppConst.UPDATE_EP_CUL_POUND, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.updatePondList();
            }
        });

        BroadcastManager.getInstance(getActivity()).register(AppConst.UPDATE_MG_EP_LIST, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.setMSpItem();
            }
        });

        BroadcastManager.getInstance(getActivity()).register(AppConst.UPDATE_DEVICE_SELECTED, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String realName = intent.getStringExtra("String");
                LogUtils.e("传递数值", realName);
                mPresenter.setEpSelectName(realName);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.UPDATE_CUL_CUSTOM);
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.UPDATE_EP_CUL_POUND);
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.UPDATE_MG_EP_LIST);
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.UPDATE_DEVICE_SELECTED);

    }

    @Override
    protected CultivateFgPresenter createPresenter() {
        return new CultivateFgPresenter((MainActivity) getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_cultivate;
    }


    @Override
    public RecyclerView getAgencyListRv() {
        return mRvGeneralAgList;
    }

    @Override
    public RecyclerView getFarmerListRV() {
        return mRvDevice;
    }


    @Override
    public MaterialSpinner getSpFarm() {
        return mSpFarm;
    }

    @Override
    public SwipeRefreshLayout getAgRefreshLayout() {
        return mSwipeAg;
    }

    @Override
    public TextView tvTimeNow() {
        return mTvTimeOne;
    }

    @Override
    public RecyclerView getPondDvList() {
        return mRvFarmDevice;
    }

    @Override
    public MaterialSpinner getPondSp() {
        return mSpPond;
    }

    @Override
    public TextView tvPondTimeNow() {
        return mTvTimeTwo;
    }

    @Override
    public TextView farmDeviceTotal() {
        return mTvFarmDeviceSt;
    }

    @Override
    public SwipeRefreshLayout getFmSwipeRefreshLayout() {
        return mSwipeFm;
    }

    @Override
    public TextView employeeDeviceTotal() {
        return mTvDeviceState;
    }


}

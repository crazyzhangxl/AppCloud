package com.jit.appcloud.ui.activity.me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.EpDeviceResponse;
import com.jit.appcloud.model.response.NormalResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_LEFT;

/**
 * @author zxl on 2018/06/26.
 *         discription: 养殖户设备管理活动
 */
public class FmDeviceManageActivity extends BaseActivity {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tvDeviceAdd)
    TextView mTvDeviceAdd;
    @BindView(R.id.tvDeviceEditor)
    TextView mTvDeviceEditor;
    @BindView(R.id.tvDeviceDel)
    TextView mTvDeviceDel;
    @BindView(R.id.rvDeviceMg)
    RecyclerView mRvDeviceMg;
    @BindView(R.id.ibToolbarMore)
    ImageButton mIbToolbarMore;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.tvRecordNum)
    TextView mTvRecordNum;

    private BaseQuickAdapter<EpDeviceResponse.DataBean, BaseViewHolder> mDeviceAdapter;
    private List<EpDeviceResponse.DataBean> mDeviceList = new ArrayList<>();
    private int mSelectPosition = -1;

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_fm_device_manage;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.title_device_manage);
        initAdapter();
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvDeviceMg.setLayoutManager(manager);
        mDeviceAdapter = new BaseQuickAdapter<EpDeviceResponse.DataBean, BaseViewHolder>(R.layout.item_fm_device_show, mDeviceList) {
            @Override
            protected void convert(BaseViewHolder helper, EpDeviceResponse.DataBean item) {
                if (mSelectPosition == helper.getAdapterPosition()) {
                    helper.getView(R.id.llDevice).setBackgroundColor(UIUtils.getColor(R.color.item_select_color));
                } else {
                    helper.getView(R.id.llDevice).setBackgroundColor(UIUtils.getColor(R.color.white));
                }
                /*  设置数据 -------------  */
                helper.setText(R.id.tvDeviceID, item.getDevice_no());
                helper.setText(R.id.tvMaIP, item.getMac_ip());
                helper.setText(R.id.tvPondID, item.getNumber());
            }
        };
        mDeviceAdapter.openLoadAnimation(SLIDEIN_LEFT);
        mDeviceAdapter.setOnItemClickListener((adapter, view, position) -> {
            mSelectPosition = position;
            mDeviceAdapter.notifyDataSetChanged();
        });
        mRvDeviceMg.setAdapter(mDeviceAdapter);
    }


    /**
     * 刷新设备数据
     */
    private void refreshData() {
        showWaitingDialog(getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().epQueryAllDevice(UserCache.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(epDeviceResponse -> {
                    hideWaitingDialog();
                    if (epDeviceResponse.getCode() == 1) {
                        mDeviceList.clear();
                        // 切记一定是addAll
                        mDeviceList.addAll(epDeviceResponse.getData());
                        mDeviceAdapter.notifyDataSetChanged();
                        updateNum();
                    }
                }, throwable -> hideWaitingDialog());

    }

    @Override
    protected void initData() {
        refreshData();
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mTvDeviceAdd.setOnClickListener(v -> jumpToActivity(EpAddDeviceActivity.class));

        mTvDeviceEditor.setOnClickListener(v -> {
            if (mSelectPosition == -1) {
                UIUtils.showToast("请选择设备!");
            } else {
                // -------------------
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConst.EXTRA_SER_AG_DEVICE_EDITOR, mDeviceList.get(mSelectPosition));
                jumpToActivity(EpEtDeviceActivity.class, bundle);
            }
        });

        mTvDeviceDel.setOnClickListener(v -> {
            if (mSelectPosition == -1) {
                UIUtils.showToast("请选择设备!");
            } else {
                ApiRetrofit.getInstance().epDeleteSingleDevice(mDeviceList.get(mSelectPosition).getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.getCode() == 1) {
                                mDeviceList.remove(mSelectPosition);
                                updateNum();
                                mDeviceAdapter.notifyItemRemoved(mSelectPosition);
                                mSelectPosition = -1;
                            } else {
                                UIUtils.showToast(response.getMsg());
                            }
                        }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
            }
        });
    }

    private void updateNum() {
        mTvRecordNum.setText(String.format("记录数:%s", String.valueOf(mDeviceList.size())));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(this).register(AppConst.UPDATE_EP_DEVICE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshData();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(this).unregister(AppConst.UPDATE_EP_DEVICE);
    }
}

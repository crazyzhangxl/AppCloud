package com.jit.appcloud.ui.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.model.bean.SensorNormalBean;
import com.jit.appcloud.model.response.SensorNmResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IRTimeMonitorAtView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import com.luck.picture.lib.decoration.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/8/9.
 *         discription: 实时监护的P层
 */

public class RTimeMonitorAtPresenter extends BasePresenter<IRTimeMonitorAtView> {

    private BaseQuickAdapter<SensorNormalBean,BaseViewHolder> mDeviceInfoAdapter;
    /**
     * 应当记得进行初始化撒
     */
    private List<SensorNormalBean> mRtMonitorList = new ArrayList<>();
    private int deatRole = -1;
    public RTimeMonitorAtPresenter(BaseActivity context, int deatRole) {
        super(context);
        this.deatRole = deatRole;
    }


    public void setDeviceAdapter() {
        if (mDeviceInfoAdapter == null){
            getView().mRvTimeMonitor().setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
            getView().mRvTimeMonitor().addItemDecoration(new RecycleViewDivider
                    (mContext,LinearLayoutManager.HORIZONTAL, UIUtils.dip2px(mContext, 0.5f),UIUtils.getColor(R.color.bg_line_2)));
            mDeviceInfoAdapter = new BaseQuickAdapter<SensorNormalBean, BaseViewHolder>(
                    R.layout.item_rtime_monitor,mRtMonitorList) {
                @Override
                protected void convert(BaseViewHolder helper, SensorNormalBean item) {
                    helper.setText(R.id.tvDeviceName,String.format("%s-%s",item.getPound_name(),item.getDevice_no()));

                    helper.setText(R.id.tvO2Value,String.format("%smg/L",String.valueOf(item.getOxygen())));
                    // PH值
                    helper.setText(R.id.tvPHValue,String.valueOf(item.getPh()));
                    // 温度值
                    helper.setText(R.id.tvTempValue,String.format("%s℃",String.valueOf(item.getTemperature())));
                    // 时间
                    helper.setText(R.id.tvTimeUpdate, TimeUtil.formatDateToMin(item.getTime()));
                }
            };

            getView().mRvTimeMonitor().setAdapter(mDeviceInfoAdapter);
        }else {
            mDeviceInfoAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 刷新塘口的设备信息
     * @param pondID
     */
    public void queryPondDateAndRf(int pondID) {
        String data = getView().mTvTime().getText().toString().trim();
        ApiRetrofit.getInstance().queryMiniSensorDayByPondID(pondID,data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sensorNmResponse -> {
                    if (sensorNmResponse != null && sensorNmResponse.getCode() == 1){
                        mRtMonitorList.clear();
                        mRtMonitorList.addAll(sensorNmResponse.getData());
                        setDeviceAdapter();
                    }else {
                        UIUtils.showToast(sensorNmResponse.getMsg());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

    /**
     * 刷新按照用户名获取的用户信息
     * @param username
     */
    public void queryFmDateAndRf(String username) {
        LogUtils.e("检测"," 执行了刷新养殖户数据");
        String data = getView().mTvTime().getText().toString().trim();
        ApiRetrofit.getInstance().queryMiniSensorDayByName(username,data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sensorNmResponse -> {
                    if (sensorNmResponse != null && sensorNmResponse.getCode() == 1){
                        mRtMonitorList.clear();
                        mRtMonitorList.addAll(sensorNmResponse.getData());
                        setDeviceAdapter();
                    }else {
                        UIUtils.showToast(sensorNmResponse.getMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    }
                });

    }
}

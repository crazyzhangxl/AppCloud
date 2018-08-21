package com.jit.appcloud.ui.presenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserOtherCache;
import com.jit.appcloud.model.response.DeviceManageResponse;
import com.jit.appcloud.ui.activity.cultivate.DeviceModifyActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IDeviceManageView;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/6/5.
 *         discription: 设备管理
 */

public class DeviceManageAtPresenter extends BasePresenter<IDeviceManageView> {
    private BaseActivity mmContext;
    private List<DeviceManageResponse.DataBean> mDeviceInfoList = new ArrayList<>();
    private BaseQuickAdapter<DeviceManageResponse.DataBean,BaseViewHolder> mDeviceAdapter;
    public DeviceManageAtPresenter(BaseActivity context) {
        super(context);
        mmContext = context;
    }

    public void setAdapterWithData(){

        setAdapter();
        getDeviceData();
    }

    public void refreshData(){
        getDeviceData();
    }

    private void getDeviceData() {
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().deviceInfoGet(UserCache.getToken(),UserOtherCache.getFarmSelectedName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceManageResponse -> {
                    mContext.hideWaitingDialog();
                    if (deviceManageResponse.getCode() == 1){
                            mDeviceInfoList.clear();
                            mDeviceAdapter.setNewData(mDeviceInfoList);
                            mDeviceInfoList.addAll(deviceManageResponse.getData());
                    } else {
                        UIUtils.showToast(deviceManageResponse.getMsg());
                    }
                }, throwable -> {
                    mContext.hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });

    }

    private void setAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        getView().getDeviceManageRV().setLayoutManager(manager);
        // ------------
        mDeviceAdapter = new BaseQuickAdapter<DeviceManageResponse.DataBean, BaseViewHolder>(R.layout.item_device_manage,mDeviceInfoList) {
            @Override
            protected void convert(BaseViewHolder helper, DeviceManageResponse.DataBean item) {
              helper.setText(R.id.tvDeviceId,String.valueOf(item.getDevice_no()));
              helper.setText(R.id.tvDeviceFirst,String.format("Mac地址:%s    塘口:%s",item.getMac_ip(),DBManager.getInstance().queryPondNameByID(item.getPound_id())));
              helper.setText(R.id.tvDeviceSecond,String.format("设备状态:%s    分配时间:%s",item.getStatus() == 1?"开":"关", TimeUtil.getMyTime(item.getTime())));
              helper.getView(R.id.ll_delete).setOnClickListener((View v) -> mmContext.showMaterialDialog(null, "确认删除该设备?",
                      UIUtils.getString(R.string.str_ensure), UIUtils.getString(R.string.str_cancel), (dialog, which) -> {
                          dialog.dismiss();
                          mmContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
                          ApiRetrofit.getInstance().deviceDelete(UserCache.getToken(),item.getId())
                                  .subscribeOn(Schedulers.io())
                                  .observeOn(AndroidSchedulers.mainThread())
                                  .subscribe(response -> {
                                      mmContext.hideWaitingDialog();
                                      if (response.getCode() == 1){
                                          BroadcastManager.getInstance(mmContext)
                                                  .sendBroadcast(AppConst.DEVICE_UPDATE);

                                      }else {
                                          UIUtils.showToast(response.getMsg());
                                      }
                                  }, throwable -> {
                                      mmContext.hideWaitingDialog();
                                      UIUtils.showToast(throwable.getLocalizedMessage());
                                  });

                      }, (dialog, which) -> dialog.dismiss()));

              helper.getView(R.id.ll_edit).setOnClickListener(v -> {
                  Bundle bundle = new Bundle();
                  bundle.putSerializable(AppConst.MODIFY_DEVICE_INFO,item);
                  mmContext.jumpToActivity(DeviceModifyActivity.class,bundle);
              });
            }
        };
        getView().getDeviceManageRV().setAdapter(mDeviceAdapter);
    }
}

package com.jit.appcloud.ui.presenter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.bean.PersonalBean;
import com.jit.appcloud.model.bean.SensorNormalBean;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.FarmDeviceResponse;
import com.jit.appcloud.model.response.PondGetByMGResponse;
import com.jit.appcloud.model.response.SensorNmResponse;
import com.jit.appcloud.model.response.UserBdAmResponse;
import com.jit.appcloud.ui.activity.cultivate.CusDtRoleAmActivity;
import com.jit.appcloud.ui.activity.cultivate.FarmDeviceActivity;
import com.jit.appcloud.ui.activity.cultivate.NormalChartActivity;
import com.jit.appcloud.ui.activity.cultivate.RTimeMonitorActivity;
import com.jit.appcloud.ui.activity.cultivate.RegisterListActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.ICultivateFgView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.MyDialog;
import com.luck.picture.lib.decoration.RecycleViewDivider;

import org.litepal.util.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
/**
 *  检测/客户的P层 ----- 重点
 * @author  zxl
 * @date 2018/4/17
 *
 * 未完成实时数据表格
 */

public class CultivateFgPresenter extends BasePresenter<ICultivateFgView>{
    private BaseActivity mmContext;
    /**
     * 总部/总代/平行总部/平行总代相关
     */
    private List<PersonalBean> mListGAData = new ArrayList<>();
    private BaseQuickAdapter<PersonalBean,BaseViewHolder> mListGAAdapter;
    private int mGgNowSelectPosition;
    private int mGPreSelectPosition = -1;

    /**
     * 经销商/平行经销相关
     */
    private List<SensorNormalBean> mDeviceList = new ArrayList<>();
    private List<String> mEmployeeList = new ArrayList<>();

    private BaseQuickAdapter<SensorNormalBean,BaseViewHolder> mDeviceAdapter;
    private List<PersonalBean> mCustomBeanList = new ArrayList<>();
    private int mAgSelected = 0;
    private int mFmSelected = 0;


    /**
     * 养殖户相关
     */
    private List<SensorNormalBean> mPondDeviceList = new ArrayList<>();
    private BaseQuickAdapter<SensorNormalBean,BaseViewHolder> mPondDeviceAdapter;
    private List<PondGetByMGResponse.DataBean> mMEpPondList = new ArrayList<>(); // 获取的塘口列表
    private List<String> mPondStrList = new ArrayList<>();

    public CultivateFgPresenter(BaseActivity context) {
        super(context);
        mmContext = context;
    }

    /* =============== 总代理身份下开始 =======================*/

    public void getAgencyList(){
        setAgencyAdapter();
        getAgencyData();
    }

    public void updateAgencyList(){
        getAgencyData();
    }

    private void getAgencyData() {
        /*  网络请求 */
        ApiRetrofit.getInstance().getAgencyNextUserInfo(UserCache.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userBdAmResponse -> {
                    if (userBdAmResponse != null && userBdAmResponse.getCode() == 1){
                        mListGAData.clear();
                        if (userBdAmResponse.getData() != null) {
                            mListGAData.addAll(userBdAmResponse.getData());
                        }
                        mListGAAdapter.notifyDataSetChanged();
                    }else {
                        UIUtils.showToast(userBdAmResponse.getMsg());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

    private void setAgencyAdapter(){
        /*  第三个参数 是是否设置颠倒布局的意思*/
        getView().getAgencyListRv().setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        getView().getAgencyListRv().addItemDecoration(new RecycleViewDivider(
                mContext, LinearLayoutManager.HORIZONTAL, UIUtils.dip2px(mContext, 0.5f), ContextCompat.getColor(mContext, R.color.bg_line_2)));
        if (mListGAAdapter == null){
            mListGAAdapter = new BaseQuickAdapter<PersonalBean, BaseViewHolder>(R.layout.item_cultivate_agency_list,mListGAData) {
                @Override
                protected void convert(BaseViewHolder helper, PersonalBean item) {
                    helper.itemView.setBackground(UIUtils.getDrawable(R.drawable.selector_item_select_state_no_press));
                    helper.itemView.setSelected(helper.getAdapterPosition() == mGgNowSelectPosition);
                    ImageView ivHead = helper.getView(R.id.ivHead);
                    helper.setText(R.id.tv_listName,item.getRealname());
                    helper.setText(R.id.tvNumReg,String.valueOf(item.getUserCount()));
                    helper.setText(R.id.tvNumEquip,String.valueOf(item.getDeviceCount()));
                    Glide.with(mContext).load(item.getImage()).apply(new RequestOptions().placeholder(R.mipmap.default_header)).into(ivHead);
                    /* 经销商数量 这时候已经是具体的经销商了，那么应该呈现进销商的信息*/
                    helper.getView(R.id.rlCus).setOnClickListener(v -> {
                        if (mGgNowSelectPosition != helper.getAdapterPosition()){
                            mGPreSelectPosition = mGgNowSelectPosition;
                            mGgNowSelectPosition = helper.getAdapterPosition();
                            notifyItemChanged(mGgNowSelectPosition);
                            notifyItemChanged(mGPreSelectPosition);
                        }
                        Intent intent = new Intent(mContext,CusDtRoleAmActivity.class);
                        intent.putExtra(AppConst.TURN_TO_SHOW_AGENCY,"agency");
                        intent.putExtra(AppConst.INFO_SHOW_DETAIL,item.getUsername());
                        intent.putExtra(AppConst.INFO_CUS_ID,item.getId());
                        mmContext.jumpToActivity(intent);
                    });

                    /* 注册数点击事件 即养殖户,这里的话确实列出的是养殖户的列表 */
                    helper.getView(R.id.tvNumReg).setOnClickListener(v -> {
                        if (mGgNowSelectPosition != helper.getAdapterPosition()){
                            mGPreSelectPosition = mGgNowSelectPosition;
                            mGgNowSelectPosition = helper.getAdapterPosition();
                            notifyItemChanged(mGgNowSelectPosition);
                            notifyItemChanged(mGPreSelectPosition);
                        }
                        Intent intent = new Intent(mContext,RegisterListActivity.class);
                        intent.putExtra(AppConst.AGENCY_NAME,item.getUsername());
                        mmContext.jumpToActivity(intent);
                    });

                    /* 设备 ==》进入设备*/
                    helper.getView(R.id.tvNumEquip).setOnClickListener(v -> {
                        if (mGgNowSelectPosition != helper.getAdapterPosition()){
                            mGPreSelectPosition = mGgNowSelectPosition;
                            mGgNowSelectPosition = helper.getAdapterPosition();
                            notifyItemChanged(mGgNowSelectPosition);
                            notifyItemChanged(mGPreSelectPosition);
                        }
                        Intent intent = new Intent(mContext,FarmDeviceActivity.class);
                        intent.putExtra(AppConst.AGENCY_NAME,item.getUsername());
                        mmContext.jumpToActivity(intent);
                    });

                   helper.getView(R.id.llDel).setOnClickListener(v -> {
                       if (mGgNowSelectPosition != helper.getAdapterPosition()){
                           mGPreSelectPosition = mGgNowSelectPosition;
                           mGgNowSelectPosition = helper.getAdapterPosition();
                           notifyItemChanged(mGgNowSelectPosition);
                           notifyItemChanged(mGPreSelectPosition);
                       }
                       if (UserCache.getRole().equals(AppConst.ROLE_VICE_AGENT)
                               || UserCache.getRole().equals(AppConst.ROLE_VICE_ADMIN)
                               || UserCache.getRole().equals(AppConst.ROLE_VICE_MANAGER)){
                           UIUtils.showToast(UIUtils.getString(R.string.please_permission_denied));
                           return;
                       }
                       showDeleteAgency(item.getId(),item.getRealname(),helper.getAdapterPosition());
                   });
                }
            };
        }
        getView().getAgencyListRv().setAdapter(mListGAAdapter);
    }

    private void showDeleteAgency(int id,String customName,int position){
        View rgScsView = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete_manage, null);
        MyDialog delDialog = new MyDialog(mContext, R.style.MyDialog);
        Window window = delDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.BottomDialog);
        window.getDecorView().setPadding(50, 0, 50, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        delDialog.setContentView(rgScsView);
        EditText etPsd = delDialog.findViewById(R.id.etPsd);
        TextView tvCancel = delDialog.findViewById(R.id.tvCancel);
        TextView tvSub =  delDialog.findViewById(R.id.tvSubmit);
        TextView tvCusName = delDialog.findViewById(R.id.tvCustomName);
        tvCusName.setText(customName);
        tvCancel.setOnClickListener(v -> delDialog.dismiss());
        tvSub.setOnClickListener((View v) -> {
            if (UserCache.getPassword().equals(etPsd.getText().toString())){
                ApiRetrofit.getInstance().deleteUser(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.getCode() == 1){
                                UIUtils.showToast(mmContext.getString(R.string.please_delete_user_success));
                                mListGAData.remove(position);
                                mListGAAdapter.notifyDataSetChanged();
                            }else {
                                UIUtils.showToast(response.getMsg());
                            }
                        }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
                delDialog.dismiss();
            }else {
                UIUtils.showToast(mmContext.getString(R.string.please_pwd_error));
            }
        });
        delDialog.show();
    }


    /* =============== 总代理身份下结束 ======================= */


    /* ===============  经销商身份开始  =========================*/

    public void getAgencyDvData(){
        // 智能检测的数据 ====
        setMSpItem();
        setFarmerAdapter();
    }

    /**
     *  由注册界面转向检测界面 ---- 设置选中的经销商的真实姓名
     *  // 这里还没刷新呢
     * @param realName 养殖户真实姓名
     */
    public void setEpSelectName(String realName){
        getView().getSpFarm().setSelectedIndex( mEmployeeList.indexOf(realName));
        materialSpSelected(AppConst.SP_TYPE_EMPLOYEE, mEmployeeList.indexOf(realName));
    }

    public void setMSpItem() {
        LogUtils.e("数值传递","-----------setMSpItem()---------------");
        ApiRetrofit.getInstance().getAgencyNextUserInfo(UserCache.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userBdAmResponse -> {
                    if (userBdAmResponse.getCode() == 1){
                        mCustomBeanList.clear();
                        if (userBdAmResponse.getData() != null) {
                            mCustomBeanList.addAll(userBdAmResponse.getData());
                        }
                        filterMS();
                    }else {
                        UIUtils.showToast(userBdAmResponse.getMsg());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));

    }

    private void filterMS() {
        if (mCustomBeanList != null && mCustomBeanList.size() != 0){
            for (PersonalBean bean:mCustomBeanList){
                mEmployeeList.add(bean.getRealname());
            }
        }
        if (mEmployeeList != null && mEmployeeList.size() != 0) {
            getView().getSpFarm().setItems(mEmployeeList);
            getView().getSpFarm().setSelectedIndex(0);
            refreshSensorRvByName(mCustomBeanList.get(0).getUsername());
            LogUtils.e("数值","默认条目完毕---------------------------------");
        }
    }


    /**
     * 下拉刷新经销商
     */
    public void refreshAgency() {
        if (mCustomBeanList == null || mCustomBeanList.size() == 0){
            getView().getFmSwipeRefreshLayout().setRefreshing(false);
            UIUtils.showToast("暂无设备信息可获得!");
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ApiRetrofit.getInstance()
                        .querySensorLatestByUName(mCustomBeanList.get(mAgSelected)
                                .getUsername())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<SensorNmResponse>() {
                            @Override
                            public void accept(SensorNmResponse sensorNmResponse) throws Exception {
                                getView().getAgRefreshLayout().setRefreshing(false);
                                if (sensorNmResponse != null && sensorNmResponse.getCode() ==1 ) {
                                    mDeviceList.clear();
                                    if (sensorNmResponse.getData() != null) {
                                        mDeviceList.addAll(sensorNmResponse.getData());
                                    }
                                    mDeviceAdapter.notifyDataSetChanged();
                                }else {
                                    UIUtils.showToast(sensorNmResponse.getMsg());
                                }

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                getView().getAgRefreshLayout().setRefreshing(false);
                                LogUtils.e("错误",throwable.getLocalizedMessage());
                                UIUtils.showToast(throwable.getLocalizedMessage());
                            }
                        });
            }
        },1000);
    }


    private void refreshSensorRvByName(String userName){
        LogUtils.e("数值传递","刷新-"+userName);
        ApiRetrofit.getInstance().querySensorLatestByUName(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sensorNmResponse -> {
                    if (sensorNmResponse != null && sensorNmResponse.getCode() == 1){
                        mDeviceList.clear();
                        if (sensorNmResponse.getData() != null) {
                            mDeviceList.addAll(sensorNmResponse.getData());
                        }
                        mDeviceAdapter.notifyDataSetChanged();
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
        getView().employeeDeviceTotal().setText(
                String.format(UIUtils.getString(R.string.format_device_total),
                        String.valueOf(totalCount),String.valueOf(countOn),String.valueOf(i)));
    }


    public void materialSpSelected(int flag, int position){
        if (flag == AppConst.SP_TYPE_EMPLOYEE) {
            mAgSelected = position;
            refreshSensorRvByName(mCustomBeanList.get(position).getUsername());
        }else if (flag == AppConst.SP_TYPE_POND){
            // 进行刷新
            mFmSelected = position;
            refreshSensorRv(mMEpPondList.get(position).getId());
        }
    }

    private void setFarmerAdapter() {
        // 模拟数据吧
        getView().getFarmerListRV().setLayoutManager
                (new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mDeviceAdapter = new BaseQuickAdapter<SensorNormalBean, BaseViewHolder>(R.layout.item_pond_info,mDeviceList) {
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

        mDeviceAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, NormalChartActivity.class);
            // 塘口ID传递过去了
            intent.putExtra(AppConst.POND_ID_SELECTED,mDeviceList.get(position).getPound_id());
            intent.putExtra(AppConst.DEVICE_ID,mDeviceList.get(position).getDevice_id());
            mContext.jumpToActivity(intent);
        });
        getView().getFarmerListRV().setAdapter(mDeviceAdapter);
    }



    /**
     * 由经销商进行跳转
     */
    public void jumpMiniChartFromEpy() {
        Intent intent = new Intent(mContext,RTimeMonitorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(AppConst.K_SELECTED_INDEX,mAgSelected);
        bundle.putString(AppConst.EXTRA_TIME,getView().tvTimeNow().getText().toString());
        bundle.putSerializable(AppConst.FM_SERIALIZABLE_ARRAY,(Serializable) mCustomBeanList);
        intent.putExtras(bundle);
        mContext.jumpToActivity(intent);
    }


    /* ===============  经销商身份结束  =========================*/



    /* ===============  养殖户身份开始  =========================*/

    public void getLogManageList(){
        setPondDeviceTable();
        getFixationList();
    }

    private void refreshTotal(int deviceTotal, int deviceOn, int deviceOff){
        getView().farmDeviceTotal().setText(
                String.format(UIUtils.getString(R.string.format_device_total),
                String.valueOf(deviceTotal),String.valueOf(deviceOn),String.valueOf(deviceOff)));
    }

    /**
     *  用于塘口状态改变进而进行调整
     */
    public void updatePondList(){
        getFixationList();
    }

    private void getFixationList(){
        ApiRetrofit.getInstance().getPondByEp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pondGetByMGResponse -> {
                    if (pondGetByMGResponse != null && pondGetByMGResponse.getCode() == 1){
                        mMEpPondList.clear();
                        if (pondGetByMGResponse.getData() != null) {
                            mMEpPondList.addAll(pondGetByMGResponse.getData());
                        }
                        mPondStrList.clear();
                        // 是这个的原因
                        if (mMEpPondList != null && mMEpPondList.size() != 0) {
                            for (PondGetByMGResponse.DataBean bean : mMEpPondList) {
                                mPondStrList.add(bean.getNumber());
                            }
                        }
                        filterPondList();
                    }else {
                        LogUtils.e("设备",pondGetByMGResponse.getMsg());
                        UIUtils.showToast(pondGetByMGResponse.getMsg());
                    }
                }, throwable -> {
                    LogUtils.e("设备",throwable.getLocalizedMessage());
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    private void filterPondList() {
        if (mPondStrList != null && mPondStrList.size() != 0) {
            getView().getPondSp().setItems(mPondStrList);
            getView().getPondSp().setSelectedIndex(0);
            refreshSensorRv(mMEpPondList.get(0).getId());
        }
    }

    private void refreshSensorRv(int pondID){
        ApiRetrofit.getInstance().querySensorLatestByPondID(pondID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sensorNmResponse -> {
                    if (sensorNmResponse != null && sensorNmResponse.getCode() == 1){
                        mPondDeviceList.clear();
                        if (sensorNmResponse.getData() != null) {
                            mPondDeviceList.addAll(sensorNmResponse.getData());
                        }
                        mPondDeviceAdapter.notifyDataSetChanged();
                        updateTotal();
                    }else {
                        UIUtils.showToast("实时设备信息获取失败!");
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e("设备",throwable.getLocalizedMessage());
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    }
                });
    }

    private void updateTotal() {
        int totalCount = mPondDeviceList.size();
        int countOn = 0;
        for (SensorNormalBean bean : mPondDeviceList){
            if ("1".equals(bean.getDevice_status())){
                countOn ++;
            }
        }
        refreshTotal(totalCount,countOn,totalCount -countOn);
    }

    private void setPondDeviceTable(){
        getView().getPondDvList().setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mPondDeviceAdapter = new BaseQuickAdapter<SensorNormalBean, BaseViewHolder>(R.layout.item_pond_info,mPondDeviceList) {
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

        mPondDeviceAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, NormalChartActivity.class);
            // 塘口ID传递过去了
            intent.putExtra(AppConst.POND_ID_SELECTED,mPondDeviceList.get(position).getPound_id());
            intent.putExtra(AppConst.DEVICE_ID,mPondDeviceList.get(position).getDevice_id());
            mContext.jumpToActivity(intent);
        });
        getView().getPondDvList().setAdapter(mPondDeviceAdapter);
    }


    /**
     *
     * 监控界面跳转向携带式表格的界面
     * 塘口信息
     */
    public void jumpMiniChartFromPond() {

        Intent intent = new Intent(mContext,RTimeMonitorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(AppConst.K_SELECTED_INDEX,mFmSelected);
        bundle.putString(AppConst.EXTRA_TIME,getView().tvPondTimeNow().getText().toString());
        bundle.putInt(AppConst.K_SELECTED_INDEX,mFmSelected);
        bundle.putSerializable(AppConst.POND_SERIALIZABLE_ARRAY,(Serializable) mMEpPondList);
        intent.putExtras(bundle);
        mContext.jumpToActivity(intent);
    }


    /**
     * 下拉刷新养殖户
     */
    public void refreshFarm() {
        if (mMEpPondList == null || mMEpPondList.size() == 0){
            getView().getFmSwipeRefreshLayout().setRefreshing(false);
            UIUtils.showToast("暂无设备信息可获得!");
            return;
        }

        new Handler().postDelayed(() -> ApiRetrofit.getInstance()
                .querySensorLatestByPondID(mMEpPondList.get(mFmSelected).getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sensorNmResponse -> {
                    getView().getFmSwipeRefreshLayout().setRefreshing(false);
                    if (sensorNmResponse != null && sensorNmResponse.getCode() ==1 ) {
                        mPondDeviceList.clear();
                        if (sensorNmResponse.getData() != null) {
                            mPondDeviceList.addAll(sensorNmResponse.getData());
                        }
                        mPondDeviceAdapter.notifyDataSetChanged();
                        LogUtils.e("错误","成功");
                    }else {
                        LogUtils.e("错误",sensorNmResponse.getMsg());
                        UIUtils.showToast(sensorNmResponse.getMsg());
                    }

                }, throwable -> {
                    getView().getFmSwipeRefreshLayout().setRefreshing(false);
                    LogUtils.e("错误",throwable.getLocalizedMessage());
                    UIUtils.showToast(throwable.getLocalizedMessage());
                }),1000);


    }

    /* ===============  养殖户身份结束  =========================*/
}

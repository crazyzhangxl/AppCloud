package com.jit.appcloud.ui.presenter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.PondMangeResponse;
import com.jit.appcloud.ui.activity.cultivate.PondDetailShowActivity;
import com.jit.appcloud.ui.activity.cultivate.PondModifyActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IPondManageView;
 import com.jit.appcloud.util.UIUtils;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/5/23.
 *         discription: 塘口管理的P
 */

public class PondManageAtPresenter extends BasePresenter<IPondManageView>{
    private BaseActivity mmContext;
    private String mFarmName;
    private BaseQuickAdapter<PondMangeResponse.DataBean,BaseViewHolder> mPondAdapter;
    private List<PondMangeResponse.DataBean> mPondList = new ArrayList<>();
    public PondManageAtPresenter(BaseActivity context) {
        super(context);
        mmContext = context;
    }

    public void setAdapterWithData(String farmName){
        setAdapter();
        this.mFarmName = farmName;
        getPondData();
    }

    private void setAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        getView().getPondManageRV().setLayoutManager(manager);
        if (mPondAdapter == null){
            mPondAdapter = new BaseQuickAdapter<PondMangeResponse.DataBean, BaseViewHolder>(R.layout.item_pond_normal,mPondList) {
                @SuppressLint("SetTextI18n")
                @Override
                protected void convert(BaseViewHolder helper, PondMangeResponse.DataBean item) {
                    LinearLayout llPondInfo =  helper.getView(R.id.llPondInfo);
                    LinearLayout llDelete =  helper.getView(R.id.ll_delete);
                    LinearLayout llEdit =  helper.getView(R.id.ll_edit);
                    TextView tvPondName =  helper.getView(R.id.tvPondName);
                    TextView tvPondCity =  helper.getView(R.id.tvPondCity); // 省市区
                    TextView tvPondDetail = helper.getView(R.id.tvPondDetail);
                    tvPondName.setText(item.getNumber());
                    tvPondCity.setText(item.getProvince()+item.getCity()+item.getCountry());
                    tvPondDetail.setText(String.format(UIUtils.getString(R.string.str_format_pond_detail)
                            ,String.valueOf(item.getLength())
                            ,String.valueOf(item.getWidth())
                            ,String.valueOf(item.getDepth())
                            ,String.valueOf(item.getSquare())
                            ,item.getUnit()));

                    llPondInfo.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.DETAIL_POND_INFO_LOOK,item);
                        mmContext.jumpToActivity(PondDetailShowActivity.class,bundle);
                    });

                    llDelete.setOnClickListener(v -> mmContext.showPSMaterialDialog(null, "确认删除该塘口?",
                            "确定", "取消", (dialog, which) -> {
                                dialog.dismiss();
                                ApiRetrofit.getInstance().deletePondByPondId(UserCache.getToken(),item.getId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(deletePondResponse -> {
                                            if (deletePondResponse.getCode() == 1){
                                                DBManager.getInstance().deletePondById(String.valueOf(item.getId()));
                                                BroadcastManager.getInstance(mmContext).sendBroadcast(AppConst.UPDATE_POND_MG);
                                            }else {
                                                UIUtils.showToast(deletePondResponse.getMsg());
                                            }
                                        }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));

                            }, (dialog, which) -> dialog.dismiss()));

                    llEdit.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.MODIFY_POND_INFO,item);
                        mmContext.jumpToActivity(PondModifyActivity.class,bundle);
                    });
                }
            };
        }
        getView().getPondManageRV().setAdapter(mPondAdapter);
    }
    /*  获得数据并设置进入*/
    private void getPondData() {
        ApiRetrofit.getInstance().getPondInfoByName(UserCache.getToken(),mFarmName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pondMangeResponse -> {
                    if (pondMangeResponse.getCode() == 1){
                        mPondList.clear();
                        mPondList.addAll(pondMangeResponse.getData());
                        mPondAdapter.setNewData(mPondList);
                    }else {
                        UIUtils.showToast(pondMangeResponse.getMsg());
                    }
                }, throwable -> {
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }
    /* 刷新数据*/
    public void refreshData(){
        getPondData();
    }


}

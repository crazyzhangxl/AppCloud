package com.jit.appcloud.ui.presenter;

import android.support.v7.widget.LinearLayoutManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.LogDeviceResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.ILogAtView;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/6/6.
 *         discription:
 */

public class LogDeviceAtPresenter extends BasePresenter<ILogAtView> {
    private int mCurrentPage;
    private int mMaxPage;
    private boolean isFirst;
    private List<LogDeviceResponse.DataBean.ListBean> mListCurrent = new ArrayList<>();
    private BaseQuickAdapter<LogDeviceResponse.DataBean.ListBean, BaseViewHolder> mAdapter;
    private int mPondId;

    public LogDeviceAtPresenter(BaseActivity context) {
        super(context);
    }

    public void initAdapter(){
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        getView().getLogRecv().setLayoutManager(manager);

        mAdapter = new BaseQuickAdapter<LogDeviceResponse.DataBean.ListBean, BaseViewHolder>(R.layout.item_log_device, mListCurrent) {
            @Override
            protected void convert(BaseViewHolder helper, LogDeviceResponse.DataBean.ListBean item) {
                helper.setText(R.id.tvLogDeviceTime, TimeUtil.getMyTime(item.getTime()));
                helper.setText(R.id.tvLogDeviceDetailName,
                        AppConst.LIST_DEVICE_CH[Arrays.asList(AppConst.LIST_DEVICE_EN).indexOf(item.getFun_name())]);
                if (AppConst.LIST_DEVICE_EN[0].equals(item.getFun_name())){
                    helper.setText(R.id.tvLogSmallNum,String.format("%s ~ %s mg/l",String.valueOf(item.getLow_in()),String.valueOf(item.getLow_out())));
                    helper.setText(R.id.tvLogBigNum,String.format("%s ~ %s mg/l",String.valueOf(item.getHigh_in()),String.valueOf(item.getHigh_out())));
                }else if (AppConst.LIST_DEVICE_EN[1].equals(item.getFun_name())){
                    helper.setText(R.id.tvLogSmallNum,String.format("%s ~ %s",String.valueOf(item.getLow_in()),String.valueOf(item.getLow_out())));
                    helper.setText(R.id.tvLogBigNum,String.format("%s ~ %s",String.valueOf(item.getHigh_in()),String.valueOf(item.getHigh_out())));
                }else if(AppConst.LIST_DEVICE_EN[2].equals(item.getFun_name())){
                    helper.setText(R.id.tvLogSmallNum,String.format("%s ~ %s ℃",String.valueOf(item.getLow_in()),String.valueOf(item.getLow_out())));
                    helper.setText(R.id.tvLogBigNum,String.format("%s ~ %s ℃",String.valueOf(item.getHigh_in()),String.valueOf(item.getHigh_out())));
                }
                helper.setText(R.id.tvLogDeviceMan,item.getUsername());
                helper.setText(R.id.tvLogDevicePondName,item.getPoundName());
                helper.setText(R.id.tvLogDeviceName,item.getDeviceName());
            }
        };
        mAdapter.setOnLoadMoreListener(() -> new android.os.Handler().postDelayed(() -> {
            if (mCurrentPage > mMaxPage) {
                mAdapter.loadMoreEnd();
            }else {
                refreshAndSetData(mPondId);
            }
        },1000),getView().getLogRecv());
        getView().getLogRecv().setAdapter(mAdapter);
    }

    public void refreshAndSetData(int poundId){
        mPondId = poundId;
        mCurrentPage = 1;
        isFirst = true;
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().getLogDevice(UserCache.getToken(),poundId,mCurrentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    mContext.hideWaitingDialog();
                    if (response.getCode() == 1){
                        mMaxPage = response.getData().getPages();
                        if ( response.getData().getPageNum() <= response.getData().getPages()){
                            mCurrentPage ++;
                        }
                        if (isFirst){
                            mListCurrent.clear();
                            mListCurrent.addAll(response.getData().getList());
                            mAdapter.setNewData(mListCurrent);
                            isFirst = false;
                        }else {
                            if (mAdapter.isLoading()){
                                mAdapter.loadMoreComplete();
                            }
                            // 可以加载 那么页数就得+1了
                            mListCurrent.addAll(response.getData().getList());
                            mAdapter.notifyDataSetChanged();
                        }

                    }else {
                        UIUtils.showToast(response.getMsg());
                    }
                }, throwable -> {
                    mContext.hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });

    }
}

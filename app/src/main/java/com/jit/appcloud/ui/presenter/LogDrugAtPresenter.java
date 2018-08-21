package com.jit.appcloud.ui.presenter;

import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.LogDrugResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.ILogAtView;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/6/4.
 *         discription:
 */

public class LogDrugAtPresenter extends BasePresenter<ILogAtView> {
    private int mCurrentPage;
    private int mMaxPage;
    private boolean isFirst;
    private List<LogDrugResponse.DataBean.ListBean> mListCurrent = new ArrayList<>();
    private BaseQuickAdapter<LogDrugResponse.DataBean.ListBean, BaseViewHolder> mAdapter;
    private int mPondId;
    public LogDrugAtPresenter(BaseActivity context) {
        super(context);
    }

    public void initAdapter(){
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        getView().getLogRecv().setLayoutManager(manager);

        mAdapter = new BaseQuickAdapter<LogDrugResponse.DataBean.ListBean, BaseViewHolder>(R.layout.item_log_drug, mListCurrent) {
            @Override
            protected void convert(BaseViewHolder helper, LogDrugResponse.DataBean.ListBean item) {
                helper.setText(R.id.tvDrugLogTime, TimeUtil.getMyTime(item.getTime()));
                helper.setText(R.id.tvLogDrugPondName,String.valueOf(item.getPound_id()));
                helper.setText(R.id.tvLogDrugBuySum,item.getBuy_amount());
                helper.setText(R.id.tvLogDrugDes,item.getDescription());
                helper.setText(R.id.tvLogDrugFunction,item.getFunction());
                helper.setText(R.id.tvLogDrugOrigin,item.getOrigin());
                helper.setText(R.id.tvLogDrugPutSum,String.valueOf(item.getAmount()));
                helper.setText(R.id.tvLogDrugRemark,item.getRemark());
                helper.setText(R.id.tvLogDrugMan,item.getUsername());
                helper.setText(R.id.tvLogDrugName,item.getName());

            }
        };
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentPage > mMaxPage) {
                            mAdapter.loadMoreEnd();
                        }else {
                            refreshAndSetData(mPondId);
                        }
                    }
                },1000);
            }
        },getView().getLogRecv());
        getView().getLogRecv().setAdapter(mAdapter);

    }

    public void refreshAndSetData(int poundId){
        mPondId = poundId;
        mCurrentPage = 1;
        isFirst = true;
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().getLogMedicine(UserCache.getToken(),poundId,mCurrentPage)
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

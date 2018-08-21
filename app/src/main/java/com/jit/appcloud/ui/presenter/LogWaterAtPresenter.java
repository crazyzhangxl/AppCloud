package com.jit.appcloud.ui.presenter;

import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.LogWaterResponse;
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

public class LogWaterAtPresenter extends BasePresenter<ILogAtView> {
    private int mCurrentPage;
    private int mMaxPage;
    private boolean isFirst;
    private List<LogWaterResponse.DataBean.ListBean> mListCurrent = new ArrayList<>();
    private BaseQuickAdapter<LogWaterResponse.DataBean.ListBean, BaseViewHolder> mAdapter;
    private int mPondId;

    public LogWaterAtPresenter(BaseActivity context) {
        super(context);
    }
    public void initAdapter(){
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        getView().getLogRecv().setLayoutManager(manager);

        mAdapter = new BaseQuickAdapter<LogWaterResponse.DataBean.ListBean, BaseViewHolder>(R.layout.item_log_water, mListCurrent) {
            @Override
            protected void convert(BaseViewHolder helper, LogWaterResponse.DataBean.ListBean item) {
                helper.setText(R.id.tvLogWaterAlkaliTmp,String.format(UIUtils.getString(R.string.str_format_normal_du),item.getAlkali()));
                helper.setText(R.id.tvLogWaterNano2,String.format(UIUtils.getString(R.string.str_format_normal_du),item.getNano2()));
                helper.setText(R.id.tvLogWaterO2,String.format(UIUtils.getString(R.string.str_format_normal_du),item.getO2()));
                helper.setText(R.id.tvLogWaterPH,item.getNh());
                helper.setText(R.id.tvLogWaterRemark,item.getRemark());
                helper.setText(R.id.tvLogWaterTime, TimeUtil.getMyTime(item.getTime()));
                helper.setText(R.id.tvLogWaterTmp,String.format(UIUtils.getString(R.string.str_format_temp),item.getTemperature()));
                helper.setText(R.id.tvLogWaterMan,item.getUsername());
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
        ApiRetrofit.getInstance().getLogWater(UserCache.getToken(),poundId,mCurrentPage)
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

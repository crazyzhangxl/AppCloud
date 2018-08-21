package com.jit.appcloud.ui.activity.cultivate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.PondBean;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.DeviceInfoGetResponse;
import com.jit.appcloud.model.response.DeviceManageResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;
import com.lqr.recyclerview.LQRRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/06/05.
 *         discription: 设备选择的二级联动菜单
 */
public class DeviceSelectedActivity extends BaseActivity {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.category_left_rv)
    RecyclerView mCategoryLeftRv;
    @BindView(R.id.category_right_rv)
    LQRRecyclerView mCategoryRightRv;

    private BaseQuickAdapter<PondBean,BaseViewHolder> mLeftAdapter;
    private BaseQuickAdapter<DeviceManageResponse.DataBean,BaseViewHolder> mRightAdapter;
    private List<PondBean> mLeftInfoList = new ArrayList<>();
    private List<DeviceManageResponse.DataBean> mRightInfoList = new ArrayList<>();
    private int selectPos = -1;
    private int rightSelect = -1;
    @Override
    protected void init() {
        mLeftInfoList = DBManager.getInstance().queryAllPond();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_device_selected;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("塘口--设备");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(getString(R.string.str_title_save));
        setLeftAdapter();
        setRightAdapter();
        setLeftSelect(0);
    }

    private void setRightAdapter() {
        mRightAdapter = new BaseQuickAdapter<DeviceManageResponse.DataBean, BaseViewHolder>(R.layout.item_category_right,mRightInfoList) {
            @Override
            protected void convert(BaseViewHolder helper, DeviceManageResponse.DataBean item) {
                ImageView ivTrue = helper.getView(R.id.ivMakeTrue);
                helper.setText(R.id.tvWeather,item.getDevice_no());
                if (rightSelect == helper.getAdapterPosition()){
                    ivTrue.setVisibility(View.VISIBLE);
                }else {
                    ivTrue.setVisibility(View.GONE);
                }
            }
        };
        mRightAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == rightSelect){
                return;
            }
            rightSelect = position;
            mRightAdapter.notifyDataSetChanged();
        });
        mCategoryRightRv.setAdapter(mRightAdapter);
    }

    private void setLeftAdapter() {
        mCategoryLeftRv.setLayoutManager(new LinearLayoutManager(mContext));
        mLeftAdapter = new BaseQuickAdapter<PondBean, BaseViewHolder>(R.layout.item_category_left,mLeftInfoList){
            @Override
            protected void convert(BaseViewHolder baseViewHolder, PondBean item) {
                if (selectPos == baseViewHolder.getAdapterPosition())
                {
                    baseViewHolder.setVisible(R.id.item_main_left_bg,true);
                    baseViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    baseViewHolder.setTextColor(R.id.item_main_left_type, Color.parseColor("#40a5f3"));
                }else{
                    baseViewHolder.itemView.setBackgroundColor(Color.parseColor("#f7f7f7"));
                    baseViewHolder.setTextColor(R.id.item_main_left_type, Color.parseColor("#333333"));
                    baseViewHolder.setVisible(R.id.item_main_left_bg,false);
                }
                baseViewHolder.setText(R.id.item_main_left_type,item.getPondName());
            }
        };
        mLeftAdapter.setOnItemClickListener((adapter, view, position) -> setLeftSelect(position));
        mCategoryLeftRv.setAdapter(mLeftAdapter);
    }

    private void setLeftSelect(int position){
        if (position == selectPos){
            return;
        }
        selectPos = position;
        mLeftAdapter.notifyDataSetChanged();
                /* 进行网络请求来刷新右边的View*/
        showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().deviceGetByUserAndPond(UserCache.getToken()
                ,UserCache.getName(),mLeftInfoList.get(position).getPondId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceInfoGetResponse -> {
                        hideWaitingDialog();
                    if (deviceInfoGetResponse.getCode() == 1){
                        mRightInfoList.clear();
                        rightSelect = -1;
                        mRightInfoList.addAll(deviceInfoGetResponse.getData());
                        mRightAdapter.setNewData(mRightInfoList);
                    }else {
                        UIUtils.showToast(deviceInfoGetResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mTvPublishNow.setOnClickListener(v -> {
            if (rightSelect == -1){
                UIUtils.showToast("客官请选择设备再保存!");
            }

            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            intent.putExtra(AppConst.DEVICE_POND_NAME,mLeftInfoList.get(selectPos).getPondName());
            intent.putExtra(AppConst.DEVICE_POND_ID,mLeftInfoList.get(selectPos).getPondId());
            intent.putExtra(AppConst.DEVICE_NO,mRightInfoList.get(rightSelect).getDevice_no());
            intent.putExtra(AppConst.DEVICE_ID,mRightInfoList.get(rightSelect).getId());
            finish();
        });
    }

}

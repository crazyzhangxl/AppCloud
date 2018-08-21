package com.jit.appcloud.ui.activity.cultivate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.ui.adapter.WeatherListShowAdapter;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author zxl on 2018/05/30.
 *         discription: 用于养殖户选择的列表活动
 */
public class FarmerChooseActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.rvFarmerList)
    RecyclerView mRvFarmerList;

    private int mSelectPosition = -1;
    private List<String> mFarmerList = new ArrayList<>();
    private WeatherListShowAdapter mAdapter;
    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_farmer_choose;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("责任人列表");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(UIUtils.getString(R.string.str_btn_confirm));
        setAdapter();
    }

    private void setAdapter() {
        mFarmerList.addAll(DBManager.getInstance().queryFarmName());
        if (mAdapter == null){
            mAdapter = new WeatherListShowAdapter(R.layout.item_weather_list,mFarmerList);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvFarmerList.setLayoutManager(linearLayoutManager);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            mSelectPosition = position;
            mAdapter.setSelectPosition(position);
            mAdapter.notifyDataSetChanged();
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRvFarmerList.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mTvPublishNow.setOnClickListener(v -> {
            if (mSelectPosition == -1){
                UIUtils.showToast("请选择责任人");
            }else {
                Intent intent = new Intent();
                //intent.putExtra(AppConst.FARMER_SELECTED,mFarmerList.get(mSelectPosition));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}

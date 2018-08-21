package com.jit.appcloud.ui.activity.cultivate;

import android.appwidget.AppWidgetProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserNormalCache;
import com.jit.appcloud.model.request.UserInfoUpRequest;
import com.jit.appcloud.ui.adapter.WeatherListShowAdapter;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/05/20.
 *         discription: 单个条目的选择确认回传活动
 */
public class SingleSelectionActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.rvSelectionList)
    RecyclerView mRvSelectionList;
    private int mSelectPosition = -1;
    private List<String> mShowList = new ArrayList<>();
    private WeatherListShowAdapter mAdapter;
    private String mTitle;
    private int mPositionType = -1;
    private String mResult;

    private String[] mType;
    private String[] mName;
    @Override
    protected void init() {
        if (getIntent().hasExtra(AppConst.FLAG_WEATHER)){
            mShowList = Arrays.asList(AppConst.WEATHER_SHOW);
            mTitle = "天气";
            mPositionType = 1;
        }else if (getIntent().hasExtra(AppConst.FLAG_AGENCY_CATEGORY)){
            mShowList = Arrays.asList(AppConst.AGENCY_CATEGORY);
            mTitle = "经销类别";
            mPositionType = 2;
        }else if (getIntent().hasExtra(AppConst.FLAG_FARMER)){
            mTitle = "责任人列表";
            mShowList.clear();
            mShowList.addAll(DBManager.getInstance().queryFarmName());
            mPositionType = 3;
        }else if (getIntent().hasExtra(AppConst.FLAG_POND)){
            mTitle = "塘口";
            mShowList= DBManager.getInstance().queryPondName();
            mPositionType = 4;
        }else if (getIntent().hasExtra(AppConst.FLAG_SILIAO)){
            mTitle = "投放物品";
            mType = AppConst.SILIAO_TYPE;
            mName = AppConst.SILIAO_NAME;
            for (int i=0;i<mType.length;i++){
                mShowList.add(mType[i] + "-" + mName[i]);
            }
            mPositionType = 5;
        }else if (getIntent().hasExtra(AppConst.FLAG_SEED_NAME)){
            mTitle = "鱼苗名称";
            mType = AppConst.SEED_TYPE;
            mName = AppConst.SEED_NAME;
            mName = AppConst.SILIAO_NAME;
            for (int i=0;i<mType.length;i++){
                mShowList.add(mType[i] + "-" + mName[i]);
            }
            mPositionType = 6;
        }else if(getIntent().hasExtra(AppConst.FLAG_DEVICE)){
            mTitle = "设备名称";
            mShowList = Arrays.asList(AppConst.LIST_DEVICE_CH);
            mPositionType = 7;
        }else if (getIntent().hasExtra(AppConst.FLAG_SILIAO_TYPE)){
            mTitle = "投放类型";
            mShowList = Arrays.asList(AppConst.LIST_FEED_PUT_TYPE);
            mPositionType = 8;
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_single_selection;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(mTitle)) {
            mTvToolbarTitle.setText(mTitle);
        }
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText("确定");
        setAdapter();
    }

    private void setAdapter() {
        if (mAdapter == null){
            mAdapter = new WeatherListShowAdapter(R.layout.item_weather_list,mShowList);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvSelectionList.setLayoutManager(linearLayoutManager);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            mSelectPosition = position;
            mAdapter.setSelectPosition(position);
            mAdapter.notifyDataSetChanged();
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRvSelectionList.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mTvPublishNow.setOnClickListener(v -> {
            if (mSelectPosition == -1){
                UIUtils.showToast("客官请先选择条目再提交");
                return;
            }
            mResult = mShowList.get(mSelectPosition);
            switch (mPositionType){
                case 1:
                    setReturn();
                    break;
                case 2:
                    postAgencyCategory();
                    break;
                case 3:
                    setReturn();
                    break;
                case 4:
                    setPondReturn();
                    break;
                case 5:
                    setReturn();
                    break;
                case 6:
                    setReturn();
                    break;
                case 7:
                    setDeviceReturn();
                    break;
                case 8:
                    setReturn();
            }
        });
    }

    private void setDeviceReturn() {
        Intent intent = new Intent();
        intent.putExtra(AppConst.SINGLE_ITEM_SELECTED, mResult);
        intent.putExtra(AppConst.SINGLE_ITEM_SELECTED_ID,AppConst.LIST_DEVICE_EN[mSelectPosition]);
        setResult(RESULT_OK,intent);
        finish();
    }


    private void postAgencyCategory(){
        showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        UserInfoUpRequest userInfoUpRequest = UserNormalCache.getUserInfoUpRequest();
        userInfoUpRequest.setCategory(mResult);
        ApiRetrofit.getInstance().updateUserInfo(UserCache.getToken()
                ,userInfoUpRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userUpdateInfoResponse -> {
                    hideWaitingDialog();
                    if (userUpdateInfoResponse.getCode() == 1){
                        UserNormalCache.setCategory(mResult);
                        setReturn();
                    }else {
                        UIUtils.showToast(userUpdateInfoResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    private void setReturn(){
        Intent intent = new Intent();
        intent.putExtra(AppConst.SINGLE_ITEM_SELECTED, mResult);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void setPondReturn(){
        Intent intent = new Intent();
        intent.putExtra(AppConst.SINGLE_ITEM_SELECTED, mResult);
        intent.putExtra(AppConst.POND_ID_SELECTED,DBManager.getInstance().queryPondIdByName(mResult));
        setResult(RESULT_OK,intent);
        finish();
    }


}

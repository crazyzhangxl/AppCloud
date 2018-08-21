package com.jit.appcloud.ui.activity.cultivate;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.UserInfoResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.MyTagFlowLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
/**
 * 现在首要的就是更新信息了
 *
 * @author zxl on 2018/05/22.
 *         discription: 客户详细信息展示
 *                      主要是经销商和养殖户信息的展示
 *                      根据用户名进行网络请求 ---- 后面还需增加塘口
 */
public class CustomerDSActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.ivHeadImage)
    CircleImageView mIvHeadImage;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.tvSignature)
    TextView mTvSignature;
    @BindView(R.id.rlTime)
    RelativeLayout mRlTime;
    @BindView(R.id.tvRegisterTime)
    TextView mTvRegisterTime;
    @BindView(R.id.tvDetailAds)
    TextView mTvDetailAds;
    @BindView(R.id.rlDetailAds)
    RelativeLayout mRlDetailAds;
    @BindView(R.id.tvCity)
    TextView mTvCity;
    @BindView(R.id.rlCity)
    RelativeLayout mRlCity;
    @BindView(R.id.cv)
    CardView mCv;
    @BindView(R.id.tvRealName)
    TextView mTvRealName;
    @BindView(R.id.rlRelName)
    RelativeLayout mRlRelName;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.rlPhone)
    RelativeLayout mRlPhone;
    @BindView(R.id.tvEmail)
    TextView mTvEmail;
    @BindView(R.id.rlEmail)
    RelativeLayout mRlEmail;
    @BindView(R.id.tvInCome)
    TextView mTvInCome;
    @BindView(R.id.rlIncome)
    RelativeLayout mRlIncome;
    @BindView(R.id.tvArea)
    TextView mTvArea;
    @BindView(R.id.rlArea)
    RelativeLayout mRlArea;
    @BindView(R.id.tvCategory)
    TextView mTvCategory;
    @BindView(R.id.rlCategory)
    RelativeLayout mRlCategory;
    @BindView(R.id.tvCompany)
    TextView mTvCompany;
    @BindView(R.id.rlCompany)
    RelativeLayout mRlCompany;
    @BindView(R.id.flowlayout)
    MyTagFlowLayout mFlowlayout;
    @BindView(R.id.ivHobby)
    ImageView mIvHobby;
    @BindView(R.id.RlFlow)
    RelativeLayout mRlFlow;
    @BindView(R.id.rlFarm)
    RelativeLayout mRlFarmer;
    @BindView(R.id.tvFarmer)
    TextView mTvFarmer;
    private ArrayList<String> mHobbyFlowList = new ArrayList<>();
    private ArrayList<String> mHobbyStrList = new ArrayList<>();
    private TagAdapter<String> mHobbyTagAdapter;

    private ArrayList<String> mFarmerFlowList = new ArrayList<>();
    private TagAdapter<String> mFarmerTagAdapter;
    private String mUserName = null;
    private UserInfoResponse.DataBean mUserBean;
    private int mPosition = -1;
    private int mFarmerNum;
    @Override
    protected void init() {

        if (getIntent().hasExtra(AppConst.TURN_TO_SHOW_AGENCY )){
            mPosition = 1;
            mUserName = getIntent().getStringExtra(AppConst.INFO_SHOW_DETAIL);
            mFarmerNum = getIntent().getIntExtra(AppConst.FARM_NUM,0);
        }else if (getIntent().hasExtra(AppConst.TURN_TO_SHOW_FARMER)){
            mPosition = 2;
            mUserName = getIntent().getStringExtra(AppConst.INFO_SHOW_DETAIL);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_customer_detail_show;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setFlowAdapter();
    }

    public void setFlowAdapter() {
        mHobbyTagAdapter = new TagAdapter<String>(mHobbyFlowList) {
            @Override
            public View getView(FlowLayout parent, int position, String bean) {
                TextView textView = null;
                textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flow_text, mFlowlayout, false);
                textView.setText(bean);
                return textView;
            }
        };
        mFlowlayout.setAdapter(mHobbyTagAdapter);
    }

    @Override
    protected void initData() {
        getDataFromServer();
    }

    private void getDataFromServer(){
        showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().getUserInfoByName(mUserName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInfoResponse -> {
                    hideWaitingDialog();
                    if (userInfoResponse.getCode() == 1){
                        mUserBean = userInfoResponse.getData();
                        if (mPosition == 1){
                            updateAgencyNow();
                        }else if (mPosition == 2){
                            updateFarmerNow();
                        }
                    }else {
                        UIUtils.showToast(userInfoResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    public void updateAgencyNow(){
        mTvToolbarTitle.setText("经销商信息明细");
        Glide.with(mContext).load(mUserBean.getImage()).apply(
                new RequestOptions().error(R.mipmap.default_header)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
        ).into(mIvHeadImage);
        mTvUserName.setText(mUserBean.getUsername());
        if (!TextUtils.isEmpty(mUserBean.getLastPasswordResetDate())) {
            mRlTime.setVisibility(View.VISIBLE);
            mTvRegisterTime.setText(TimeUtil.getMyTime(mUserBean.getLastPasswordResetDate()));
        }
        if (!TextUtils.isEmpty(mUserBean.getSign())){
            mTvSignature.setText(mUserBean.getSign());
        }

        if (!TextUtils.isEmpty(mUserBean.getRealname())){
            mTvRealName.setText(mUserBean.getRealname());
        }

        if (!TextUtils.isEmpty(mUserBean.getTel())){
            mTvPhone.setText(mUserBean.getTel());
        }

        if (!TextUtils.isEmpty(mUserBean.getAddress())){
            mTvDetailAds.setText(mUserBean.getAddress());
        }

        if (!TextUtils.isEmpty(mUserBean.getProvince())){
            mTvCity.setText(UIUtils.getString(R.string.str_format_city_bond
                    ,mUserBean.getProvince(),mUserBean.getCity(),mUserBean.getCountry()));
        }

        if (!TextUtils.isEmpty(mUserBean.getCategory())){
            mRlCategory.setVisibility(View.VISIBLE);
            mTvCategory.setText(mUserBean.getCategory());
        }

        if (!TextUtils.isEmpty(mUserBean.getArea())){
            mRlArea.setVisibility(View.VISIBLE);
            mTvArea.setText(mUserBean.getArea());
        }

        if (!TextUtils.isEmpty(mUserBean.getDepartment())){
            mRlCompany.setVisibility(View.VISIBLE);
            mTvCompany.setText(mUserBean.getDepartment());
        }

        if (!TextUtils.isEmpty(mUserBean.getEmail())){
            mTvEmail.setText(mUserBean.getEmail());
        }

        if (!TextUtils.isEmpty(mUserBean.getHobby())){
            mRlFlow.setVisibility(View.VISIBLE);
            String hobbyStr = mUserBean.getHobby();
            String[] hobbyArray = hobbyStr.split(",");
            mHobbyFlowList.clear();
            mHobbyFlowList.addAll(Arrays.asList(hobbyArray));
            mHobbyTagAdapter.notifyDataChanged();
        }

        mRlFarmer.setVisibility(View.VISIBLE);
        /* 不可以将非字符串设置给 texTview或者editText*/
        mTvFarmer.setText(String.valueOf(mFarmerNum));
    }

    public void updateFarmerNow(){
        mTvToolbarTitle.setText("养殖户信息明细");
        Glide.with(mContext).load(mUserBean.getImage()).apply(
                new RequestOptions().error(R.mipmap.default_header)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
        ).into(mIvHeadImage);
        if (!TextUtils.isEmpty(mUserBean.getLastPasswordResetDate())) {
            mRlTime.setVisibility(View.VISIBLE);
            mTvRegisterTime.setText(TimeUtil.getMyTime(mUserBean.getLastPasswordResetDate()));
        }
        mTvUserName.setText(mUserBean.getUsername());
        if (!TextUtils.isEmpty(mUserBean.getSign())){
            mTvSignature.setText(mUserBean.getSign());
        }

        if (!TextUtils.isEmpty(mUserBean.getRealname())){
            mTvRealName.setText(mUserBean.getRealname());
        }

        if (!TextUtils.isEmpty(mUserBean.getTel())){
            mTvPhone.setText(mUserBean.getTel());
        }

        if (!TextUtils.isEmpty(mUserBean.getAddress())){
            mTvDetailAds.setText(mUserBean.getAddress());
        }

        if (!TextUtils.isEmpty(mUserBean.getProvince())){
            mTvCity.setText(UIUtils.getString(R.string.str_format_city_bond
                    ,mUserBean.getProvince(),mUserBean.getCity(),mUserBean.getCountry()));
        }

        if (!TextUtils.isEmpty(mUserBean.getHobby())){
            mRlFlow.setVisibility(View.VISIBLE);
            String hobbyStr = mUserBean.getHobby();
            String[] hobbyArray = hobbyStr.split(",");
            mHobbyFlowList.clear();
            mHobbyFlowList.addAll(Arrays.asList(hobbyArray));
            mHobbyTagAdapter.notifyDataChanged();
        }
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
    }
}

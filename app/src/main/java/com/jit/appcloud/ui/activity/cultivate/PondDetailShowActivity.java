package com.jit.appcloud.ui.activity.cultivate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.response.PondMangeResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zxl on 2018/05/23.
 *         discription:塘口详情的活动
 *         主要通过序列化的Bean进行传递
 */
public class PondDetailShowActivity extends BaseActivity {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvPondNameShow)
    TextView mTvPondNameShow;
    @BindView(R.id.tvPondKindShow)
    TextView mTvPondKindShow;
    @BindView(R.id.tvPondAreaShow)
    TextView mTvPondAreaShow;
    @BindView(R.id.tvPondLengthShow)
    TextView mTvPondLengthShow;
    @BindView(R.id.tvPondWidthShow)
    TextView mTvPondWidthShow;
    @BindView(R.id.tvPondDepthShow)
    TextView mTvPondDepthShow;
    @BindView(R.id.tvPondRSManShow)
    TextView mTvPondRSManShow;
    @BindView(R.id.rlRsMan)
    RelativeLayout mRlRsMan;
    @BindView(R.id.tvPondPhoneShow)
    TextView mTvPondPhoneShow;
    @BindView(R.id.rlPhone)
    RelativeLayout mRlPhone;
    @BindView(R.id.tvPondEmailShow)
    TextView mTvPondEmailShow;
    @BindView(R.id.rlEmail)
    RelativeLayout mRlEmail;
    @BindView(R.id.tvPondLocation)
    TextView mTvPondLocation;
    @BindView(R.id.tvAShowLocation)
    TextView mTvAShowLocation;
    @BindView(R.id.rlModifyLocation)
    RelativeLayout mRlModifyLocation;
    @BindView(R.id.tvDetailLocation)
    TextView mTvDetailLocation;
    @BindView(R.id.rlDetailLocation)
    RelativeLayout mRlDetailLocation;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tvTimeAdded)
    TextView mTvTimeAdded;
    @BindView(R.id.rlAddTime)
    RelativeLayout mRlAddTime;
    private PondMangeResponse.DataBean mBean;

    @Override
    protected void init() {
        if (getIntent() != null){
            mBean = (PondMangeResponse.DataBean) getIntent().getExtras().getSerializable(AppConst.DETAIL_POND_INFO_LOOK);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_pond_detail_show;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(UIUtils.getString(R.string.str_title_pond_detail));
        setViewData();
    }

    @SuppressLint("SetTextI18n")
    private void setViewData() {
        if (mBean != null){
            mTvPondNameShow.setText(mBean.getNumber());
            mTvPondKindShow.setText(mBean.getType());
            mTvPondAreaShow.setText(mBean.getSquare()+mBean.getUnit());
            mTvPondLengthShow.setText(String.valueOf(mBean.getLength()));
            mTvPondWidthShow.setText(String.valueOf(mBean.getWidth()));
            mTvPondDepthShow.setText(String.valueOf(mBean.getDepth()));
            mTvAShowLocation.setText(String.format(UIUtils.getString(R.string.str_format_city_bond)
            ,mBean.getProvince(),mBean.getCity(),mBean.getCountry()));
            mTvDetailLocation.setText(mBean.getAddress());
            mTvPondRSManShow.setText(mBean.getUsername());
            mTvTimeAdded.setText(TimeUtil.getMyTime(mBean.getTime()));
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
    }


}

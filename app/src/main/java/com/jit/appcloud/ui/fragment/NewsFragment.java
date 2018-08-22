package com.jit.appcloud.ui.fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.ui.activity.MainActivity;
import com.jit.appcloud.ui.activity.news.MyPublishActivity;
import com.jit.appcloud.ui.base.BaseFragment;
import com.jit.appcloud.ui.presenter.NewsFgPresenter;
import com.jit.appcloud.ui.view.INewsFgView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.jaredrummler.materialspinner.MaterialSpinner;
import butterknife.BindView;


/**
 *
 * @author zxl
 * @date 2018/4/17
 */

public class NewsFragment extends BaseFragment<INewsFgView, NewsFgPresenter> implements INewsFgView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.llToolbarTitle)
    LinearLayout mLlToolbarTitle;
    @BindView(R.id.llToolbarMyAnno)
    LinearLayout mLlToolbarMyAnno;
    @BindView(R.id.tvNewsTabSyn)
    TextView mTvNewsTabSyn;
    @BindView(R.id.viewNewsHeadSyn)
    View mViewNewsHeadSyn;
    @BindView(R.id.llNewsTabSyn)
    LinearLayout mLlNewsTabSyn;
    @BindView(R.id.tvNewsTabHots)
    TextView mTvNewsTabHots;
    @BindView(R.id.viewNewsTabHots)
    View mViewNewsTabHots;
    @BindView(R.id.llNewsTabHots)
    LinearLayout mLlNewsTabHots;
    @BindView(R.id.tvNewsTabNew)
    TextView mTvNewsTabNew;
    @BindView(R.id.llNewsTabNew)
    LinearLayout mLlNewsTabNew;
    @BindView(R.id.viewNewsHeadNew)
    View mViewNewsHeadNew;
    @BindView(R.id.spNewsHead)
    MaterialSpinner mSpNewsHead;
    @BindView(R.id.viewNewsHeadSpinner)
    View mViewNewsHeadSpinner;
    @BindView(R.id.rcvNewsBody)
    RecyclerView mRcvNewsBody;
    private int mPrePosition = -1;

    @Override
    protected NewsFgPresenter createPresenter() {
        return new NewsFgPresenter((MainActivity) getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_news;
    }


    @Override
    public RecyclerView getRCVNewsBody() {
        return mRcvNewsBody;
    }



    @Override
    public void init() {

    }

    @Override
    public void initView(View rootView) {
        mPrePosition = -1; // 再次初始化 因为没有被回收
        mIvToolbarNavigation.setVisibility(View.GONE);
        mVToolbarDivision.setVisibility(View.INVISIBLE);
        mTvToolbarTitle.setText(UIUtils.getString(R.string.str_main_news));
        if (AppConst.ROLE_AGENT.equals(UserCache.getRole())
                || AppConst.ROLE_AGENCY.equals(UserCache.getRole())
                || AppConst.ROLE_GENERAL_AGENCY.equals(UserCache.getRole())){
            mLlToolbarMyAnno.setVisibility(View.VISIBLE);
        }
        // 设置spinner
        mSpNewsHead.setItems(AppConst.SP_PRODUCT_TYPE);
        mPresenter.setRevAdapter();
        /*  ===== 初始化设置  即加载综合哦 ====*/
        initMyListener();
    }

    @Override
    public void initData() {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mLlNewsTabSyn.callOnClick();
    }

    @Override
    public void initListener() {
    }

    private void initMyListener(){
        mLlToolbarMyAnno.setOnClickListener(v -> ((MainActivity)getActivity()).jumpToActivity(MyPublishActivity.class));

        mLlNewsTabSyn.setOnClickListener(v -> {
            setHeadClicked(mPrePosition, 0);  // =========
        });

        mLlNewsTabHots.setOnClickListener(v -> {
            setHeadClicked(mPrePosition, 1);  // ============
        });

        mLlNewsTabNew.setOnClickListener(v -> setHeadClicked(mPrePosition, 2));

        // spinner点击事件
        mSpNewsHead.setOnItemSelectedListener((view, position, id, item) -> {
            // 把其他设置为颜色
            setHeadClicked(mPrePosition, 3);
            mPresenter.loadAllData(AppConst.NewsFlag.SYN,AppConst.SP_PRODUCT_TYPE[position]);
        });

    }


    public void setHeadClicked(int prePosition, int nowPosition) {
        if (prePosition == nowPosition) {
            return;
        }
        switch (prePosition) {
            case 0:
                mViewNewsHeadSyn.setVisibility(View.GONE);
                mTvNewsTabSyn.setTextColor(UIUtils.getColor(R.color.black));
                break;
            case 1:
                mViewNewsTabHots.setVisibility(View.GONE);
                mTvNewsTabHots.setTextColor(UIUtils.getColor(R.color.black));
                break;
            case 2:
                mViewNewsHeadNew.setVisibility(View.GONE);
                mTvNewsTabNew.setTextColor(UIUtils.getColor(R.color.black));
                break;
            case 3:
                mViewNewsHeadSpinner.setVisibility(View.GONE);
                mSpNewsHead.setTextColor(UIUtils.getColor(R.color.black));
                break;
            default:
                break;
        }
        switch (nowPosition) {
            case 0:
                mViewNewsHeadSyn.setVisibility(View.VISIBLE);
                mTvNewsTabSyn.setTextColor(UIUtils.getColor(R.color.green3));
                mPresenter.loadAllData(AppConst.NewsFlag.SYN,AppConst.SP_PRODUCT_TYPE[0]);
                break;
            case 1:
                mViewNewsTabHots.setVisibility(View.VISIBLE);
                mTvNewsTabHots.setTextColor(UIUtils.getColor(R.color.green3));
                mPresenter.loadAllData(AppConst.NewsFlag.HOT,AppConst.SP_PRODUCT_TYPE[0]);
                break;
            case 2:
                mViewNewsHeadNew.setVisibility(View.VISIBLE);
                mTvNewsTabNew.setTextColor(UIUtils.getColor(R.color.green3));
                mPresenter.loadAllData(AppConst.NewsFlag.DISCOUNT,AppConst.SP_PRODUCT_TYPE[0]);
                break;
            case 3:
                mViewNewsHeadSpinner.setVisibility(View.VISIBLE);
                mSpNewsHead.setTextColor(UIUtils.getColor(R.color.green3));
                break;
            default:
                break;
        }
        mPrePosition = nowPosition;
    }
}

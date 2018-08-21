package com.jit.appcloud.ui.activity.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.SearchUserAtPresenter;
import com.jit.appcloud.ui.view.ISearchUserAtView;

import butterknife.BindView;

/**
 * @author zxl on 2018/06/11.
 *         discription: 搜索用户的活动
 */
public class SearchUserActivity extends BaseActivity<ISearchUserAtView, SearchUserAtPresenter> implements ISearchUserAtView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.etSearchContent)
    EditText mEtSearchContent;
    @BindView(R.id.llToolbarSearch)
    LinearLayout mLlToolbarSearch;
    @BindView(R.id.tvMsg)
    TextView mTvMsg;
    @BindView(R.id.llSearch)
    LinearLayout mLlSearch;
    @BindView(R.id.rlNoResultTip)
    RelativeLayout mRlNoResultTip;

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_search_user;
    }

    @Override
    protected SearchUserAtPresenter createPresenter() {
        return new SearchUserAtPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setVisibility(View.GONE);
        mLlToolbarSearch.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mEtSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = mEtSearchContent.getText().toString().trim();
                mRlNoResultTip.setVisibility(View.GONE);
                if (content.length() > 0) {
                    mLlSearch.setVisibility(View.VISIBLE);
                    mTvMsg.setText(content);
                } else {
                    mLlSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mLlSearch.setOnClickListener(v -> mPresenter.searchUser());
    }


    @Override
    public EditText getEtSearchContent() {
        return mEtSearchContent;
    }

    @Override
    public RelativeLayout getRlNoResultTip() {
        return mRlNoResultTip;
    }

    @Override
    public LinearLayout getLlSearch() {
        return mLlSearch;
    }
}

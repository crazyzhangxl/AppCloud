package com.jit.appcloud.ui.activity.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.PostScriptAtPresenter;
import com.jit.appcloud.ui.view.IPostScriptAtView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zxl on 2018/06/12.
 *         discription: 添加附言活动
 */
public class PostScriptActivity extends BaseActivity<IPostScriptAtView, PostScriptAtPresenter> implements IPostScriptAtView {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.etMsg)
    EditText mEtMsg;
    @BindView(R.id.ibClear)
    ImageButton mIbClear;
    @BindView(R.id.btnToolbarSend)
    Button mBtnToolbarSend;

    private String mFriendName;
    private String mFriendID;

    @Override
    public EditText getEtMsg() {
        return mEtMsg;
    }

    @Override
    protected void init() {
        mFriendName = getIntent().getStringExtra(AppConst.EXTRA_FRIEND_NAME);
        mFriendID = getIntent().getStringExtra(AppConst.EXTRA_FRIEND_ID);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_post_script;
    }

    @Override
    protected PostScriptAtPresenter createPresenter() {
        return new PostScriptAtPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("附录信息");
        mBtnToolbarSend.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(mFriendName)) {
            finish();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mIbClear.setOnClickListener(v -> mEtMsg.setText(""));
        mBtnToolbarSend.setOnClickListener(v -> mPresenter.addFriend(mFriendName,mFriendID));
    }
}

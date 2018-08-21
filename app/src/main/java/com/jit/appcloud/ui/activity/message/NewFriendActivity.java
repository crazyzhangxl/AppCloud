package com.jit.appcloud.ui.activity.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.NewFriendAtPresenter;
import com.jit.appcloud.ui.view.INewFriendAtView;
import com.lqr.recyclerview.LQRRecyclerView;
import butterknife.BindView;
/**
 * @author zxl on 2018/06/11.
 *         discription: 新朋友的活动
 *         <p>
 *         添加用户的通知会进入该活动
 *         由该活动可进入添加朋友
 */
public class NewFriendActivity extends BaseActivity<INewFriendAtView, NewFriendAtPresenter> implements INewFriendAtView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.llNoNewFriend)
    LinearLayout mLlNoNewFriend;
    @BindView(R.id.tvNewFriend)
    TextView mTvNewFriend;
    @BindView(R.id.rvNewFriend)
    LQRRecyclerView mRvNewFriend;
    @BindView(R.id.llHasNewFriend)
    LinearLayout mLlHasNewFriend;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;

    @Override
    public LinearLayout getLlNoNewFriend() {
        return mLlNoNewFriend;
    }

    @Override
    public LinearLayout getLlHasNewFriend() {
        return mLlHasNewFriend;
    }

    @Override
    public LQRRecyclerView getRvNewFriend() {
        return mRvNewFriend;
    }

    @Override
    public TextView getNewFriendText() {
        return mTvNewFriend;
    }

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_new_friend;
    }

    @Override
    protected NewFriendAtPresenter createPresenter() {
        return new NewFriendAtPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("新的朋友");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText("搜索朋友");
    }

    @Override
    protected void initData() {
        mPresenter.loadNewFriendData();
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mTvPublishNow.setOnClickListener(v -> jumpToActivity(SearchUserActivity.class));
        mLlHasNewFriend.setOnClickListener(v -> jumpToActivity(SearchUserActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(mContext).register(AppConst.UPDATE_NEW_FRIEND, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.refreshFriendData();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(mContext).unregister(AppConst.UPDATE_NEW_FRIEND);
    }
}

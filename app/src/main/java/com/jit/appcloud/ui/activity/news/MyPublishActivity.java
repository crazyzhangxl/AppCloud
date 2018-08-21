package com.jit.appcloud.ui.activity.news;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.MyPublishAtPresenter;
import com.jit.appcloud.ui.view.IMyPublishAtView;
import butterknife.BindView;
/**
 * @author zxl on 2018/05/24.
 *         discription: 我的资讯展示,可删除查看和编辑对应的资讯
 *         // 咨询发布只能够先等一等了 -----------
 */
public class MyPublishActivity extends BaseActivity<IMyPublishAtView, MyPublishAtPresenter> implements IMyPublishAtView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.rvMyPublish)
    RecyclerView mRvMyPublish;
    @BindView(R.id.btnAddPublish)
    Button mBtnAddPublish;
    @BindView(R.id.llMoveToTop)
    LinearLayout mLlMoveToTop;
    @BindView(R.id.llMoveToBottom)
    LinearLayout mLlMoveToBottom;
    @BindView(R.id.llMoveToTrick)
    LinearLayout mLlMoveToTrick;
    @BindView(R.id.llSave)
    LinearLayout mLlSave;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_my_publish;
    }

    @Override
    protected MyPublishAtPresenter createPresenter() {
        return new MyPublishAtPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.str_news_announce);
        mPresenter.setRcvAdapter();
        mPresenter.setMyPublish();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mBtnAddPublish.setOnClickListener(v -> MyPublishActivity.this.jumpToActivity(PublishInfoActivity.class));
        mLlMoveToTop.setOnClickListener(v -> mPresenter.actionMoveToTop());
        mLlMoveToBottom.setOnClickListener(v -> mPresenter.actionMoveTopBottom());
        mLlMoveToTrick.setOnClickListener(v -> mPresenter.actionMoveToTrick());
        mLlSave.setOnClickListener(v -> mPresenter.actionMoveToSaveSort());
    }

    @Override
    public RecyclerView getRVPublish() {
        return mRvMyPublish;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(this).register(AppConst.ADD_MY_PB, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.addMyPublish(intent.getStringExtra("String"));
            }
        });

        /*  ============ 修改只需要更新就好了 ==========*/
        BroadcastManager.getInstance(this).register(AppConst.UPDATE_MY_PB, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.updatePublish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(this).unregister(AppConst.ADD_MY_PB);
        BroadcastManager.getInstance(this).unregister(AppConst.UPDATE_MY_PB);
    }
}

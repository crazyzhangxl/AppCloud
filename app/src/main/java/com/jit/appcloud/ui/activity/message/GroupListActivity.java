package com.jit.appcloud.ui.activity.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.GroupListAtPresenter;
import com.jit.appcloud.ui.view.IGroupListAtView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zxl on 2018/07/23.
 *         discription: 群聊展示的活动
 */
public class GroupListActivity extends BaseActivity<IGroupListAtView, GroupListAtPresenter> implements IGroupListAtView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.ibAddMenu)
    ImageButton mIbAddMenu;
    @BindView(R.id.rvGroupList)
    RecyclerView mRvGroupList;
    @BindView(R.id.llGroups)
    LinearLayout mLlGroups;

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_group_list;
    }

    @Override
    protected GroupListAtPresenter createPresenter() {
        return new GroupListAtPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("群聊");
        mIbAddMenu.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initData() {
        mPresenter.loadGroups();

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mIbAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupListActivity.this,CreateGroupActivity.class);
                intent.putExtra(AppConst.EXTRA_FLAG_GROUP,AppConst.GROUP_FLAG_CREATE);
                GroupListActivity.this.jumpToActivity(intent);
            }
        });

    }

    private void registerBR() {
        BroadcastManager.getInstance(this).register(AppConst.GROUP_LIST_UPDATE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.loadGroups();
            }
        });
    }

    private void unRegisterBR() {
        BroadcastManager.getInstance(this).unregister(AppConst.GROUP_LIST_UPDATE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBR();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBR();
    }

    @Override
    public LinearLayout getLlGroups() {
        return mLlGroups;
    }

    @Override
    public RecyclerView getRvGroupList() {
        return mRvGroupList;
    }



}

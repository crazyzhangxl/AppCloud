package com.jit.appcloud.ui.fragment.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.ui.activity.MainActivity;
import com.jit.appcloud.ui.base.BaseFragment;
import com.jit.appcloud.ui.presenter.MsgRecentFgPresenter;
import com.jit.appcloud.ui.view.IMsgRecentView;
import com.jit.appcloud.util.LogUtils;
import com.lqr.recyclerview.LQRRecyclerView;

import butterknife.BindView;

/**
 *
 * @author zxl
 * @date 2018/4/27
 */

public class MsgRecentFragment extends BaseFragment<IMsgRecentView, MsgRecentFgPresenter> implements IMsgRecentView {
    @BindView(R.id.rvRecentMessage)
    LQRRecyclerView mRvRecentMessage;

    @Override
    public void init() {
        registerBR();
    }


    @Override
    public void initView(View rootView) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    protected MsgRecentFgPresenter createPresenter() {
        return new MsgRecentFgPresenter((MainActivity) getActivity());
    }


    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_msg_recent;
    }


    /**
     * 可见,必定执行刷新操作,了解了
     */
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getConversations();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegisterBR();

    }

    private void registerBR() {
        // 注册广播当获得通知即更新消息
        BroadcastManager.getInstance(getActivity()).register(AppConst.UPDATE_CONVERSATIONS, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.getConversations();
            }
        });
    }


    private void unRegisterBR() {
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.UPDATE_CONVERSATIONS);
    }


    @Override
    public LQRRecyclerView getRvRecentMessage() {
        return mRvRecentMessage;
    }
}

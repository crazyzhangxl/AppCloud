package com.jit.appcloud.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import com.jit.appcloud.R;
import com.jit.appcloud.model.bean.MsgTabBean;
import com.jit.appcloud.ui.activity.MainActivity;
import com.jit.appcloud.ui.activity.message.FriendCircleActivity;
import com.jit.appcloud.ui.adapter.MessagePagerAdapter;
import com.jit.appcloud.ui.base.BaseFragment;
import com.jit.appcloud.ui.base.MsgFragmentFactory;
import com.jit.appcloud.ui.presenter.MessageFgPresenter;
import com.jit.appcloud.ui.view.IMessageFgView;
import butterknife.BindView;
/**
 *
 * @author 张先磊
 * @date 2018/4/17
 */

public class MessageFragment extends BaseFragment<IMessageFgView, MessageFgPresenter> {
    @BindView(R.id.fg_mg_tab)
    TabLayout mFgMgTab;
    @BindView(R.id.fg_mg_viewpager)
    ViewPager mFgMgViewpager;
    private MessagePagerAdapter mMessagePagerAdapter;
    @BindView(R.id.llFind)
    LinearLayout mLlFind;
    @Override
    public void init() {

    }

    @Override
    public void initView(View rootView) {
       mMessagePagerAdapter =  new MessagePagerAdapter(getActivity(),getActivity().getSupportFragmentManager());
       mMessagePagerAdapter.addFlag(new Pair<>(MsgFragmentFactory.getInstance().getMsgContactsFragment(),new MsgTabBean("通讯录",R.drawable.ic_msg_comm_list)));
       //mMessagePagerAdapter.addFlag(new Pair<>(MsgFragmentFactory.getInstance().getMsgCrowdFragment(),new MsgTabBean("群聊",R.drawable.ic_msg_crowd)));
       mMessagePagerAdapter.addFlag(new Pair<>(MsgFragmentFactory.getInstance().getMsgRecentFragment(),new MsgTabBean("消息",R.drawable.ic_msg_recent)));
       //mMessagePagerAdapter.addFlag(new Pair<>(MsgFragmentFactory.getInstance().getMsgFindFragment(),new MsgTabBean("发现",R.drawable.ic_msg_find)));
       mFgMgViewpager.setAdapter(mMessagePagerAdapter);
       mFgMgTab.setupWithViewPager(mFgMgViewpager);
       for (int index =0 ;index<mMessagePagerAdapter.getCount();index++){
           mFgMgTab.getTabAt(index).setCustomView(mMessagePagerAdapter.getTabView(index,mFgMgTab));
       }
       mFgMgViewpager.setOffscreenPageLimit(mMessagePagerAdapter.getCount());
        // 默认切换到消息栏
        mFgMgViewpager.setCurrentItem(0);
    }


    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mLlFind.setOnClickListener(v -> jumpToActivity(FriendCircleActivity.class));
    }

    @Override
    protected MessageFgPresenter createPresenter() {
        return new MessageFgPresenter((MainActivity) getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_message;
    }


}

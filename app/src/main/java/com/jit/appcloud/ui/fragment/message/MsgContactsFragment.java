package com.jit.appcloud.ui.fragment.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.db_model.Friend;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.ui.activity.MainActivity;
import com.jit.appcloud.ui.activity.message.NewFriendActivity;
import com.jit.appcloud.ui.activity.message.GroupListActivity;
import com.jit.appcloud.ui.base.BaseFragment;
import com.jit.appcloud.ui.presenter.MsgContactsFgPresenter;
import com.jit.appcloud.ui.view.IMsgContactsFgView;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.QuickIndexBar;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRHeaderAndFooterAdapter;
import com.lqr.recyclerview.LQRRecyclerView;
import java.util.List;
import butterknife.BindView;

/**
 * Created by 张先磊 on 2018/4/27.
 * <p>
 * des: 通讯录的Fragment
 */

public class MsgContactsFragment extends BaseFragment<IMsgContactsFgView, MsgContactsFgPresenter> implements IMsgContactsFgView {
    @BindView(R.id.rvContacts)
    LQRRecyclerView mRvContacts;
    @BindView(R.id.qib)
    QuickIndexBar mQib;
    @BindView(R.id.tvLetter)
    TextView mTvLetter;

    private View mHeaderView;
    private TextView mFooterView;
    private TextView mTvNewFriendUnread;
    @Override
    public void init() {

    }

    @Override
    public void initView(View rootView) {
        mHeaderView = View.inflate(getActivity(), R.layout.header_rv_contacts, null);
        mTvNewFriendUnread = (TextView) mHeaderView.findViewById(R.id.tvNewFriendUnread);
        mFooterView = new TextView(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2Px(50));
        mFooterView.setLayoutParams(params);
        mFooterView.setGravity(Gravity.CENTER);
        showQuickIndexBar(true);
        // 注册广播 稍等下
        registerBR();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBR();
    }

    private void registerBR() {
        BroadcastManager.getInstance(getActivity()).register(AppConst.UPDATE_RED_DOT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                /*((MainActivity) getActivity()).mTvContactRedDot.setVisibility(View.VISIBLE);*/
                mTvNewFriendUnread.setVisibility(View.VISIBLE);
            }
        });
        BroadcastManager.getInstance(getActivity()).register(AppConst.UPDATE_FRIEND, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.loadContacts();
            }
        });
    }

    private void unregisterBR() {
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.UPDATE_RED_DOT);
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.UPDATE_FRIEND);
    }

    private void showLetter(String letter) {
        mTvLetter.setVisibility(View.VISIBLE);
        mTvLetter.setText(letter);
    }

    private void hideLetter() {
        mTvLetter.setVisibility(View.GONE);
    }

    /**
     * 是否显示快速导航条
     *
     * @param show
     */
    public void showQuickIndexBar(boolean show) {
        if (mQib != null) {
            mQib.setVisibility(show ? View.VISIBLE : View.GONE);
            mQib.invalidate();
        }
    }

    @Override
    public void initData() {
        mPresenter.loadContacts();
    }

    @Override
    public void initListener() {
        mHeaderView.findViewById(R.id.llNewFriend).setOnClickListener(v -> {
            ((MainActivity) getActivity()).jumpToActivity(NewFriendActivity.class);
            //((MainActivity) getActivity()).mTvContactRedDot.setVisibility(View.GONE);
            mTvNewFriendUnread.setVisibility(View.GONE);
        });
        mHeaderView.findViewById(R.id.llGroup).setOnClickListener(v -> {
                jumpToActivity(GroupListActivity.class);
        });
        mQib.setOnLetterUpdateListener(new QuickIndexBar.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                //显示对话框
                showLetter(letter);
                //滑动到第一个对应字母开头的联系人
                if ("↑".equalsIgnoreCase(letter)) {
                    mRvContacts.moveToPosition(0);
                } else if ("☆".equalsIgnoreCase(letter)) {
                    mRvContacts.moveToPosition(0);
                } else {
                    List<Friend> data = ((LQRAdapterForRecyclerView) ((LQRHeaderAndFooterAdapter) mRvContacts.getAdapter()).getInnerAdapter()).getData();
                    for (int i = 0; i < data.size(); i++) {
                        Friend friend = data.get(i);
                        String c = friend.getDisplayNameSpelling().charAt(0) + "";
                        if (c.equalsIgnoreCase(letter)) {
                            mRvContacts.moveToPosition(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onLetterCancel() {
                //隐藏对话框
                hideLetter();
            }
        });
    }

    @Override
    protected MsgContactsFgPresenter createPresenter() {
        return new MsgContactsFgPresenter((MainActivity) getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_msg_comm_list;
    }

    @Override
    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public LQRRecyclerView getRvContacts() {
        return mRvContacts;
    }

    @Override
    public TextView getFooterView() {
        return mFooterView;
    }
}

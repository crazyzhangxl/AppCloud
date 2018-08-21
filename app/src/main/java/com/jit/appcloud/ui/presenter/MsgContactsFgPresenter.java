package com.jit.appcloud.ui.presenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.Friend;
import com.jit.appcloud.ui.activity.message.UserInfoBySearchActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IMsgContactsFgView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.SortUtils;
import com.jit.appcloud.util.UIUtils;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRHeaderAndFooterAdapter;
import com.lqr.adapter.LQRViewHolderForRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/6/11.
 *         discription: 通讯录的Presenter
 */

public class MsgContactsFgPresenter extends BasePresenter<IMsgContactsFgView> {
    private List<Friend> mData = new ArrayList<>();
    private LQRHeaderAndFooterAdapter mAdapter;

    public MsgContactsFgPresenter(BaseActivity context) {
        super(context);
    }


    public void loadContacts(){
        setAdapter();
        loadData();
    }

    private void loadData() {
        /* 加载数据晚点再做吧!*/
        LogUtils.e("纠正","加载了联系人信息");
        Observable.just(DBManager.getInstance().getFriends())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(friends -> {
                    if (friends != null && friends.size() > 0) {
                        LogUtils.e("纠正--------------------","1");
                        mData.clear();
                        mData.addAll(friends);

                        getView().getFooterView().setText(UIUtils.getString(R.string.count_of_contacts, mData.size()));
                        //整理排序
                        LogUtils.e("纠正--------------------","2");
                        SortUtils.sortContacts(mData);
                        if (mAdapter != null)
                            mAdapter.notifyDataSetChanged();
                    }else {
                        mData.clear();
                        mAdapter.notifyDataSetChanged();
                        getView().getFooterView().setText(UIUtils.getString(R.string.count_of_contacts,0));
                    }
                }, throwable -> {
                    LogUtils.e("纠正+++错误",throwable.getLocalizedMessage());
                    UIUtils.showToast("加载联系人失败!");
                });
    }

    private void setAdapter() {
        if (mAdapter == null) {
            LQRAdapterForRecyclerView adapter = new LQRAdapterForRecyclerView<Friend>(mContext, mData, R.layout.item_contact) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, Friend item, int position) {
                    helper.setText(R.id.tvName, item.getDisplayName());
                    ImageView ivHeader = helper.getView(R.id.ivHeader);
                    Glide.with(mContext).load(item.getPortraitUri()).into(ivHeader);

                    String str = "";
                    //得到当前字母
                    String currentLetter = item.getDisplayNameSpelling().charAt(0) + "";
                    if (position == 0) {
                        str = currentLetter;
                    } else {
                        //得到上一个字母
                        String preLetter = mData.get(position - 1).getDisplayNameSpelling().charAt(0) + "";
                        //如果和上一个字母的首字母不同则显示字母栏
                        if (!preLetter.equalsIgnoreCase(currentLetter)) {
                            str = currentLetter;
                        }
                    }
                    int nextIndex = position + 1;
                    if (nextIndex < mData.size() - 1) {
                        //得到下一个字母
                        String nextLetter = mData.get(nextIndex).getDisplayNameSpelling().charAt(0) + "";
                        //如果和下一个字母的首字母不同则隐藏下划线
                        if (!nextLetter.equalsIgnoreCase(currentLetter)) {
                            helper.setViewVisibility(R.id.vLine, View.INVISIBLE);
                        } else {
                            helper.setViewVisibility(R.id.vLine, View.VISIBLE);
                        }
                    } else {
                        helper.setViewVisibility(R.id.vLine, View.INVISIBLE);
                    }
                    if (position == mData.size() - 1) {
                        helper.setViewVisibility(R.id.vLine, View.GONE);
                    }

                    //根据str是否为空决定字母栏是否显示
                    if (TextUtils.isEmpty(str)) {
                        helper.setViewVisibility(R.id.tvIndex, View.GONE);
                    } else {
                        helper.setViewVisibility(R.id.tvIndex, View.VISIBLE);
                        helper.setText(R.id.tvIndex, str);
                    }
                }
            };
            adapter.addHeaderView(getView().getHeaderView());
            adapter.addFooterView(getView().getFooterView());
            mAdapter = adapter.getHeaderAndFooterAdapter();
            getView().getRvContacts().setAdapter(mAdapter);
        }
        ((LQRAdapterForRecyclerView) mAdapter.getInnerAdapter()).setOnItemClickListener((lqrViewHolder, viewGroup, view, i) -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConst.EXTRA_FRIEND_INFO,DBManager.getInstance().getUserInfo(mData.get(i - 1).getUserId()));
            mContext.jumpToActivity(UserInfoBySearchActivity.class,bundle);
        });
    }

}

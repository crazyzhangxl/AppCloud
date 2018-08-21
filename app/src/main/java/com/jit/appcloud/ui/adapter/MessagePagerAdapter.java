package com.jit.appcloud.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.model.bean.MsgTabBean;
import com.jit.appcloud.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张先磊 on 2018/4/27.
 */

public class MessagePagerAdapter extends FragmentPagerAdapter {
    private final List<Pair<BaseFragment,MsgTabBean>> mFragmentList = new ArrayList<>();

    private Context mContext;
    public MessagePagerAdapter(Context mContext,FragmentManager fm) {
        super(fm);
        this.mContext = mContext;
    }

    public void addFlag(Pair<BaseFragment,MsgTabBean> msgTabBeanPair){
        mFragmentList.add(msgTabBeanPair);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position).first;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public View getTabView(int position, ViewGroup mGroup){
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.view_table_msg,mGroup,false);
        ImageView img =  view.findViewById(R.id.iv_msg_tab);
        TextView tv = view.findViewById(R.id.tv_msg_tab);
        img.setImageResource(mFragmentList.get(position).second.getResId());
        tv.setText(mFragmentList.get(position).second.getTitle());
        return view;
    }

}

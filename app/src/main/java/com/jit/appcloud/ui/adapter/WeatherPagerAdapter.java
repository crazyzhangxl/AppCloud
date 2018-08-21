package com.jit.appcloud.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jit.appcloud.R;
import com.jit.appcloud.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张先磊 on 2018/4/23.
 */

public class WeatherPagerAdapter extends FragmentPagerAdapter {
    private final List<Pair<BaseFragment,Integer>> mFragmentList = new ArrayList<>();
    private Context mContext;
    public WeatherPagerAdapter(Context mContext, FragmentManager fm) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position).first;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Pair<BaseFragment,Integer> fragmentPair) {
        mFragmentList.add(fragmentPair);
    }

    public View getTabView(int position, ViewGroup parent){
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.view_table_weather, parent, false);
        ImageView img =  view.findViewById(R.id.tab_icon);
        img.setImageResource(mFragmentList.get(position).second);
        return view;
    }
}

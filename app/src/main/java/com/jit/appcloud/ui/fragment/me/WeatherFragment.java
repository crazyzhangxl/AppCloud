package com.jit.appcloud.ui.fragment.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jit.appcloud.R;
import com.jit.appcloud.model.response.HeWeatherResponse;
import com.jit.appcloud.ui.adapter.weather_info_more.DailyWeatherData;
import com.jit.appcloud.ui.adapter.weather_info_more.DailyWeatherHolder;
import com.jit.appcloud.ui.adapter.weather_info_more.GuideData;
import com.jit.appcloud.ui.adapter.weather_info_more.GuideHolder;
import com.jit.appcloud.ui.adapter.weather_info_more.LifeGuideHolder;
import com.jit.appcloud.ui.adapter.weather_info_more.LifeIndexData;
import com.jit.appcloud.ui.adapter.weather_info_more.LifeIndexGuideData;
import com.jit.appcloud.ui.adapter.weather_info_more.LifeIndexesHolder;
import com.jit.appcloud.ui.base.BaseFragment;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.util.adapterhelper.BaseRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by 张先磊 on 2018/4/23.
 */

public class WeatherFragment extends BaseFragment {
    @BindView(R.id.weather_info_recyclerView)
    RecyclerView mWeatherInfoRecyclerView;
    private BaseRecyclerAdapter mMoreInfoAdapter;

    public static WeatherFragment getInstance() {
        WeatherFragment weatherFragment;
        weatherFragment = new WeatherFragment();
        return weatherFragment;
    }


    @Override
    public void init() {

    }

    @Override
    public void initView(View rootView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        mWeatherInfoRecyclerView.setLayoutManager(linearLayoutManager);
        mWeatherInfoRecyclerView.setBackgroundResource(R.color.gray4);
        mMoreInfoAdapter = new BaseRecyclerAdapter(_mActivity);
        mWeatherInfoRecyclerView.setAdapter(mMoreInfoAdapter);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateViews(HeWeatherResponse.HeWeather6Bean heWeather6Bean){
        if (!isVisible() || !isAdded()){
            return;
        }
        if (heWeather6Bean != null) {
            List<HeWeatherResponse.HeWeather6Bean.DailyForecastBean> dailyForecastBeans = heWeather6Bean.getDaily_forecast();
            List<HeWeatherResponse.HeWeather6Bean.LifestyleBean> lifestyle = heWeather6Bean.getLifestyle();
            mMoreInfoAdapter.clear();
            if (dailyForecastBeans != null) {
                GuideData guideData1 = new GuideData(getString(R.string.weather_future_weather));
                mMoreInfoAdapter.registerHolder(GuideHolder.class, guideData1);

                List<DailyWeatherData> dailyWeatherData = new ArrayList<>();
                for (int count = 0; count < dailyForecastBeans.size(); count++) {
                    dailyWeatherData.add(new DailyWeatherData(dailyForecastBeans.get(count)));
                }
                mMoreInfoAdapter.registerHolder(DailyWeatherHolder.class, dailyWeatherData);
            }

            if (lifestyle != null){
                LifeIndexGuideData lifeIndexGuideData = new LifeIndexGuideData(UIUtils.getString(R.string.weather_lifeIndexes));
                mMoreInfoAdapter.registerHolder(LifeGuideHolder.class, lifeIndexGuideData);
                mMoreInfoAdapter.registerHolder(LifeIndexesHolder.class,new LifeIndexData(lifestyle));

            }
        }
        mMoreInfoAdapter.notifyDataSetChanged();

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_wh_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

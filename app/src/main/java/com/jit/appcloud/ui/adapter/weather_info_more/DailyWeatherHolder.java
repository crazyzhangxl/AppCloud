package com.jit.appcloud.ui.adapter.weather_info_more;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.jit.appcloud.R;
import com.jit.appcloud.commom.ResourceProvider;
import com.jit.appcloud.model.response.HeWeatherResponse;
import com.jit.appcloud.util.Check;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.adapterhelper.BaseRecyclerAdapter;
import com.jit.appcloud.util.adapterhelper.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by SilenceDut on 16/10/20.
 */

public class DailyWeatherHolder extends BaseViewHolder<DailyWeatherData> {

    @BindView(R.id.date_week)
    TextView dateWeek;
    @BindView(R.id.weather_status_daily)
    TextView weatherStatusDaily;
    @BindView(R.id.weather_icon_daily)
    ImageView weatherIconDaily;
    @BindView(R.id.temp_daily)
    TextView tempDaily;
    @BindView(R.id.tv_wind)
    TextView windDaily;



    public DailyWeatherHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void updateItem(DailyWeatherData data, int position) {

        HeWeatherResponse.HeWeather6Bean.DailyForecastBean dailyForecastData = data.dailyForecastData;
        if (Check.isNull(dailyForecastData)){
            return;
        }

        if(TimeUtil.getWeek(dailyForecastData.getDate()).equals(TimeUtil.getWeek(TimeUtil.getMopnthDay()))) {
            dateWeek.setText("今天"+"\n"+TimeUtil.getSimpleTime(dailyForecastData.getDate()));
        }else {
            dateWeek.setText(TimeUtil.getWeek(dailyForecastData.getDate())+"\n"+TimeUtil.getSimpleTime(dailyForecastData.getDate()));
        }
        //dateWeek.setText(dailyForecastData.getDate());
        weatherStatusDaily.setText(dailyForecastData.getCond_txt_d());
        tempDaily.setText(dailyForecastData.getTmp_min()+"~"+dailyForecastData.getTmp_max()+"°");
        weatherIconDaily.setImageResource(ResourceProvider.getIconId(dailyForecastData.getCond_txt_d()));
        windDaily.setText(dailyForecastData.getWind_dir()+"\n"+dailyForecastData.getWind_sc()+"级");

    }

    @Override
    public int getContentViewId() {
        return R.layout.item_weather_daily_forecast;
    }

}

package com.jit.appcloud.ui.adapter.weather_info_more;


import com.jit.appcloud.R;
import com.jit.appcloud.model.response.HeWeatherResponse;
import com.jit.appcloud.util.adapterhelper.BaseAdapterData;

/**
 * Created by SilenceDut on 16/10/20.
 */

public class DailyWeatherData implements BaseAdapterData {

    public HeWeatherResponse.HeWeather6Bean.DailyForecastBean dailyForecastData;

    public DailyWeatherData(HeWeatherResponse.HeWeather6Bean.DailyForecastBean dailyForecastData) {
        this.dailyForecastData = dailyForecastData;
    }


    @Override
    public int getContentViewId() {
        return R.layout.item_weather_daily_forecast;
    }
}

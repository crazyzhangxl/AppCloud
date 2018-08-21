package com.jit.appcloud.commom;

import com.jit.appcloud.R;
import com.jit.appcloud.util.Check;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 张先磊 on 2018/4/23.
 */

public class ResourceProvider {
    private ResourceProvider() {
    }
    //SharedPreferences KEY
    private static Map<String, Integer> sWeatherIcons = new HashMap<>();
    private static final long[] SCHEDULES = {30 * 60, 60 * 60, 3 * 60 * 60, 0};
    private static final String[] SUNNY = {"晴", "多云"};
    private static final String[] WEATHERS = {"阴", "晴", "多云", "大雨", "雨", "雪", "风", "雾霾", "雨夹雪"};
    private static final int[] ICONS_ID = {R.mipmap.ic_weather_clouds, R.mipmap.ic_weather_sunny, R.mipmap.ic_weather_few_clouds, R.mipmap.ic_weather_big_rain, R.mipmap.ic_weather_rain, R.mipmap.ic_weather_snow, R.mipmap.ic_weather_wind, R.mipmap.ic_weather_haze, R.mipmap.ic_weather_rain_snow};
    // 静态域 直接初始化,相绑定
    static {
        for (int index = 0; index < WEATHERS.length; index++) {
            sWeatherIcons.put(WEATHERS[index], ICONS_ID[index]);
        }
    }

    public static long getSchedule(int which) {
        return SCHEDULES[which];
    }

    public static boolean sunny(String weather) {
        for (String weatherKey : SUNNY) {
            if (weatherKey.contains(weather) || weather.contains(weatherKey)) {
                return true;
            }
        }
        return false;
    }

    public static int getIconId(String weather) {

        if (Check.isEmpty(weather)) {
            return R.mipmap.ic_weather_none_available;
        }

        // 如果找到了 说明匹配 那么就返回
        if (sWeatherIcons.get(weather) != null) {
            return sWeatherIcons.get(weather);
        }

        for (String weatherKey : sWeatherIcons.keySet()) {
            if (weatherKey.contains(weather) || weather.contains(weatherKey)) {
                return sWeatherIcons.get(weatherKey);
            }
        }

        return R.mipmap.ic_weather_none_available;
    }
}

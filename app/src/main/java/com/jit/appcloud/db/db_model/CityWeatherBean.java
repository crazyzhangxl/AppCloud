package com.jit.appcloud.db.db_model;

import org.litepal.crud.DataSupport;

/**
 * Created by 张先磊 on 2018/4/24.
 * 用以储存该用户下的 常搜索的城市天气
 */

public class CityWeatherBean  extends DataSupport{
    private String cityId;
    private String cityName;
    private String temp;
    private String weatherStatus;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(String weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    public CityWeatherBean(String cityId, String cityName, String temp, String weatherStatus) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.temp = temp;
        this.weatherStatus = weatherStatus;
    }
}

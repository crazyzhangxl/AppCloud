package com.jit.appcloud.model.bean;

/**
 * Created by 张先磊 on 2018/4/24.
 */

public class WeatherBasicData {
    private boolean mLocationIsCurrent;
    private String updateTime;
    private String temp;
    private String wind;
    private String wind_sc;

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWind_sc() {
        return wind_sc;
    }

    public void setWind_sc(String wind_sc) {
        this.wind_sc = wind_sc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String weather;
    private String location;

    public boolean isLocationIsCurrent() {
        return mLocationIsCurrent;
    }

    public void setLocationIsCurrent(boolean locationIsCurrent) {
        mLocationIsCurrent = locationIsCurrent;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}

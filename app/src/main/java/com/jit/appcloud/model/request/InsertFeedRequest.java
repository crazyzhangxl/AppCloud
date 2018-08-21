package com.jit.appcloud.model.request;

/**
 * @author zxl on 2018/5/30.
 *         discription:
 */

public class InsertFeedRequest {
    private String name;
    private int pound_id;
    private double count;
    private String unit;
    private String time;
    private String feed_brand;
    private String feed_type;
    private String weather;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPound_id() {
        return pound_id;
    }

    public void setPound_id(int pound_id) {
        this.pound_id = pound_id;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFeed_brand() {
        return feed_brand;
    }

    public void setFeed_brand(String feed_brand) {
        this.feed_brand = feed_brand;
    }

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
    }
}

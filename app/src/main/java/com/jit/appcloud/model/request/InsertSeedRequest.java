package com.jit.appcloud.model.request;

/**
 * @author zxl on 2018/5/30.
 *         discription: 种苗投放的对象
 */

public class InsertSeedRequest {
    private String seed_brand;
    private String name;
    private String type;
    private double amount;
    private String unit;
    private int pound_id;
    private String time;
    private String weather;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getSeed_brand() {
        return seed_brand;
    }

    public void setSeed_brand(String seed_brand) {
        this.seed_brand = seed_brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getPound_id() {
        return pound_id;
    }

    public void setPound_id(int pound_id) {
        this.pound_id = pound_id;
    }
}

package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/8/9.
 *         discription:
 */

public class SensorDtBean {
    private float temperature;
    private float oxygen;
    private float ph;
    private String time;

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getOxygen() {
        return oxygen;
    }

    public void setOxygen(float oxygen) {
        this.oxygen = oxygen;
    }

    public float getPh() {
        return ph;
    }

    public void setPh(float ph) {
        this.ph = ph;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

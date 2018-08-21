package com.jit.appcloud.model.request;

/**
 * @author zxl on 2018/6/5.
 *         discription: 插入日常设备日志的请求
 */

public class InsertDeviceLogRequest {
      private  String fun_name;
      private double low_out;
      private double low_in;
      private double high_in;
      private double high_out;
      private String time;
      private double device_id;
      private double pound_id;

    public String getFun_name() {
        return fun_name;
    }

    public void setFun_name(String fun_name) {
        this.fun_name = fun_name;
    }

    public double getLow_out() {
        return low_out;
    }

    public void setLow_out(double low_out) {
        this.low_out = low_out;
    }

    public double getLow_in() {
        return low_in;
    }

    public void setLow_in(double low_in) {
        this.low_in = low_in;
    }

    public double getHigh_in() {
        return high_in;
    }

    public void setHigh_in(double high_in) {
        this.high_in = high_in;
    }

    public double getHigh_out() {
        return high_out;
    }

    public void setHigh_out(double high_out) {
        this.high_out = high_out;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getDevice_id() {
        return device_id;
    }

    public void setDevice_id(double device_id) {
        this.device_id = device_id;
    }

    public double getPound_id() {
        return pound_id;
    }

    public void setPound_id(double pound_id) {
        this.pound_id = pound_id;
    }
}

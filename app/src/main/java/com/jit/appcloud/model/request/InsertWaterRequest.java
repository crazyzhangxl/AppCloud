package com.jit.appcloud.model.request;

/**
 * @author zxl on 2018/5/30.
 *         discription: 插入水质管理
 */

public class InsertWaterRequest {
    private int pound_id;
    private String nh;
    private String nano2;
    private String alkali;
    private String o2;
    private String temperature;
    private String remark;


    public int getPound_id() {
        return pound_id;
    }

    public void setPound_id(int pound_id) {
        this.pound_id = pound_id;
    }

    public String getNh() {
        return nh;
    }

    public void setNh(String nh) {
        this.nh = nh;
    }

    public String getNano2() {
        return nano2;
    }

    public void setNano2(String nano2) {
        this.nano2 = nano2;
    }

    public String getAlkali() {
        return alkali;
    }

    public void setAlkali(String alkali) {
        this.alkali = alkali;
    }

    public String getO2() {
        return o2;
    }

    public void setO2(String o2) {
        this.o2 = o2;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

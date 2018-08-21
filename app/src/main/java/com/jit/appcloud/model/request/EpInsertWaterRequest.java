package com.jit.appcloud.model.request;

import java.io.File;
import java.util.List;

/**
 * @author zxl on 2018/7/10.
 *         discription: 养殖户增加水质量信息
 */

public class EpInsertWaterRequest {
    private String nh;
    private String nano2;
    private String Alkali;
    private String O2;
    private String remark;
    private String medicine;
    private String data;
    private String weather;
    private int pound_id;
    private List<File> content;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
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
        return Alkali;
    }

    public void setAlkali(String alkali) {
        Alkali = alkali;
    }

    public String getO2() {
        return O2;
    }

    public void setO2(String o2) {
        O2 = o2;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPound_id() {
        return pound_id;
    }

    public void setPound_id(int pound_id) {
        this.pound_id = pound_id;
    }

    public List<File> getContent() {
        return content;
    }

    public void setContent(List<File> content) {
        this.content = content;
    }
}

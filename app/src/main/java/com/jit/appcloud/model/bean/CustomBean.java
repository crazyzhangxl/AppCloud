package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/6/23.
 *         discription: 模拟下用户列表的数据
 */

public class CustomBean {
    private int imageRes;
    private String customName;
    private String deviceNum;

    public CustomBean(int imageRes, String customName, String deviceNum) {
        this.imageRes = imageRes;
        this.customName = customName;
        this.deviceNum = deviceNum;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }
}

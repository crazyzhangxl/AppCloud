package com.jit.appcloud.db.db_model;

import org.litepal.crud.DataSupport;

/**
 * @author zxl on 2018/6/25.
 *         discription: 详细的设备信息
 *          功能  - / -
 */

public class AgDeviceDetailBean extends DataSupport {
    private int id;
    private String deviceId;
    private String macAddress;
    private String pondName;
    private String workAddress;
    private String functionName;
    private float yellow1;
    private float yellow2;
    private float yellow3;
    private float yellow4;
    private float green1;
    private float green2;

    public AgDeviceDetailBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getPondName() {
        return pondName;
    }

    public void setPondName(String pondName) {
        this.pondName = pondName;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public float getYellow1() {
        return yellow1;
    }

    public void setYellow1(float yellow1) {
        this.yellow1 = yellow1;
    }

    public float getYellow2() {
        return yellow2;
    }

    public void setYellow2(float yellow2) {
        this.yellow2 = yellow2;
    }

    public float getYellow3() {
        return yellow3;
    }

    public void setYellow3(float yellow3) {
        this.yellow3 = yellow3;
    }

    public float getYellow4() {
        return yellow4;
    }

    public void setYellow4(float yellow4) {
        this.yellow4 = yellow4;
    }

    public float getGreen1() {
        return green1;
    }

    public void setGreen1(float green1) {
        this.green1 = green1;
    }

    public float getGreen2() {
        return green2;
    }

    public void setGreen2(float green2) {
        this.green2 = green2;
    }

}

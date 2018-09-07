package com.jit.appcloud.db.db_model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author zxl on 2018/6/25.
 *         discription:
 */

public class AgDeviceBean extends DataSupport implements Serializable{
    private int id;
    private String deviceID;
    private String macAddress;
    private String pondName;
    private String workAddress;
    private String functionName;
    private int pondId;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPondId() {
        return pondId;
    }

    public void setPondId(int pondId) {
        this.pondId = pondId;
    }

    public AgDeviceBean() {
    }

    public AgDeviceBean(String deviceID,
                        String macAddress,
                        String pondName,
                        String workAddress,
                        String functionName) {
        this.deviceID = deviceID;
        this.macAddress = macAddress;
        this.pondName = pondName;
        this.workAddress = workAddress;
        this.functionName = functionName;
    }

    public AgDeviceBean(String deviceID,
                        String macAddress,
                        String pondName,
                        String workAddress,
                        String functionName,
                        int type){
        this(deviceID,macAddress,pondName,workAddress,functionName);
        this.type = type;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
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


}

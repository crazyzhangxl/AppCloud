package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/7/9.
 *         discription:
 */

public class DeviceBatchInsert {
    private String device_no;
    private String mac_ip;
    private int pound_id;
    private int status;
    private String address;
    private String time;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DeviceBatchInsert() {
    }

    public DeviceBatchInsert(String device_no, String mac_ip, int pound_id, int status, String address, String time) {
        this.device_no = device_no;
        this.mac_ip = mac_ip;
        this.pound_id = pound_id;
        this.status = status;
        this.address = address;
        this.time = time;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public String getMac_ip() {
        return mac_ip;
    }

    public void setMac_ip(String mac_ip) {
        this.mac_ip = mac_ip;
    }

    public int getPound_id() {
        return pound_id;
    }

    public void setPound_id(int pound_id) {
        this.pound_id = pound_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

package com.jit.appcloud.model.request;

/**
 * @author zxl on 2018/6/5.
 *         discription:
 */

public class DeviceInsertRequest {
    private int device_no;
    private String mac_ip;
    private int pound_id;
    private int status;
    private String username;

    public int getDevice_no() {
        return device_no;
    }

    public void setDevice_no(int device_no) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/8/9.
 *         discription:
 */

public class SensorNormalBean {
    /**
     * id : 8
     * device_id : 27
     * pound_id : 2
     * type : C01
     * oxygen : 18
     * temperature : 23.5
     * ph : 12
     * orp : 0
     * time : 2018-08-02 16:32:46
     * username : 养殖户1
     * pound_name : 666
     * device_no : A000000000000027
     * mac_ip : null
     * device_status : 0
     * fromUser : null
     */

    private int id;
    private int device_id;
    private int pound_id;
    private String type;
    private double oxygen;
    private double temperature;
    private double ph;
    private double orp;
    private String time;
    private String username;
    private String pound_name;
    private String device_no;
    private String device_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getPound_id() {
        return pound_id;
    }

    public void setPound_id(int pound_id) {
        this.pound_id = pound_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getOxygen() {
        return oxygen;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = oxygen;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public double getOrp() {
        return orp;
    }

    public void setOrp(double orp) {
        this.orp = orp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPound_name() {
        return pound_name;
    }

    public void setPound_name(String pound_name) {
        this.pound_name = pound_name;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }



    public String getDevice_status() {
        return device_status;
    }

    public void setDevice_status(String device_status) {
        this.device_status = device_status;
    }

}

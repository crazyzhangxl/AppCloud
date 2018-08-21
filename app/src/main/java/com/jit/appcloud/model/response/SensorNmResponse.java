package com.jit.appcloud.model.response;

import com.jit.appcloud.model.bean.SensorNormalBean;

import java.util.List;

/**
 * @author zxl on 2018/8/9.
 *         discription: 普通设备的response
 */

public class SensorNmResponse {

    /**
     * code : 1
     * msg : 成功
     * data : [{"id":8,"device_id":27,"pound_id":2,"type":"C01","oxygen":18,"temperature":23.5,"ph":12,"orp":0,"time":"2018-08-02 16:32:46","username":"养殖户1","pound_name":"666","device_no":"A000000000000027","mac_ip":null,"device_status":"0","fromUser":null},{"id":82,"device_id":28,"pound_id":142,"type":"C01","oxygen":4.5,"temperature":34.8,"ph":0,"orp":0,"time":"2018-08-09 10:58:03","username":"养殖户1","pound_name":"666","device_no":"A00000000000004D","mac_ip":null,"device_status":"0","fromUser":null}]
     */

    private int code;
    private String msg;
    private List<SensorNormalBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SensorNormalBean> getData() {
        return data;
    }

    public void setData(List<SensorNormalBean> data) {
        this.data = data;
    }

}

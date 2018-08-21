package com.jit.appcloud.model.response;

import com.jit.appcloud.model.bean.SensorDtBean;
import com.jit.appcloud.model.bean.SensorNormalBean;

import java.util.List;

/**
 * @author zxl on 2018/8/9.
 *         discription: 普通设备的详细信息
 */

public class SensorDfInfoResponse {
    private int code;
    private String msg;
    private List<SensorDtBean> data;

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

    public List<SensorDtBean> getData() {
        return data;
    }

    public void setData(List<SensorDtBean> data) {
        this.data = data;
    }
}

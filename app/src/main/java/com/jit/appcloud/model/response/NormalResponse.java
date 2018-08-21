package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/5/25.
 *         discription:普通的返回结果
 *         包含code和msg
 *         可复用
 */

public class NormalResponse {
    private int code;
    private String msg;

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
}

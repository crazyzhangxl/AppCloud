package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/5/19.
 *         discription: 修改密码返回
 */

public class ChangePwdResponse {

    /**
     * code : 1
     * msg : 成功
     * data : true
     */

    private int code;
    private String msg;
    private boolean data;

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

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}

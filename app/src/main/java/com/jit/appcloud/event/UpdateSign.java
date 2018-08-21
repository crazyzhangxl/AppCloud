package com.jit.appcloud.event;

/**
 * Created by 张先磊 on 2018/4/24.
 */

public class UpdateSign {
    private String str;
    private int code;

    public UpdateSign(String str, int code) {
        this.str = str;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}

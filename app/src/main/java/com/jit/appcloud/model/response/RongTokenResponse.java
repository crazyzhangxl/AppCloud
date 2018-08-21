package com.jit.appcloud.model.response;

/**
 * Created by 张先磊 on 2018/4/26.
 */

public class RongTokenResponse {

    /**
     * code : 200
     * userId : 11111
     * token : jHQSuqYX61NCmquJScQobnl0B8fADuy8ivc82nBp2Z4fF5mqV6Cc0U/MYQ97PgezKLXewy3GGIXQO/FKn1JTFQ==
     */

    private int code;
    private String userId;
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

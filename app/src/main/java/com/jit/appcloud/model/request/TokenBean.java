package com.jit.appcloud.model.request;

/**
 * Created by 张先磊 on 2018/4/26.
 */

public class TokenBean {
    public String userId;
    public String name;
    public String portraitUri;

    public TokenBean(String userId, String name, String portraitUri) {
        this.userId = userId;
        this.name = name;
        this.portraitUri = portraitUri;
    }
}

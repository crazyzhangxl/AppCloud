package com.jit.appcloud.model.request;

/**
 * @author zxl on 2018/5/19.
 *         discription: 分配的平行用户
 */

public class AllocateLookUserRequest {
    private String username ;
    private String realname;
    private String password;
    public AllocateLookUserRequest(String username, String realname,String password) {
        this.username = username;
        this.realname = realname;
        this.password = password;
    }

    public AllocateLookUserRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}

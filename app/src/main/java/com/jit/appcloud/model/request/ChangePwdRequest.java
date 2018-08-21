package com.jit.appcloud.model.request;

/**
 * @author zxl on 2018/5/19.
 *         discription: 修改密码请求
 */

public class ChangePwdRequest {
    private String oldPassword;
    private String newPassword;

    public ChangePwdRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

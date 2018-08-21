package com.jit.appcloud.model.response;

import com.jit.appcloud.model.bean.PersonalBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zxl on 2018/6/1.
 *         discription:
 *         注册列表的用户信息=====================
 */

public class UserBBAMResponse {
    private int code;
    private String msg;
    private List<PersonalBean> data;

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

    public List<PersonalBean> getData() {
        return data;
    }

    public void setData(List<PersonalBean> data) {
        this.data = data;
    }

    /*public static class DataBean implements Serializable {
        private int id;
        private String username;
        private String realname;
        private String image;
        private String sign;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }*/
}



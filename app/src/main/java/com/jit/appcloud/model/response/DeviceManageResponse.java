package com.jit.appcloud.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author zxl on 2018/6/5.
 *         discription: 设备管理
 */

public class DeviceManageResponse {

    /**
     * code : 1
     * msg : 成功
     * data : [{"id":26,"device_no":"22","mac_ip":"22222","pound_id":123,"status":0,"time":"2018-06-05 15:25:01","username":"abcd","fromUser":"abc"},{"id":27,"device_no":"22","mac_ip":"2222","pound_id":123,"status":1,"time":"2018-06-05 17:25:52","username":"abcd","fromUser":"abc"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 26
         * device_no : 22
         * mac_ip : 22222
         * pound_id : 123
         * status : 0
         * time : 2018-06-05 15:25:01
         * username : abcd
         * fromUser : abc
         */

        private int id;
        private String device_no;
        private String mac_ip;
        private int pound_id;
        private int status;
        private String time;
        private String username;
        private String fromUser;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDevice_no() {
            return device_no;
        }

        public void setDevice_no(String device_no) {
            this.device_no = device_no;
        }

        public String getMac_ip() {
            return mac_ip;
        }

        public void setMac_ip(String mac_ip) {
            this.mac_ip = mac_ip;
        }

        public int getPound_id() {
            return pound_id;
        }

        public void setPound_id(int pound_id) {
            this.pound_id = pound_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFromUser() {
            return fromUser;
        }

        public void setFromUser(String fromUser) {
            this.fromUser = fromUser;
        }
    }
}



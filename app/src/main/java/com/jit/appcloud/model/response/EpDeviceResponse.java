package com.jit.appcloud.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author zxl on 2018/7/13.
 *         discription: 经销商设备的response
 */

public class EpDeviceResponse {
    /**
     * code : 1
     * msg : 成功
     * data : [{"id":6,"device_no":"5","mac_ip":"121212","pound_id":23,"address":"设备地址1","status":1,"time":"2018-06-26 10:59:52","username":"养殖户1","fromUser":"经销商1"},{"id":7,"device_no":"2","mac_ip":"121212","pound_id":23,"address":"设备地址1","status":1,"time":null,"username":null,"fromUser":"经销商1"}]
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

    public static class DataBean implements Serializable{
        /**
         * id : 6
         * device_no : 5
         * mac_ip : 121212
         * pound_id : 23
         * address : 设备地址1
         * status : 1
         * time : 2018-06-26 10:59:52
         * username : 养殖户1
         * fromUser : 经销商1
         */

        private int id;
        private String device_no;
        private String mac_ip;
        private int pound_id;
        private String address;
        private int status;
        private String time;
        private String username;
        private String fromUser;
        private String number;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

package com.jit.appcloud.model.response;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author zxl on 2018/6/12.
 *         discription: 获得所有活动的返回
 */

public class FriendAddGetResponse {


    /**
     * code : 1
     * msg : 成功
     * data : [
     * {"id":2,"username":"ak","friendname":"gk",
     * "message":"hello","nickname":"ffe","status":1,"image":null},
     * {"id":8,"username":"ak","friendname":"pp","message":"fff"
     * ,"nickname":"pp","status":2,"image":"http://210.28.188.99/yunlian/image/8ac74ad2-08f2-417c-8518-b7cf1d27655b.jpg"}]
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

    public static class DataBean implements Comparable{
        /**
         * id : 2
         * username : ak
         * friendname : gk
         * message : hello
         * nickname : ffe
         * status : 1
         * image : null
         */

        private int id;
        private String username;
        private String friendname;
        private String message;
        private String nickname;
        private int status;
        private String image;

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

        public String getFriendname() {
            return friendname;
        }

        public void setFriendname(String friendname) {
            this.friendname = friendname;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public int compareTo(@NonNull Object o) {
            return 0;
        }
    }
}

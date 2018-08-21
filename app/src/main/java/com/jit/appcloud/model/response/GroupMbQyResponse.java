package com.jit.appcloud.model.response;

import java.util.List;

/**
 * @author zxl on 2018/7/25.
 *         discription: 根据群id查询群成员信息
 */

public class GroupMbQyResponse {

    /**
     * code : 1
     * msg : 成功
     * data : [{"id":33,"user_id":77,"group_id":19,"admin":0,"username":"经销商1","image":"http://223.2.197.240:8888/yunlian/image/688a888b-6bcd-4c82-9e1a-2e936982d0e0.jpg"},{"id":34,"user_id":64,"group_id":19,"admin":0,"username":"总部3","image":null},{"id":35,"user_id":65,"group_id":19,"admin":0,"username":"总代","image":"http://223.2.197.240:8888/yunlian/image/1d270cf0-e72c-4988-8eab-28faa26ad57a.png"},{"id":36,"user_id":103,"group_id":19,"admin":1,"username":"我是谁","image":null}]
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

    public static class DataBean {
        /**
         * id : 33
         * user_id : 77
         * group_id : 19
         * admin : 0
         * username : 经销商1
         * image : http://223.2.197.240:8888/yunlian/image/688a888b-6bcd-4c82-9e1a-2e936982d0e0.jpg
         */

        private int id;
        private int user_id;
        private int group_id;
        private int admin;
        private String username;
        private String image;
        private String nickname;
        private int is_show;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public int getAdmin() {
            return admin;
        }

        public void setAdmin(int admin) {
            this.admin = admin;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}

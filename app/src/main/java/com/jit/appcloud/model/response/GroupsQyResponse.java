package com.jit.appcloud.model.response;

import java.util.List;

/**
 * @author zxl on 2018/7/25.
 *         discription: 用于用户登录过程中 获取所在群的信息，之后是进行同步操作
 *
 */

public class GroupsQyResponse {

    /**
     * code : 1
     * msg : 成功
     * data : [{"id":19,"groupname":"新群组1","image":null,"admin":1}]
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
         * id : 19
         * groupname : 新群组1
         * image : null
         * admin : 1
         */

        private int id;
        private String groupname;
        private String image;
        private int admin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getAdmin() {
            return admin;
        }

        public void setAdmin(int admin) {
            this.admin = admin;
        }
    }
}

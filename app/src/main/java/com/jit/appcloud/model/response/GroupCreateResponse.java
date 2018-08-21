package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/7/25.
 *         discription:
 */

public class GroupCreateResponse {


    /**
     * code : 1
     * msg : 成功
     * data : {"id":19,"groupname":"新群组1","image":null,"admin":null}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 19
         * groupname : 新群组1
         * image : null
         * admin : null
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

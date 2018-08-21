package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/7/30.
 *         discription:
 */

public class GroupInfoByIdResponse {


    /**
     * code : 1
     * msg : 成功
     * data : {"id":29,"groupname":"新群组修改","image":null,"admin":null,"createId":77}
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
         * id : 29
         * groupname : 新群组修改
         * image : null
         * admin : null
         * createId : 77
         */

        private int id;
        private String groupname;
        private String image;
        private int createId;

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

        public int getCreateId() {
            return createId;
        }

        public void setCreateId(int createId) {
            this.createId = createId;
        }
    }
}

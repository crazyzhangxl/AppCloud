package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/5/31.
 *         discription:
 */

public class UserHeadPostResponse {

    /**
     * code : 1
     * msg : 成功
     * data : {"image":"f41e9543-e723-4d7e-94a4-4759040aefc8.jpg"}
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
         * image : f41e9543-e723-4d7e-94a4-4759040aefc8.jpg
         */

        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}

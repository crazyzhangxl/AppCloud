package com.jit.appcloud.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author zxl on 2018/7/11.
 *         discription: 照片信息
 */

public class PhotoResponse {
    /**
     * code : 1
     * msg : 成功
     * data : [{"id":null,"username":"经销商1","image":"http://223.2.197.240:8888/yunlian/image/ca30c1d2-08d8-4909-8c99-c5cd9ab47eb4.png"},{"id":null,"username":"经销商1","image":"http://223.2.197.240:8888/yunlian/image/ca30c1d2-08d8-4909-8c99-c5cd9ab47eb4.png"}]
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

    public static class DataBean  implements Serializable{
        public DataBean(String image) {
            this.image = image;
        }

        /**
         * id : null
         * username : 经销商1
         * image : http://223.2.197.240:8888/yunlian/image/ca30c1d2-08d8-4909-8c99-c5cd9ab47eb4.png
         */


        private int id;
        private String username;
        private String image;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}

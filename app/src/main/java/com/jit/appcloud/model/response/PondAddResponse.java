package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/6/5.
 *         discription:
 */

public class PondAddResponse {

    /**
     * code : 1
     * msg : 成功
     * data : {"id":14,"number":"2","type":"蟹塘","square":205,"time":"2018-05-28 14:30:01","username":"p1","address":"xxx路口","unit":"平方米","province":"江苏省","city":"南京市","country":"高淳县","length":10,"width":20.5,"depth":3.2,"fromUser":"gk"}
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
         * id : 14
         * number : 2
         * type : 蟹塘
         * square : 205
         * time : 2018-05-28 14:30:01
         * username : p1
         * address : xxx路口
         * unit : 平方米
         * province : 江苏省
         * city : 南京市
         * country : 高淳县
         * length : 10
         * width : 20.5
         * depth : 3.2
         * fromUser : gk
         */

        private int id;
        private String number;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}

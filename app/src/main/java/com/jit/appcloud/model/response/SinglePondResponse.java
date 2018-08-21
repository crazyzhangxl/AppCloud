package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/7/10.
 *         discription: 单个塘口的数据
 */

public class SinglePondResponse {

    /**
     * code : 1
     * msg : 成功
     * data : {"id":52,"number":"塘口3号","type":"","square":0,"seed_time":"2018-07-10 10:37:20","username":"养殖3","seed_type":"","seed_brand":"","seed_number":0,"feed_brand":"","feed_type":"","fromUser":"我是谁","realname":null}
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
         * id : 52
         * number : 塘口3号
         * type : 
         * square : 0
         * seed_time : 2018-07-10 10:37:20
         * username : 养殖3
         * seed_type : 
         * seed_brand : 
         * seed_number : 0
         * feed_brand : 
         * feed_type : 
         * fromUser : 我是谁
         * realname : null
         */

        private int id;
        private String number;
        private String type;
        private int square;
        private String seed_time;
        private String username;
        private String seed_type;
        private String seed_brand;
        private int seed_number;
        private String feed_brand;
        private String feed_type;
        private String fromUser;
        private String realname;
        private String tel;
        private String address;

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSquare() {
            return square;
        }

        public void setSquare(int square) {
            this.square = square;
        }

        public String getSeed_time() {
            return seed_time;
        }

        public void setSeed_time(String seed_time) {
            this.seed_time = seed_time;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSeed_type() {
            return seed_type;
        }

        public void setSeed_type(String seed_type) {
            this.seed_type = seed_type;
        }

        public String getSeed_brand() {
            return seed_brand;
        }

        public void setSeed_brand(String seed_brand) {
            this.seed_brand = seed_brand;
        }

        public int getSeed_number() {
            return seed_number;
        }

        public void setSeed_number(int seed_number) {
            this.seed_number = seed_number;
        }

        public String getFeed_brand() {
            return feed_brand;
        }

        public void setFeed_brand(String feed_brand) {
            this.feed_brand = feed_brand;
        }

        public String getFeed_type() {
            return feed_type;
        }

        public void setFeed_type(String feed_type) {
            this.feed_type = feed_type;
        }

        public String getFromUser() {
            return fromUser;
        }

        public void setFromUser(String fromUser) {
            this.fromUser = fromUser;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }
    }
}

package com.jit.appcloud.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author zxl on 2018/5/23.
 *         discription: 塘口管理的bean
 *         至少含有:责任人,电话,邮箱
 *            塘口的信息:塘口号，长度，宽度，深度，面积，
 *                     省，市，区，地址详情
 *
 */

public class PondMangeResponse {

    /**
     * code : 1
     * msg : 成功
     * data : [{"id":35,"number":"再来一个塘","type":"虾塘","square":100,"time":"2018-05-31 10:11:54","username":"abcd","address":"高淳啊！！啊啊啊","unit":"亩","province":"北京市","city":"北京市","country":"石景山区","length":10,"width":10,"depth":3,"fromUser":"abc"},{"id":100,"number":"12","type":"虾塘","square":20,"time":"2018-06-01 12:41:37","username":"abcd","address":"塘口吧。。。","unit":"亩","province":"北京市","city":"北京市","country":"通州区","length":10,"width":20,"depth":22,"fromUser":"abc"},{"id":104,"number":"yy","type":"其他","square":22,"time":"2018-06-01 14:19:29","username":"abcd","address":"hhh","unit":"亩","province":"北京市","city":"北京市","country":"西城区","length":6,"width":55,"depth":5,"fromUser":"abc"},{"id":105,"number":"uy","type":"其他","square":56,"time":"2018-06-01 14:19:48","username":"abcd","address":"bhh","unit":"公顷","province":"北京市","city":"北京市","country":"东城区","length":8,"width":5,"depth":5,"fromUser":"abc"},{"id":106,"number":"y","type":"其他","square":2,"time":"2018-06-01 14:20:08","username":"abcd","address":"uuu","unit":"亩","province":"北京市","city":"北京市","country":"东城区","length":2,"width":5,"depth":2,"fromUser":"abc"},{"id":107,"number":"uuu","type":"其他","square":663,"time":"2018-06-01 14:20:27","username":"abcd","address":"hjj","unit":"亩","province":"北京市","city":"北京市","country":"西城区","length":3,"width":3,"depth":3,"fromUser":"abc"},{"id":108,"number":"hhh","type":"其他","square":223,"time":"2018-06-01 14:20:46","username":"abcd","address":"bjj","unit":"亩","province":"北京市","city":"北京市","country":"西城区","length":6,"width":3,"depth":6,"fromUser":"abc"},{"id":109,"number":"j","type":"其他","square":5,"time":"2018-06-01 14:21:26","username":"abcd","address":"jkkk","unit":"亩","province":"北京市","city":"北京市","country":"西城区","length":5,"width":66,"depth":6,"fromUser":"abc"},{"id":110,"number":"jii","type":"其他","square":233,"time":"2018-06-01 14:21:48","username":"abcd","address":"hhuj","unit":"公顷","province":"北京市","city":"北京市","country":"西城区","length":333,"width":333,"depth":333,"fromUser":"abc"},{"id":111,"number":"jjj","type":"其他","square":32,"time":"2018-06-01 14:22:07","username":"abcd","address":"jjjj","unit":"亩","province":"北京市","city":"北京市","country":"西城区","length":333,"width":666,"depth":666,"fromUser":"abc"},{"id":112,"number":"njj","type":"其他","square":333,"time":"2018-06-01 14:22:31","username":"abcd","address":"jjj","unit":"公顷","province":"北京市","city":"北京市","country":"西城区","length":333,"width":33,"depth":33,"fromUser":"abc"},{"id":113,"number":"12","type":"蟹塘","square":233,"time":"2018-06-01 14:23:33","username":"abcd","address":"kkkk","unit":"公顷","province":"北京市","city":"北京市","country":"西城区","length":52,"width":53,"depth":55,"fromUser":"abc"},{"id":114,"number":"23","type":"其他","square":22,"time":"2018-06-01 14:33:24","username":"abcd","address":"bbj","unit":"亩","province":"北京市","city":"北京市","country":"崇文区","length":3,"width":22,"depth":53,"fromUser":"abc"},{"id":115,"number":"kkkk","type":"其他","square":556,"time":"2018-06-01 14:39:17","username":"abcd","address":"ghbb","unit":"亩","province":"北京市","city":"北京市","country":"崇文区","length":233,"width":232,"depth":233,"fromUser":"abc"},{"id":116,"number":"hhhh","type":"蟹塘","square":222,"time":"2018-06-01 14:59:42","username":"abcd","address":"bhhb","unit":"公顷","province":"北京市","city":"北京市","country":"西城区","length":3,"width":3,"depth":3,"fromUser":"abc"},{"id":117,"number":"uuuu","type":"虾塘","square":233,"time":"2018-06-01 15:17:42","username":"abcd","address":"bhjj","unit":"亩","province":"北京市","city":"北京市","country":"西城区","length":3,"width":3,"depth":3,"fromUser":"abc"},{"id":118,"number":"塘口啊","type":"虾塘","square":222,"time":"2018-06-01 15:25:12","username":"abcd","address":"看看！","unit":"亩","province":"北京市","city":"北京市","country":"崇文区","length":3,"width":3,"depth":3,"fromUser":"abc"}]
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
         * id : 35
         * number : 再来一个塘
         * type : 虾塘
         * square : 100
         * time : 2018-05-31 10:11:54
         * username : abcd
         * address : 高淳啊！！啊啊啊
         * unit : 亩
         * province : 北京市
         * city : 北京市
         * country : 石景山区
         * length : 10
         * width : 10
         * depth : 3
         * fromUser : abc
         */

        private int id;
        private String number;
        private String type;
        private int square;
        private String time;
        private String username;
        private String address;
        private String unit;
        private String province;
        private String city;
        private String country;
        private int length;
        private int width;
        private int depth;
        private String fromUser;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public String getFromUser() {
            return fromUser;
        }

        public void setFromUser(String fromUser) {
            this.fromUser = fromUser;
        }
    }
}

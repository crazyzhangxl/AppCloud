package com.jit.appcloud.model.response;
import com.jit.appcloud.model.bean.PersonalBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zxl on 2018/5/18.
 *         描述:Admin管辖下的经销商信息
 */

public class UserBdAmResponse {
    /**
     * code : 1
     * msg : 成功
     * data : [
     * {"id":37,"username":"abc"
     * ,"image":null
     * ,"realname":null
     * ,"department":null 店面名
     * ,"province":null
     * ,"city":null
     * ,"country":null
     * ,"address":null
     * ,"tel":null
     * ,"area":null
     * ,"category":null    经销的类别
     * ,"sign":null
     * ,"email":null
     * ,"hobby":null
     * ,"userCount":1      该经销商下的养殖户数
     * ,"deviceCount":null
     * ,"poundName":null}
     * ,{"id":61,"username":"aac","image":null,"realname":null,"department":null,"province":null,"city":null,"country":null,"address":null,"tel":null,"area":null,"category":null,"sign":null,"email":null,"hobby":null,"userCount":0,"deviceCount":null,"poundName":null}
     * ,{"id":75,"username":"bbc","image":null,"realname":null,"department":null,"province":null,"city":null,"country":null,"address":null,"tel":null,"area":null,"category":null,"sign":null,"email":null,"hobby":null,"userCount":0,"deviceCount":null,"poundName":null}]
     */

    private int code;
    private String msg;
    private List<PersonalBean> data;

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

    public List<PersonalBean> getData() {
        return data;
    }

    public void setData(List<PersonalBean> data) {
        this.data = data;
    }


/*    public static class DataBean implements Serializable{
        *//**
         * id : 37
         * username : abc
         * image : null
         * realname : null
         * department : null
         * province : null
         * city : null
         * country : null
         * address : null
         * tel : null
         * area : null
         * category : null
         * sign : null
         * email : null
         * hobby : null
         * userCount : 1
         * deviceCount : null
         * poundName : null
         *//*

        private int id;
        private String username;
        private String image;
        private String realname;
        private String department;
        private String province;
        private String city;
        private String country;
        private String address;
        private String tel;
        private String area;
        private String category;
        private String sign;
        private String email;
        private String hobby;
        private int userCount;
        private int  deviceCount;
        private String poundName;

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

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public int getUserCount() {
            return userCount;
        }

        public void setUserCount(int userCount) {
            this.userCount = userCount;
        }

        public int getDeviceCount() {
            return deviceCount;
        }

        public void setDeviceCount(int deviceCount) {
            this.deviceCount = deviceCount;
        }

        public String getPoundName() {
            return poundName;
        }

        public void setPoundName(String poundName) {
            this.poundName = poundName;
        }
    }*/
}

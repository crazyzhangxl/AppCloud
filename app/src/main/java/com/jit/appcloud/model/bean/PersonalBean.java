package com.jit.appcloud.model.bean;

import java.io.Serializable;

/**
 * @author zxl on 2018/8/10.
 *         discription: 个人信息的Bean
 */

public class PersonalBean implements Serializable{
    /**
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
     */
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
}

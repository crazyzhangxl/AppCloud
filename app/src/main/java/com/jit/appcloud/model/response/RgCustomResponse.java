package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/7/8.
 *         discription: 注册客户的返回信息
 */
public class RgCustomResponse {
    /**
     * code : 1
     * msg : 成功
     * data : {"id":73,"username":"总代ww","password":"$2a$10$615Bcxpp5Iu89cVoS1wPae4PYNFBxtDet0i7FOZW/5Pf7q0g5cR6G","image":null,"realname":"总代王w","department":null,"province":null,"city":null,"country":null,"address":null,"tel":"12534562541","income":null,"area":null,"category":null,"sign":null,"super_id":71,"lastPasswordResetDate":"2018-06-22 09:44:18","email":null,"hobby":null,"rong_token":"2hHKbuy4e0Mc/hEvJsMQvmt8cDGonG7SS7i+HH4FEQ7JzUe1rkbn2wqrmshb1YKeY6bBSjGThGCDDTFOJHtdEw==","vice_id":0,"content":null,"role":"ROLE_AGENT"}
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
         * id : 73
         * username : 总代ww
         * password : $2a$10$615Bcxpp5Iu89cVoS1wPae4PYNFBxtDet0i7FOZW/5Pf7q0g5cR6G
         * image : null
         * realname : 总代王w
         * department : null
         * province : null
         * city : null
         * country : null
         * address : null
         * tel : 12534562541
         * income : null
         * area : null
         * category : null
         * sign : null
         * super_id : 71
         * lastPasswordResetDate : 2018-06-22 09:44:18
         * email : null
         * hobby : null
         * rong_token : 2hHKbuy4e0Mc/hEvJsMQvmt8cDGonG7SS7i+HH4FEQ7JzUe1rkbn2wqrmshb1YKeY6bBSjGThGCDDTFOJHtdEw==
         * vice_id : 0
         * content : null
         * role : ROLE_AGENT
         */

        private int id;
        private String username;
        private String password;
        private Object image;
        private String realname;
        private Object department;
        private Object province;
        private Object city;
        private Object country;
        private Object address;
        private String tel;
        private Object income;
        private Object area;
        private Object category;
        private Object sign;
        private int super_id;
        private String lastPasswordResetDate;
        private Object email;
        private Object hobby;
        private String rong_token;
        private int vice_id;
        private Object content;
        private String role;

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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Object getImage() {
            return image;
        }

        public void setImage(Object image) {
            this.image = image;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public Object getDepartment() {
            return department;
        }

        public void setDepartment(Object department) {
            this.department = department;
        }

        public Object getProvince() {
            return province;
        }

        public void setProvince(Object province) {
            this.province = province;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getCountry() {
            return country;
        }

        public void setCountry(Object country) {
            this.country = country;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public Object getIncome() {
            return income;
        }

        public void setIncome(Object income) {
            this.income = income;
        }

        public Object getArea() {
            return area;
        }

        public void setArea(Object area) {
            this.area = area;
        }

        public Object getCategory() {
            return category;
        }

        public void setCategory(Object category) {
            this.category = category;
        }

        public Object getSign() {
            return sign;
        }

        public void setSign(Object sign) {
            this.sign = sign;
        }

        public int getSuper_id() {
            return super_id;
        }

        public void setSuper_id(int super_id) {
            this.super_id = super_id;
        }

        public String getLastPasswordResetDate() {
            return lastPasswordResetDate;
        }

        public void setLastPasswordResetDate(String lastPasswordResetDate) {
            this.lastPasswordResetDate = lastPasswordResetDate;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getHobby() {
            return hobby;
        }

        public void setHobby(Object hobby) {
            this.hobby = hobby;
        }

        public String getRong_token() {
            return rong_token;
        }

        public void setRong_token(String rong_token) {
            this.rong_token = rong_token;
        }

        public int getVice_id() {
            return vice_id;
        }

        public void setVice_id(int vice_id) {
            this.vice_id = vice_id;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}

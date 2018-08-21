package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/5/31.
 *         discription: 用户个人信息返回
 */

public class UserInfoResponse {
    /**
     * code : 1
     * msg : 成功
     * data : {"id":44,"username":"abcd","password":"$2a$10$2C1wyJ6.Qn.wKrIvP4fO2.GsoFE4sJCloW0e3xLh59MW6BNTUEprC","image":null,"realname":"abcd","department":null,"province":null,"city":null,"country":null,"address":null,"tel":null,"income":null,"area":null,"category":null,"qr_code":null,"sign":null,"super_id":37,"lastPasswordResetDate":"2018-05-24 16:03:22","email":null,"hobby":null,"rong_token":null}
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
         * id : 44
         * username : abcd
         * password : $2a$10$2C1wyJ6.Qn.wKrIvP4fO2.GsoFE4sJCloW0e3xLh59MW6BNTUEprC
         * image : null
         * realname : abcd
         * department : null
         * province : null
         * city : null
         * country : null
         * address : null
         * tel : null
         * income : null
         * area : null
         * category : null
         * qr_code : null
         * sign : null
         * super_id : 37
         * lastPasswordResetDate : 2018-05-24 16:03:22
         * email : null
         * hobby : null
         * rong_token : null
         */

        private int id;
        private String username;
        private String password;
        private String image;
        private String realname;
        private String department;
        private String province;
        private String city;
        private String country;
        private String address;
        private String tel;
        private int income;
        private String area; // area
        private String category;
        private String qr_code;
        private String sign;
        private int super_id;
        private String lastPasswordResetDate;
        private String email;
        private String hobby;
        private String rong_token;

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

        public int getIncome() {
            return income;
        }

        public void setIncome(int income) {
            this.income = income;
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

        public String getQr_code() {
            return qr_code;
        }

        public void setQr_code(String qr_code) {
            this.qr_code = qr_code;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
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

        public String getRong_token() {
            return rong_token;
        }

        public void setRong_token(String rong_token) {
            this.rong_token = rong_token;
        }
    }
}

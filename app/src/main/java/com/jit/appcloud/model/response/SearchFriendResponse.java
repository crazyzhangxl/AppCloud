package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/6/12.
 *         discription: 搜素好友返回
 */

public class SearchFriendResponse {

    /**
     * code : 1
     * msg : 成功
     * data : {"id":16,"username":"gk","password":"$2a$10$BzzS3/IQ9pn4PVZhFNN8VupYb04yue0Y9eDiw0YPYyQ5j5gJtMxMG","image":null,"realname":"程林","department":null,"province":null,"city":null,"country":null,"address":null,"tel":null,"income":null,"area":null,"category":null,"qr_code":"55938557.jpg","sign":null,"super_id":14,"lastPasswordResetDate":"2018-05-31 09:42:31","email":null,"hobby":null,"rong_token":null}
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
         * id : 16
         * username : gk
         * password : $2a$10$BzzS3/IQ9pn4PVZhFNN8VupYb04yue0Y9eDiw0YPYyQ5j5gJtMxMG
         * image : null
         * realname : 程林
         * department : null
         * province : null
         * city : null
         * country : null
         * address : null
         * tel : null
         * income : null
         * area : null
         * category : null
         * qr_code : 55938557.jpg
         * sign : null
         * super_id : 14
         * lastPasswordResetDate : 2018-05-31 09:42:31
         * email : null
         * hobby : null
         * rong_token : null
         */

        private int id;
        private String username;
        private String image;
        private String province;
        private String city;
        private String country;
        private String sign;

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

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}

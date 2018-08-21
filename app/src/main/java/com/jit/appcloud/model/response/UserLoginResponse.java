package com.jit.appcloud.model.response;

/**
 * Created by 张先磊 on 2018/5/11.
 */

public class UserLoginResponse<T> {

    /**
     * code : 1
     * msg : 成功
     * data : {"username":"aaa","realname":"aaa","password":"aaaaaa","role":"1","image":null,"register_time":"2018-05-11 15:13:57","token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYWEiLCJjcmVhdGVkIjoxNTI2MDI2Mzg5NzQ0LCJleHAiOjE1MjY2MzExODl9.718PaL2IGhoKkzn4joomRMVBxd3OgS-t9P1FCyl9m4-_puzA04Tlq0akgshtt-jqbqmK5BqO4jQCu4aJed0-oQ"}
     */

    private int code;
    private String msg;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * username : aaa
         * realname : aaa
         * password : aaaaaa
         * role : 1
         * image : null
         * register_time : 2018-05-11 15:13:57
         * token : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYWEiLCJjcmVhdGVkIjoxNTI2MDI2Mzg5NzQ0LCJleHAiOjE1MjY2MzExODl9.718PaL2IGhoKkzn4joomRMVBxd3OgS-t9P1FCyl9m4-_puzA04Tlq0akgshtt-jqbqmK5BqO4jQCu4aJed0-oQ
         */

        private String username;
        private String realname;
        private String password;
        private String role;
        private String register_time;
        private String token;
        private int user_id;
        private String rong_token;
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getRegister_time() {
            return register_time;
        }

        public void setRegister_time(String register_time) {
            this.register_time = register_time;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getRong_token() {
            return rong_token;
        }

        public void setRong_token(String rong_token) {
            this.rong_token = rong_token;
        }
    }
}

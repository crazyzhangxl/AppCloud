package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/5/29.
 *         discription:
 */

public class CheckProductResponse {

    /**
     * code : 1
     * msg : 成功
     * data : {"id":9,"title":"螃蟹","price":163.5,"image":"http://210.28.188.99/yunlian/product/bb26f092-156c-477b-b64c-9b27d8b28854.jpg","time":"2018-05-08 15:25:58","username":"a4","type":"蟹类","content":"[http://210.28.188.99/yunlian/product/bb26f092-156c-477b-b64c-9b27d8b28854.jpg, http://210.28.188.99/yunlian/product/51a00532-1c41-414f-a150-a00898e9bd30.jpg]","description":"螃蟹养殖","sum":6,"realname":"a4","user_image":null}
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
         * id : 9
         * title : 螃蟹
         * price : 163.5
         * image : http://210.28.188.99/yunlian/product/bb26f092-156c-477b-b64c-9b27d8b28854.jpg
         * time : 2018-05-08 15:25:58
         * username : a4
         * type : 蟹类
         * content : [http://210.28.188.99/yunlian/product/bb26f092-156c-477b-b64c-9b27d8b28854.jpg, http://210.28.188.99/yunlian/product/51a00532-1c41-414f-a150-a00898e9bd30.jpg]
         * description : 螃蟹养殖
         * sum : 6
         * realname : a4
         * user_image : null
         */
        private int id;
        private String title;
        private double price;
        private String image;
        private String time;
        private String username;
        private String type;
        private String content;
        private String description;
        private int sum;
        private String realname;
        private String user_image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public Object getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }
    }
}

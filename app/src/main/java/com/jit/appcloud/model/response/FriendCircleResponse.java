package com.jit.appcloud.model.response;

import com.jit.appcloud.model.bean.CommentBean;
import com.jit.appcloud.model.bean.FavortItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxl on 2018/8/2.
 *         discription: 朋友圈的信息获取
 */

public class FriendCircleResponse {
    /**
     * code : 1
     * msg : 成功
     * data : [{"id":11,"messageUsername":"经销商1","messageUserId":77,"content":"你好","picture":"http://172.22.31.58:8888/yunlian/circle/e2162d45-f9d6-4d36-9711-228bf9950689.png, http://172.22.31.58:8888/yunlian/circle/d72cc99d-df9c-4967-8afd-703e455cc962.png","location":"","createTime":"2018-08-03 10:25:33","messageImage":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg","yesUser":[{"userId":77,"username":"经销商1","realName":"张三ww","image":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg"}],"comments":[]},{"id":10,"messageUsername":"经销商1","messageUserId":77,"content":"\"你好\"","picture":"http://172.22.31.58:8888/yunlian/circle/cf6c6cc4-4c85-4574-b6ea-c23d39dc9adc.jpg, http://172.22.31.58:8888/yunlian/circle/37b4c784-6632-4b62-b666-ee8a83ef1b18.png","location":"","createTime":"2018-08-03 10:00:26","messageImage":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg","yesUser":[{"userId":77,"username":"经销商1","realName":"张三ww","image":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg"}],"comments":[]},{"id":9,"messageUsername":"经销商1","messageUserId":77,"content":"走过路过不要错过，千载难逢的好机会","picture":"http://172.22.31.58:8888/yunlian/circle/16067219-00f8-4c49-bf04-d4b043315d95.png, http://172.22.31.58:8888/yunlian/circle/70722deb-2e97-413e-a785-423ca59e7251.jpg, http://172.22.31.58:8888/yunlian/circle/13a6bb52-071f-4ed2-ad53-596a2b95c6ae.jpg","location":"","createTime":"2018-08-01 09:51:38","messageImage":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg","yesUser":[{"userId":63,"username":"总部2","realName":"总部张2","image":null},{"userId":77,"username":"经销商1","realName":"张三ww","image":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg"}],"comments":[{"id":7,"username":"经销商1","userId":77,"content":"有需要的进来看看，绝对不亏","super_id":0,"sub_comment":[{"id":8,"username":"经销商1","userId":77,"content":"都有什么好东西啊，这么卖力推荐","super_id":7,"sub_comment":[]},{"id":9,"username":"经销商1","userId":77,"content":"都有什么好东西啊，这么卖力推荐","super_id":7,"sub_comment":[]}]}]},{"id":8,"messageUsername":"经销商1","messageUserId":77,"content":"走过路过不要错过，千载难逢的好机会","picture":"","location":"","createTime":"2018-08-01 09:51:22","messageImage":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg","yesUser":[],"comments":[]},{"id":6,"messageUsername":"经销商1","messageUserId":77,"content":"心情不好","picture":"http://172.22.31.58:8888/yunlian/circle/97f2c25d-0a2b-4de0-9c39-9eeea9b07582.png, http://172.22.31.58:8888/yunlian/circle/83deda9f-2127-43e2-9920-acfb0c14f8d3.jpg, http://172.22.31.58:8888/yunlian/circle/6aa8105c-08c8-4d70-a991-2eea8649860f.jpg","location":"","createTime":"2018-07-31 09:08:19","messageImage":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg","yesUser":[{"userId":77,"username":"经销商1","realName":"张三ww","image":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg"}],"comments":[{"id":3,"username":"经销商1","userId":77,"content":"谢谢大家关心","super_id":0,"sub_comment":[{"id":4,"username":"养殖户1","userId":78,"content":"怎么啦？","super_id":3,"sub_comment":[]}]}]},{"id":3,"messageUsername":"养殖户1","messageUserId":78,"content":"物美价廉有需要的赶紧订购","picture":null,"location":null,"createTime":"2018-07-30 17:04:53","messageImage":"http://223.2.197.240:8888/yunlian/image/903892f4-a812-4fc3-a8dd-37edb72c97dd.jpg","yesUser":[],"comments":[]},{"id":1,"messageUsername":"经销商1","messageUserId":77,"content":"今天天气好热啊","picture":"http://172.22.31.58:8888/yunlian/circle/28e07981-41dd-4fed-9d4b-22bd59a99109.png, http://172.22.31.58:8888/yunlian/circle/4b44d66f-a945-4a6d-b178-1fba7478a168.jpg, http://172.22.31.58:8888/yunlian/circle/089f0f48-4714-4620-a07d-10b1d7aa1049.jpg","location":"","createTime":"2018-07-30 14:54:14","messageImage":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg","yesUser":[{"userId":73,"username":"总代ww","realName":"张三","image":"http://223.2.197.240:8888/yunlian/image/a51d12c6-a6c1-42fd-8e9a-13f25b50d211.jpg"},{"userId":77,"username":"经销商1","realName":"张三ww","image":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg"}],"comments":[{"id":1,"username":"经销商1","userId":77,"content":"是啊，三十五六度呢","super_id":0,"sub_comment":[{"id":2,"username":"经销商1","userId":77,"content":"热啊，请求放假","super_id":1,"sub_comment":[]}]}]}]
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

    public static class DataBean {
        /**
         * id : 11
         * messageUsername : 经销商1
         * messageUserId : 77
         * content : 你好
         * picture : http://172.22.31.58:8888/yunlian/circle/e2162d45-f9d6-4d36-9711-228bf9950689.png, http://172.22.31.58:8888/yunlian/circle/d72cc99d-df9c-4967-8afd-703e455cc962.png
         * location :
         * createTime : 2018-08-03 10:25:33
         * messageImage : http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg
         * yesUser : [{"userId":77,"username":"经销商1","realName":"张三ww","image":"http://223.2.197.240:8888/yunlian/image/16b19902-6d3c-49fb-be7e-83d7bef73cb9.jpg"}]
         * comments : []
         */

        private int id;
        private String messageUsername;
        private int messageUserId;
        private String content;
        private String picture;
        private String location;
        private String createTime;
        private String messageImage;
        /**
         * 教训 添加数据的时候; 一定要记得初始化
         */
        private List<FavortItem> yesUser = new ArrayList<>();
        private List<CommentBean> comments = new ArrayList<>();

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMessageUsername() {
            return messageUsername;
        }

        public void setMessageUsername(String messageUsername) {
            this.messageUsername = messageUsername;
        }

        public int getMessageUserId() {
            return messageUserId;
        }

        public void setMessageUserId(int messageUserId) {
            this.messageUserId = messageUserId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getMessageImage() {
            return messageImage;
        }

        public void setMessageImage(String messageImage) {
            this.messageImage = messageImage;
        }

        public List<FavortItem> getYesUser() {
            return yesUser;
        }

        public void setYesUser(List<FavortItem> yesUser) {
            this.yesUser = yesUser;
        }

        public List<CommentBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentBean> comments) {
            this.comments = comments;
        }
    }
}

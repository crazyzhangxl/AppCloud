package com.jit.appcloud.model.response;

import java.util.List;

/**
 * @author zxl on 2018/6/4.
 *         discription:
 */

public class LogWaterResponse {

    /**
     * code : 1
     * msg : 成功
     * data : {"pageNum":1,"pageSize":10,"total":5,"pages":1,"list":[{"id":1,"temperature":"18°","pound_id":3,"image":"dffdf","username":"employee1","remark":"备注","time":"2018-05-07 09:36:27","nh":"0.5","nano2":"3","alkali":"5","o2":"20"},{"id":4,"temperature":"18°","pound_id":3,"image":"dffdf","username":"employee1","remark":"备注","time":"2018-05-07 09:36:58","nh":"0.54","nano2":"32","alkali":"5","o2":"20"},{"id":5,"temperature":"18°","pound_id":3,"image":"dffdf","username":"employee1","remark":"备注","time":"2018-05-07 14:00:49","nh":"0.5","nano2":"3","alkali":"5","o2":"20"}]}
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
         * pageNum : 1
         * pageSize : 10
         * total : 5
         * pages : 1
         * list : [{"id":1,"temperature":"18°","pound_id":3,"image":"dffdf","username":"employee1","remark":"备注","time":"2018-05-07 09:36:27","nh":"0.5","nano2":"3","alkali":"5","o2":"20"},{"id":4,"temperature":"18°","pound_id":3,"image":"dffdf","username":"employee1","remark":"备注","time":"2018-05-07 09:36:58","nh":"0.54","nano2":"32","alkali":"5","o2":"20"},{"id":5,"temperature":"18°","pound_id":3,"image":"dffdf","username":"employee1","remark":"备注","time":"2018-05-07 14:00:49","nh":"0.5","nano2":"3","alkali":"5","o2":"20"}]
         */

        private int pageNum;
        private int pageSize;
        private int total;
        private int pages;
        private List<ListBean> list;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 1
             * temperature : 18°
             * pound_id : 3
             * image : dffdf
             * username : employee1
             * remark : 备注
             * time : 2018-05-07 09:36:27
             * nh : 0.5
             * nano2 : 3
             * alkali : 5
             * o2 : 20
             */

            private int id;
            private String temperature;
            private int pound_id;
            private String image;
            private String username;
            private String remark;
            private String time;
            private String nh;
            private String nano2;
            private String alkali;
            private String o2;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public int getPound_id() {
                return pound_id;
            }

            public void setPound_id(int pound_id) {
                this.pound_id = pound_id;
            }

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

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getNh() {
                return nh;
            }

            public void setNh(String nh) {
                this.nh = nh;
            }

            public String getNano2() {
                return nano2;
            }

            public void setNano2(String nano2) {
                this.nano2 = nano2;
            }

            public String getAlkali() {
                return alkali;
            }

            public void setAlkali(String alkali) {
                this.alkali = alkali;
            }

            public String getO2() {
                return o2;
            }

            public void setO2(String o2) {
                this.o2 = o2;
            }
        }
    }
}

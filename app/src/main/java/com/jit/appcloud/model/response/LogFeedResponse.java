package com.jit.appcloud.model.response;

import java.util.List;

/**
 * @author zxl on 2018/6/4.
 *         discription:日常投喂日志
 */

public class LogFeedResponse {


    /**
     * code : 1
     * msg : 成功
     * data : {"pageNum":1,"pageSize":10,"total":1,"pages":1,"list":[{"id":1,"username":"employee1","name":"豆粕22","pound_id":3,"count":22,"unit":"斤","time":"2018-05-15 14:54:23","feed_brand":"大豆牌","feed_type":"232"}]}
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
         * total : 1
         * pages : 1
         * list : [{"id":1,"username":"employee1","name":"豆粕22","pound_id":3,"count":22,"unit":"斤","time":"2018-05-15 14:54:23","feed_brand":"大豆牌","feed_type":"232"}]
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
             * username : employee1
             * name : 豆粕22
             * pound_id : 3
             * count : 22
             * unit : 斤
             * time : 2018-05-15 14:54:23
             * feed_brand : 大豆牌
             * feed_type : 232
             */

            private int id;
            private String username;
            private String name;
            private int pound_id;
            private int count;
            private String unit;
            private String time;
            private String feed_brand;
            private String feed_type;
            private String weather;

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPound_id() {
                return pound_id;
            }

            public void setPound_id(int pound_id) {
                this.pound_id = pound_id;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getFeed_brand() {
                return feed_brand;
            }

            public void setFeed_brand(String feed_brand) {
                this.feed_brand = feed_brand;
            }

            public String getFeed_type() {
                return feed_type;
            }

            public void setFeed_type(String feed_type) {
                this.feed_type = feed_type;
            }
        }
    }
}

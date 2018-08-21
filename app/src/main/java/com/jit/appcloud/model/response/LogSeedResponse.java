package com.jit.appcloud.model.response;

import java.util.List;

/**
 * @author zxl on 2018/6/4.
 *         discription:
 */

public class LogSeedResponse {

    /**
     * code : 1
     * msg : 成功
     * data : {"pageNum":1,"pageSize":10,"total":1,"pages":1,"list":[{"id":1,"username":"employee1","name":"豆粕","pound_id":3,"amount":20,"unit":"斤","time":"2018-05-07 14:01:14","seed_brand":"大豆牌","type":"232"}]}
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
         * list : [{"id":1,"username":"employee1","name":"豆粕","pound_id":3,"amount":20,"unit":"斤","time":"2018-05-07 14:01:14","seed_brand":"大豆牌","type":"232"}]
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
             * name : 豆粕
             * pound_id : 3
             * amount : 20
             * unit : 斤
             * time : 2018-05-07 14:01:14
             * seed_brand : 大豆牌
             * type : 232
             */

            private int id;
            private String username;
            private String name;
            private int pound_id;
            private int amount;
            private String unit;
            private String time;
            private String seed_brand;
            private String type;
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

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
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

            public String getSeed_brand() {
                return seed_brand;
            }

            public void setSeed_brand(String seed_brand) {
                this.seed_brand = seed_brand;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}

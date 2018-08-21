package com.jit.appcloud.model.response;

import java.util.List;

/**
 * @author zxl on 2018/6/4.
 *         discription:
 */

public class LogDrugResponse {

    /**
     * code : 1
     * msg : 成功
     * data : {"pageNum":1,"pageSize":10,"total":2,"pages":1,"list":[{"id":5,"name":"渔药1","amount":11,"function":"调节水质","description":"主要用于渔药产品","origin":"来源","buy_amount":"20","username":"employee1","remark":"备注内容","pound_id":2,"time":"2018-05-07 14:00:11"},{"id":6,"name":"虾药","amount":11,"function":"预防虾病","description":"主要用于预防虾生病","origin":"来源","buy_amount":"20","username":"p1","remark":"备注内容","pound_id":2,"time":"2018-05-11 16:13:40"}]}
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
         * total : 2
         * pages : 1
         * list : [{"id":5,"name":"渔药1","amount":11,"function":"调节水质","description":"主要用于渔药产品","origin":"来源","buy_amount":"20","username":"employee1","remark":"备注内容","pound_id":2,"time":"2018-05-07 14:00:11"},{"id":6,"name":"虾药","amount":11,"function":"预防虾病","description":"主要用于预防虾生病","origin":"来源","buy_amount":"20","username":"p1","remark":"备注内容","pound_id":2,"time":"2018-05-11 16:13:40"}]
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
             * id : 5
             * name : 渔药1
             * amount : 11
             * function : 调节水质
             * description : 主要用于渔药产品
             * origin : 来源
             * buy_amount : 20
             * username : employee1
             * remark : 备注内容
             * pound_id : 2
             * time : 2018-05-07 14:00:11
             */

            private int id;
            private String name;
            private int amount;
            private String function;
            private String description;
            private String origin;
            private String buy_amount;
            private String username;
            private String remark;
            private int pound_id;
            private String time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getFunction() {
                return function;
            }

            public void setFunction(String function) {
                this.function = function;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
            }

            public String getBuy_amount() {
                return buy_amount;
            }

            public void setBuy_amount(String buy_amount) {
                this.buy_amount = buy_amount;
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

            public int getPound_id() {
                return pound_id;
            }

            public void setPound_id(int pound_id) {
                this.pound_id = pound_id;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}

package com.jit.appcloud.model.response;

import java.util.List;

/**
 * @author zxl on 2018/6/6.
 *         discription:设备日志
 */

public class LogDeviceResponse {

    /**
     * code : 1
     * msg : 成功
     * data : {"pageNum":1,"pageSize":10,"total":1,"pages":1,"list":[{"id":1,"fun_name":"ph","low_out":4,"low_in":5,"high_in":6,"high_out":7,"time":"2018-05-07 14:04:39","device_id":1,"pound_id":3,"username":"employee1"}]}
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
         * list : [{"id":1,"fun_name":"ph","low_out":4,"low_in":5,"high_in":6,"high_out":7,"time":"2018-05-07 14:04:39","device_id":1,"pound_id":3,"username":"employee1"}]
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
             * fun_name : ph
             * low_out : 4
             * low_in : 5
             * high_in : 6
             * high_out : 7
             * time : 2018-05-07 14:04:39
             * device_id : 1
             * pound_id : 3
             * username : employee1
             */

            private int id;
            private String fun_name;
            private double low_out;
            private double low_in;
            private double high_in;
            private double high_out;
            private String time;
            private int device_id;
            private int pound_id;
            private String username;
            private String poundName;
            private String deviceName;

            public String getPoundName() {
                return poundName;
            }

            public void setPoundName(String poundName) {
                this.poundName = poundName;
            }

            public String getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getFun_name() {
                return fun_name;
            }

            public void setFun_name(String fun_name) {
                this.fun_name = fun_name;
            }

            public double getLow_out() {
                return low_out;
            }

            public void setLow_out(double low_out) {
                this.low_out = low_out;
            }

            public double getLow_in() {
                return low_in;
            }

            public void setLow_in(double low_in) {
                this.low_in = low_in;
            }

            public double getHigh_in() {
                return high_in;
            }

            public void setHigh_in(double high_in) {
                this.high_in = high_in;
            }

            public double getHigh_out() {
                return high_out;
            }

            public void setHigh_out(double high_out) {
                this.high_out = high_out;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getDevice_id() {
                return device_id;
            }

            public void setDevice_id(int device_id) {
                this.device_id = device_id;
            }

            public int getPound_id() {
                return pound_id;
            }

            public void setPound_id(int pound_id) {
                this.pound_id = pound_id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
}

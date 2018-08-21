package com.jit.appcloud.model.response;

import java.util.List;

/**
 * @author zxl on 2018/7/11.
 *         discription:
 */

public class DownLogResponse {

    /**
     * code : 1
     * msg : 成功
     * data : [{"id":1,"downlogname":"11111","downlogtime":"2018-07-10 10:22:10","username":"总代ww"}]
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
         * id : 1
         * downlogname : 11111
         * downlogtime : 2018-07-10 10:22:10
         * username : 总代ww
         */

        private int id;
        private String downlogname;
        private String downlogtime;
        private String username;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDownlogname() {
            return downlogname;
        }

        public void setDownlogname(String downlogname) {
            this.downlogname = downlogname;
        }

        public String getDownlogtime() {
            return downlogtime;
        }

        public void setDownlogtime(String downlogtime) {
            this.downlogtime = downlogtime;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}

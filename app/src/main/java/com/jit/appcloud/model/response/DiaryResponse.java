package com.jit.appcloud.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author zxl on 2018/7/10.
 *         discription:
 */

public class DiaryResponse {

    /**
     * code : 1
     * msg : 成功
     * data : [{"id":12,"count1":null,"count2":null,"count3":null,"count4":null,"count5":null,"count6":null,"count_total":null,"ph_min":null,"ph_max":null,"ph_range":null,"o2_min":null,"o2_max":null,"o2_range":null,"temperature_min":null,"temperature_max":null,"temperature_range":null,"nh":null,"medicine":null,"remark":null,"date":"2018-06-26 00:00:00","weather":null,"alkali":null,"nano2":null}]
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

    public static class DataBean  implements Serializable{
        /**
         * id : 12
         * count1 : null
         * count2 : null
         * count3 : null
         * count4 : null
         * count5 : null
         * count6 : null
         * count_total : null
         * ph_min : null
         * ph_max : null
         * ph_range : null
         * o2_min : null
         * o2_max : null
         * o2_range : null
         * temperature_min : null
         * temperature_max : null
         * temperature_range : null
         * nh : null
         * medicine : null
         * remark : null
         * date : 2018-06-26 00:00:00
         * weather : null
         * alkali : null
         * nano2 : null
         */

        private int id;
        private int count1;
        private int count2;
        private int count3;
        private int count4;
        private int count5;
        private int count6;
        private int count_total;
        private float ph_min;
        private float ph_max;
        private float ph_range;
        private float o2_min;
        private float o2_max;
        private float o2_range;
        private float temperature_min;
        private float temperature_max;
        private float temperature_range;
        private String nh;
        private String medicine;
        private String remark;
        private String date;
        private String weather;
        private String alkali;
        private String nano2;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCount1() {
            return count1;
        }

        public void setCount1(int count1) {
            this.count1 = count1;
        }

        public int getCount2() {
            return count2;
        }

        public void setCount2(int count2) {
            this.count2 = count2;
        }

        public int getCount3() {
            return count3;
        }

        public void setCount3(int count3) {
            this.count3 = count3;
        }

        public int getCount4() {
            return count4;
        }

        public void setCount4(int count4) {
            this.count4 = count4;
        }

        public int getCount5() {
            return count5;
        }

        public void setCount5(int count5) {
            this.count5 = count5;
        }

        public int getCount6() {
            return count6;
        }

        public void setCount6(int count6) {
            this.count6 = count6;
        }

        public int getCount_total() {
            return count_total;
        }

        public void setCount_total(int count_total) {
            this.count_total = count_total;
        }

        public float getPh_min() {
            return ph_min;
        }

        public void setPh_min(float ph_min) {
            this.ph_min = ph_min;
        }

        public float getPh_max() {
            return ph_max;
        }

        public void setPh_max(float ph_max) {
            this.ph_max = ph_max;
        }

        public float getPh_range() {
            return ph_range;
        }

        public void setPh_range(float ph_range) {
            this.ph_range = ph_range;
        }

        public float getO2_min() {
            return o2_min;
        }

        public void setO2_min(float o2_min) {
            this.o2_min = o2_min;
        }

        public float getO2_max() {
            return o2_max;
        }

        public void setO2_max(float o2_max) {
            this.o2_max = o2_max;
        }

        public float getO2_range() {
            return o2_range;
        }

        public void setO2_range(float o2_range) {
            this.o2_range = o2_range;
        }

        public float getTemperature_min() {
            return temperature_min;
        }

        public void setTemperature_min(float temperature_min) {
            this.temperature_min = temperature_min;
        }

        public float getTemperature_max() {
            return temperature_max;
        }

        public void setTemperature_max(float temperature_max) {
            this.temperature_max = temperature_max;
        }

        public float getTemperature_range() {
            return temperature_range;
        }

        public void setTemperature_range(float temperature_range) {
            this.temperature_range = temperature_range;
        }

        public String getNh() {
            return nh;
        }

        public void setNh(String nh) {
            this.nh = nh;
        }

        public String getMedicine() {
            return medicine;
        }

        public void setMedicine(String medicine) {
            this.medicine = medicine;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getAlkali() {
            return alkali;
        }

        public void setAlkali(String alkali) {
            this.alkali = alkali;
        }

        public String getNano2() {
            return nano2;
        }

        public void setNano2(String nano2) {
            this.nano2 = nano2;
        }
    }
}

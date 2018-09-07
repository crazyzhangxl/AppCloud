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
        private float ph_fluctuate;//
        private float oxygen_min;  //
        private float oxygen_max ; //

        public float getPh_fluctuate() {
            return ph_fluctuate;
        }

        public void setPh_fluctuate(float ph_fluctuate) {
            this.ph_fluctuate = ph_fluctuate;
        }

        public float getOxygen_min() {
            return oxygen_min;
        }

        public void setOxygen_min(float oxygen_min) {
            this.oxygen_min = oxygen_min;
        }

        public float getOxygen_max() {
            return oxygen_max;
        }

        public void setOxygen_max(float oxygen_max) {
            this.oxygen_max = oxygen_max;
        }

        public float getOxygen_fluctuate() {
            return oxygen_fluctuate;
        }

        public void setOxygen_fluctuate(float oxygen_fluctuate) {
            this.oxygen_fluctuate = oxygen_fluctuate;
        }

        public float getTemperature_fluctuate() {
            return temperature_fluctuate;
        }

        public void setTemperature_fluctuate(float temperature_fluctuate) {
            this.temperature_fluctuate = temperature_fluctuate;
        }

        private float oxygen_fluctuate; //
        private float temperature_min;
        private float temperature_max;
        private float temperature_fluctuate; //

        private String salt;
        private String ammo_nitro; // 氨氮
        private String alkali; // 碱度
        private String nano2; // 亚硝酸盐

        private String medicine;
        private String remark;
        private String date;
        private String weather;

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getAmmo_nitro() {
            return ammo_nitro;
        }

        public void setAmmo_nitro(String ammo_nitro) {
            this.ammo_nitro = ammo_nitro;
        }

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

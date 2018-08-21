package com.jit.appcloud.ui.adapter.multi;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jit.appcloud.db.db_model.CityWeatherBean;

/**
 *
 * @author 张先磊
 * @date 2018/4/23
 */

public class MultiItem implements MultiItemEntity {
    public static final int CITY = 1;
    public static final int ADDSYMBOL = 2;
    private int itemType;

    // 对应的每个Item的具体数值
    public String cityId;
    public String cityName; // 就是江宁
    public String temp;
    public String weatherStatus;
    public int backgroundId;

    // 用于给最后一个加号用
    public MultiItem() {
        this.itemType = ADDSYMBOL;
    }

    // 给前面的城市用的
    public MultiItem(CityWeatherBean cityWeatherBean,int backgroundId){
        if (cityWeatherBean !=null) {
            this.cityId = cityWeatherBean.getCityId();
            this.cityName = cityWeatherBean.getCityName();
            this.temp = cityWeatherBean.getTemp();
            this.weatherStatus = cityWeatherBean.getWeatherStatus();
            this.backgroundId = backgroundId;
        }
        this.itemType = CITY;
    }


    public void setItemRype(int itemType){
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}

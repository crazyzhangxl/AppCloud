package com.jit.appcloud.ui.adapter.weather_info_more;

import com.jit.appcloud.R;
import com.jit.appcloud.model.response.HeWeatherResponse;
import com.jit.appcloud.util.adapterhelper.BaseAdapterData;

import java.util.List;

/**
 * Created by 张先磊 on 2018/5/9.
 */

public class LifeIndexData implements BaseAdapterData {
    public List<HeWeatherResponse.HeWeather6Bean.LifestyleBean> lifeIndexesData;

    public LifeIndexData(List<HeWeatherResponse.HeWeather6Bean.LifestyleBean> lifeIndexesData) {
        this.lifeIndexesData = lifeIndexesData;
    }

    @Override
    public int getContentViewId() {
        return R.layout.item_weather_item_life;
    }
}

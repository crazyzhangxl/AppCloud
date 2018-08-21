package com.jit.appcloud.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.ResourceProvider;
import com.jit.appcloud.model.response.HeWeatherResponse;

import java.util.List;

/**
 * Created by 张先磊 on 2018/4/24.
 */

public class HoursForecastAdapter extends BaseQuickAdapter<HeWeatherResponse.HeWeather6Bean.HourlyBean,BaseViewHolder> {
    public HoursForecastAdapter(int layoutResId, @Nullable List<HeWeatherResponse.HeWeather6Bean.HourlyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HeWeatherResponse.HeWeather6Bean.HourlyBean item) {
        if (item == null){
            return;
        }
        helper.setText(R.id.hour,item.getTime().substring(11, 16));
        helper.setImageResource(R.id.hour_icon, ResourceProvider.getIconId(item.getCond_txt()));
        helper.setText(R.id.hour_temp,item.getTmp()+"°");
    }
}

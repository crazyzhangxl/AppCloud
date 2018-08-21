package com.jit.appcloud.ui.adapter.weather_info_more;

import com.jit.appcloud.R;
import com.jit.appcloud.util.adapterhelper.BaseAdapterData;

/**
 * Created by 张先磊 on 2018/5/9.
 */

public class LifeIndexGuideData implements BaseAdapterData {

    String guide;

    public LifeIndexGuideData(String guide) {
        this.guide = guide;
    }

    @Override
    public int getContentViewId() {
        return R.layout.item_weather_item_index_guide;
    }
}

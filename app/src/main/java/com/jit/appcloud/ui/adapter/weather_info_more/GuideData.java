package com.jit.appcloud.ui.adapter.weather_info_more;


import com.jit.appcloud.R;
import com.jit.appcloud.util.adapterhelper.BaseAdapterData;

/**
 * Created by SilenceDut on 16/10/22.
 */

public class GuideData implements BaseAdapterData {
    public String guide;
    public int guideIconId;

    public GuideData(String guide) {
        this.guide = guide;
    }

    public void setGuideIconId(int guideIconId) {
        this.guideIconId = guideIconId;
    }

    @Override
    public int getContentViewId() {
        return R.layout.item_weather_guide;
    }
}

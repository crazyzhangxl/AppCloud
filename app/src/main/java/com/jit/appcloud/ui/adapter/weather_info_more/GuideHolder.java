package com.jit.appcloud.ui.adapter.weather_info_more;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.jit.appcloud.R;
import com.jit.appcloud.util.adapterhelper.BaseRecyclerAdapter;
import com.jit.appcloud.util.adapterhelper.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by SilenceDut on 16/10/22.
 */

public class GuideHolder extends BaseViewHolder<GuideData> {
    @BindView(R.id.guide_title)
    TextView mGuideTitle;
    @BindView(R.id.guide_icon)
    ImageView mGuideIcon;

    public GuideHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @Override
    public void updateItem(GuideData data, int position) {
        mGuideTitle.setText(data.guide);
        if (data.guideIconId != 0) {
            mGuideIcon.setImageResource(data.guideIconId);
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.item_weather_guide;
    }

}

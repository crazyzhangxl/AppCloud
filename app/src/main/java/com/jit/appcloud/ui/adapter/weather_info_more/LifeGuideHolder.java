package com.jit.appcloud.ui.adapter.weather_info_more;

import android.view.View;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.util.adapterhelper.BaseRecyclerAdapter;
import com.jit.appcloud.util.adapterhelper.BaseViewHolder;
import com.silencedut.router.Router;

import butterknife.BindView;

/**
 * Created by 张先磊 on 2018/5/9.
 */

public class LifeGuideHolder extends BaseViewHolder<LifeIndexGuideData> implements WeatherCallBack.LifeAdvice {
    @BindView(R.id.guide_title)
    TextView mGuideTitle;

    public LifeGuideHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
        Router.instance().register(this);
    }

    @Override
    public void updateItem(LifeIndexGuideData data, int position) {
        mGuideTitle.setText(data.guide);
    }


    @Override
    public void lifeAdvice(String index, String advice) {
        mGuideTitle.setText(index);
    }

    @Override
    public int getContentViewId() {
        return R.layout.item_weather_item_index_guide;
    }
}

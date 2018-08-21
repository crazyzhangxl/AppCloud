package com.jit.appcloud.ui.adapter.weather_info_more;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.model.response.HeWeatherResponse;
import com.jit.appcloud.util.Check;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.util.adapterhelper.BaseAdapterData;
import com.jit.appcloud.util.adapterhelper.BaseRecyclerAdapter;
import com.jit.appcloud.util.adapterhelper.BaseViewHolder;
import com.silencedut.router.Router;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 张先磊 on 2018/5/9.
 */

public class LifeIndexesHolder extends BaseViewHolder<LifeIndexData> implements WeatherCallBack.LifeAdvice{

    private static final String[] LIFE_INDEXES= { "舒适度","穿衣", "感冒","运动", "旅游","防晒",  "洗车",  "空气"};
    private static final int[] LIFE_INDEXES_ICONIDS = { R.mipmap.weather_comf,R.mipmap.weather_clothing, R.mipmap.weather_health,R.mipmap.weather_sport_mode, R.mipmap.weather_travel,R.mipmap.weather_protection,  R.mipmap.weather_wash_car,  R.mipmap.weather_air};
    @BindView(R.id.lifeRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.life_advice)
    TextView mLifeAdvice;
    private BaseRecyclerAdapter mLifeAdapter;

    public LifeIndexesHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
        Router.instance().register(this);
        initViews();

    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mLifeAdapter = new BaseRecyclerAdapter(getContext());
        mRecyclerView.setAdapter(mLifeAdapter);
        mLifeAdapter.registerHolder(LifeItemHolder.class, R.layout.item_weather_item_life_index);
    }

    @Override
    public int getContentViewId() {
        return R.layout.item_weather_item_life;
    }

    @Override
    public void updateItem(LifeIndexData lifeIndexData, int position) {
        List<HeWeatherResponse.HeWeather6Bean.LifestyleBean> lifeIndexesData = lifeIndexData.lifeIndexesData;
        if (Check.isNull(lifeIndexData)) {
            return;
        }
        try {
            List<LifeItemData> lifeItems = new ArrayList<>();
            for (int index = 0; index < lifeIndexesData.size(); index++) {
                lifeIndexesData.get(index).setType(LIFE_INDEXES[index]);
                lifeItems.add(new LifeItemData(lifeIndexesData.get(index), LIFE_INDEXES_ICONIDS[index]));
            }
            mLifeAdapter.setData(lifeItems);
        }catch (Exception e) {
        }
    }

    @OnClick(R.id.life_advice)
    void onClick() {
        Router.instance().getReceiver(WeatherCallBack.LifeAdvice.class).lifeAdvice(UIUtils.getString(R.string.weather_lifeIndexes), UIUtils.getString(R.string.weather_lifeIndexes));
        mLifeAdvice.setVisibility(View.GONE);

    }

    @Override
    public void lifeAdvice(String index, String advice) {
        mLifeAdvice.setText(advice);
        mLifeAdvice.setVisibility(View.VISIBLE);
    }


    private static final class LifeItemData implements BaseAdapterData {

        HeWeatherResponse.HeWeather6Bean.LifestyleBean weatherLifeIndexData;
        int lifeIndexIconId = R.mipmap.weather_sport_mode;

        LifeItemData(HeWeatherResponse.HeWeather6Bean.LifestyleBean weatherLifeIndexData, int lifeIndexIconId) {
            this.weatherLifeIndexData = weatherLifeIndexData;
            this.lifeIndexIconId = lifeIndexIconId;
        }

        HeWeatherResponse.HeWeather6Bean.LifestyleBean getWeatherLifeIndexData() {
            return weatherLifeIndexData;
        }

        @Override
        public int getContentViewId() {
            return R.layout.item_weather_item_life_index;
        }
    }

    static final class LifeItemHolder extends BaseViewHolder<LifeItemData> {

        @BindView(R.id.life_type)
        TextView lifeType;
        @BindView(R.id.life_level)
        TextView lifeLevel;
        @BindView(R.id.life_index_icon)
        ImageView lifeIndexIcon;
        HeWeatherResponse.HeWeather6Bean.LifestyleBean weatherLifeIndexData;

        public LifeItemHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
            super(itemView, baseRecyclerAdapter);
        }

        @Override
        public void updateItem(LifeItemData lifeItemData, int position) {
            weatherLifeIndexData = lifeItemData.getWeatherLifeIndexData();
            if (Check.isNull(weatherLifeIndexData)) {
                return;
            }
            lifeType.setText(weatherLifeIndexData.getType());
            lifeLevel.setText(weatherLifeIndexData.getBrf());
            lifeIndexIcon.setImageResource(lifeItemData.lifeIndexIconId);
        }

        @Override
        public int getContentViewId() {
            return R.layout.item_weather_item_life_index;
        }

        @OnClick(R.id.life_index_content)
        public void onClick() {
            Router.instance().getReceiver(WeatherCallBack.LifeAdvice.class).lifeAdvice(weatherLifeIndexData.getType(), weatherLifeIndexData.getTxt());

        }
    }
}

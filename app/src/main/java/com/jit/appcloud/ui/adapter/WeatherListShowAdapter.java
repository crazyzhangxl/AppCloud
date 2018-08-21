package com.jit.appcloud.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;

import java.util.List;

/**
 * @author zxl on 2018/5/20.
 *         discription:
 */

public class WeatherListShowAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    private int selectPosition = -1;
    public void setSelectPosition(int position){
        this.selectPosition = position;
    }

    public int getSelectPosition(){
        return selectPosition;
    }

    public WeatherListShowAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView mIvMakeTrue = helper.getView(R.id.ivMakeTrue);
        if (selectPosition == helper.getAdapterPosition()){
            mIvMakeTrue.setVisibility(View.VISIBLE);
        }else {
            mIvMakeTrue.setVisibility(View.GONE);
        }
        TextView tvWeather =  helper.getView(R.id.tvWeather);
        tvWeather.setText(item);
    }
}

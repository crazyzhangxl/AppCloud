package com.jit.appcloud.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jit.appcloud.R;
import com.jit.appcloud.util.GlideLoaderUtils;
import java.io.File;
/**
 * des:圈子消息头
 * Created by xsf
 * on 2016.07.15:18
 * @author 张先磊
 */
public class ZoneHeaderView extends LinearLayout {
    private ImageView img_avater,img_bg;
    private TextView tv_name;
    private WaveView waveView;
    public ZoneHeaderView(Context context) {
        super(context);
        initView();
    }

    public ZoneHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ZoneHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ZoneHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.item_zone_header, null);
        img_bg = (ImageView)view.findViewById(R.id.img_bg) ;
        img_avater = (ImageView) view.findViewById(R.id.img_avater);
        waveView= (WaveView) view.findViewById(R.id.wave_view);
        tv_name = (TextView) view.findViewById(R.id.tv_name);

        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img_avater.getLayoutParams();
        waveView.setOnWaveAnimationListener(y -> {
            lp.setMargins(0, 0, 0, (int) y + 2);
            img_avater.setLayoutParams(lp);
        });
        addView(view);
    }
    /**
     * 设置基本信息
     */
    public void setData(String name, String avater){
        tv_name.setText(name);
        GlideLoaderUtils.display(getContext(),img_avater,avater);
    }

    public void changeBg(File bg){
        GlideLoaderUtils.display(getContext(),img_bg,bg);
    }

}

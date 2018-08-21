package com.jit.appcloud.widget.spannable;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.jit.appcloud.R;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.spannable.ISpanClick;

/**
 * @author zxl on 2018/8/2.
 *         discription: 单击监听
 */

public class NameClickable extends ClickableSpan  implements View.OnClickListener{
    private final ISpanClick mListener;
    private int mPosition;

    public NameClickable(ISpanClick l, int position) {
        mListener = l;
        mPosition = position;
    }

    @Override
    public void onClick(View widget) {
        mListener.onClick(mPosition);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        int colorValue = UIUtils.getColor(
                R.color.main_color);
        ds.setColor(colorValue);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}

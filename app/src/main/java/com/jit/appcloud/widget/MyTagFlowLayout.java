package com.jit.appcloud.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zhy.view.flowlayout.TagFlowLayout;

/**
 * @author zxl on 2018/5/22.
 *         discription:MyTagFlowLayout
 */

public class MyTagFlowLayout extends TagFlowLayout {


    public MyTagFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }
}

package com.jit.appcloud.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author zxl on 2018/9/13.
 *         discription:
 */

public class MyScrollTextView extends android.support.v7.widget.AppCompatTextView {

    private float mStartX;
    private float mStartY;

    public MyScrollTextView(Context context) {
        super(context);
    }

    public MyScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getRawX();
                float endY = event.getRawY();
                if (Math.abs(endY -  mStartY) > Math.abs(endX - mStartX)){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}

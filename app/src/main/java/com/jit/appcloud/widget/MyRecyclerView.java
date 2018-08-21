package com.jit.appcloud.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * @author zxl on 2018/7/4.
 *         discription: 第一解决位于 scroll冲突
 */

public class MyRecyclerView extends RecyclerView {
    private float mStartX;
    private float mStartY;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mStartX = ev.getRawX();
                mStartY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = ev.getRawY();
                float endX = ev.getRawX();
                float x = endX - mStartX;
                float y = endY - mStartY;
                /* 左右滑动不拦截,上下滑动拦截*/
                if (Math.abs(y) > Math.abs(x))
                {
                    /* 已经在顶部了*/
                    if (y > 0 && !canScrollVertically(-1)){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else if (y < 0 && !canScrollVertically(1)){
                        // 不能再上滑了 ========================
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}

package com.jit.appcloud.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by 张先磊 on 2018/5/4.
 */

public class MyKeyBoardLinearLayout extends LinearLayout {
    private OnSoftKeyboardListener mSoftKeyboardListener;

    public MyKeyBoardLinearLayout(@NonNull Context context) {
        super(context);
    }

    public MyKeyBoardLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyKeyBoardLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnSoftKeyboardListener{
         void onSoftKeyboardChange();
    }

    public void setSoftKeyboardListener(OnSoftKeyboardListener listener){
        mSoftKeyboardListener=listener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mSoftKeyboardListener != null) {
            mSoftKeyboardListener.onSoftKeyboardChange();
        }
    }
}

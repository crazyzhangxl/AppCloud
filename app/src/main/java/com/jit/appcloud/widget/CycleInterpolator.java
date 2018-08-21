package com.jit.appcloud.widget;

/**
 * @author zxl on 2018/7/2.
 *         discription: 动画拦截器
 */

public class CycleInterpolator implements android.view.animation.Interpolator{
    private final float mCycles = 0.5f;

    @Override
    public float getInterpolation(final float input) {
        return (float) Math.sin(2.0f * mCycles * Math.PI * input);
    }
}

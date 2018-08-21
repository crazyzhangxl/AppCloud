package com.jit.appcloud.ui.activity.calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import com.jit.appcloud.R;
import com.jit.appcloud.util.UIUtils;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time:2017/3/23 13:32.
 *    *     *   *         *   * *       Email address:ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site:www.ocnyang.com
 *******************************************************************/


public class AnnulusSpan implements LineBackgroundSpan {
    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        Paint paint = new Paint();
        paint.setAntiAlias(true); //消除锯齿
        paint.setStyle(Paint.Style.STROKE);//绘制空心圆或 空心矩形
        int ringWidth = CircleBackGroundSpan.dip2px(1);//圆环宽度
        //绘制圆环
        paint.setColor(UIUtils.getColor(R.color.green0));
        paint.setStrokeWidth(ringWidth);
        c.drawCircle((right - left) / 2, (bottom - top) / 2 /*+ CircleBackGroundSpan.dip2px(4)*/,
                /*CircleBackGroundSpan.dip2px(20),*/
                right/2-CircleBackGroundSpan.dip2px(1),
                paint);
    }
}

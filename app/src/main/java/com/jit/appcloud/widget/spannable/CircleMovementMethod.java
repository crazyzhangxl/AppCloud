package com.jit.appcloud.widget.spannable;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.BaseMovementMethod;
import android.text.method.Touch;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.util.UIUtils;

/**
 * des:
 * Created by xsf
 * on 2016.07.11:14
 * @author 张先磊
 */
public class CircleMovementMethod extends BaseMovementMethod {
    public final String TAG = CircleMovementMethod.class.getSimpleName();
    /**
     * 默认颜色
     */
    public final static int DEFAULT_COLOR = R.color.transparent;
    /**
     * 文本背景颜色
     */
    private int mTextViewBgColorId ;
    /**
     * 图片背景颜色
     */
    private int mClickableSpanBgClorId;

    /**
     * 背景颜色深的Span类
     */
    private BackgroundColorSpan mBgSpan;
    /**
     * 点击的数组
     */
    private ClickableSpan[] mClickLinks;

    /**
     * 响应时间 ----- 是响应Textd的还是响应内部的
     */
    private boolean isPassToTv = true;
    /**
     * true：响应textview的点击事件， false：响应设置的clickableSpan事件
     */
    public boolean isPassToTv() {
        return isPassToTv;
    }
    private void setPassToTv(boolean isPassToTv){
        this.isPassToTv = isPassToTv;
    }

    public CircleMovementMethod(){
        mTextViewBgColorId = DEFAULT_COLOR;
        mClickableSpanBgClorId = DEFAULT_COLOR;
    }

    public CircleMovementMethod(int clickableSpanBgClorId){
        mClickableSpanBgClorId = clickableSpanBgClorId;
        mTextViewBgColorId = DEFAULT_COLOR;
    }

    public CircleMovementMethod(int clickableSpanBgClorId, int textViewBgColorId){
        mClickableSpanBgClorId = clickableSpanBgClorId;
        mTextViewBgColorId = textViewBgColorId;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {

        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            // 获得触摸点距离该控件左上角的坐标
            int x = (int) event.getX();
            int y = (int) event.getY();
            // 减去Padding值
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            // 加上控件滑动的值
            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            mClickLinks = buffer.getSpans(off, off, ClickableSpan.class);
            if(mClickLinks.length > 0){
                // 点击的是Span区域，不要把点击事件传递
                setPassToTv(false);
                Selection.setSelection(buffer,
                        buffer.getSpanStart(mClickLinks[0]),
                        buffer.getSpanEnd(mClickLinks[0]));
                //设置点击区域的背景色
                mBgSpan = new BackgroundColorSpan(UIUtils.getColor(mClickableSpanBgClorId));
                buffer.setSpan(mBgSpan,
                        buffer.getSpanStart(mClickLinks[0]),
                        buffer.getSpanEnd(mClickLinks[0]),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else{
                setPassToTv(true);
                // textview选中效果
                widget.setBackgroundResource(mTextViewBgColorId);
            }

        }else if(action == MotionEvent.ACTION_UP){
            if(mClickLinks.length > 0){
                mClickLinks[0].onClick(widget);
                if(mBgSpan != null){
                    buffer.removeSpan(mBgSpan);
                }
            }else{
                if(mBgSpan != null){
                    buffer.removeSpan(mBgSpan);
                }
            }
            Selection.removeSelection(buffer);
            widget.setBackgroundResource(DEFAULT_COLOR);
        }else if(action == MotionEvent.ACTION_MOVE){

        }else{
            if(mBgSpan != null){
                buffer.removeSpan(mBgSpan);
            }
            widget.setBackgroundResource(DEFAULT_COLOR);
        }
        return Touch.onTouchEvent(widget, buffer, event);
    }
}

package com.jit.appcloud.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.jit.appcloud.ui.adapter.FavortListAdapter;
import com.jit.appcloud.widget.spannable.ISpanClick;

/**
 * @author zxl on 2018/8/2.
 *         discription:
 */

public class FavortListView extends android.support.v7.widget.AppCompatTextView {
    private ISpanClick mSpanClickListener;

    public void setSpanClickListener(ISpanClick listener){
        mSpanClickListener = listener;
    }
    public ISpanClick getSpanClickListener(){
        return  mSpanClickListener;
    }

    public FavortListView(Context context) {
        super(context);
    }

    public FavortListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavortListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(FavortListAdapter adapter){
        adapter.bindListView(this);
    }

}

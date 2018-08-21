/*
 * Apache DropDownView
 * Copyright 2017 The Apache Software Foundation
 *
 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).
 */

package com.jit.appcloud.widget;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jit.appcloud.R;

/**
 * @author zxl 修改版的DropDownView
 */

public class DropDownView extends RelativeLayout {
    private static final int DROP_DOWN_CONTAINER_MIN_DEFAULT_VIEWS = 1;
    private static final int DROP_DOWN_HEADER_CONTAINER_MIN_DEFAULT_VIEWS = 0;
    /**
     *  注解该View可以为空
     * */
    @Nullable
    private View expandedView;
    @Nullable
    private View headerView;
    private ViewGroup dropDownHeaderContainer;
    private LinearLayout dropDownContainer;
    private boolean isExpanded;
    private DropDownListener dropDownListener;
    private int backgroundColor;
    private int overlayColor;
    private FrameLayout mFmContainer;
    private View mMaskView;

    public DropDownView(Context context) {
        super(context);
        init(context, null);
    }

    public DropDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DropDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     *  目标 Api 21 Android 5.0
     * */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DropDownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * @return the {@link DropDownListener} that was set by you. Default is null.
     * @see #setDropDownListener(DropDownListener)
     */
    @Nullable
    public DropDownListener getDropDownListener() {
        return dropDownListener;
    }

    /**
     * @param dropDownListener your implementation of {@link DropDownListener}.
     * @see DropDownListener
     */
    public void setDropDownListener(DropDownListener dropDownListener) {
        this.dropDownListener = dropDownListener;
    }

    /**
     * @return true if the view is expanded, false otherwise.
     */
    public boolean isExpanded() {
        return isExpanded;
    }

    /**
     *
     * @param headerView your header view
     */
    public void setHeaderView(@NonNull View headerView) {
        this.headerView = headerView;
        /* ------ 移除所有头布局包含的控件 --- */
        if (dropDownHeaderContainer.getChildCount() > DROP_DOWN_HEADER_CONTAINER_MIN_DEFAULT_VIEWS) {
            for (int i = DROP_DOWN_HEADER_CONTAINER_MIN_DEFAULT_VIEWS; i < dropDownHeaderContainer.getChildCount(); i++) {
                dropDownHeaderContainer.removeViewAt(i);
            }
        }
        dropDownHeaderContainer.addView(headerView);
    }



    /**
     *  精华设置伸展的布局
     * @param expandedView your header view
     */

    public void setExpandedView(@NonNull View expandedView,@NonNull View beyondView) {
        this.expandedView = expandedView;
        if (mFmContainer.getChildCount() > DROP_DOWN_CONTAINER_MIN_DEFAULT_VIEWS) {
            for (int i = DROP_DOWN_CONTAINER_MIN_DEFAULT_VIEWS; i < dropDownContainer.getChildCount(); i++) {
                mFmContainer.removeViewAt(i);
            }
        }
        mFmContainer.addView(beyondView,0);
        mMaskView = new View(getContext());
        mMaskView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mMaskView.setBackgroundColor(0x88888888);
        mFmContainer.addView(mMaskView,1);
        mMaskView.setOnClickListener(emptyDropDownSpaceClickListener);

        mFmContainer.addView(expandedView,2);
        mMaskView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        expandedView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    public void setIsHeadShow(boolean isShow){
        if (!isShow){
            dropDownHeaderContainer.setVisibility(GONE);
        }
    }

    /**
     *  伸展
     */

    public void expandDropDown() {
        if (!isExpanded && expandedView != null) {

            if (dropDownListener != null) {
                dropDownListener.onExpandDropDown();
            }
            expandedView.setVisibility(View.VISIBLE);
            expandedView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
            mMaskView.setVisibility(VISIBLE);
            mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
            isExpanded = true;
        }
    }

    /**
     *   缩起来了
     * */
    public void collapseDropDown() {
        if (isExpanded && expandedView != null) {
            expandedView.setVisibility(View.GONE);
            expandedView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            mMaskView.setVisibility(GONE);
            mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            if (dropDownListener != null) {
                dropDownListener.onCollapseDropDown();
            }
            isExpanded = false;
        }
    }

    /**
     *  初始化自定义View
     * */
    private void init(Context context, AttributeSet attrs) {
        handleAttrs(context, attrs);
        // 注意这里是 this( 父布局是当前控件---- 一定要记得)----
        inflate(getContext(), R.layout.view_ddv_drop_down, this);
        bindViews();
        setupViews();
    }

    /**
     *  处理自定义的属性
     * */
    private void handleAttrs(Context context, AttributeSet attrs) {
        if (context != null && attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.DropDownView,
                    0, 0);

            try {
                backgroundColor = a.getColor(R.styleable.DropDownView_containerBackgroundColor, ContextCompat.getColor(context, R.color.dDVColorPrimary));
                overlayColor = a.getColor(R.styleable.DropDownView_overlayColor, ContextCompat.getColor(context, R.color.dDVTransparentGray));
            } finally {
                a.recycle(); // 进行回收
            }
        }
        // 设置默认的颜色 ---
        if (backgroundColor == 0) {
            backgroundColor = ContextCompat.getColor(context, R.color.dDVColorPrimary);
        }
        if (overlayColor == 0) {
            overlayColor = ContextCompat.getColor(context, R.color.dDVTransparentGray);
        }
    }

    private void setupViews() {
        dropDownHeaderContainer.setOnClickListener(dropDownHeaderClickListener);
        // 头容器
        dropDownHeaderContainer.setBackgroundColor(backgroundColor);

    }

    private void bindViews() {
        dropDownContainer = (LinearLayout) findViewById(R.id.drop_down_container);
        dropDownHeaderContainer = (ViewGroup) findViewById(R.id.drop_down_header);
        mFmContainer = (FrameLayout) findViewById(R.id.fmContainer);
    }


    private final OnClickListener dropDownHeaderClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isExpanded) {
                collapseDropDown();
            } else {
                expandDropDown();
            }
        }
    };

    private final OnClickListener emptyDropDownSpaceClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            collapseDropDown();
        }
    };

    /**
     *  设置监听回调
     * */
    public interface DropDownListener {
        /**
         * 扩张成功
         * This method will only be triggered when {@link #expandDropDown()} is called successfully.
         */
        void onExpandDropDown();

        /**
         * 折叠成功
         * This method will only be triggered when {@link #collapseDropDown()} is called successfully.
         */
        void onCollapseDropDown();
    }
}

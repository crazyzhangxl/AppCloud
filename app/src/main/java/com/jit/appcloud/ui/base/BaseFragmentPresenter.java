package com.jit.appcloud.ui.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by 张先磊 on 2018/5/2.
 */

public class BaseFragmentPresenter<V> {
        /*================== 以下是网络请求接口 ==================*/

    public BaseFragmentActivity mContext;

    public BaseFragmentPresenter(BaseFragmentActivity context) {
        mContext = context;
    }

    protected Reference<V> mViewRef;

    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public V getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }
}

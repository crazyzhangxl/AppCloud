package com.jit.appcloud.ui.view;

import android.widget.EditText;


import com.lqr.recyclerview.LQRRecyclerView;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 张先磊 on 2018/5/2.
 */

public interface ISessionAtView {
    BGARefreshLayout getRefreshLayout();

    LQRRecyclerView getRvMsg();

    EditText getEtContent();
}

package com.jit.appcloud.ui.view;

import android.view.View;
import android.widget.TextView;

import com.lqr.recyclerview.LQRRecyclerView;

/**
 * @author zxl on 2018/6/11.
 *         discription: 通讯录的View
 */

public interface IMsgContactsFgView {
    View getHeaderView();

    LQRRecyclerView getRvContacts();

    TextView getFooterView();
}

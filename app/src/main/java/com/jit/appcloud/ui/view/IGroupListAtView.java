package com.jit.appcloud.ui.view;

import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;


/**
 * @author zxl on 2018/7/24.
 *         discription: 群聊的View层
 */

public interface IGroupListAtView {
    /**
     * 包好群聊文字和群聊列表的布局
     * @return
     */
    LinearLayout getLlGroups();

    /**
     * 返回 群聊的列表
     * @return
     */
    RecyclerView getRvGroupList();
}

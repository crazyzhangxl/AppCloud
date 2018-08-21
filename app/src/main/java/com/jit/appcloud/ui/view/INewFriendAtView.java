package com.jit.appcloud.ui.view;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.lqr.recyclerview.LQRRecyclerView;

/**
 * @author zxl on 2018/6/11.
 *         discription: 新朋友的活动
 */

public interface INewFriendAtView {
    LinearLayout getLlNoNewFriend();

    LinearLayout getLlHasNewFriend();

    LQRRecyclerView getRvNewFriend();

    TextView getNewFriendText();
}

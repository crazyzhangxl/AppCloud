package com.jit.appcloud.ui.view;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

/**
 * @author zxl on 2018/8/9.
 *         discription: 实时监护的View层
 */

public interface IRTimeMonitorAtView {
    RecyclerView mRvTimeMonitor();
    TextView mTvTime();
}

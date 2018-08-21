package com.jit.appcloud.ui.view;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author zxl on 2018/6/11.
 *         discription:
 */

public interface ISearchUserAtView {
    EditText getEtSearchContent();

    RelativeLayout getRlNoResultTip();

    LinearLayout getLlSearch();
}

package com.jit.appcloud.ui.view;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jit.appcloud.widget.EditTextWithDel;

/**
 * Created by zxl on 2018/5/10.
 */

public interface ILoginAtView {
    ImageView getUserNameIcon();
    ImageView getUserPsdIcon();
    EditTextWithDel getUserNameText();
    EditTextWithDel getUserPsdText();
    RelativeLayout getRealName();
    RelativeLayout getRealPsd();
}

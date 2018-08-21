package com.jit.appcloud.ui.view;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jit.appcloud.widget.EditTextWithDel;

/**
 * Created by 张先磊 on 2018/5/10.
 */

public interface IRegisterFgView {
    ImageView getUserNameIcon();
    ImageView getUserRelNameIcon();
    ImageView getUserPsdIcon();
    EditTextWithDel getUserNameEt();
    EditTextWithDel getUserPsdEt();
    EditTextWithDel getUserRelNameEt();
    RelativeLayout getUserNameRy();
    RelativeLayout getUserPsdRy();
    RelativeLayout getUserRelNameRy();
}

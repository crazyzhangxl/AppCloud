package com.jit.appcloud.ui.view;

import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.kyleduo.switchbutton.SwitchButton;
import com.lqr.optionitemview.OptionItemView;

/**
 * @author zxl on 2018/7/24.
 *         discription: 用户会话信息的View层
 */

public interface ISessionInfoAtView {
    RecyclerView getRvMember();

    OptionItemView getOivGroupName();

    OptionItemView getOivNickNameInGroup();

    SwitchButton getSbToTop();

    SwitchButton getSbShowNickView();

    Button getBtnQuit();
}

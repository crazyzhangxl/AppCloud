package com.jit.appcloud.ui.view;

import android.widget.EditText;

/**
 * @author zxl on 2018/5/19.
 *         discription: 平行用户增加的V层
 */

public interface IViceUserAddView {
    /**
     * ...
     * @return 用户姓名的ET
     */
    EditText getUserName();

    /**
     * ...
     * @return 用户真实姓名的ET
     */
    EditText getUserRealName();

    /**
     * ...
     * @return 账号的ET
     */
    EditText getPwdEt();

    /**
     * ...
     * @return 确认账号的ET
     */
    EditText getPwdCfEt();
}

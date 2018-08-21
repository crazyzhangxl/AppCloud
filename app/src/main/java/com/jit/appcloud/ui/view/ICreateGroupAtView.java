package com.jit.appcloud.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lqr.recyclerview.LQRRecyclerView;

/**
 * @author zxl on 2018/7/23.
 *         discription: 创建群聊的View层
 */

public interface ICreateGroupAtView {

    /**
     *
     * @return 发起群聊
     */
    TextView getTextSend();

    /**
     *
     * @return  群聊的成员
     */
    LQRRecyclerView getRvContacts();


    /**
     *
     * @return 群聊名称的et
     */
    EditText getNameText();


}

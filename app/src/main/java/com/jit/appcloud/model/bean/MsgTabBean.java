package com.jit.appcloud.model.bean;

/**
 * Created by 张先磊 on 2018/4/27.
 */

public class MsgTabBean {
    private String title;
    private int resId;

    public MsgTabBean(String title, int resId) {
        this.title = title;
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}

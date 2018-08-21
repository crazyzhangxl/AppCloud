package com.jit.appcloud.model.bean;

/**
 * Created by 张先磊 on 2018/5/11.
 */

public class MulPicBean {
    private String filePath;
    private int type;

    public String getfilePath() {
        return filePath;
    }

    public void setfilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MulPicBean(String filePath, int type) {
        this.filePath = filePath;
        this.type = type;
    }
}

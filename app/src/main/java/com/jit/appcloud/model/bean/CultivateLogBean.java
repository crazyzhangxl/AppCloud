package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/5/18.
 * discription:养殖管理的图标和文字
 */

public class CultivateLogBean {
    private int imageId;
    private String title;
    private String des;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public CultivateLogBean(int imageId, String title, String des) {
        this.imageId = imageId;
        this.title = title;
        this.des = des;
    }
}

package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/6/28.
 *         discription:
 */

public class GalleryNiceBean {
    private String imagePath;
    private boolean isChecked;

    public GalleryNiceBean(String imagePath) {
        this.imagePath = imagePath;
    }

    public GalleryNiceBean(String imagePath, boolean isChecked) {
        this.imagePath = imagePath;
        this.isChecked = isChecked;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

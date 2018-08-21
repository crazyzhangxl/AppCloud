package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/6/27.
 *         discription: 照片墙对应的图片Bean
 */

public class GyImageBean {
    private int imageId;
    private String imagePath;

    public GyImageBean(String imagePath) {
        this.imagePath = imagePath;
    }

    public GyImageBean(int imageId, String imagePath) {
        this.imageId = imageId;
        this.imagePath = imagePath;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

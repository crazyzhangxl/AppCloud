package com.jit.appcloud.model.request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zxl on 2018/5/24.
 *         discription:发布图片的请求Bean
 */

public class PublishProductRequest {
    private String title;
    private float price;
    private String type;
    private String description;
    private File image;
    private String msg_type;
    private int hot;
    private int discount;
    private String summary;

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    private List<File> content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public List<File> getContent() {
        return content;
    }

    public void setContent(List<File> content) {
        this.content = content;
    }
}

package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/5/26.
 *         discription:流式多布局
 */

public class MulFlowBean {
    private String content;
    private boolean isContent;

    public MulFlowBean(String content, boolean isContent) {
        this.content = content;
        this.isContent = isContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isContent() {
        return isContent;
    }

    public void setContent(boolean content) {
        isContent = content;
    }
}

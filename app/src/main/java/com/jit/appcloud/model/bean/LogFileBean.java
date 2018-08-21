package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/7/4.
 *         discription:
 */

public class LogFileBean {
    private String fileType;
    private String des;

    public LogFileBean(String fileType, String des) {
        this.fileType = fileType;
        this.des = des;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}

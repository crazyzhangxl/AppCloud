package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/7/4.
 *         discription:
 */

public class FileHyBean {
    private boolean isCheck;
    private String fileName;
    private String historyTime;

    public FileHyBean() {
    }

    public FileHyBean(String fileName, String historyTime) {
        this.fileName = fileName;
        this.historyTime = historyTime;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHistoryTime() {
        return historyTime;
    }

    public void setHistoryTime(String historyTime) {
        this.historyTime = historyTime;
    }
}

package com.jit.appcloud.db.db_model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author zxl on 2018/6/25.
 *         discription: 经销商注册时的塘口管理
 *
 */

public class AgPondBean  extends DataSupport implements Serializable{
    private int id;
    private String pondId;
    private String pondCg;
    private String pondVy;
    private String seedBread;
    private int seedNum;
    private String seedArea;
    private String seedTime;
    private String liaoName;
    private String liaoType;
    private int pondPosition;

    public AgPondBean(String pondId, int pondPosition) {
        this.pondId = pondId;
        this.pondPosition = pondPosition;
    }

    public int getPondPosition() {
        return pondPosition;
    }

    public void setPondPosition(int pondPosition) {
        this.pondPosition = pondPosition;
    }

    public int getId() {
        return id;
    }

    public AgPondBean() {
    }

    public AgPondBean(String pondId, String pondCg, String pondVy, String seedBread, int seedNum, String seedArea, String seedTime, String liaoName, String liaoType) {
        this.pondId = pondId;
        this.pondCg = pondCg;
        this.pondVy = pondVy;
        this.seedBread = seedBread;
        this.seedNum = seedNum;
        this.seedArea = seedArea;
        this.seedTime = seedTime;
        this.liaoName = liaoName;
        this.liaoType = liaoType;
    }

    public String getPondId() {
        return pondId;
    }

    public void setPondId(String pondId) {
        this.pondId = pondId;
    }

    public String getPondCg() {
        return pondCg;
    }

    public void setPondCg(String pondCg) {
        this.pondCg = pondCg;
    }

    public String getPondVy() {
        return pondVy;
    }

    public void setPondVy(String pondVy) {
        this.pondVy = pondVy;
    }

    public String getSeedBread() {
        return seedBread;
    }

    public void setSeedBread(String seedBread) {
        this.seedBread = seedBread;
    }

    public int getSeedNum() {
        return seedNum;
    }

    public void setSeedNum(int seedNum) {
        this.seedNum = seedNum;
    }

    public String getSeedArea() {
        return seedArea;
    }

    public void setSeedArea(String seedArea) {
        this.seedArea = seedArea;
    }

    public String getSeedTime() {
        return seedTime;
    }

    public void setSeedTime(String seedTime) {
        this.seedTime = seedTime;
    }

    public String getLiaoName() {
        return liaoName;
    }

    public void setLiaoName(String liaoName) {
        this.liaoName = liaoName;
    }

    public String getLiaoType() {
        return liaoType;
    }

    public void setLiaoType(String liaoType) {
        this.liaoType = liaoType;
    }
}

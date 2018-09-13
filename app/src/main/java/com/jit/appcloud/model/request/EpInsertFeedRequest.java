package com.jit.appcloud.model.request;

import java.util.Date;

/**
 * @author zxl on 2018/7/10.
 *         discription: 养殖户增加餐数的活动
 */

public class EpInsertFeedRequest {
    private int pound_id;
    private long count1;
    private long count2;
    private long count3;
    private long count4;
    private long count5;
    private long count6;
    private String date;

    public EpInsertFeedRequest() {
    }

    public int getPound_id() {
        return pound_id;
    }

    public void setPound_id(int pound_id) {
        this.pound_id = pound_id;
    }

    public long getCount1() {
        return count1;
    }

    public void setCount1(long count1) {
        this.count1 = count1;
    }

    public long getCount2() {
        return count2;
    }

    public void setCount2(long count2) {
        this.count2 = count2;
    }

    public long getCount3() {
        return count3;
    }

    public void setCount3(long count3) {
        this.count3 = count3;
    }

    public long getCount4() {
        return count4;
    }

    public void setCount4(long count4) {
        this.count4 = count4;
    }

    public long getCount5() {
        return count5;
    }

    public void setCount5(long count5) {
        this.count5 = count5;
    }

    public long getCount6() {
        return count6;
    }

    public void setCount6(long count6) {
        this.count6 = count6;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

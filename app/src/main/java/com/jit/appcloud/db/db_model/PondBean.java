package com.jit.appcloud.db.db_model;

import org.litepal.crud.DataSupport;

/**
 * @author zxl on 2018/6/2.
 *         discription:
 */

public class PondBean extends DataSupport {
    private int pondId;
    private String pondName;

    public PondBean(int pondId, String pondName) {
        this.pondId = pondId;
        this.pondName = pondName;
    }

    public int getPondId() {
        return pondId;
    }

    public void setPondId(int pondId) {
        this.pondId = pondId;
    }

    public String getPondName() {
        return pondName;
    }

    public void setPondName(String pondName) {
        this.pondName = pondName;
    }

}

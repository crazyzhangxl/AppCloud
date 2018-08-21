package com.jit.appcloud.db.db_model;

import org.litepal.crud.DataSupport;

/**
 * @author zxl on 2018/5/30.
 *         discription:持久化经销商用户下的养殖户的姓名
 */

public class FarmerNameBean  extends DataSupport{
    private String farmName;

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public FarmerNameBean(String farmName) {
        this.farmName = farmName;
    }
}

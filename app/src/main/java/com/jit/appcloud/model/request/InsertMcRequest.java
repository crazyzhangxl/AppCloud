package com.jit.appcloud.model.request;

/**
 * @author zxl on 2018/5/30.
 *         discription:新增药品
 */

public class InsertMcRequest {
    private String name;
    private int pound_id;
    private String remark;
    private String origin;
    private String description;
    private String function;
    private int mount;
    private int buy_amout;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPound_id() {
        return pound_id;
    }

    public void setPound_id(int pound_id) {
        this.pound_id = pound_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getMount() {
        return mount;
    }

    public void setMount(int mount) {
        this.mount = mount;
    }

    public int getBuy_amout() {
        return buy_amout;
    }

    public void setBuy_amout(int buy_amout) {
        this.buy_amout = buy_amout;
    }
}

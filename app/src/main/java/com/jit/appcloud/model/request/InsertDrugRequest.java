package com.jit.appcloud.model.request;

/**
 * @author zxl on 2018/6/2.
 *         discription:
 */

public class InsertDrugRequest {
    private int amount;
    private int pound_id;
    private String name;
    private String function;
    private String description;
    private String origin;
    private String buy_amount;
    private String remark;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPound_id() {
        return pound_id;
    }

    public void setPound_id(int pound_id) {
        this.pound_id = pound_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getBuy_amount() {
        return buy_amount;
    }

    public void setBuy_amount(String buy_amount) {
        this.buy_amount = buy_amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

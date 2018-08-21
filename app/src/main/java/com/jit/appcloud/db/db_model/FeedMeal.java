package com.jit.appcloud.db.db_model;
import org.litepal.crud.DataSupport;
import java.io.Serializable;

/**
 * @author zxl on 2018/6/12.
 *         discription: 投放套餐持久化
 */

public class FeedMeal extends DataSupport implements Serializable{
    private String MealName;
    private String feedType;
    private String feedBrand;
    private String feedName;
    private double inputNum;
    private String inputUnit;

    public FeedMeal(String mealName, String feedType, String feedBrand, String feedName, double inputNum, String inputUnit) {
        MealName = mealName;
        this.feedType = feedType;
        this.feedBrand = feedBrand;
        this.feedName = feedName;
        this.inputNum = inputNum;
        this.inputUnit = inputUnit;
    }

    public String getMealName() {
        return MealName;
    }

    public void setMealName(String mealName) {
        MealName = mealName;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public String getFeedBrand() {
        return feedBrand;
    }

    public void setFeedBrand(String feedBrand) {
        this.feedBrand = feedBrand;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public double getInputNum() {
        return inputNum;
    }

    public void setInputNum(double inputNum) {
        this.inputNum = inputNum;
    }

    public String getInputUnit() {
        return inputUnit;
    }

    public void setInputUnit(String inputUnit) {
        this.inputUnit = inputUnit;
    }
}

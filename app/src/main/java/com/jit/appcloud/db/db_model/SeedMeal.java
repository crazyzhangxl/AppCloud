package com.jit.appcloud.db.db_model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author zxl on 2018/6/12.
 *         discription:
 */

public class SeedMeal extends DataSupport implements Serializable {
    private String MealName;
    private String SeedType;
    private String SeedBrand;
    private String SeedName;
    private double inputNum;
    private String inputUnit;

    public SeedMeal(String mealName, String seedType, String seedBrand, String seedName, double inputNum, String inputUnit) {
        MealName = mealName;
        SeedType = seedType;
        SeedBrand = seedBrand;
        SeedName = seedName;
        this.inputNum = inputNum;
        this.inputUnit = inputUnit;
    }

    public String getMealName() {
        return MealName;
    }

    public void setMealName(String mealName) {
        MealName = mealName;
    }

    public String getSeedType() {
        return SeedType;
    }

    public void setSeedType(String seedType) {
        SeedType = seedType;
    }

    public String getSeedBrand() {
        return SeedBrand;
    }

    public void setSeedBrand(String seedBrand) {
        SeedBrand = seedBrand;
    }

    public String getSeedName() {
        return SeedName;
    }

    public void setSeedName(String seedName) {
        SeedName = seedName;
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

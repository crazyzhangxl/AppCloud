package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/7/9.
 *         discription:
 */
public class AgAddPondBean {
    private String number;
    private String type;
    private int square;
    private String username;
    private String seed_type;
    private String seed_brand;
    private String seed_time;
    private int seed_number;
    private String feed_brand;
    private String feed_type;

    public AgAddPondBean() {
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSquare(int square) {
        this.square = square;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSeed_type(String seed_type) {
        this.seed_type = seed_type;
    }

    public void setSeed_brand(String seed_brand) {
        this.seed_brand = seed_brand;
    }

    public void setSeed_time(String seed_time) {
        this.seed_time = seed_time;
    }

    public void setSeed_number(int seed_number) {
        this.seed_number = seed_number;
    }

    public void setFeed_brand(String feed_brand) {
        this.feed_brand = feed_brand;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
    }

    public AgAddPondBean(String number, String type, int square, String username, String seed_type, String seed_brand, String seed_time, int seed_number, String feed_brand, String feed_type) {
        this.number = number;
        this.type = type;
        this.square = square;
        this.username = username;
        this.seed_type = seed_type;
        this.seed_brand = seed_brand;
        this.seed_time = seed_time;
        this.seed_number = seed_number;
        this.feed_brand = feed_brand;
        this.feed_type = feed_type;
    }
}

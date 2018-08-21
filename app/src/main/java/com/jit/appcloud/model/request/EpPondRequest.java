package com.jit.appcloud.model.request;

/**
 * @author zxl on 2018/7/13.
 *         discription: 养殖户塘口的请求体
 */

public class EpPondRequest {

    private String number;
    private String type;
    private int square;
    private String username;
    private String seed_type;
    private String seed_brand;
    private int seed_number;
    private String feed_brand;
    private String feed_type;

    public EpPondRequest() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSquare() {
        return square;
    }

    public void setSquare(int square) {
        this.square = square;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSeed_type() {
        return seed_type;
    }

    public void setSeed_type(String seed_type) {
        this.seed_type = seed_type;
    }

    public String getSeed_brand() {
        return seed_brand;
    }

    public void setSeed_brand(String seed_brand) {
        this.seed_brand = seed_brand;
    }

    public int getSeed_number() {
        return seed_number;
    }

    public void setSeed_number(int seed_number) {
        this.seed_number = seed_number;
    }

    public String getFeed_brand() {
        return feed_brand;
    }

    public void setFeed_brand(String feed_brand) {
        this.feed_brand = feed_brand;
    }

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
    }
}

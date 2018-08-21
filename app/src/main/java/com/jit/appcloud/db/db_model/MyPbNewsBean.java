package com.jit.appcloud.db.db_model;
import org.litepal.crud.DataSupport;
import java.io.Serializable;
/**
 * @author zxl on 2018/7/3.
 *         discription: 说明了什么,说明了不能在内部类中申明类型DB-------->
 */

public class MyPbNewsBean extends DataSupport implements Serializable {
    /**
     * id : 11
     * title : 龙虾
     * price : 253
     * image : http://223.2.197.240:8888/yunlian/product/2bf4475c-46bb-404d-807a-cd3ecbcaf436.jpg
     * time : 2018-06-26 17:04:40
     * username : 总部w
     * type : 苗
     * msg_type : 打开网址
     * content : null
     * description : http://www.yzniuyang01.com/
     * hot : 0
     * discount : 0
     * summary : 鲤鱼跃龙门
     * position : 3
     * isTop : 1
     */

    private int id;
    private String title;
    private float price;
    private String image;
    private String time;
    private String username;
    private String type;
    private String msg_type;
    private String content;
    private String description;
    private int hot;
    private int discount;
    private String summary;
    private int position;
    private int isTop;
    private int newsId;

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }
}

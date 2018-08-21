package com.jit.appcloud.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxl on 2018/8/3.
 *         discription:
 */

public class CommentBean {
    /**
     * id : 3
     * username : 经销商1
     * userId : 77
     * content : 谢谢大家关心
     * super_id : 0
     * sub_comment : [{"id":4,"username":"养殖户1","userId":78,"content":"怎么啦？","super_id":3,"sub_comment":[]}]
     */

    private int id;
    private String username;
    private int userId;
    private String content;
    private int super_id;
    private String commentedUsername;
    private int commentedUserId;

    public String getCommentedUsername() {
        return commentedUsername;
    }

    public void setCommentedUsername(String commentedUsername) {
        this.commentedUsername = commentedUsername;
    }

    public int getCommentedUserId() {
        return commentedUserId;
    }

    public void setCommentedUserId(int commentedUserId) {
        this.commentedUserId = commentedUserId;
    }

    private List<CommentBean> sub_comment = new ArrayList<>();

    public CommentBean(String commentedUsername,int commentedUserId,String username, int userId, String content, int super_id) {
        this.commentedUsername = commentedUsername;
        this.commentedUserId = commentedUserId;
        this.username = username;
        this.userId = userId;
        this.content = content;
        this.super_id = super_id;
    }

    public CommentBean(String username, int userId, String content, int super_id) {
        this.username = username;
        this.userId = userId;
        this.content = content;
        this.super_id = super_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSuper_id() {
        return super_id;
    }

    public void setSuper_id(int super_id) {
        this.super_id = super_id;
    }

    public List<CommentBean> getSub_comment() {
        return sub_comment;
    }

    public void setSub_comment(List<CommentBean> sub_comment) {
        this.sub_comment = sub_comment;
    }

}

package com.jit.appcloud.model.bean;

/**
 * @author zxl on 2018/8/3.
 *         discription:
 */

public class CommentConfig {
    public static enum Type{
        PUBLIC("public"), REPLY("reply");

        private String value;
        private Type(String value){
            this.value = value;
        }

    }

    public int circlePosition;
    public int commentPosition;
    private int commentReplyID;

    public int getCommentReplyID() {
        return commentReplyID;
    }

    public void setCommentReplyID(int commentReplyID) {
        this.commentReplyID = commentReplyID;
    }

    public Type commentType;
    private int publishId;
    private String publishUserId;
    private String id;//被评论人id
    private String name;//被评论人name
    private String headUrl;
    private boolean isOpen=false;

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public int getPublishId() {
        return publishId;
    }

    public void setPublishId(int publishId) {
        this.publishId = publishId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getCirclePosition() {
        return circlePosition;
    }

    public void setCirclePosition(int circlePosition) {
        this.circlePosition = circlePosition;
    }

    public int getCommentPosition() {
        return commentPosition;
    }

    public void setCommentPosition(int commentPosition) {
        this.commentPosition = commentPosition;
    }

    public Type getCommentType() {
        return commentType;
    }

    public void setCommentType(Type commentType) {
        this.commentType = commentType;
    }

}

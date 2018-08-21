package com.jit.appcloud.db.db_model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author 张先磊
 * @创建者 CSDN_LQR
 * @描述 群成员
 *
 * "is_interrupt": null
 *  明天询问老师 这里数据类型是什么 ==== 最好是int 0/1
 */
public class GroupMember extends DataSupport implements Serializable {
    private String userId;
    private String name;
    private String portraitUri;
    private String groupId;
    private String displayName;
    private String nameSpelling;
    private String displayNameSpelling;
    private String groupName;
    private String groupNameSpelling;
    private String groupPortrait;
    private boolean isShowNickName;

    public boolean isShowNickName() {
        return isShowNickName;
    }

    public void setShowNickName(boolean showNickName) {
        isShowNickName = showNickName;
    }

    public GroupMember(String userId, String name, String portraitUri) {
        this.userId = userId;
        this.name = name;
        this.portraitUri = portraitUri;
    }

    public GroupMember(String groupId, String userId, String name, String portraitUri, String displayName, String groupName, String groupPortrait) {
        this(userId, name, portraitUri);
        this.groupId = groupId;
        this.displayName = displayName;
        this.groupName = groupName;
        this.groupPortrait = groupPortrait;
    }

    public GroupMember(String groupId, String userId, String name, String portraitUri, String displayName,  String groupName,  String groupPortrait,boolean isShowNickName) {
        this(userId, name, portraitUri);
        this.groupId = groupId;
        this.displayName = displayName;
        this.groupName = groupName;
        this.groupPortrait = groupPortrait;
        this.isShowNickName = isShowNickName;
    }

    public GroupMember(String groupId, String userId, String name, String portraitUri, String displayName, String nameSpelling, String displayNameSpelling, String groupName, String groupNameSpelling) {
        this(userId, name, portraitUri);
        this.groupId = groupId;
        this.displayName = displayName;
        this.nameSpelling = nameSpelling;
        this.displayNameSpelling = displayNameSpelling;
        this.groupName = groupName;
        this.groupNameSpelling = groupNameSpelling;
    }

    public GroupMember(String groupId, String userId, String name, String portraitUri, String displayName) {
        this(userId, name, portraitUri);
        this.groupId = groupId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNameSpelling() {
        return nameSpelling;
    }

    public void setNameSpelling(String nameSpelling) {
        this.nameSpelling = nameSpelling;
    }

    public String getDisplayNameSpelling() {
        return displayNameSpelling;
    }

    public void setDisplayNameSpelling(String displayNameSpelling) {
        this.displayNameSpelling = displayNameSpelling;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupNameSpelling() {
        return groupNameSpelling;
    }

    public void setGroupNameSpelling(String groupNameSpelling) {
        this.groupNameSpelling = groupNameSpelling;
    }

    public String getGroupPortrait() {
        return groupPortrait;
    }

    public void setGroupPortrait(String groupPortrait) {
        this.groupPortrait = groupPortrait;
    }
}

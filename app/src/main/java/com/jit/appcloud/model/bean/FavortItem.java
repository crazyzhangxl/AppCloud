package com.jit.appcloud.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zxl on 2018/8/2.
 *         discription: 点赞分类
 */

public class FavortItem implements Parcelable {
    private int userId;
    private String username;
    private String realName;

    public FavortItem(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.username);
        dest.writeString(this.realName);
    }

    public FavortItem() {
    }

    protected FavortItem(Parcel in) {
        this.userId = in.readInt();
        this.username = in.readString();
        this.realName = in.readString();
    }

    public static final Parcelable.Creator<FavortItem> CREATOR = new Parcelable.Creator<FavortItem>() {
        @Override
        public FavortItem createFromParcel(Parcel source) {
            return new FavortItem(source);
        }

        @Override
        public FavortItem[] newArray(int size) {
            return new FavortItem[size];
        }
    };
}

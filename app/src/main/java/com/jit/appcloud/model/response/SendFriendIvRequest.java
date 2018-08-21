package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/6/13.
 *         discription:
 */

public class SendFriendIvRequest {
    private String friendName;
    private String message;

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SendFriendIvRequest(String friendName, String message) {
        this.friendName = friendName;
        this.message = message;
    }
}

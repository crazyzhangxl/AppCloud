package com.jit.appcloud.receiver;

import android.content.Context;

import com.jit.appcloud.util.LogUtils;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * @author zxl on 2018/7/28.
 *         discription:
 */

public class RongPushReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        LogUtils.e("广播","11111111111111");
        return true;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }
}

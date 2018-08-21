package com.jit.appcloud.util;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.receiver.NotificationReceiver;
import java.util.Random;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * @author 张先磊
 */
public class NotificationUtils {
    private static final Random RANDOM = new Random();
    //提醒（响铃震动）的周期
    private static final long REMIND_PERIOD = 5 * 1000;

    //通知栏上次提醒时间
    private static long mNotificationRemindTime;
    //前台上次提醒时间
    private static long mForegroundRemindPreTime;

    private static NotificationManager nm;
    private NotificationUtils(Conversation.ConversationType conversationType, String sendID, String sendName,String title, String sendMsg, Context context){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context,sendID);
        //可以让通知显示在最上面
        mBuilder.setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        if (shouldRemind(true)) {
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
            //使用默认的声音、振动、闪光
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(
                context.getResources(), R.mipmap.ic_launcher);
        sendMsg = sendName + ":" + sendMsg; //内容为 xxx:内容
               int unreadCount = RongIMClient.getInstance().getUnreadCount(conversationType, sendID);
               if (unreadCount > 1) {
                    //如果未读数大于1，则还有拼接上[x条]
                    String num = String.format("[%d条]", unreadCount);
                    sendMsg = num + sendMsg;//内容为 [x条] xxx:内容
                }

        mBuilder.setLargeIcon(largeIcon)
                .setContentTitle(title);

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("sessionId",sendID);// ================
        if (conversationType == Conversation.ConversationType.PRIVATE) {
            intent.putExtra("sessionType", AppConst.SESSION_TYPE_PRIVATE);
        }else if (conversationType == Conversation.ConversationType.GROUP){
            intent.putExtra("sessionType", AppConst.SESSION_TYPE_GROUP);
        }else {
            return;
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, getRandomNum(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //通知首次出现在通知栏，带上升动画效果的
        mBuilder.setTicker(sendMsg);
        //内容
        mBuilder.setContentText(sendMsg);

        mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();

        int notifyId;
        try {
            notifyId = Integer.parseInt(sendID);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            notifyId = -1;
        }

        //弹出通知栏
        nm.notify(notifyId, notification);
    }

    public static class Build {
        private Context mContext;
        private String sendId;
        private String sendName;
        private String sendTitle;
        private String sendMsg;
        private Conversation.ConversationType mConversationType;
        public Build(Context context){
            this.mContext = context;
            if (nm == null){
                nm = (NotificationManager) context
                        .getSystemService(NOTIFICATION_SERVICE);
            }
        }

        public Build setsendTitle(@NonNull String sendTitle){
            this.sendTitle = sendTitle;
            return this;
        }

        public Build setConType(@NonNull Conversation.ConversationType conversationType){
            this.mConversationType = conversationType;
            return this;
        }

        public Build setSendId(@NonNull String  sendId){
            this.sendId = sendId;
            return this;
        }

        public Build setSendName(@NonNull String sendName){
            this.sendName = sendName;
            return this;
        }

        public Build setSendMsg(@NonNull String sendMsg){
            this.sendMsg = sendMsg;
            return this;
        }


        public void cancelTargetNf(@NonNull String targetID){
            if (nm != null){
                nm.cancel(Integer.parseInt(targetID));
            }
        }

        public void cancelAll(){
            if (nm != null){
                nm.cancelAll();
            }
        }

        public NotificationUtils build(){
            return new NotificationUtils(mConversationType,sendId,sendName,sendTitle,sendMsg,mContext);
        }

    }


    /**
     * 判断是否需要提醒，根据是否超过周期
     *
     * @return
     */
    private static boolean shouldRemind(boolean isNotification) {
        if (isNotification) {
            if (System.currentTimeMillis() - mNotificationRemindTime >= REMIND_PERIOD) {
                mNotificationRemindTime = System.currentTimeMillis();
                return true;
            }
            return false;
        }

        //如果是判断前台提醒
        if (System.currentTimeMillis() - mForegroundRemindPreTime >= REMIND_PERIOD) {
            mForegroundRemindPreTime = System.currentTimeMillis();
            return true;
        }

        return false;
    }

    public static int getRandomNum() {
        return RANDOM.nextInt(Integer.MAX_VALUE);
    }
}

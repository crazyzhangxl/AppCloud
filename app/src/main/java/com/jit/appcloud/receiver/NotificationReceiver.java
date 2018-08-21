package com.jit.appcloud.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import com.jit.appcloud.app.MyApp;
import com.jit.appcloud.ui.activity.message.SessionActivity;

import java.util.List;

import static com.jit.appcloud.commom.AppConst.SESSION_TYPE_PRIVATE;

/**
 * @author zxl
 *  descrption: 用于通知栏的跳转;不能在peddingIntent中跳转
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent launchIntent;
        if (isRunning(context,"com.jit.appcloud")) {
            launchIntent = new Intent(context, SessionActivity.class);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }else {
            // 启动页
            launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.jit.appcloud");
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        }
        launchIntent.putExtra("sessionId",intent.getStringExtra("sessionId"));// ================
        launchIntent.putExtra("sessionType", intent.getIntExtra("sessionType",SESSION_TYPE_PRIVATE));
        launchIntent.putExtra("open","open");
        context.startActivity(launchIntent);
    }

    private boolean isRunning(Context context,String pName){
        boolean appRunning = isAppRunning(context, pName);
        boolean activityHas = (MyApp.activities != null) && (MyApp.activities.size()!=0);
        return appRunning && activityHas;
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     * Created by cafeting on 2017/2/4.
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    private   boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    private   int getPackageUid(Context context, String packageName) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                return applicationInfo.uid;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /**
     * 判断某一 uid 的程序是否有正在运行的进程，即是否存活
     * Created by cafeting on 2017/2/4.
     *
     * @param context     上下文
     * @param uid 已安装应用的 uid
     * @return true 表示正在运行，false 表示没有运行
     */
    private   boolean isProcessRunning(Context context, int uid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() > 0) {
            for (ActivityManager.RunningServiceInfo appProcess : runningServiceInfos){
                if (uid == appProcess.uid) {
                    return true;
                }
            }
        }
        return false;
    }


}

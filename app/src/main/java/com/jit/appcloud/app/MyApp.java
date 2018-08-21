package com.jit.appcloud.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.Friend;
import com.jit.appcloud.db.db_model.Groups;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.message.DeleteContactMessage;
import com.jit.appcloud.model.message.GroupNotificationMessageData;
import com.jit.appcloud.model.response.ContactNotificationMessageData;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.PinyinUtils;
import com.jit.appcloud.util.UIUtils;
import com.lqr.emoji.LQREmotionKit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.LinkedList;
import java.util.List;

import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.GroupNotificationMessage;
import retrofit2.HttpException;

/**
 *
 * @author zxl
 * @date 2018/4/17
 */

public class MyApp extends MultiDexApplication implements RongIMClient.OnReceiveMessageListener {
    public static List<Activity> activities = new LinkedList<>();
    private static Handler mHandler;//主线程Handler
    private static long mMainThreadId;//主线程id

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;//上下文
    /**
     * 屏幕密度
     */
    public static float screenDensity;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();
        screenDensity = getApplicationContext().getResources().getDisplayMetrics().density;
        LitePal.initialize(this);
        //初始化融云
        initRongCloud();

        //初始化表情控件
        LQREmotionKit.init(this, (context, path, imageView) -> Glide.with(context).load(path).apply(new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(imageView));
    }


    private void initRongCloud() {
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if ("io.rong.push".equals(getCurProcessName(getApplicationContext()))
                || getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            RongIMClient.init(this);
        }
        //监听接收到的消息
        RongIMClient.setOnReceiveMessageListener(this);
        try {
            /* ============== 接受删除消息 ===============*/
            RongIMClient.registerMessageType(DeleteContactMessage.class);
        }catch (AnnotationNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

    public static String topActivityName(){
        if (activities == null || activities.size() == 0){
            return null;
        }
        return activities.get(activities.size()-1).getComponentName().getClassName();
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    public boolean onReceived(Message message, int i) {
        MessageContent messageContent = message.getContent();
        if (messageContent instanceof ContactNotificationMessage){
            LogUtils.e("接收","类型1");
            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
            if (contactNotificationMessage.getOperation().equals(ContactNotificationMessage.CONTACT_OPERATION_REQUEST)) {
                //对方发来好友邀请
                BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_RED_DOT);
            } else {
                //对方同意我的好友请求
                ContactNotificationMessageData c = null;
                try {
                    Gson gson = new Gson();
                    c = gson.fromJson(contactNotificationMessage.getExtra(), ContactNotificationMessageData.class);
                } catch (HttpException e) {
                    e.printStackTrace();
                    return false;
                }
                if (c != null) {
                    if (DBManager.getInstance().isMyFriend(contactNotificationMessage.getSourceUserId())) {
                        return false;
                    }
                    DBManager.getInstance().saveOrUpdateFriend(
                            new Friend(contactNotificationMessage.getSourceUserId(),
                                    c.getSourceUserNickname(),
                                    null, c.getSourceUserNickname(), null, null,
                                    null, null,
                                    PinyinUtils.getPinyin(c.getSourceUserNickname()),
                                    PinyinUtils.getPinyin(c.getSourceUserNickname())
                            )
                    );
                    BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_FRIEND);
                    BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_RED_DOT);
                }
            }
        }else if (messageContent instanceof DeleteContactMessage){
            // 删除消息接收到了
            LogUtils.e("接收","类型2");
            DeleteContactMessage deleteContactMessage = (DeleteContactMessage) messageContent;
            String contactId = deleteContactMessage.getContact_id();
            RongIMClient.getInstance().getConversation(Conversation.ConversationType.PRIVATE, contactId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    RongIMClient.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, contactId, new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            RongIMClient.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, contactId, null);
                            BroadcastManager.getInstance(getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
            /* 根据好友Id来删除好友*/
            DBManager.getInstance().deleteFriendById(contactId);
            BroadcastManager.getInstance(getContext()).sendBroadcast(AppConst.UPDATE_FRIEND);

        }else if (messageContent instanceof GroupNotificationMessage){
            LogUtils.e("接收","类型3");
            GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) messageContent;
            String groupId = message.getTargetId();
            LogUtils.e("群组消息的通知","创建者:"+groupNotificationMessage.getOperatorUserId()+
            "   类型: "+groupNotificationMessage.getOperation()+"  消息: "+groupNotificationMessage.getMessage()
            +"  data = "+groupNotificationMessage.getData());
            GroupNotificationMessageData data = null;
            try {
                String curUserId = UserCache.getId();
                try {
                     // 数据转换这里还是得注意的啊
                    data = jsonToBean(groupNotificationMessage.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //如果: 创建了群聊天,那么就应该对应的通知所属的群成员 进而去拉取网络数据,储存到数据库,且广播通知
                if (groupNotificationMessage.getOperation().equals(GroupNotificationMessage
                        .GROUP_OPERATION_CREATE)) {
                    DBManager.getInstance().getGroups(groupId);
                    DBManager.getInstance().getGroupMember(groupId);

                } else if (groupNotificationMessage.getOperation().equals(GroupNotificationMessage.GROUP_OPERATION_DISMISS)) {
                    handleGroupDismiss(groupId);
                } else if (groupNotificationMessage.getOperation().equals(GroupNotificationMessage.GROUP_OPERATION_KICKED)) {
                    if (data != null) {
                        List<String> memberIdList = data.getTargetUserIds();
                        if (memberIdList != null) {
                            for (String userId : memberIdList) {
                                if (curUserId.equals(userId)) {
                                    RongIMClient.getInstance().removeConversation(Conversation.ConversationType.GROUP, message.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                                        @Override
                                        public void onSuccess(Boolean aBoolean) {
                                            LogUtils.e("融云","Conversation remove successfully.");
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode e) {

                                        }
                                    });
                                }
                            }
                        }
                        List<String> kickedUserIDs = data.getTargetUserIds();
                        DBManager.getInstance().deleteGroupMembers(groupId, kickedUserIDs);
                        //因为操作存在异步，故不在这里发送广播
                    }
                } else if (groupNotificationMessage.getOperation().equals(GroupNotificationMessage
                        .GROUP_OPERATION_ADD)) {
                    DBManager.getInstance().getGroups(groupId);
                    DBManager.getInstance().getGroupMember(groupId);
                    /* 发送刷新信息列表的广播*/
                    //因为操作存在异步，故不在这里发送广播
                } else if (groupNotificationMessage.getOperation().equals(GroupNotificationMessage.GROUP_OPERATION_QUIT)) {
                    if (data != null) {
                        List<String> quitUserIDs = data.getTargetUserIds();
                        DBManager.getInstance().deleteGroupMembers(groupId, quitUserIDs);
                        //因为操作存在异步，故不在这里发送广播
                    }
                } else if (groupNotificationMessage.getOperation().equals(GroupNotificationMessage.GROUP_OPERATION_RENAME)) {
                    if (data != null) {
                        String targetGroupName = data.getTargetGroupName();
                        DBManager.getInstance().updateGroupsName(groupId, targetGroupName);
                        //更新群名
                        BroadcastManager.getInstance(getContext()).sendBroadcast(AppConst.UPDATE_CURRENT_SESSION_NAME);
                        BroadcastManager.getInstance(getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            BroadcastManager.getInstance(getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
        }else {
            // 单聊消息是类型四  普通的消息
            LogUtils.e("接收","类型4");
            // 如果的话,也是在这里弹出窗体

            // 更新会话上层
            BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
            // 更新最新的消息
            BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_CURRENT_SESSION, message);
        }
        return false;
    }

    private void handleGroupDismiss(final String groupId) {
        RongIMClient.getInstance().getConversation(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                RongIMClient.getInstance().clearMessages(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        RongIMClient.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                DBManager.getInstance().deleteGroup(new Groups(groupId));
                                DBManager.getInstance().deleteGroupMembersByGroupId(groupId);
                                BroadcastManager.getInstance(getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                                BroadcastManager.getInstance(getContext()).sendBroadcast(AppConst.GROUP_LIST_UPDATE);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }


    private GroupNotificationMessageData jsonToBean(String data) {
        GroupNotificationMessageData dataEntity = new GroupNotificationMessageData();
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("operatorNickname")) {
                dataEntity.setOperatorNickname(jsonObject.getString("operatorNickname"));
            }
            if (jsonObject.has("targetGroupName")) {
                dataEntity.setTargetGroupName(jsonObject.getString("targetGroupName"));
            }
            if (jsonObject.has("timestamp")) {
                dataEntity.setTimestamp(jsonObject.getLong("timestamp"));
            }
            if (jsonObject.has("targetUserIds")) {
                JSONArray jsonArray = jsonObject.getJSONArray("targetUserIds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    dataEntity.getTargetUserIds().add(jsonArray.getString(i));
                }
            }
            if (jsonObject.has("targetUserDisplayNames")) {
                JSONArray jsonArray = jsonObject.getJSONArray("targetUserDisplayNames");
                for (int i = 0; i < jsonArray.length(); i++) {
                    dataEntity.getTargetUserDisplayNames().add(jsonArray.getString(i));
                }
            }
            if (jsonObject.has("oldCreatorId")) {
                dataEntity.setOldCreatorId(jsonObject.getString("oldCreatorId"));
            }
            if (jsonObject.has("oldCreatorName")) {
                dataEntity.setOldCreatorName(jsonObject.getString("oldCreatorName"));
            }
            if (jsonObject.has("newCreatorId")) {
                dataEntity.setNewCreatorId(jsonObject.getString("newCreatorId"));
            }
            if (jsonObject.has("newCreatorName")) {
                dataEntity.setNewCreatorName(jsonObject.getString("newCreatorName"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataEntity;
    }
}

package com.jit.appcloud.ui.presenter;

import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IPostScriptAtView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ContactNotificationMessage;

/**
 * @author zxl on 2018/6/12.
 *         discription:
 */

public class PostScriptAtPresenter extends BasePresenter<IPostScriptAtView> {
    public PostScriptAtPresenter(BaseActivity context) {
        super(context);
    }
    public void addFriend(String friendName,String mFriendId) {
        String msg = getView().getEtMsg().getText().toString().trim();
        LogUtils.e("好友数据",friendName+"  "+msg);
        ApiRetrofit.getInstance().sendFriendInvitation(UserCache.getToken(),friendName,msg)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getCode() == 1){
                        UIUtils.showToast("请求已发送");
                        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_NEW_FRIEND);
                        /*  发送添加好友的请求 =======*/
                        ContactNotificationMessage requestMessage = ContactNotificationMessage.obtain(ContactNotificationMessage.CONTACT_OPERATION_REQUEST, UserCache.getId(), mFriendId, null);
                        RongIMClient.getInstance().sendMessage(Message.obtain(mFriendId, Conversation.ConversationType.PRIVATE, requestMessage), "", "", null, null);
                        mContext.finish();
                    }else {
                        UIUtils.showToast(response.getMsg());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

}

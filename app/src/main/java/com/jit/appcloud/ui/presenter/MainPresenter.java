package com.jit.appcloud.ui.presenter;

import android.content.Intent;
import android.text.TextUtils;
import com.jit.appcloud.R;
import com.jit.appcloud.app.MyApp;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.ui.activity.message.SessionActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.MainView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import io.rong.imlib.RongIMClient;

import static com.jit.appcloud.commom.AppConst.SESSION_TYPE_PRIVATE;

/**
 *
 * @author 张先磊
 * @date 2018/4/17
 *
 * OK MainPresenter与融云SDK有关
 *
 * 应该明确这里 只要你这个用户id在 那么信息都在的，OK了解了,注销也没问题的
 */

public class MainPresenter extends BasePresenter<MainView> {
    public MainPresenter(BaseActivity context) {
        super(context);
        // 当登录入主界面时,说明这时候已经登录了,那么这个时候需要建立连接了
        // 这个时候我写死了,按照道理来说需要从服务端获取的
        if (!TextUtils.isEmpty(UserCache.getToken())) {
            connect(UserCache.getRongToken());
        }
        DBManager.getInstance().getAllUserInfo();
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {
        LogUtils.e("广播","-----connect 执行了-----------");

        if (UIUtils.getContext().getApplicationInfo().packageName.equals(MyApp.getCurProcessName(UIUtils.getContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    LogUtils.e("融云","--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    LogUtils.e("融云","连接成功"+userid);
                    // 发送广播来更新会话
                    BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                    if (mContext.getIntent().hasExtra("open")){
                        Intent intent = new Intent(mContext,SessionActivity.class);
                        intent.putExtra("sessionId",mContext.getIntent().getStringExtra("sessionId"));
                        intent.putExtra("sessionType", mContext.getIntent().getIntExtra("sessionType",SESSION_TYPE_PRIVATE));
                        mContext.jumpToActivity(intent);
                    }
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtils.e("融云",errorCode.getValue()+" ");
                    UIUtils.showToast(UIUtils.getString(R.string.disconnect_server));
                }
            });
        }
    }
}

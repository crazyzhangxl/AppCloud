package com.jit.appcloud.ui.activity.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.Friend;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.message.DeleteContactMessage;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.lqr.optionitemview.OptionItemView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;

/**
 * @author zxl on 2018/06/11.
 *         discription:  通过用户名获取的用户信息的界面
 *         用于添加好友和发送信息的!!
 */
public class UserInfoBySearchActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.ivHeader)
    ImageView mIvHeader;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.ivGender)
    ImageView mIvGender;
    @BindView(R.id.tvAccount)
    TextView mTvAccount;
    @BindView(R.id.tvNickName)
    TextView mTvNickName;
    @BindView(R.id.oivAliasAndTag)
    OptionItemView mOivAliasAndTag;
    @BindView(R.id.tvArea)
    TextView mTvArea;
    @BindView(R.id.llArea)
    LinearLayout mLlArea;
    @BindView(R.id.tvSignature)
    TextView mTvSignature;
    @BindView(R.id.llSignature)
    LinearLayout mLlSignature;
    @BindView(R.id.btnCheat)
    Button mBtnCheat;
    @BindView(R.id.btnAddToContact)
    Button mBtnAddToContact;
    @BindView(R.id.ibToolbarMore)
    ImageButton mIbToolbarMore;
    @BindView(R.id.oivAlias)
    OptionItemView mOivAlias;
    @BindView(R.id.oivDelete)
    OptionItemView mOivDelete;
    @BindView(R.id.svMenu)
    ScrollView mSvMenu;
    @BindView(R.id.rlMenu)
    RelativeLayout mRlMenu;

    private UserInfo mUserInfo;
    private Friend mFriend;
    private String mSighature;
    private String mArea;

    @Override
    protected void init() {
        /* 获得上个活动传递过来的用户信息*/
        Intent intent = getIntent();
        mUserInfo =  intent.getExtras().getParcelable(AppConst.EXTRA_FRIEND_INFO);
        mSighature = intent.getExtras().getString(AppConst.EXTRA_SIGNATURE);
        mArea = intent.getExtras().getString(AppConst.EXTRA_AREA);
        registerBR();

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user_info_by_search;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mTvToolbarTitle.setText("详细资料");
        mIbToolbarMore.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        mFriend = DBManager.getInstance().getFriendById(mUserInfo.getUserId());
        Glide.with(this).load(DBManager.getInstance().getPortraitUri(mUserInfo)).into(mIvHeader);
        mTvAccount.setText(UIUtils.getString(R.string.my_chat_account,mUserInfo.getName()));
        mTvName.setText(mUserInfo.getName());
        if (!TextUtils.isEmpty(mSighature)) {
            mTvSignature.setText(mSighature);
        }
        if (!TextUtils.isEmpty(mArea)) {
            mTvArea.setText(mArea);
        }

        /* 陌生人且是用户那么就添加好友 否则说明是我 好友对的吧*/
        if (mFriend == null) {
            if (mUserInfo != null && UserCache.getId().equals(mUserInfo.getUserId())){
                mTvNickName.setVisibility(View.INVISIBLE);
                mIbToolbarMore.setVisibility(View.GONE);
                mOivAliasAndTag.setVisibility(View.GONE);
                mBtnAddToContact.setVisibility(View.GONE);
                mBtnCheat.setVisibility(View.GONE);
            }else {
                mBtnCheat.setVisibility(View.GONE);
                mBtnAddToContact.setVisibility(View.VISIBLE);
                mTvNickName.setVisibility(View.INVISIBLE);
                mIbToolbarMore.setVisibility(View.GONE);
                mOivAliasAndTag.setVisibility(View.GONE);
            }
        } else {
          if (DBManager.getInstance().isMyFriend(mFriend.getUserId())) {// 好友
                String nickName = mFriend.getDisplayName();
                mTvName.setText(nickName);
                if (TextUtils.isEmpty(nickName)) {
                    mTvNickName.setVisibility(View.INVISIBLE);
                } else {
                    mTvNickName.setText(UIUtils.getString(R.string.nickname_colon, mFriend.getName()));
                }
            } else {
                //陌生人
                mBtnCheat.setVisibility(View.GONE);
                mBtnAddToContact.setVisibility(View.VISIBLE);
                mTvNickName.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mOivAliasAndTag.setOnClickListener(v -> jumpToSetAlias());

        mIbToolbarMore.setOnClickListener(v -> showMenu());

        /* 添加好友 */
        mBtnAddToContact.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoBySearchActivity.this, PostScriptActivity.class);
            intent.putExtra(AppConst.EXTRA_FRIEND_NAME, mUserInfo.getName());
            intent.putExtra(AppConst.EXTRA_FRIEND_ID, mUserInfo.getUserId());
            jumpToActivity(intent);
            finish();
            // 添转并且结束该活动
        });

        /* 发送消息 */
        mBtnCheat.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoBySearchActivity.this, SessionActivity.class);
            intent.putExtra("sessionId", String.valueOf(mUserInfo.getUserId()));// ================
            intent.putExtra("sessionType", AppConst.SESSION_TYPE_PRIVATE);
            jumpToActivity(intent);
            finish();
        });

        mOivAlias.setOnClickListener(v -> {
            jumpToSetAlias();
            hideMenu();
        });

        mRlMenu.setOnClickListener(v -> hideMenu());

        mOivDelete.setOnClickListener(v -> {
            hideMenu();
            showMaterialDialog(UIUtils.getString(R.string.delete_contact),
                    UIUtils.getString(R.string.delete_contact_content, mUserInfo.getName()),
                    UIUtils.getString(R.string.delete),
                    UIUtils.getString(R.string.cancel),
                    (dialog, which) -> ApiRetrofit.getInstance()
                            .deleteFriendByName(UserCache.getToken(), mUserInfo.getName())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(deleteFriendResponse -> {
                                UserInfoBySearchActivity.this.hideMaterialDialog();
                                if (deleteFriendResponse.getCode() == 1) {
                                    // 删除好友
                                    DBManager.getInstance().deleteFriendById(mUserInfo.getUserId());
                                    /* id是好友啊*/
                                    RongIMClient.getInstance().getConversation(Conversation.ConversationType.PRIVATE, mUserInfo.getUserId(), new RongIMClient.ResultCallback<Conversation>() {
                                        @Override
                                        public void onSuccess(Conversation conversation) {
                                            RongIMClient.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, mUserInfo.getUserId(), new RongIMClient.ResultCallback<Boolean>() {
                                                @Override
                                                public void onSuccess(Boolean aBoolean) {
                                                    LogUtils.e("删除123", "====删除回调成功了的!");
                                                    RongIMClient.getInstance().removeConversation(Conversation.ConversationType.PRIVATE,null);
                                                    RongIMClient.getInstance().deleteMessages(Conversation.ConversationType.PRIVATE, mUserInfo.getUserId(),null);
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
                                    //通知对方被删除(把我的id发给对方)
                                    DeleteContactMessage deleteContactMessage = DeleteContactMessage.obtain(UserCache.getId());
                                    RongIMClient.getInstance().sendMessage(Message.obtain(mUserInfo.getUserId(), Conversation.ConversationType.PRIVATE, deleteContactMessage), "", "", null, null);
                                    /* 删除并且更新联系人*/
                                    BroadcastManager.getInstance(UserInfoBySearchActivity.this).sendBroadcast(AppConst.UPDATE_FRIEND);
                                    UIUtils.showToast(UIUtils.getString(R.string.delete_success));
                                    UserInfoBySearchActivity.this.finish();
                                } else {
                                    UIUtils.showToast(UIUtils.getString(R.string.delete_fail));
                                }
                            }, UserInfoBySearchActivity.this::loadError)
                    , (dialog, which) -> UserInfoBySearchActivity.this.hideMaterialDialog());
        });
    }

    private void loadError(Throwable throwable) {
        hideMaterialDialog();
        UIUtils.showToast(throwable.getLocalizedMessage());
        LogUtils.e("删除失败",throwable.getLocalizedMessage());
    }

    private void jumpToSetAlias(){
        /* ===== 将好友姓名传递过去 =======*/
        Intent intent = new Intent(this, SetAliasActivity.class);
        intent.putExtra(AppConst.EXTRA_FRIEND_NAME, mUserInfo.getName());
        intent.putExtra(AppConst.EXTRA_FRIEND_ID, mUserInfo.getUserId());
        jumpToActivity(intent);
    }

    private void showMenu() {
        mRlMenu.setVisibility(View.VISIBLE);
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(200);
        mSvMenu.startAnimation(ta);
    }

    /**
     * 隐藏菜单
     */
    private void hideMenu() {
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mRlMenu.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ta.setDuration(200);
        mSvMenu.startAnimation(ta);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBR();
    }

    private void registerBR() {
        /* 该用户信息修改的广播*/
        BroadcastManager.getInstance(this).register(AppConst.CHANGE_INFO_FOR_USER_INFO, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mUserInfo = DBManager.getInstance().getUserInfo(mUserInfo.getUserId());
                initData();
            }
        });

    }

    private void unRegisterBR() {
        BroadcastManager.getInstance(this).unregister(AppConst.CHANGE_INFO_FOR_USER_INFO);
    }

}

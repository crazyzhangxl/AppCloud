package com.jit.appcloud.ui.activity;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import com.jit.appcloud.R;
import com.jit.appcloud.app.MyApp;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.GroupMember;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.ui.activity.message.SessionActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.FragmentFactory;
import com.jit.appcloud.ui.fragment.CultivateFragment;
import com.jit.appcloud.ui.fragment.MessageFragment;
import com.jit.appcloud.ui.fragment.NewsFragment;
import com.jit.appcloud.ui.fragment.MeFragment;
import com.jit.appcloud.ui.fragment.center.CtAmUserFragment;
import com.jit.appcloud.ui.fragment.center.CtFarmLogInFragment;
import com.jit.appcloud.ui.fragment.center.CtMgUserFragment;
import com.jit.appcloud.ui.presenter.MainPresenter;
import com.jit.appcloud.ui.view.MainView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.NotificationUtils;
import com.jit.appcloud.util.UIUtils;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;


import butterknife.BindView;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * @author zxl
 */
public class MainActivity extends BaseActivity<MainView, MainPresenter> {
    @BindView(R.id.fg_main)
    FrameLayout mFgMain;
    private SupportFragment[] mFragments = new SupportFragment[5];
    public static final int TIME = 0;
    public static final int FAVOURITE = 1;
    public static final int CENTER = 2;  // 中间的Center
    public static final int ADDRESS = 3;
    public static final int ME = 4;
    public static final int EXIT_SECONDS = 2000;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    private long exitTime;
    private int prePosition;
    private BottomBarTab mBTabFirst;// 第一个界面撒

    @Override
    protected void initListener() {
        mBottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_recents:
                    showHideFragment(mFragments[0], mFragments[prePosition]);
                    prePosition = 0;
                    break;
                case R.id.tab_favorites:
                    showHideFragment(mFragments[1], mFragments[prePosition]);
                    prePosition = 1;
                    break;
                case R.id.tab_center:
                    showHideFragment(mFragments[2], mFragments[prePosition]);
                    prePosition = 2;
                    break;
                case R.id.tab_nearby:
                    showHideFragment(mFragments[3], mFragments[prePosition]);
                    prePosition = 3;
                    break;
                case R.id.tab_friends:
                    showHideFragment(mFragments[4], mFragments[prePosition]);
                    prePosition = 4;
                    break;
                default:
                    break;
            }
        });

    }

    /**
     *  用于从客户界面转向检测界面 ---------------
     */
    public void turnToCul(){
        mBottomBar.onClick(mBTabFirst);
    }

    @Override
    protected void initData() {

    }

    /**
     * 设置信息未读取的数量 -------
     * @param count
     */
    public void setUnReadCont(int count){
        BottomBarTab nearby = mBottomBar.getTabWithId(R.id.tab_nearby);
        if (count != 0) {
            nearby.setBadgeCount(count);
        }else {
            nearby.removeBadge();
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        BottomBarTab bTabCenter = mBottomBar.findViewById(R.id.tab_center);
        mBTabFirst = mBottomBar.findViewById(R.id.tab_recents);
        BottomBarTab bTabNearBy =  mBottomBar.findViewById(R.id.tab_nearby);
        AppCompatImageView ivCenter = bTabCenter.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_icon);
        AppCompatImageView ivFirst = mBTabFirst.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_icon);
        if (AppConst.ROLE_AGENCY.equals(UserCache.getRole())){
            // 经销商
            bTabCenter.setTitle("客户");
            ivCenter.setImageResource(R.drawable.ic_main_center_user);
        }else if (AppConst.ROLE_FARMER.equals(UserCache.getRole())){
            // 养殖户
            bTabCenter.setTitle("录入");
            ivCenter.setImageResource(R.drawable.ic_main_center_fm);
        }else if (AppConst.ROLE_GENERAL_AGENCY.equals(UserCache.getRole()) || AppConst.ROLE_AGENT.equals(UserCache.getRole())){
            // 总代理
            bTabCenter.setTitle("注册");
            ivCenter.setImageResource(R.drawable.ic_main_center_rg);
            mBTabFirst.setTitle("客户");
            ivFirst.setImageResource(R.drawable.ic_main_center_user);
        }else if (AppConst.ROLE_VICE_MANAGER.equals(UserCache.getRole())){
            // 平行经销商
            bTabCenter.setTitle("客户");
            ivCenter.setImageResource(R.drawable.ic_main_center_user);
            bTabNearBy.setVisibility(View.GONE);
        }else  {
            // 平行总部/平行总代
            bTabNearBy.setVisibility(View.GONE);
            bTabCenter.setVisibility(View.GONE);
        }

        if (savedInstanceState == null) {
            mFragments[TIME] = FragmentFactory.getInstance().getCultivateFragment();
            mFragments[FAVOURITE] = FragmentFactory.getInstance().getNewsFragment();
            mFragments[ME] = FragmentFactory.getInstance().getMeFragment();
            if (AppConst.ROLE_FARMER.equals(UserCache.getRole())){
                // 养殖户
                mFragments[ADDRESS] = FragmentFactory.getInstance().getMessageFragment();
                mFragments[CENTER] = FragmentFactory.getInstance().getCtFarmLogInFragment();
                loadMultipleRootFragment(R.id.fg_main,TIME,
                        mFragments[TIME],
                        mFragments[FAVOURITE],
                        mFragments[CENTER],
                        mFragments[ADDRESS],
                        mFragments[ME]);
            }else if (AppConst.ROLE_AGENCY.equals(UserCache.getRole())){
                // 经销商
                mFragments[ADDRESS] = FragmentFactory.getInstance().getMessageFragment();
                mFragments[CENTER] = FragmentFactory.getInstance().getCtMgUserFragment();
                loadMultipleRootFragment(R.id.fg_main, TIME,
                        mFragments[TIME],
                        mFragments[FAVOURITE],
                        mFragments[CENTER],
                        mFragments[ADDRESS],
                        mFragments[ME]);
            } else if (AppConst.ROLE_GENERAL_AGENCY.equals(UserCache.getRole())
                    || AppConst.ROLE_AGENT.equals(UserCache.getRole())){
                // 总部 总代
                mFragments[ADDRESS] = FragmentFactory.getInstance().getMessageFragment();
                mFragments[CENTER] = FragmentFactory.getInstance().getCtAmUserFragment();
                loadMultipleRootFragment(R.id.fg_main,ME,
                        mFragments[TIME],
                        mFragments[FAVOURITE],
                        mFragments[CENTER],
                        mFragments[ADDRESS],
                        mFragments[ME]);
            }else if (AppConst.ROLE_VICE_ADMIN.equals(UserCache.getRole())
                    || AppConst.ROLE_VICE_AGENT.equals(UserCache.getRole())){
                // 平行总部/总代 参数二指代后面第几个 3
                loadMultipleRootFragment(R.id.fg_main,CENTER,
                        mFragments[TIME],
                        mFragments[FAVOURITE],
                        mFragments[ME]);
            }else {
                // 平行经销(显示第1个)
                mFragments[CENTER] = FragmentFactory.getInstance().getCtMgUserFragment();
                loadMultipleRootFragment(R.id.fg_main,TIME,
                        mFragments[TIME],
                        mFragments[FAVOURITE],
                        mFragments[CENTER],
                        mFragments[ME]);
            }
        } else {
            // 这里我们需要拿到mFragments的引用
            // ,也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[TIME] = findFragment(CultivateFragment.class);
            mFragments[FAVOURITE] = findFragment(NewsFragment.class);
            mFragments[ME] = findFragment(MeFragment.class);
            if (AppConst.ROLE_AGENCY.equals(UserCache.getRole())) {
                mFragments[CENTER] = findFragment(CtMgUserFragment.class);
                mFragments[ADDRESS] = findFragment(MessageFragment.class);
            }else if (AppConst.ROLE_FARMER.equals(UserCache.getRole())){
                mFragments[CENTER] = findFragment(CtFarmLogInFragment.class);
                mFragments[ADDRESS] = findFragment(MessageFragment.class);
            }else if (AppConst.ROLE_GENERAL_AGENCY.equals(UserCache.getRole())
                    ||AppConst.ROLE_AGENT.equals(UserCache.getRole())){
                mFragments[CENTER] = findFragment(CtAmUserFragment.class);
                mFragments[ADDRESS] = findFragment(MessageFragment.class);
            }else if (AppConst.ROLE_VICE_MANAGER.equals(UserCache.getRole())){
                // 经销平行用户
                mFragments[CENTER] = findFragment(CtMgUserFragment.class);
            }
        }

        if (AppConst.ROLE_FARMER.equals(UserCache.getRole())
                || AppConst.ROLE_AGENCY.equals(UserCache.getRole())
                || AppConst.ROLE_VICE_MANAGER.equals(UserCache.getRole())
                ){
            mBottomBar.setDefaultTab(R.id.tab_recents);
            prePosition = 0;
        }else if (AppConst.ROLE_GENERAL_AGENCY.equals(UserCache.getRole())
                || AppConst.ROLE_AGENT.equals(UserCache.getRole())
                || AppConst.ROLE_VICE_ADMIN.equals(UserCache.getRole())
                || AppConst.ROLE_VICE_AGENT.equals(UserCache.getRole())
                ){
            mBottomBar.setDefaultTab(R.id.tab_friends);
            prePosition = 4;
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(mContext).register(AppConst.UPDATE_CURRENT_SESSION, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Message message = intent.getParcelableExtra("result");
                if (message != null) {
                    // 判断是好友或者群发过来的 ===== 这个晚点再说
                    // 是否开启消息免打扰
                    if (UserCache.getMsgNotify()) {
                        // 只有不等于才会进行通知的 ----
                        if (!AppConst.SESSION_TOP_NAME.equals(MyApp.topActivityName())) {
                            MessageContent content = message.getContent();
                            String sedMsg;
                            if (content instanceof TextMessage) {
                                sedMsg = ((TextMessage) content).getContent();
                            } else if (content instanceof ImageMessage) {
                                sedMsg = "[图片]";
                            } else if (content instanceof FileMessage) {
                                sedMsg = "[文件]";
                            } else if (content instanceof VoiceMessage) {
                                sedMsg = "[语音]";
                            }else {
                                sedMsg = "[未识别信息]";
                            }
                            LogUtils.e("当前",sedMsg);
                            String sendID = message.getTargetId();
                            LogUtils.e("当前","检查是好友ID 还是群号ID: ======"+sendID);
                            LogUtils.e("当前","检查发送者ID"+message.getSenderUserId());
                            Conversation.ConversationType type = message.getConversationType();
                            String title = "";
                            String sendName = "";
                            if (type == Conversation.ConversationType.PRIVATE){
                                sendName =  DBManager.getInstance().getFriendNameByID(sendID);
                                title = sendName;
                            }else {
                                // 群组消息
                                GroupMember groupMember = DBManager.getInstance()
                                        .getGroupMemberById(message.getTargetId(), message.getSenderUserId());
                                if (groupMember != null) {
                                    title = groupMember.getGroupName();
                                    sendName = groupMember.getDisplayName();
                                }
                            }

                            if (!TextUtils.isEmpty(sendName)) {
                                LogUtils.e("当前", sendName + " " + sendID);
                                new NotificationUtils.Build(MainActivity.this)
                                        .setsendTitle(title)
                                        .setSendId(sendID)
                                        .setSendName(sendName)
                                        .setConType(type)
                                        .setSendMsg(sedMsg)
                                        .build();
                            }

                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(mContext).unregister(AppConst.UPDATE_CURRENT_SESSION);
    }

    @Override
    public void onBackPressedSupport() {
        if ((System.currentTimeMillis() - exitTime) > EXIT_SECONDS) {
            UIUtils.showToast("再按一次退出该应用");
            exitTime = System.currentTimeMillis();
            return;
        }
        super.onBackPressedSupport();
    }
}

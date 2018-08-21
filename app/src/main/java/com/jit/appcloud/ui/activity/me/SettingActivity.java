package com.jit.appcloud.ui.activity.me;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.allen.library.SuperTextView;
import com.jit.appcloud.R;
import com.jit.appcloud.app.MyApp;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserNormalCache;
import com.jit.appcloud.model.cache.UserOtherCache;
import com.jit.appcloud.ui.activity.LoginActivity;
import com.jit.appcloud.ui.activity.message.QRCodeCardActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.SettingAtPresenter;
import com.jit.appcloud.ui.view.ISettingAtView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.CustomDialog;

import org.litepal.util.LogUtil;

import java.io.File;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;

public class SettingActivity extends BaseActivity<ISettingAtView, SettingAtPresenter> {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.llToolbarTitle)
    LinearLayout mLlToolbarTitle;
    @BindView(R.id.flToolBar)
    FrameLayout mFlToolBar;
    @BindView(R.id.stv_notify_ps)
    SuperTextView mStvNotifyPs;
    @BindView(R.id.stv_about_cloud)
    SuperTextView mStvAboutCloud;
    @BindView(R.id.stv_logout)
    SuperTextView mStvLogout;

    private View mExitView;
    private CustomDialog mExitDialog;
    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mStvNotifyPs.setSwitchCheckedChangeListener((compoundButton, b) -> UserCache.setMsgNotify(b));
        mStvLogout.setOnSuperTextViewClickListener(superTextView -> {
            if (mExitView == null){
                mExitView = View.inflate(mContext,R.layout.dialog_exit,null);
                mExitDialog = new CustomDialog(mContext,mExitView,R.style.MyDialog);
                mExitView.findViewById(R.id.tvExitAccount).setOnClickListener((View v) -> {
                    /*RongIMClient.getInstance().logout();*/
                    mExitDialog.dismiss();
                    showWaitingDialog(getString(R.string.str_please_waiting));
                    Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                        UserCache.clear();
                        UserNormalCache.clear();
                        UserOtherCache.clear();
                        deleteFile();
                        emitter.onNext(true);
                        emitter.onComplete();
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aBoolean -> {
                                if (aBoolean){
                                    SettingActivity.this.hideWaitingDialog();
                                    MyApp.exit();
                                    SettingActivity.this.jumpToActivityAndClearTask(LoginActivity.class);
                                }
                            });
                });

                mExitView.findViewById(R.id.tvExitApp).setOnClickListener(v -> {
                    RongIMClient.getInstance().disconnect();
                    mExitDialog.dismiss();
                    MyApp.exit();
                });
            }
            mExitDialog.show();
        });

        mStvAboutCloud.setOnSuperTextViewClickListener(superTextView -> {
        });
    }

    private void deleteFile(){
        File appDir = new File(Environment.getExternalStorageDirectory(), AppConst.PS_SAVE_LEAST_DIR);
        if (appDir.exists()) {
            File[] files = appDir.listFiles();
            if (files != null) {
                for (int i=0;i<files.length;i++) {
                    files[i].delete();
                }
            }
            appDir.delete();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("设置");
        mStvNotifyPs.setSwitchIsChecked(UserCache.getMsgNotify());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {

    }

    @Override
    protected SettingAtPresenter createPresenter() {
        return new SettingAtPresenter(this);
    }


}

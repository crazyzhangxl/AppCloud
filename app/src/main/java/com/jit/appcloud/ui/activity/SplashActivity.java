package com.jit.appcloud.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jit.appcloud.R;
import com.jit.appcloud.app.MyApp;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.Check;
import com.jit.appcloud.util.ExcelUtils;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import java.sql.Time;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.jit.appcloud.commom.AppConst.SESSION_TYPE_PRIVATE;


/**
 * @author zxl
 */
@RuntimePermissions
public class SplashActivity extends BaseActivity {

    @BindView(R.id.tv_version_name)
    TextView mTvVersionName;
    @BindView(R.id.rl_splash)
    RelativeLayout mRlSplash;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private AlphaAnimation mAlphaAnimation;
    private boolean mIsNotLogin;
    private ScheduledExecutorService mScheduledService;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initAnimation();
        SplashActivityPermissionsDispatcher.requestNormalInfoWithCheck(this);
    }

    private void initAnimation() {
        mAlphaAnimation = new AlphaAnimation(0, 1);
        mAlphaAnimation.setDuration(1500);//设置时间
        mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mTvVersionName.setText("版本名称:"+UIUtils.getVersionName());
                if (mProgressBar.getVisibility() == View.INVISIBLE){
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mIsNotLogin = Check.isEmpty(UserCache.getToken());
                LogUtils.e("身份",UserCache.getId()+" ");
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                // ========= 用于测试 =============
                // 然后这里就需要判断啦
                updateApp();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        PgyUpdateManager.setIsForced(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unregister();
    }

    private void login(){
        if (mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        if (AppConst.isTesting){
            if (TextUtils.isEmpty(UserCache.getToken())) {
                UserCache.save("11111",
                        "zxl",
                        AppConst.ROLE_AGENCY,
                        "Bearer " + "111",
                        null,null,null,null);
            }
            jumpToActivity(MainActivity.class);
            finish();
            return;
        }
        if (mIsNotLogin){
            // token 为空需要登录
            jumpToActivity(LoginActivity.class);
            finish();
            MyApp.activities.remove(this);
        }else {
            // 含有token 进入主界面
            LogUtils.e("检测更新","执行跳转啊");
            Intent intent = new Intent(this,MainActivity.class);
            if (getIntent().hasExtra("open")){
                intent.putExtra("sessionId",getIntent().getStringExtra("sessionId"));// ================
                intent.putExtra("sessionType", getIntent().getIntExtra("sessionType",SESSION_TYPE_PRIVATE));
                intent.putExtra("open","open");
            }
            jumpToActivity(intent);
            finish();
            MyApp.activities.remove(this);
        }
    }

    private void updateApp(){
        mScheduledService = Executors.newScheduledThreadPool(1);
        mScheduledService.schedule(new Runnable() {
            @Override
            public void run() {
                SplashActivity.this.runOnUiThread(() -> SplashActivity.this.login());
            }
        }, 3, TimeUnit.SECONDS);
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                LogUtils.e("检测更新","========无更新=========");
            }

            @Override
            public void onUpdateAvailable(String s) {
                mScheduledService.shutdownNow();
                mScheduledService.shutdown();
                mProgressBar.setVisibility(View.INVISIBLE);
                AppBean appBean = getAppBeanFromString(s);
                showPSMaterialDialog("版本更新", "", "确定", "取消",
                        (MaterialDialog dialog, DialogAction which) ->
                        {
                            dialog.dismiss();
                            startDownloadTask(SplashActivity.this, appBean.getDownloadURL());
                        }, (dialog, which) -> {
                            dialog.dismiss();
                            login();
                        });
            }
        });

    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
    /**
     *=========================== 申请运行时权限开始 ==============================
     **/


    @NeedsPermission({Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.ACCESS_FINE_LOCATION
    })
    void requestNormalInfo() {
        mRlSplash.startAnimation(mAlphaAnimation);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.ACCESS_FINE_LOCATION})
    void infoShowRT(final PermissionRequest request) {
        showPSMaterialDialog(null, getPermissionTips()+"权限需要您的授权", "确定", "取消",
                (dialog, which) -> {
                    dialog.dismiss();
                    request.proceed();
                }, (dialog, which) -> {
                    dialog.dismiss();
                    request.cancel();
                });
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.ACCESS_FINE_LOCATION
    })

    void infoPSDenied() {

        showPSMaterialDialog("权限说明", "未取得"+getPermissionTips()+"的使用权限，云联无法开启。", "赋予权限", "", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                SplashActivityPermissionsDispatcher.requestNormalInfoWithCheck(SplashActivity.this);
            }
        },null);
    }

    @OnNeverAskAgain({Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.ACCESS_FINE_LOCATION})
    void infoNever() {
        showPSMaterialDialog(null, "未取得"+getPermissionTips()+"的使用权限，云联无法开启。请前往应用权限设置打开权限", "去打开", "", (dialog, which) -> {
            dialog.dismiss();
            getAppDetailSettingIntent();
        },null);
    }



    private String getPermissionTips(){
        StringBuilder mStringBuilder = new StringBuilder();
        if (lackPermission(Manifest.permission.READ_PHONE_STATE)){
            mStringBuilder.append("手机信息");
        }
        if (lackPermission(Manifest.permission.READ_EXTERNAL_STORAGE) || lackPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            if (TextUtils.isEmpty(mStringBuilder.toString())) {
                mStringBuilder.append("读写存储");
            }else {
                mStringBuilder.append(" 读写存储");
            }
        }

        if (lackPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            if (TextUtils.isEmpty(mStringBuilder.toString())) {
                mStringBuilder.append("定位");
            }else {
                mStringBuilder.append(" 定位");
            }
        }
        return mStringBuilder.toString();
    }

    /**
     * 用于从权限设置界面返回时的对权限进行的检查

     * */
    @Override
    protected void onRestart() {
        super.onRestart();
        SplashActivityPermissionsDispatcher.requestNormalInfoWithCheck(SplashActivity.this);
    }

    // 打开权限执行了一次onStop（）
    /*
     *  =========================== 申请运行时权限结束 ==============================
    */
}

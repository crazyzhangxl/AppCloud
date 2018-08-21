package com.jit.appcloud.ui.activity;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.LoginAtPresenter;
import com.jit.appcloud.ui.view.ILoginAtView;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.CycleInterpolator;
import com.jit.appcloud.widget.EditTextWithDel;

import butterknife.BindView;
/**
 * @author 张先磊
 */
public class LoginActivity extends BaseActivity<ILoginAtView, LoginAtPresenter> implements ILoginAtView {
    @SuppressWarnings("SpellCheckingInspection")
    @BindView(R.id.loginusericon)
    ImageView mLoginusericon;
    @BindView(R.id.userph)
    EditTextWithDel mUserName;
    @SuppressWarnings("SpellCheckingInspection")
    @BindView(R.id.rela_name)
    RelativeLayout mRelaName;
    @SuppressWarnings("SpellCheckingInspection")
    @BindView(R.id.codeicon)
    ImageView mCodeicon;
    @BindView(R.id.userpass)
    EditTextWithDel mUserPass;
    @BindView(R.id.rela_pass)
    RelativeLayout mRelaPass;
    @BindView(R.id.bt_login)
    Button mBtLogin;
    @SuppressWarnings("SpellCheckingInspection")
    @BindView(R.id.tv_forgetcode)
    TextView mTvForgetcode;
    @BindView(R.id.iv_weibo)
    ImageView mIvWeibo;
    @BindView(R.id.iv_qq)
    ImageView mIvQq;
    @BindView(R.id.iv_weixin)
    ImageView mIvWeiXin;
    @BindView(R.id.llContent)
    LinearLayout mLlRoot;
    private boolean isFirst = true;
    private int mButtonHeight;
    private ViewTreeObserver mTreeObserver;
    private ViewTreeObserver.OnGlobalLayoutListener mListener;

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView(Bundle savedInstanceState) {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        buttonBeyondKeyboardLayout(mLlRoot,mBtLogin);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCompat.animate(v)
                        .setDuration(400)
                        .scaleY(0.9f)
                        .scaleY(0.9f)
                        .setInterpolator(new CycleInterpolator())
                        .setListener(new ViewPropertyAnimatorListener() {
                            @Override
                            public void onAnimationStart(View view) {

                            }

                            @Override
                            public void onAnimationEnd(View view) {
                                mPresenter.login();
                            }

                            @Override
                            public void onAnimationCancel(View view) {

                            }
                        })
                        .withLayer()
                        .start();
            }
        });
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mRelaName.setBackground(UIUtils.getDrawable(R.drawable.bg_border_color_black));
            }
        });

        mUserPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mRelaPass.setBackground(UIUtils.getDrawable(R.drawable.bg_border_color_black));
            }
        });
    }

    @Override
    protected LoginAtPresenter createPresenter() {
        return new LoginAtPresenter(this);
    }

    @Override
    public ImageView getUserNameIcon() {
        return mLoginusericon;
    }

    @Override
    public ImageView getUserPsdIcon() {
        return mCodeicon;
    }

    @Override
    public EditTextWithDel getUserNameText() {
        return mUserName;
    }

    @Override
    public EditTextWithDel getUserPsdText() {
        return mUserPass;
    }

    @Override
    public RelativeLayout getRealName() {
        return mRelaName;
    }

    @Override
    public RelativeLayout getRealPsd() {
        return mRelaPass;
    }

    @Override
    protected void onDestroy() {
        if (mListener != null && mTreeObserver != null && mTreeObserver.isAlive()) {
            mTreeObserver.removeOnGlobalLayoutListener(mListener);
        }
        super.onDestroy();
    }

    private void buttonBeyondKeyboardLayout(final View root, final View button) {
        // 监听根布局的视图变化
        // 获取内容布局在窗体的可视区域
        // 获取内容布局在窗体的不可视区域高度(被其他View遮挡的区域高度)
        // top = 48 = 24*2 即为去除状态栏之外的区域
        // 若不可视区域高度大于100，则键盘显示
        // 获取须顶上去的控件在窗体的坐标
        // 计算root滚动高度，使scrollToView在可见区域
        /* scrollTo并未改变原来的位置
        *  貌似OnLayout可以
        * */// 键盘隐藏
        mListener = () -> {
            Rect rect = new Rect();
            // 获取内容布局在窗体的可视区域
            root.getWindowVisibleDisplayFrame(rect);
            // 获取内容布局在窗体的不可视区域高度(被其他View遮挡的区域高度)
            // top = 48 = 24*2 即为去除状态栏之外的区域
            int rootInvisibleHeight = root.getHeight() - rect.bottom;
            // 若不可视区域高度大于100，则键盘显示
            if (rootInvisibleHeight > 100) {
                int[] location = new int[2];
                // 获取须顶上去的控件在窗体的坐标
                button.getLocationInWindow(location);
                // 计算root滚动高度，使scrollToView在可见区域
                    /* scrollTo并未改变原来的位置
                    *  貌似OnLayout可以
                    * */
                int buttonHeight = button.getHeight() + location[1];
                // 判断登录按钮是否包含在可是区域内
                if (rect.bottom > buttonHeight){
                    if (mListener != null && mTreeObserver.isAlive()) {
                        mTreeObserver.removeOnGlobalLayoutListener(mListener);
                    }
                    mListener = null;
                } else {
                    if (isFirst) {
                        mButtonHeight = (buttonHeight - rect.bottom + UIUtils.px2dip(20));
                        isFirst = false;
                    }
                    root.scrollTo(0, mButtonHeight);
                }


            } else {
                // 键盘隐藏
                root.scrollTo(0, 0);
                isFirst = true;
            }
        };
        mTreeObserver = root.getViewTreeObserver();
                mTreeObserver.addOnGlobalLayoutListener(mListener);
    }
}

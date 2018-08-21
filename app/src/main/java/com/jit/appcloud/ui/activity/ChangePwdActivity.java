package com.jit.appcloud.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jit.appcloud.R;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.ChangePwdPresenter;
import com.jit.appcloud.ui.view.IChangePwdAtView;
import com.jit.appcloud.util.UIUtils;
import butterknife.BindView;

/**
 * @author zxl on 2018/5/9 9:35.
 *         discription: 修改密码的活动
 */
public class ChangePwdActivity extends BaseActivity<IChangePwdAtView,ChangePwdPresenter> implements IChangePwdAtView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.etPwd)
    EditText mEtPwd;
    @BindView(R.id.vLineOldPwd)
    View mVLineOldPwd;
    @BindView(R.id.etOldPwd)
    EditText mEtOldPwd;
    @BindView(R.id.ivSeePwd)
    ImageView mIvSeePwd;
    @BindView(R.id.vLinePwd)
    View mVLinePwd;
    @BindView(R.id.etCfmPwd)
    EditText mEtCfmPwd;
    @BindView(R.id.ivSeeCfmPwd)
    ImageView mIvSeeCfmPwd;
    @BindView(R.id.vLineCfmPwd)
    View mVLineCfmPwd;
    @BindView(R.id.btnConfirm)
    Button mBtnConfirm;

    TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mBtnConfirm.setEnabled(canChange());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected ChangePwdPresenter createPresenter() {
        return new ChangePwdPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(UIUtils.getString(R.string.str_title_change_pwd));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mBtnConfirm.setOnClickListener(v -> {
            if (mEtCfmPwd.getText().toString().trim().equals(mEtPwd.getText().toString().trim())){
                mPresenter.changePwd();
            }else {
                UIUtils.showToast(getString(R.string.pwd_input_twice_error));
            }
        });
        mEtOldPwd.addTextChangedListener(mWatcher);
        mEtPwd.addTextChangedListener(mWatcher);
        mEtCfmPwd.addTextChangedListener(mWatcher);


        mEtOldPwd.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                mVLineOldPwd.setBackgroundColor(UIUtils.getColor(R.color.orange_add_pond));
            }else {
                mVLineOldPwd.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        });

        mEtPwd.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                mVLinePwd.setBackgroundColor(UIUtils.getColor(R.color.orange_add_pond));
            }else {
                mVLinePwd.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        });

        mEtCfmPwd.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                mVLineCfmPwd.setBackgroundColor(UIUtils.getColor(R.color.orange_add_pond));
            }else {
                mVLineCfmPwd.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        });

        // 密码可见的状态改变事件
        mIvSeePwd.setOnClickListener(v -> {
            if (mEtPwd.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                // 由可见变为不可见
                mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mIvSeePwd.setImageResource(R.mipmap.ic_eye_normal);
            } else {
                // 由不可见变为可见
                mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mIvSeePwd.setImageResource(R.mipmap.ic_eye_pressed);
            }
            /* 光标移动到的位置*/
            mEtPwd.setSelection(mEtPwd.getText().toString().trim().length());
        });

        mIvSeeCfmPwd.setOnClickListener(v -> {
            if (mEtCfmPwd.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                mEtCfmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mIvSeeCfmPwd.setImageResource(R.mipmap.ic_eye_normal);
            } else {
                mEtCfmPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mIvSeeCfmPwd.setImageResource(R.mipmap.ic_eye_pressed);
            }
            /* 光标移动到的位置*/
            mEtCfmPwd.setSelection(mEtCfmPwd.getText().toString().trim().length());
        });

    }

    private boolean canChange(){
        int oldPwdLength = mEtOldPwd.getText().toString().trim().length();
        int psdLength = mEtPwd.getText().toString().trim().length();
        int confirmPsd = mEtCfmPwd.getText().toString().trim().length();
        return  oldPwdLength >0 &&  psdLength > 0 && confirmPsd > 0;
    }


    @Override
    public EditText getOldPwdText() {
        return mEtOldPwd;
    }

    @Override
    public EditText getConfirmPwdText() {
        return mEtCfmPwd;
    }
}

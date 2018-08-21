package com.jit.appcloud.ui.activity.me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.ViceUserAddAtPresenter;
import com.jit.appcloud.ui.view.IViceUserAddView;
import com.jit.appcloud.util.UIUtils;

import butterknife.BindView;

/**
 * @author zxl on 2018/5/14
 *         描述: 添加平行用户的活动 =========
 */
public class ViceUserAddActivity extends BaseActivity<IViceUserAddView, ViceUserAddAtPresenter> implements IViceUserAddView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.etUserName)
    EditText mEtUserName;
    @BindView(R.id.vUserName)
    View mVUserName;
    @BindView(R.id.etRealName)
    EditText mEtRealName;
    @BindView(R.id.vRealName)
    View mVRealName;
    @BindView(R.id.etUserPwd)
    EditText mEtUserPwd;
    @BindView(R.id.vUserPwd)
    View mVUserPwd;
    @BindView(R.id.etCfUserPwd)
    EditText mEtCfUserPwd;
    @BindView(R.id.vCfUserPwd)
    View mVCfUserPwd;
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
        return R.layout.activity_vice_user_add;
    }

    @Override
    protected ViceUserAddAtPresenter createPresenter() {
        return new ViceUserAddAtPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.title_add_pl_user);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mEtUserName.addTextChangedListener(mWatcher);
        mEtRealName.addTextChangedListener(mWatcher);
        mEtUserPwd.addTextChangedListener(mWatcher);
        mEtCfUserPwd.addTextChangedListener(mWatcher);
        mEtUserName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mVUserName.setBackgroundColor(UIUtils.getColor(R.color.orange_add_pond));
            } else {
                mVUserName.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        });

        mEtRealName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mVRealName.setBackgroundColor(UIUtils.getColor(R.color.orange_add_pond));
            } else {
                mVRealName.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        });

        mEtUserPwd.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mVUserPwd.setBackgroundColor(UIUtils.getColor(R.color.orange_add_pond));
            } else {
                mVUserPwd.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        });


        mEtCfUserPwd.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mVCfUserPwd.setBackgroundColor(UIUtils.getColor(R.color.orange_add_pond));
            } else {
                mVCfUserPwd.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        });

        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mBtnConfirm.setOnClickListener(v -> mPresenter.addViceUser());
    }

    private boolean canChange() {
        int userLength = mEtUserName.getText().toString().trim().length();
        int realNameLength = mEtRealName.getText().toString().trim().length();
        int pwdLength = mEtUserPwd.getText().toString().trim().length();
        int cPwdLength = mEtCfUserPwd.getText().toString().trim().length();
        return userLength > 0 && realNameLength > 0 && pwdLength > 0 && cPwdLength > 0;
    }

    @Override
    public EditText getUserName() {
        return mEtUserName;
    }

    @Override
    public EditText getUserRealName() {
        return mEtRealName;
    }

    @Override
    public EditText getPwdEt() {
        return mEtUserPwd;
    }

    @Override
    public EditText getPwdCfEt() {
        return mEtCfUserPwd;
    }


}

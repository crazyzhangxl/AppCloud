package com.jit.appcloud.ui.activity.me;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.bean.PersonalBean;
import com.jit.appcloud.model.request.AllocateLookUserRequest;
import com.jit.appcloud.model.response.NormalResponse;
import com.jit.appcloud.model.response.UserBBAMResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/07/16.
 *         discription: 平行用户修改活动
 */
public class ViceUserEditorActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.iv_save_two)
    ImageView mIvSave;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.etUserName)
    TextView mEtUserName;
    @BindView(R.id.vUserName)
    View mVUserName;
    @BindView(R.id.tvRealName)
    TextView mTvRealName;
    @BindView(R.id.etRealName)
    EditText mEtRealName;
    @BindView(R.id.vRealName)
    View mVRealName;
    @BindView(R.id.tvUserPwd)
    TextView mTvUserPwd;
    @BindView(R.id.etUserPwd)
    EditText mEtUserPwd;
    @BindView(R.id.vUserPwd)
    View mVUserPwd;
    @BindView(R.id.tvCfUserPwd)
    TextView mTvCfUserPwd;
    @BindView(R.id.etCfUserPwd)
    EditText mEtCfUserPwd;
    @BindView(R.id.vCfUserPwd)
    View mVCfUserPwd;
    private PersonalBean mMBean;


    @Override
    protected void init() {
        if (getIntent() != null) {
            mMBean = (PersonalBean) getIntent().getExtras().getSerializable(AppConst.EXTRA_VICE_INFO);
        }

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_vice_user_editor;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("编辑用户");
        mIvSave.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initData() {
        if (mMBean != null){
            mEtUserName.setText(mMBean.getUsername());
            mEtRealName.setText(mMBean.getRealname());
            mEtRealName.setSelection(mMBean.getRealname().length());
        }
    }

    @Override
    protected void initListener() {
        mVToolbarDivision.setOnClickListener(v -> onBackPressed());

        mIvSave.setOnClickListener(v -> verifyAndSubmitData());


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

    }

    private void verifyAndSubmitData() {
        String realName = mEtRealName.getText().toString().trim();

        if (TextUtils.isEmpty(realName)){
            UIUtils.showToast(getString(R.string.please_real_name));
            return;
        }

        AllocateLookUserRequest request = new AllocateLookUserRequest();
        request.setUsername(mEtUserName.getText().toString().trim());
        request.setRealname(realName);

        String strPwd = mEtUserPwd.getText().toString().trim();
        String strCfPwd = mEtCfUserPwd.getText().toString().trim();
        if (TextUtils.isEmpty(strCfPwd) && !TextUtils.isEmpty(strPwd)){
            UIUtils.showToast(getString(R.string.please_in_pwd));
            return;
        }else if (!TextUtils.isEmpty(strCfPwd) && TextUtils.isEmpty(strPwd)){
            UIUtils.showToast(getString(R.string.please_in_cf_pwd));
            return;
        }else if (!TextUtils.isEmpty(strCfPwd) && !TextUtils.isEmpty(strPwd)){
            if (strCfPwd.equals(strPwd)){
                request.setPassword(strCfPwd);
            }else {
                UIUtils.showToast(getString(R.string.please_in_two_error));
                return;
            }
        }

        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        showWaitingDialog(getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().updateViceInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    hideWaitingDialog();
                    if (response.getCode() == 1){
                        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_VICE_INFO);
                        finish();
                    }else {
                        UIUtils.showToast(response.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }


}

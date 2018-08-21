package com.jit.appcloud.ui.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserNormalCache;
import com.jit.appcloud.model.request.UserInfoUpRequest;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.CheckUtils;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * @author zxl on 2018/05/26.
 *         discription:编辑单行内容的活动
 */
public class EditInfoActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.edContent)
    EditText mEdContent;
    @BindView(R.id.ivDelete)
    ImageView mIvDelete;

    private int mOrigin;
    private String mContent;
    private String mHint;
    @Override
    protected void init() {
        if (getIntent().hasExtra(AppConst.CONTENT_FORM_EMAIL)){
            mOrigin = 2;
            mContent = getIntent().getStringExtra(AppConst.CONTENT_FORM_EMAIL);
            mHint = getString(R.string.str_hint_email);
        }else if (getIntent().hasExtra(AppConst.CONTENT_FROM_PHONE)){
            mOrigin = 1;
            mContent = getIntent().getStringExtra(AppConst.CONTENT_FROM_PHONE);
            mHint = getString(R.string.str_hint_phone);
        }else if (getIntent().hasExtra(AppConst.CONTENT_FROM_COMPANY)){
            mOrigin = 3;
            mContent = getIntent().getStringExtra(AppConst.CONTENT_FROM_COMPANY);
            mHint = getString(R.string.str_hint_company);
        }else if (getIntent().hasExtra(AppConst.CONTENT_FROM_DETAIL_ADS)){
            mOrigin = 4;
            mContent = getIntent().getStringExtra(AppConst.CONTENT_FROM_DETAIL_ADS);
            mHint = getString(R.string.str_hint_detail_ads);
        }else if (getIntent().hasExtra(AppConst.CONTENT_FROM_REAL_NAME)){
            mOrigin = 5;
            mContent = getIntent().getStringExtra(AppConst.CONTENT_FROM_REAL_NAME);
            mHint = getString(R.string.str_hint_realname);
        }else if (getIntent().hasExtra(AppConst.CONTENT_FROM_INCOME)){
            mOrigin = 6;
            mContent = getIntent().getStringExtra(AppConst.CONTENT_FROM_INCOME);
            mHint = getString(R.string.str_hint_income);
        }else if (getIntent().hasExtra(AppConst.CONTENT_FORM_FILE_LOG)){
            mOrigin = 7;
            mContent = getIntent().getStringExtra(AppConst.CONTENT_FORM_FILE_LOG);
            mHint = getString(R.string.str_hint_file);
        }else if (getIntent().hasExtra(AppConst.CONTENT_FORM_GROUP_NAME)){
            mOrigin = 8;
            mContent = getIntent().getStringExtra(AppConst.CONTENT_FORM_GROUP_NAME);
            mHint = getString(R.string.str_hint_group);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_edit_info;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (mOrigin ==7){
            mTvToolbarTitle.setText("编辑文件名");
        }else if (mOrigin == 8){
            mTvToolbarTitle.setText("编辑群聊名称");
        } else {
            mTvToolbarTitle.setText(R.string.str_title_editor);
        }
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(UIUtils.getString(R.string.str_title_save));

        mEdContent.setHint(mHint);
        if (mOrigin == 6) {
            mEdContent.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        if (!TextUtils.isEmpty(mContent)) {
            mEdContent.setText(mContent);
            mEdContent.setSelection(mContent.length());
            mIvDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mIvDelete.setOnClickListener(v -> mEdContent.setText(""));


        mEdContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(mEdContent.getText().toString().trim())){
                    mIvDelete.setVisibility(View.GONE);
                }else {
                    mIvDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTvPublishNow.setOnClickListener(v -> {
            // 进行检查


            String result = mEdContent.getText().toString().trim();
            if (mOrigin == 7){
                if (TextUtils.isEmpty(result)){
                    UIUtils.showToast(getString(R.string.str_toast_check_file));
                }else {
                    Intent intent = new Intent();
                    intent.putExtra(AppConst.RESULT_FROM_EDIT,result);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                return;
            }

            if (mOrigin == 8){
                if (TextUtils.isEmpty(result)){
                    UIUtils.showToast(getString(R.string.str_toast_check_file));
                }else {
                    Intent intent = new Intent();
                    intent.putExtra(AppConst.RESULT_FROM_EDIT,result);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                return;
            }


            if (TextUtils.isEmpty(result)){
                UIUtils.showToast(getString(R.string.str_toast_check_edit));
                return;
            }

            if (mOrigin == 1 && !CheckUtils.isMobile(result)){
                UIUtils.showToast(getString(R.string.str_toast_check_phone));
                mEdContent.setText("");
                return;
            }

            if (mOrigin == 2 && !CheckUtils.isEmail(result)){
                UIUtils.showToast(getString(R.string.str_toast_check_email));
                mEdContent.setText("");
                return;
            }
            showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
            UserInfoUpRequest userInfoUpRequest = UserNormalCache.getUserInfoUpRequest();
            switch (mOrigin){
                case 1:
                    userInfoUpRequest.setTel(result);
                    break;
                case 2:
                    userInfoUpRequest.setEmail(result);
                    break;
                case 3:
                    userInfoUpRequest.setDepartment(result);
                    break;
                case 4:
                    userInfoUpRequest.setAddress(result);
                    break;
                case 5:
                    userInfoUpRequest.setRealname(result);
                    break;
                case 6:
                    userInfoUpRequest.setIncome(Integer.parseInt(result));
                    break;
                default:
                    break;
            }

            String route = new Gson().toJson(userInfoUpRequest);
            ApiRetrofit.getInstance().updateUserInfo(UserCache.getToken()
            ,userInfoUpRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userUpdateInfoResponse -> {
                        hideWaitingDialog();
                        if (userUpdateInfoResponse.getCode() == 1){
                            switch (mOrigin){
                                case 1:
                                    UserNormalCache.setTel(result);
                                    break;
                                case 2:
                                    UserNormalCache.setEmail(result);
                                    break;
                                case 3:
                                    UserNormalCache.setDepartment(result);
                                    break;
                                case 4:
                                    UserNormalCache.setAddress(result);
                                    break;
                                case 5:
                                    UserNormalCache.setRealName(result);
                                    break;
                                case 6:
                                    UserNormalCache.setIncome(Integer.parseInt(result));
                                    break;
                                default:
                                    break;
                            }
                            Intent intent = new Intent();
                            intent.putExtra(AppConst.RESULT_FROM_EDIT,result);
                            setResult(RESULT_OK,intent);
                            finish();
                        }else {
                            UIUtils.showToast(userUpdateInfoResponse.getMsg());
                        }
                    }, throwable -> {
                        hideWaitingDialog();
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    });
        });
    }


}

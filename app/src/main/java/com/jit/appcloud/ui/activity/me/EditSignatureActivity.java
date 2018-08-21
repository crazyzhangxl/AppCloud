package com.jit.appcloud.ui.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserNormalCache;
import com.jit.appcloud.model.request.UserInfoUpRequest;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/05/31.
 *         discription: 编辑签名的活动!!
 */
public class EditSignatureActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.etKeyInput)
    EditText mEtKeyInput;
    @BindView(R.id.tvWordNum)
    TextView mTvWordNum;
    private String mSignature;

    @Override
    protected void init() {
        if (getIntent() != null ){
            mSignature = getIntent().getStringExtra(AppConst.CONTENT_FROM_SIGNATURE);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_edit_signature;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("编辑签名");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(UIUtils.getString(R.string.str_title_save));
        mEtKeyInput.setHint("好的签名能让他人更好的记住您哦!");
        if (!TextUtils.isEmpty(mSignature)) {
            mEtKeyInput.setText(mSignature);
            mEtKeyInput.setSelection(mSignature.length());
        }
        mTvWordNum.setText(String.valueOf(AppConst.NUM_KEY_INPUT-mEtKeyInput.getText().length()));

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mEtKeyInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvWordNum.setText(String.valueOf(AppConst.NUM_KEY_INPUT-mEtKeyInput.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mTvPublishNow.setOnClickListener(v -> {
            String result = mEtKeyInput.getText().toString().trim();
            if (TextUtils.isEmpty(result)){
                UIUtils.showToast(getString(R.string.str_toast_check_edit));
                return;
            }
            showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
            ApiRetrofit.getInstance().updateSignature(result)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userUpdateInfoResponse -> {
                        hideWaitingDialog();
                        if (userUpdateInfoResponse.getCode() == 1){
                            /* 返回给前面就可以了 ======= */
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

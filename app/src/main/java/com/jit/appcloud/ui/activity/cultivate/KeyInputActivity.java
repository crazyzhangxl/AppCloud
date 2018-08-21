package com.jit.appcloud.ui.activity.cultivate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zxl on 2018/05/23.
 *         discription:该活动用于输入文字用的
 *         规避了文字输入的一些问题
 */
public class KeyInputActivity extends BaseActivity {
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

    private String mTitle;
    private String mContentFromBefore;
    @Override
    protected void init() {
        if (getIntent()!=null && getIntent().hasExtra(AppConst.KEY_INPUT_TITLE )){
            mTitle = getIntent().getStringExtra(AppConst.KEY_INPUT_TITLE);
            mContentFromBefore= getIntent().getStringExtra(AppConst.KEY_TO_INPUT_CONTENT);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_key_input;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(mTitle);
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(UIUtils.getString(R.string.str_title_save));
        if (!TextUtils.isEmpty(mContentFromBefore)) {
            mEtKeyInput.setText(mContentFromBefore);
            mEtKeyInput.setSelection(mContentFromBefore.length());
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

        mTvPublishNow.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mEtKeyInput.getText().toString())){
                UIUtils.showToast("请输入内容!");
            }else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                if (isSoftShowing()) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                Intent intent = new Intent();
                intent.putExtra(AppConst.KEY_FROM_INPUT,mEtKeyInput.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        mIvToolbarNavigation.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            // 隐藏软键盘
            if (isSoftShowing()) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
            onBackPressed();
        });
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }
}

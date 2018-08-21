package com.jit.appcloud.ui.activity.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.Friend;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.PinyinUtils;
import com.jit.appcloud.util.UIUtils;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/06/13.
 *         discription: 添加备注的活动
 */
public class SetAliasActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.etAlias)
    EditText mEtAlias;
    @BindView(R.id.ibClearAlias)
    ImageButton mIbClearAlias;
    @BindView(R.id.btnToolbarSend)
    Button mBtnToolbarSend;
    private String mFriendId;
    private String mFriendName;
    private Friend mFriend;
    @Override
    protected void init() {
        mFriendId = getIntent().getStringExtra(AppConst.EXTRA_FRIEND_ID);
        mFriendName = getIntent().getStringExtra(AppConst.EXTRA_FRIEND_NAME);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_set_alias;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (TextUtils.isEmpty(mFriendId)) {
            finish();
            return;
        }

        mBtnToolbarSend.setVisibility(View.VISIBLE);
        mBtnToolbarSend.setText("完成");
    }

    @Override
    protected void initData() {
        mFriend = DBManager.getInstance().getFriendById(mFriendId);
        if (mFriend != null)
            mEtAlias.setText(mFriend.getDisplayName());
        mEtAlias.setSelection(mEtAlias.getText().toString().trim().length());
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mEtAlias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBtnToolbarSend.setEnabled(mEtAlias.getText().toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBtnToolbarSend.setOnClickListener(v -> {
            String displayName = mEtAlias.getText().toString().trim();
            if (TextUtils.isEmpty(displayName)) {
                UIUtils.showToast(UIUtils.getString(R.string.alias_no_empty));
                return;
            }
            showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
            ApiRetrofit.getInstance().changeNickName(UserCache.getToken()
            ,displayName,mFriendName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        if (response.getCode() == 1) {
                            UIUtils.showToast(UIUtils.getString(R.string.change_success));
                            //更新本地好友数据库
                            mFriend.setDisplayName(displayName);
                            mFriend.setDisplayNameSpelling(PinyinUtils.getPinyin(displayName));
                            DBManager.getInstance().saveOrUpdateFriend(mFriend);
                            BroadcastManager.getInstance(SetAliasActivity.this).sendBroadcast(AppConst.UPDATE_FRIEND);
                            BroadcastManager.getInstance(SetAliasActivity.this).sendBroadcast(AppConst.CHANGE_INFO_FOR_USER_INFO);
                            finish();
                        } else {
                            UIUtils.showToast(UIUtils.getString(R.string.change_fail));
                        }
                        hideWaitingDialog();
                    }, throwable -> {
                        hideWaitingDialog();
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    });
        });
    }

}

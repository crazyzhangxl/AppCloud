package com.jit.appcloud.ui.activity.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.request.EpPondRequest;
import com.jit.appcloud.model.response.PondGetByMGResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/07/12.
 *         discription: 养殖户自己编辑塘口 =======
 */
public class EpEdPondActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.etPondId)
    EditText mEtPondId;
    @BindView(R.id.etPondCg)
    EditText mEtPondCg;
    @BindView(R.id.etVy)
    EditText mEtVy;
    @BindView(R.id.etBreedBrand)
    EditText mEtBreedBrand;
    @BindView(R.id.etBreedNum)
    EditText mEtBreedNum;
    @BindView(R.id.etArea)
    EditText mEtArea;
    @BindView(R.id.tvBreedTime)
    TextView mTvBreedTime;
    @BindView(R.id.rlBreedTime)
    RelativeLayout mRlBreedTime;
    @BindView(R.id.etLiaoName)
    EditText mEtLiaoName;
    @BindView(R.id.etLiaoType)
    EditText mEtLiaoType;
    private PondGetByMGResponse.DataBean mEpPondBean;

    @Override
    protected void init() {
        mEpPondBean = (PondGetByMGResponse.DataBean) getIntent().getSerializableExtra(AppConst.EXTRA_SER_AG_POND_EDITOR);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_ag_pond_editor;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("编译鱼塘信息");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(getString(R.string.str_title_save));
        refreshView();
    }

    private void refreshView() {
        if (mEpPondBean != null){
            mEtPondId.setText(mEpPondBean.getNumber());
            mEtPondCg.setText(mEpPondBean.getType());
            mEtVy.setText(mEpPondBean.getSeed_type());
            mEtBreedBrand.setText(mEpPondBean.getSeed_brand());
            mEtBreedNum.setText(String.valueOf(mEpPondBean.getSeed_number()));
            mEtArea.setText(String.valueOf(mEpPondBean.getSquare()));
            mTvBreedTime.setText(mEpPondBean.getSeed_time());
            mEtLiaoName.setText(mEpPondBean.getFeed_brand());
            mEtLiaoType.setText(mEpPondBean.getFeed_type());
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mTvPublishNow.setOnClickListener(v -> {
            // 保存信息
            String pondId = mEtPondId.getText().toString();
            if (TextUtils.isEmpty(pondId)){
                UIUtils.showToast(getString(R.string.pound_not_null));
                return;
            }
            showWaitingDialog(getString(R.string.str_please_waiting));
            EpPondRequest epPondRequest = new EpPondRequest();
            epPondRequest.setNumber(pondId);
            epPondRequest.setType(mEtPondCg.getText().toString());
            if (!TextUtils.isEmpty(mEtArea.getText().toString()))
                epPondRequest.setSquare(Integer.parseInt(mEtArea.getText().toString()));
            epPondRequest.setSeed_brand(mEtBreedBrand.getText().toString());
            epPondRequest.setSeed_type(mEtVy.getText().toString());
            if (!TextUtils.isEmpty(mEtBreedNum.getText().toString()))
                epPondRequest.setSeed_number(Integer.parseInt(mEtBreedNum.getText().toString()));
            epPondRequest.setFeed_brand(mEtLiaoName.getText().toString());
            epPondRequest.setFeed_type(mEtLiaoType.getText().toString());
            epPondRequest.setUsername(UserCache.getName());

            ApiRetrofit.getInstance().epUpdatePound(mEpPondBean.getId(),epPondRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        hideWaitingDialog();
                        if (response.getCode() == 1){
                            UIUtils.showToast(getString(R.string.success_update_pound));
                            BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_EP_POUND);
                            finish();
                        }else {
                            UIUtils.showToast(response.getMsg());
                        }
                    }, throwable -> {
                        hideWaitingDialog();
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    });
        });
    }
}

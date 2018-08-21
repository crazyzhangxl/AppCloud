package com.jit.appcloud.ui.activity.cultivate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.AgPondBean;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;
import butterknife.BindView;
/**
 * @author zxl on 2018/06/25.
 *         discription:
 */
public class AgPondEditorActivity extends BaseActivity {
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
    private AgPondBean mAgPondBean;

    @Override
    protected void init() {
        mAgPondBean = (AgPondBean) getIntent().getSerializableExtra(AppConst.EXTRA_SER_AG_POND_EDITOR);
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
        if (mAgPondBean != null){
            mEtPondId.setText(mAgPondBean.getPondId());
            if (!TextUtils.isEmpty(mAgPondBean.getPondCg()))
                mEtPondCg.setText(mAgPondBean.getPondCg());

            if (!TextUtils.isEmpty(mAgPondBean.getPondVy()))
                mEtVy.setText(mAgPondBean.getPondVy());

            if (!TextUtils.isEmpty(mAgPondBean.getSeedBread()))
                mEtBreedBrand.setText(mAgPondBean.getSeedBread());
            mEtBreedNum.setText(String.valueOf(mAgPondBean.getSeedNum()));

            if (!TextUtils.isEmpty(mAgPondBean.getSeedArea()))
                mEtArea.setText(mAgPondBean.getSeedArea());
            mTvBreedTime.setText(mAgPondBean.getSeedTime());

            mEtLiaoName.setText(mAgPondBean.getLiaoName());
            mEtLiaoType.setText(mAgPondBean.getLiaoType());
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
                UIUtils.showToast("塘号不能为空!");
                return;
            }
            String pondCg = mEtPondCg.getText().toString();
            String vy = mEtVy.getText().toString();
            String breedBrand = mEtBreedBrand.getText().toString();
            String breedNum = mEtBreedNum.getText().toString();
            String area = mEtArea.getText().toString();
            String breedTime = mTvBreedTime.getText().toString();
            String liaoName = mEtLiaoName.getText().toString();
            String liaoType = mEtLiaoType.getText().toString();

            AgPondBean agPondBean = new AgPondBean();
            agPondBean.setPondId(pondId);
            agPondBean.setPondCg(pondCg);
            agPondBean.setPondVy(vy);
            agPondBean.setSeedBread(breedBrand);
            agPondBean.setSeedNum(Integer.parseInt(breedNum));
            agPondBean.setSeedArea(area);
            agPondBean.setSeedTime(breedTime);
            agPondBean.setLiaoName(liaoName);
            agPondBean.setLiaoType(liaoType);
            DBManager.getInstance().updateAgPond(agPondBean,mAgPondBean.getId());

            setResult(RESULT_OK);
            finish();
        });
    }

}

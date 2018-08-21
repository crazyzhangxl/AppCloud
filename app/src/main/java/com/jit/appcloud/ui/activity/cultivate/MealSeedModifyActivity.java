package com.jit.appcloud.ui.activity.cultivate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.SeedMeal;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserOtherCache;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zxl on 2018/06/12.
 *         discription:
 */
public class MealSeedModifyActivity extends BaseActivity {


    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.etMealName)
    EditText mEtMealName;
    @BindView(R.id.ll_feedType)
    LinearLayout mLlFeedType;
    @BindView(R.id.tvGoodsSelected)
    TextView mTvGoodsSelected;
    @BindView(R.id.ll_inputType)
    LinearLayout mLlInputType;
    @BindView(R.id.tvPondArea)
    TextView mTvPondArea;
    @BindView(R.id.etInputNum)
    EditText mEtInputNum;
    @BindView(R.id.spInputUnit)
    MaterialSpinner mSpInputUnit;
    @BindView(R.id.tvMealType)
    TextView mTvMealType;
    @BindView(R.id.llMealType)
    LinearLayout mLlMealType;
    @BindView(R.id.llDelete)
    LinearLayout mLlDelete;

    private String mSeedBrand;
    private String mSeedName;
    private String mType;
    private String mOriginMealName;
    private SeedMeal mSeedMeal;
    @Override
    protected void init() {
        mSeedMeal = (SeedMeal) getIntent().getExtras().getSerializable(AppConst.SEED_MEAL);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_meal_seed_modify;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("编辑套餐");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(getString(R.string.str_title_save));
        mSpInputUnit.setItems(AppConst.SP_SEED_UNIT);
        mSpInputUnit.setSelectedIndex(0);
        refreshView();
    }

    private void refreshView() {
        List<String> items = Arrays.asList(AppConst.SP_SEED_UNIT);
        mSpInputUnit.setSelectedIndex(items.indexOf(mSeedMeal.getInputUnit()));
        mEtMealName.setText(mSeedMeal.getMealName());
        mTvGoodsSelected.setText(String.format("%s-%s",mSeedMeal.getSeedBrand(),mSeedMeal.getSeedName()));
        mEtInputNum.setText(String.valueOf(mSeedMeal.getInputNum()));
        mTvMealType.setText(mSeedMeal.getSeedType());

        mSeedName = mSeedMeal.getSeedName();
        mSeedBrand = mSeedMeal.getSeedBrand();
        mType = mSeedMeal.getSeedType();

        mOriginMealName = mSeedMeal.getMealName();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mLlInputType.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,SingleSelectionActivity.class);
            intent.putExtra(AppConst.FLAG_SEED_NAME,"seedName");
            startActivityForResult(intent,AppConst.RECODE_TO_SEED_TYPE_ITEM_LIST);
        });

        mLlMealType.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,SingleSelectionActivity.class);
            intent.putExtra(AppConst.FLAG_SILIAO_TYPE,"siliaotype");
            startActivityForResult(intent,AppConst.RECODE_TO_SILIAO_TYPE_SINGLE_ITEM_LIST);
        });


        mEtMealName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (DBManager.getInstance().isHaveSeedMeal(mEtMealName.getText().toString(),mOriginMealName)){
                    UIUtils.showToast(mEtMealName.getText().toString()+"已经存在");
                    mEtMealName.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTvPublishNow.setOnClickListener(v -> verifyAndAdd());

        mLlDelete.setOnClickListener(v -> showMaterialDialog("删除套餐", "您确认要删除该套餐?", "确定", "取消"
                , (dialog, which) -> {
                    dialog.dismiss();
                    DBManager.getInstance().deleteSeedMealByName(mOriginMealName);
                    BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_SEED_MEAL);
                    if (mOriginMealName.equals(UserOtherCache.getSeedMealSelected())){
                        // 如果修改的时原始的话,那么久需要删除了
                        UserOtherCache.setSeedMealSelected("");
                        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_SUBMIT_SEED_MEAL_NAME,"");
                    }

                    finish();
                }, (dialog, which) -> dialog.dismiss()));
    }

    private void verifyAndAdd() {
        String mealName = mEtMealName.getText().toString();
        if (TextUtils.isEmpty(mealName)){
            UIUtils.showToast("请输入套餐名");
            return;
        }

        String inputNum = mEtInputNum.getText().toString();
        if (TextUtils.isEmpty(inputNum)){
            UIUtils.showToast("请输入投放数量");
            return;
        }

        SeedMeal seedMeal = new SeedMeal(mealName,mType,mSeedBrand,mSeedName,Double.parseDouble(inputNum),AppConst.SP_SEED_UNIT[mSpInputUnit.getSelectedIndex()]);
        DBManager.getInstance().saveOrUpdateSeedMeal(seedMeal,mOriginMealName);
        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_SEED_MEAL);
        Log.e("套餐", "verifyAndAdd:     "+mOriginMealName );
        if (mOriginMealName.equals(UserOtherCache.getSeedMealSelected())){
            UserOtherCache.setSeedMealSelected(mealName);
            BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_SUBMIT_SEED_MEAL_NAME,mealName);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case AppConst.RECODE_TO_SILIAO_ITEM_LIST:
                    String result = data.getStringExtra(AppConst.SINGLE_ITEM_SELECTED);
                    mTvGoodsSelected.setText(result);
                    String[] seedBound = result.split("-");
                    mSeedBrand = seedBound[0];
                    mSeedName = seedBound[1];
                    break;
                case AppConst.RECODE_TO_SILIAO_TYPE_SINGLE_ITEM_LIST:
                    mType = data.getStringExtra(AppConst.SINGLE_ITEM_SELECTED);
                    mTvMealType.setText(mType);
                    break;
            }
        }
    }

}

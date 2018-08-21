package com.jit.appcloud.ui.activity.cultivate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.FeedMeal;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;


import butterknife.BindView;

/**
 * @author zxl on 2018/06/12.
 *         discription:
 */
public class MealFeedAddActivity extends BaseActivity {

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

    private String mSiliaoName;
    private String mSiliaoType;
    private String mType;

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_meal_feed_add;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("添加新套餐");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(getString(R.string.str_title_save));
        mSpInputUnit.setItems(AppConst.SP_INPUT_UNIT);
        mSpInputUnit.setSelectedIndex(0);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mLlInputType.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,SingleSelectionActivity.class);
            intent.putExtra(AppConst.FLAG_SILIAO,"siliao");
            startActivityForResult(intent,AppConst.RECODE_TO_SILIAO_ITEM_LIST);
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
                if (DBManager.getInstance().isHaveMeal(mEtMealName.getText().toString())){
                    UIUtils.showToast(mEtMealName.getText().toString()+"已经存在");
                    mEtMealName.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTvPublishNow.setOnClickListener(v -> verifyAndAdd());
    }

    private void verifyAndAdd(){
        String mealName = mEtMealName.getText().toString();
        if (TextUtils.isEmpty(mealName)){
            UIUtils.showToast("请输入套餐名");
            return;
        }

        if (TextUtils.isEmpty(mSiliaoName)){
            UIUtils.showToast("请选择投放物品");
            return;
        }

        String inputNum = mEtInputNum.getText().toString();
        if (TextUtils.isEmpty(inputNum)){
            UIUtils.showToast("请输入投放数量");
            return;
        }

        if (TextUtils.isEmpty(mType)){
            UIUtils.showToast("请选择投放类型");
            return;
        }

        FeedMeal feedMeal = new FeedMeal(mealName,mType,mSiliaoType,mSiliaoName,Double.parseDouble(inputNum),AppConst.SP_INPUT_UNIT[mSpInputUnit.getSelectedIndex()]);
        DBManager.getInstance().saveFeedMeal(feedMeal);
        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_FEED_MEAL);
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
                    String[] siliaoBoud = result.split("-");
                    mSiliaoType = siliaoBoud[0];
                    mSiliaoName = siliaoBoud[1];
                    break;
                case AppConst.RECODE_TO_SILIAO_TYPE_SINGLE_ITEM_LIST:
                    mType = data.getStringExtra(AppConst.SINGLE_ITEM_SELECTED);
                    mTvMealType.setText(mType);
                    break;
            }
        }
    }
}

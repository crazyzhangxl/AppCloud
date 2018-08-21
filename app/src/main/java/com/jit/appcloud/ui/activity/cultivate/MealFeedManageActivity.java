package com.jit.appcloud.ui.activity.cultivate;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.FeedMeal;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserOtherCache;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * @author zxl on 2018/06/12.
 *         discription: 投放套餐的选择界面
 */
public class MealFeedManageActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.rvMealManage)
    RecyclerView mRvMealManage;
    @BindView(R.id.btnAddMeal)
    Button mBtnAddMeal;

    private BaseQuickAdapter<FeedMeal,BaseViewHolder> mMealAdapter;
    private List<FeedMeal> mMealList = new ArrayList<>();
    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_meal_feed_manage;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("我的投放套餐");
        initAdapter();
        getDataAndRefresh();
    }

    private void getDataAndRefresh() {
        mMealList.clear();
        mMealList.addAll(DBManager.getInstance().queryFeedMeal());
        mMealAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvMealManage.setLayoutManager(manager);
        mMealAdapter = new BaseQuickAdapter<FeedMeal, BaseViewHolder>(R.layout.item_meal_feed_manage,mMealList) {
            @Override
            protected void convert(BaseViewHolder helper, FeedMeal item) {
                  helper.setText(R.id.tvMealName,item.getMealName());
                  helper.setText(R.id.tvMealType,item.getFeedType());
                  helper.setText(R.id.tvMealDetail,
                          String.format("%s-%s %s%s",item.getFeedBrand(),item.getFeedName()
                          ,String.valueOf(item.getInputNum()),item.getInputUnit()));

                  helper.getView(R.id.tvEditor).setOnClickListener(v -> {
                      Bundle bundle = new Bundle();
                      bundle.putSerializable(AppConst.FEED_MEAL,item);
                      jumpToActivity(MealFeedModifyActivity.class,bundle);
                  });
            }
        };
        mMealAdapter.setOnItemClickListener((adapter, view, position) -> {
            UserOtherCache.setFeedMealSelected(mMealList.get(position).getMealName());
            Intent intent = new Intent();
            Bundle  bundle = new Bundle();
            bundle.putSerializable(AppConst.FEED_MEAL,mMealList.get(position));
            intent.putExtras(bundle);
            setResult(RESULT_OK,intent);
            finish();
        });
        mRvMealManage.setAdapter(mMealAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mBtnAddMeal.setOnClickListener(v -> jumpToActivity(MealFeedAddActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(mContext).register(AppConst.UPDATE_FEED_MEAL, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getDataAndRefresh();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(mContext).unregister(AppConst.UPDATE_FEED_MEAL);
    }
}

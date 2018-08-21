package com.jit.appcloud.ui.activity.me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.response.PondGetByMGResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_LEFT;

/**
 * @author zxl on 2018/06/26.
 *         discription: 养殖户塘口管理活动
 */
public class FmPondManageActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tvPondAdd)
    TextView mTvPondAdd;
    @BindView(R.id.tvPondEditor)
    TextView mTvPondEditor;
    @BindView(R.id.tvPondDel)
    TextView mTvPondDel;
    @BindView(R.id.rvPondMg)
    RecyclerView mRvPondMg;
    @BindView(R.id.tvRecordNum)
    TextView mTvRecordNum;
    private BaseQuickAdapter<PondGetByMGResponse.DataBean, BaseViewHolder> mPondAdapter;
    private List<PondGetByMGResponse.DataBean> mPondList = new ArrayList<>();
    private int mSelectPosition = -1;

    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_fm_pond_manage;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.title_fish_pond_manage);
        initAdapter();
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvPondMg.setLayoutManager(manager);
        mPondAdapter = new BaseQuickAdapter<PondGetByMGResponse.DataBean, BaseViewHolder>(R.layout.item_fm_pond_show, mPondList) {
            @Override
            protected void convert(BaseViewHolder helper, PondGetByMGResponse.DataBean item) {
                if (mSelectPosition == helper.getAdapterPosition()) {
                    helper.getView(R.id.llPond).setBackgroundColor(UIUtils.getColor(R.color.item_select_color));
                } else {
                    helper.getView(R.id.llPond).setBackgroundColor(UIUtils.getColor(R.color.white));
                }
                /*  设置数据 -------------  */
                helper.setText(R.id.tvPondId, item.getNumber());
                helper.setText(R.id.tvPondCg, item.getType());
                helper.setText(R.id.tvPondBrand, item.getSeed_brand());
            }
        };
        mPondAdapter.openLoadAnimation(SLIDEIN_LEFT);
        mPondAdapter.setOnItemClickListener((adapter, view, position) -> {
            mSelectPosition = position;
            mPondAdapter.notifyDataSetChanged();
        });
        mRvPondMg.setAdapter(mPondAdapter);
    }

    @Override
    protected void initData() {
        // 请求网络数据 进行刷新
        refreshPondInfo();
    }

    private void refreshPondInfo() {
        showWaitingDialog(getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().getPondByEp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pondGetByMGResponse -> {
                    hideWaitingDialog();
                    if (pondGetByMGResponse != null && pondGetByMGResponse.getCode() == 1) {
                        mPondList.clear();
                        mPondList.addAll(pondGetByMGResponse.getData());
                        mPondAdapter.notifyDataSetChanged();
                        updateNum();
                    } else {
                        UIUtils.showToast(pondGetByMGResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mTvPondAdd.setOnClickListener(v -> jumpToActivity(EpAddPondActivity.class));

        mTvPondEditor.setOnClickListener(v -> {
            if (mSelectPosition == -1) {
                UIUtils.showToast(getString(R.string.please_choose_one_pond));
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConst.EXTRA_SER_AG_POND_EDITOR, mPondList.get(mSelectPosition));
                jumpToActivity(EpEdPondActivity.class, bundle);
            }
        });

        mTvPondDel.setOnClickListener(v -> {
            if (mSelectPosition == -1) {
                UIUtils.showToast(getString(R.string.please_choose_one_pond));
            } else {
                ApiRetrofit.getInstance().epDeletePound(mPondList.get(mSelectPosition).getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.getCode() == 1) {
                                mPondList.remove(mSelectPosition);
                                mSelectPosition = -1;
                                mPondAdapter.notifyDataSetChanged();
                                updateNum();
                                BroadcastManager.getInstance(this).sendBroadcast(AppConst.UPDATE_EP_CUL_POUND);
                            } else {
                                UIUtils.showToast(response.getMsg());
                            }
                        }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));

            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(this).register(AppConst.UPDATE_EP_POUND, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshPondInfo();
            }
        });
    }

    private void updateNum() {
        mTvRecordNum.setText(String.format("记录数:%s", String.valueOf(mPondList.size())));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(this).unregister(AppConst.UPDATE_EP_POUND);
    }
}

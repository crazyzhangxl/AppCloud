package com.jit.appcloud.ui.activity.me;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.bean.PersonalBean;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.PondGetByMGResponse;
import com.jit.appcloud.model.response.UserBdAmResponse;
import com.jit.appcloud.ui.activity.FarmLogActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.DropDownView;
import com.luck.picture.lib.tools.ScreenUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_LEFT;

/**
 * @author zxl on 2018/06/26.
 *         discription: 养殖户数据管理活动
 */
public class FmDataManageActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tvRecordNum)
    TextView mTvRecordNum;
    @BindView(R.id.dropDownView)
    DropDownView mDropDownView;
    RecyclerView mRvDataMg;
    private View expandedView;
    private View collapsedView;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter<String,BaseViewHolder> mAdapter ;
    private List<String> mDataCusList = new ArrayList<>();
    private BaseQuickAdapter<PondGetByMGResponse.DataBean , BaseViewHolder> mDataAdapter;
    private List<PondGetByMGResponse.DataBean > mDataList = new ArrayList<>();
    private int mDropSelectPosition = 0;
    private int mDropPrePosition = -1;
    private int mSelectPosition = -1;
    private RelativeLayout mRlCusAction;
    private TextView mTvCusName;
    private ImageView mArrowImage;
    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_fm_data_manage;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.title_data_manage);
        seuUpDropDownView();
    }

    private void seuUpDropDownView() {
        collapsedView = LayoutInflater.from(this).inflate(R.layout.drop_head_data_cus,null);
        expandedView = LayoutInflater.from(this).inflate(R.layout.drop_body_rv,null);
        View dropBodyView = LayoutInflater.from(this).inflate(R.layout.drop_data_body, null);
        mRvDataMg =dropBodyView.findViewById(R.id.rvDataMg);
        initDataAdapter();

        mRecyclerView =  expandedView.findViewById(R.id.recyclerView);
        mRlCusAction = collapsedView.findViewById(R.id.rlCusAction);
        mTvCusName = collapsedView.findViewById(R.id.tvCusNameShow);
        mArrowImage = collapsedView.findViewById(R.id.arrowImage);
        mDropDownView.setHeaderView(collapsedView);
        mDropDownView.setExpandedView(expandedView,dropBodyView);
        if (AppConst.ROLE_FARMER.equals(UserCache.getRole())){
            mDropDownView.setIsHeadShow(false);
        }
        setDropCusAdapter();
    }

    private void setDropCusAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(mContext) * 0.5);
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_drop_data_cus,mDataCusList) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.itemView.setBackground (UIUtils.getDrawable(R.drawable.selector_item_select_state));
                helper.itemView.setSelected(helper.getAdapterPosition() == mDropSelectPosition);
                helper.setText(R.id.tvTitle,item);
            }
        };
        mAdapter.openLoadAnimation(SLIDEIN_LEFT);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position != mDropSelectPosition) {
                mDropPrePosition = mDropSelectPosition;
                mDropSelectPosition = position;
                mAdapter.notifyItemChanged(mDropPrePosition);
                mAdapter.notifyItemChanged(mDropSelectPosition);
                mTvCusName.setText(mDataCusList.get(mDropSelectPosition));
                refreshPond(mDataCusList.get(position));
            }
            mDropDownView.collapseDropDown();
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateNum(){
        mTvRecordNum.setText(String.format("记录数:%s",String.valueOf(mDataList.size())));
    }

    private void refreshPond(String realName) {

        if (AppConst.ROLE_AGENT.equals(UserCache.getRole()) ||
                AppConst.ROLE_VICE_AGENT.equals(UserCache.getRole())) {
            ApiRetrofit.getInstance().queryPoundByMgName(realName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pondGetByMGResponse -> {
                        if (pondGetByMGResponse.getCode() == 1) {
                            mDataList.clear();
                            mDataList.addAll(pondGetByMGResponse.getData());
                            mDataAdapter.notifyDataSetChanged();
                            updateNum();
                        } else {
                            UIUtils.showToast(pondGetByMGResponse.getMsg());
                        }
                    }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
        }else if (AppConst.ROLE_AGENCY.equals(UserCache.getRole())
                || AppConst.ROLE_VICE_MANAGER.equals(UserCache.getRole())){
            ApiRetrofit.getInstance().queryPoundByEpName(realName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pondGetByMGResponse -> {
                        if (pondGetByMGResponse.getCode() == 1) {
                            mDataList.clear();
                            mDataList.addAll(pondGetByMGResponse.getData());
                            mDataAdapter.notifyDataSetChanged();
                            updateNum();
                        } else {
                            UIUtils.showToast(pondGetByMGResponse.getMsg());
                        }
                    }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
        }else if (AppConst.ROLE_GENERAL_AGENCY.equals(UserCache.getRole())
                || AppConst.ROLE_VICE_ADMIN.equals(UserCache.getRole())){
            ApiRetrofit.getInstance().getAgentPondsInfo(realName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pondGetByMGResponse -> {
                        if (pondGetByMGResponse.getCode() == 1) {
                            mDataList.clear();
                            mDataList.addAll(pondGetByMGResponse.getData());
                            mDataAdapter.notifyDataSetChanged();
                            updateNum();
                        } else {
                            UIUtils.showToast(pondGetByMGResponse.getMsg());
                        }
                    }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
        }
    }

    private void initDataAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvDataMg.setLayoutManager(manager);
        mDataAdapter = new BaseQuickAdapter<PondGetByMGResponse.DataBean , BaseViewHolder>(R.layout.item_fm_data_show, mDataList) {
            @Override
            protected void convert(BaseViewHolder helper, PondGetByMGResponse.DataBean  item) {
                if (mSelectPosition == helper.getAdapterPosition()) {
                    helper.getView(R.id.llData).setBackgroundColor(UIUtils.getColor(R.color.item_select_color));
                }
                else {
                    helper.getView(R.id.llData).setBackgroundColor(UIUtils.getColor(R.color.white));
                }
                /*  设置数据 -------------  */
                helper.setText(R.id.tvPondId,String.format("%s-%s",item.getRealname(),item.getNumber()));
            }
        };
        mDataAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mSelectPosition != position) {
                mSelectPosition = position;
                mDataAdapter.notifyDataSetChanged();
            }
            Intent intent = new Intent(FmDataManageActivity.this,FarmLogActivity.class);
            intent.putExtra(AppConst.POND_ID_SELECTED,mDataList.get(position).getId());
            jumpToActivity(intent);
        });
        mDataAdapter.openLoadAnimation(SLIDEIN_LEFT);
        mRvDataMg.setAdapter(mDataAdapter);
    }

    @Override
    protected void initData() {
        // 总代ok的
        if (AppConst.ROLE_VICE_ADMIN.equals(UserCache.getRole()) ||
                AppConst.ROLE_GENERAL_AGENCY.equals(UserCache.getRole())){
            showWaitingDialog(getString(R.string.str_please_waiting));
            ApiRetrofit.getInstance().getAgencyNextUserInfo(UserCache.getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(userBdAmResponse -> {
                        if (userBdAmResponse.getCode() == 1) {
                            List<PersonalBean> beans = userBdAmResponse.getData();
                            for (PersonalBean bean : beans) {
                                mDataCusList.add(bean.getRealname());
                            }
                            mAdapter.notifyDataSetChanged();
                            if (mDataCusList.size() != 0) {
                                mTvCusName.setText(mDataCusList.get(0));
                                return Observable.just(mDataCusList.get(0));
                            } else {
                                return Observable.just("");
                            }
                        } else {
                            UIUtils.showToast(userBdAmResponse.getMsg());
                            return Observable.just("");
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(s -> {
                        if (!TextUtils.isEmpty(s)){
                            return ApiRetrofit.getInstance().getAgentPondsInfo(s);
                        }
                        return null;
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pondGetByMGResponse -> {
                        hideWaitingDialog();
                        if (pondGetByMGResponse != null && pondGetByMGResponse.getCode() == 1){
                            mDataList.clear();
                            mDataList.addAll(pondGetByMGResponse.getData());
                            mDataAdapter.notifyDataSetChanged();
                            updateNum();
                        }else {
                            UIUtils.showToast(pondGetByMGResponse.getMsg());
                        }
                    }, throwable -> {
                        hideWaitingDialog();
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    });

        } else if (AppConst.ROLE_AGENT.equals(UserCache.getRole())
                || AppConst.ROLE_VICE_AGENT.equals(UserCache.getRole())) {
            showWaitingDialog(getString(R.string.str_please_waiting));
            ApiRetrofit.getInstance().getAgencyNextUserInfo(UserCache.getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(userBdAmResponse -> {
                        if (userBdAmResponse.getCode() == 1) {
                            List<PersonalBean> beans = userBdAmResponse.getData();
                            for (PersonalBean bean : beans) {
                                mDataCusList.add(bean.getRealname());
                            }
                            mAdapter.notifyDataSetChanged();
                            if (mDataCusList.size() != 0) {
                                mTvCusName.setText(mDataCusList.get(0));
                                return Observable.just(mDataCusList.get(0));
                            } else {
                                return Observable.just("");
                            }
                        } else {
                            UIUtils.showToast(userBdAmResponse.getMsg());
                            return Observable.just("");
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(s -> {
                        if (!TextUtils.isEmpty(s)){
                            return ApiRetrofit.getInstance().queryPoundByMgName(s);
                        }
                        return null;
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pondGetByMGResponse -> {
                        hideWaitingDialog();
                        if (pondGetByMGResponse != null && pondGetByMGResponse.getCode() == 1){
                            mDataList.clear();
                            mDataList.addAll(pondGetByMGResponse.getData());
                            mDataAdapter.notifyDataSetChanged();
                            updateNum();
                        }else {
                            UIUtils.showToast(pondGetByMGResponse.getMsg());
                        }
                    }, throwable -> {
                        hideWaitingDialog();
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    });
        }else if (AppConst.ROLE_AGENCY.equals(UserCache.getRole())
                || AppConst.ROLE_VICE_MANAGER.equals(UserCache.getRole())){
            showWaitingDialog(getString(R.string.str_please_waiting));
            ApiRetrofit.getInstance().getAgencyNextUserInfo(UserCache.getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(userBdAmResponse -> {
                        if (userBdAmResponse.getCode() == 1) {
                            List<PersonalBean> beans = userBdAmResponse.getData();
                            for (PersonalBean bean : beans) {
                                mDataCusList.add(bean.getRealname());
                            }
                            mAdapter.notifyDataSetChanged();
                            if (mDataCusList.size() != 0) {
                                mTvCusName.setText(mDataCusList.get(0));
                                return Observable.just(mDataCusList.get(0));
                            } else {
                                return Observable.just("");
                            }
                        } else {
                            UIUtils.showToast(userBdAmResponse.getMsg());
                            return Observable.just("");
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(s -> {
                        if (!TextUtils.isEmpty(s)){
                            return ApiRetrofit.getInstance().queryPoundByEpName(s);
                        }
                        return null;
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pondGetByMGResponse -> {
                        hideWaitingDialog();
                        if (pondGetByMGResponse != null && pondGetByMGResponse.getCode() == 1){
                            mDataList.clear();
                            mDataList.addAll(pondGetByMGResponse.getData());
                            mDataAdapter.notifyDataSetChanged();
                            updateNum();
                        }else {
                            UIUtils.showToast(pondGetByMGResponse.getMsg());
                        }
                    }, throwable -> {
                        hideWaitingDialog();
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    });

        }else if (AppConst.ROLE_FARMER.equals(UserCache.getRole())){
            showWaitingDialog(getString(R.string.str_please_waiting));
            ApiRetrofit.getInstance().getPondByEp()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pondGetByMGResponse -> {
                        hideWaitingDialog();
                        if (pondGetByMGResponse != null && pondGetByMGResponse.getCode() == 1){
                            mDataList.clear();
                            mDataList.addAll(pondGetByMGResponse.getData());
                            mDataAdapter.notifyDataSetChanged();
                            updateNum();
                        }else {
                            UIUtils.showToast(pondGetByMGResponse.getMsg());
                        }
                    }, throwable -> {
                        hideWaitingDialog();
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    });
        }

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mDropDownView.setDropDownListener(new DropDownView.DropDownListener() {
            @Override
            public void onExpandDropDown() {
                ObjectAnimator.ofFloat(mArrowImage, View.ROTATION.getName(), 180).start();

            }

            @Override
            public void onCollapseDropDown() {
                ObjectAnimator.ofFloat(mArrowImage, View.ROTATION.getName(), -180, 0).start();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mArrowImage.setRotation(mDropDownView.isExpanded()
                ? 180f : 0f);
    }
}

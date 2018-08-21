package com.jit.appcloud.ui.activity.cultivate;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.bean.PersonalBean;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.UserBBAMResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.ManInfoPpWindow;
import com.luck.picture.lib.decoration.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/05/19.
 *         discription:
 *         注册列表信息 ---------------------------------
 */
public class RegisterListActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.rv_showFarmerList)
    RecyclerView mRvRg;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    private List<PersonalBean> mList = new ArrayList<>();
    private BaseQuickAdapter<PersonalBean, BaseViewHolder> mRgListAdapter;
    private String mAgencyName;
    private int mNowPosition = -1;
    private int mPrePosition = -1;

    private ManInfoPpWindow mManInfoPpWindow;

    @Override
    protected void init() {
        if (getIntent() != null) {
            mAgencyName = getIntent().getStringExtra(AppConst.AGENCY_NAME);
        }

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_farmer_list;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.title_register_list);
        initAdapter();
    }

    private void initAdapter() {
        mRvRg.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvRg.addItemDecoration(new RecycleViewDivider(
                mContext, LinearLayoutManager.HORIZONTAL, UIUtils.dip2px(mContext, 0.5f), UIUtils.getColor(R.color.bg_line_2)));
        mRgListAdapter = new BaseQuickAdapter<PersonalBean, BaseViewHolder>(R.layout.item_farm_info, mList) {
            @Override
            protected void convert(BaseViewHolder helper, PersonalBean item) {
                helper.itemView.setBackground(UIUtils.getDrawable(R.drawable.selector_item_select_state));
                helper.itemView.setSelected(helper.getAdapterPosition() == mNowPosition);
                helper.setText(R.id.tvUserName, item.getRealname());
                ImageView ivHead = helper.getView(R.id.ivUserHead);
                Glide.with(RegisterListActivity.this).load(item.getImage())
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.default_header))
                        .into(ivHead);
            }
        };

        mRgListAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position != mNowPosition) {
                mPrePosition = mNowPosition;
                mNowPosition = position;
                mRgListAdapter.notifyItemChanged(mPrePosition);
                mRgListAdapter.notifyItemChanged(mNowPosition);
            }
            queryPhotosAndShow(position);

        });

        mRvRg.setAdapter(mRgListAdapter);
    }

    private void queryPhotosAndShow(int position){
        ApiRetrofit.getInstance().queryAllPhotos((mList.get(position).getUsername()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photoResponse -> {
                    if (photoResponse.getCode() == 1){
                        mManInfoPpWindow = new ManInfoPpWindow(this);
                        mManInfoPpWindow.setHeadImage(mList.get(position).getImage());
                        mManInfoPpWindow.setUserName(mList.get(position).getRealname());
                        mManInfoPpWindow.setSignature(mList.get(position).getSign());
                        mManInfoPpWindow.setFlag(AppConst.GALLERY_TYPE.SHOW);
                        mManInfoPpWindow.setListGallery(photoResponse.getData());
                        mManInfoPpWindow.showAsDropDown(mAppBarLayout);
                    }else {
                        UIUtils.showToast(photoResponse.getMsg());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

    @Override
    protected void initData() {
        /*加载数据*/
        showWaitingDialog(getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().getMMFMInfo(UserCache.getToken(), mAgencyName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userBBAMResponse -> {
                    hideWaitingDialog();
                    if (userBBAMResponse.getCode() == 1) {
                        mList.clear();
                        mList.addAll(userBBAMResponse.getData());
                        mRgListAdapter.setNewData(mList);
                    } else {
                        UIUtils.showToast(userBBAMResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    LogUtils.e("错误",throwable.getLocalizedMessage());
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
    }

}

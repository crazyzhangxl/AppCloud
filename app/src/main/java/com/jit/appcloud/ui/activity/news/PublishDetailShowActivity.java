package com.jit.appcloud.ui.activity.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.db_model.MyPbNewsBean;
import com.jit.appcloud.ui.activity.MulPictureShowActivity;
import com.jit.appcloud.ui.activity.message.ShowBigImageActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.TimeUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author zxl on 2018/05/25.
 *         discription: 用于咨询展示的活动
 */
public class PublishDetailShowActivity extends BaseActivity {
    @BindView(R.id.img_item_bg)
    ImageView mImgItemBg;
    @BindView(R.id.iv_one_photo)
    ImageView mIvOnePhoto;
    @BindView(R.id.tvPrice)
    TextView mTvPrice;
    @BindView(R.id.tv_one_directors)
    TextView mTvOneDirectors;
    @BindView(R.id.tv_one_genres)
    TextView mTvOneGenres;
    @BindView(R.id.tv_one_day)
    TextView mTvOneDay;
    @BindView(R.id.tvLook)
    TextView mTvLook;
    @BindView(R.id.ll_one_item)
    LinearLayout mLlOneItem;
    @BindView(R.id.fl_Header_view)
    FrameLayout mFlHeaderView;
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.llToolbarTitle)
    LinearLayout mLlToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.tvDes)
    TextView mTvDes;
    @BindView(R.id.rcvPc)
    RecyclerView mRcvPc;
    @BindView(R.id.llAbstract)
    LinearLayout mLlAbstract;
    @BindView(R.id.tvAbstract)
    TextView mTvAbstract;
    @BindView(R.id.llDetail)
    LinearLayout mLlDetail;
    @BindView(R.id.llPicShow)
    LinearLayout mLlPicShow;
    private BaseQuickAdapter<String, BaseViewHolder> mPicShowAdapter;
    private List<String> mListImages = new ArrayList<>();
    private String mImageUrl;
    private MyPbNewsBean mMyPbNewsBean;

    public static void startAction(Activity mContext, MyPbNewsBean myPbNewsBean, View view) {
        Intent intent = new Intent(mContext, PublishDetailShowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConst.NEWS_FROM_MY_TO_EDITOR, myPbNewsBean);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(mContext, view, AppConst.TRANSITION_ANIMATION_NEWS_PHOTOS);
            ActivityCompat.startActivity(mContext, intent, options.toBundle());
        } else {
            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(mContext, intent, options.toBundle());
        }
    }

    @Override
    protected void init() {
        if (getIntent() != null) {
            mMyPbNewsBean = (MyPbNewsBean) getIntent().getSerializableExtra(AppConst.NEWS_FROM_MY_TO_EDITOR);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_publish_detail_show;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.title_ses_news);
        setAdapter();
        loadDetail();
    }

    private void setAdapter() {
        mRcvPc.setLayoutManager(new GridLayoutManager(this, 3));
        mPicShowAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_new_detail_show, mListImages) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                ImageView iv = helper.getView(R.id.ivImage);
                Glide.with(mContext).load(item).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder)).into(iv);
            }
        };
        mPicShowAdapter.setOnItemClickListener((adapter, view, position) -> MulPictureShowActivity.startImagePagerActivity(PublishDetailShowActivity.this, mListImages, position));
        mRcvPc.setAdapter(mPicShowAdapter);
    }

    private void loadDetail() {
        if (mMyPbNewsBean != null) {
            updateViews(mMyPbNewsBean);
        }
    }

    private void updateViews(MyPbNewsBean mGetBean) {
        if (mGetBean.getDiscount() == 1 && mGetBean.getHot() == 1) {
            mTvLook.setText(R.string.news_state_1);
        } else if (mGetBean.getDiscount() == 1) {
            mTvLook.setText(R.string.news_state_2);
        } else if (mGetBean.getHot() == 1) {
            mTvLook.setText(R.string.news_state_3);
        } else {
            mTvLook.setVisibility(View.GONE);
        }
        mTvPrice.setText(String.format("价格: ¥%s", String.valueOf(mGetBean.getPrice())));
        mTvOneDirectors.setText(mGetBean.getUsername());
        if (!TextUtils.isEmpty(mGetBean.getType())) {
            mTvOneGenres.setText(String.format("类型: %s", mGetBean.getType()));
        } else {
            mTvOneGenres.setVisibility(View.GONE);
        }

        mTvOneDay.setText(String.format("发布日期: %s", TimeUtil.getMyTime(mGetBean.getTime())));

        if (!TextUtils.isEmpty(mGetBean.getDescription())) {
            mTvDes.setText(mGetBean.getDescription());
        }else {
            mLlDetail.setVisibility(View.GONE);
        }
        mImageUrl = mGetBean.getImage();
        Glide.with(mContext).load(mImageUrl).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder)).into(mIvOnePhoto);
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                mImgItemBg.setImageDrawable(resource);
            }
        };
        Glide.with(this).load(AppConst.IMAGE_URL_GLIDE)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(23, 4)).error(R.drawable.stackblur_default))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(simpleTarget);
        if (!TextUtils.isEmpty(mGetBean.getSummary())){
            mTvAbstract.setText(mGetBean.getSummary());
        }else {
            mLlAbstract.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(mGetBean.getContent())) {
            mListImages.clear();
            mListImages.addAll(Arrays.asList(mGetBean.getContent().split(", ")));
            mPicShowAdapter.notifyDataSetChanged();
        } else {
            mLlPicShow.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvOnePhoto.setOnClickListener(v -> ShowBigImageActivity.startAction(mContext, mIvOnePhoto, mImageUrl));
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
    }
}

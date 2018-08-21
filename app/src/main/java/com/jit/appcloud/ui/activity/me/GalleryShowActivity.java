package com.jit.appcloud.ui.activity.me;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.response.PhotoResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.widget.viewbigimage.ViewBigImageActivity;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.tools.ScreenUtils;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
/**
 * @author zxl on 2018/07/06.
 *         discription: 照片墙展示的活动
 */
public class GalleryShowActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.picRcv)
    RecyclerView mPicRcv;

    private BaseQuickAdapter<PhotoResponse.DataBean, BaseViewHolder> mAdapter;
    private List<PhotoResponse.DataBean> mMPhotoList = new ArrayList<>();
    private ArrayList<String> mShowList = new ArrayList<>();
    @Override
    protected void init() {
        if (getIntent() != null){
            mMPhotoList = (List<PhotoResponse.DataBean>) getIntent().getExtras().getSerializable(AppConst.EXTRA_PHOTO_LIST);
            for (PhotoResponse.DataBean bean : mMPhotoList){
                mShowList.add(bean.getImage());
            }
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_gallery_show;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.ttle_pic_wall);
        initAdapter();
    }

    private void initAdapter() {
        mPicRcv.setHasFixedSize(true);
        mPicRcv.addItemDecoration(new GridSpacingItemDecoration(3,
                ScreenUtils.dip2px(this, 5), false));
        mPicRcv.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new BaseQuickAdapter<PhotoResponse.DataBean, BaseViewHolder>(R.layout.item_gallery_nice,mMPhotoList) {
            @Override
            protected void convert(BaseViewHolder helper, PhotoResponse.DataBean item) {
                ImageView ivPic =  helper.getView(R.id.iv_picture);
                helper.getView(R.id.ll_check).setVisibility(View.GONE);
                Glide.with(GalleryShowActivity.this).load(item.getImage())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL)).into(ivPic);
                helper.itemView.setOnClickListener(v -> showMulBigImages(helper.getLayoutPosition(),mShowList));
            }
        };
        mPicRcv.setAdapter(mAdapter);
    }

    private void showMulBigImages(int nowPosition,ArrayList<String> mList){
        Bundle bundle = new Bundle();
        bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
        bundle.putInt("code", nowPosition);//第几张
        bundle.putStringArrayList("imageuri",mList);
        jumpToActivity(ViewBigImageActivity.class,bundle);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
    }

}

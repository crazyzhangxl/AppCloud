package com.jit.appcloud.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.response.PhotoResponse;
import com.jit.appcloud.ui.activity.MainActivity;
import com.jit.appcloud.ui.activity.cultivate.RegisterListActivity;
import com.jit.appcloud.ui.activity.me.GalleryActivity;
import com.jit.appcloud.ui.activity.me.GalleryNiceActivity;
import com.jit.appcloud.ui.activity.me.GalleryShowActivity;
import com.jit.appcloud.util.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

/**
 * @author zxl on 2018/6/28.
 *         discription: 个人中心的PopWidow
 */

public class ManInfoPpWindow extends PopupWindow {
    private Context context;
    private View window;
    private Animation animationIn, animationOut;
    private boolean isDismiss = false;
    private final RequestOptions mOptions;
    private List<PhotoResponse.DataBean> mListGallery = new ArrayList<>();
    private BaseQuickAdapter<PhotoResponse.DataBean , BaseViewHolder> mAdapter;
    private LinearLayout mMLlRoot;
    private View mViewMore;
    private ImageView mIvHead;
    private TextView mTvName;
    private TextView mTvSignature;
    private int mFlag;

    public void setListGallery(List<PhotoResponse.DataBean > listGallery) {
        mListGallery.clear();
        mListGallery.addAll(listGallery);
        if (mAdapter != null){
            mAdapter.notifyDataSetChanged();
            if (mListGallery != null && mListGallery.size() != 0){
                mViewMore.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setHeadImage(String mHeadImage){
        if (!TextUtils.isEmpty(mHeadImage)){
            Glide.with(context).load(mHeadImage).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.default_header)).into(mIvHead);
        }
    }

    public void setUserName(String userName){
        if (!TextUtils.isEmpty(userName))
            mTvName.setText(userName);
    }

    public void setSignature(String signature){
        if (!TextUtils.isEmpty(signature))
            mTvSignature.setText(signature);
    }

    public void setFlag(int flag){
        this.mFlag = flag;
    }

    public ManInfoPpWindow(Context context) {
        this.context = context;
        window = LayoutInflater.from(context).inflate(R.layout.dialog_person_info, null);
        this.setContentView(window);
        this.setWidth(UIUtils.getScreenWidth(context));
        this.setHeight(UIUtils.getScreenHeight(context));
        this.setAnimationStyle(R.style.WindowStyle);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        this.setBackgroundDrawable(new ColorDrawable(Color.argb(123, 0, 0, 0)));
        animationIn = AnimationUtils.loadAnimation(context, R.anim.photo_album_show);
        animationOut = AnimationUtils.loadAnimation(context, R.anim.photo_album_dismiss);
        mOptions = new RequestOptions()
                .placeholder(com.luck.picture.lib.R.drawable.ic_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        initView();
    }

    private void initView() {
        RecyclerView rvPic =  window.findViewById(R.id.rcPic);
        mMLlRoot = window.findViewById(R.id.llRoot);
        mViewMore = window.findViewById(R.id.ivMore);
        mIvHead = window.findViewById(R.id.ivHeader);
        mTvName = window.findViewById(R.id.tvName);
        mTvSignature = window.findViewById(R.id.tvSignature);
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPic.setLayoutManager(manager);
        mAdapter = new BaseQuickAdapter<PhotoResponse.DataBean , BaseViewHolder>(R.layout.item_gallery, mListGallery) {
            @Override
            protected void convert(BaseViewHolder helper, PhotoResponse.DataBean  item) {
                if (helper.getAdapterPosition() <= 2) {
                    // 加载图片了
                    ImageView view = helper.getView(R.id.ivPic);
                    Glide.with(context)
                            .asBitmap()
                            .load(item.getImage())
                            .apply(mOptions)
                            .into(new BitmapImageViewTarget(view) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.
                                                    create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCornerRadius(8);
                                    view.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }
            }
        };
        rvPic.setAdapter(mAdapter);
        mViewMore.setOnClickListener(v -> {
            if (mFlag == AppConst.GALLERY_TYPE.EDIT) {
                ((MainActivity) context).jumpToActivity(GalleryNiceActivity.class);
            } else{
                 Bundle bundle = new Bundle();
                 bundle.putSerializable(AppConst.EXTRA_PHOTO_LIST,(Serializable) mListGallery);
                ((RegisterListActivity)context).jumpToActivity(GalleryShowActivity.class,bundle);
            }

            ManInfoPpWindow.this.dismiss();
        });

        mMLlRoot.setOnClickListener(v -> ManInfoPpWindow.this.dismiss());
    }

    @Override
    public void showAsDropDown(View anchor) {
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                Rect rect = new Rect();
                anchor.getGlobalVisibleRect(rect);
                int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                setHeight(h);
            }
            super.showAsDropDown(anchor);
            isDismiss = false;
            mMLlRoot.startAnimation(animationIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        if (isDismiss) {
            return;
        }
        isDismiss = true;
        mMLlRoot.startAnimation(animationOut);
        dismiss();
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isDismiss = false;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                    dismiss4Pop();
                } else {
                    ManInfoPpWindow.super.dismiss();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 在android4.1.1和4.1.2版本关闭PopWindow
     */
    private void dismiss4Pop() {
        new Handler().post(() -> ManInfoPpWindow.super.dismiss());
    }

}

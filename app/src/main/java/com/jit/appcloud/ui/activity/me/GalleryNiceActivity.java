package com.jit.appcloud.ui.activity.me;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.PhotoResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.viewbigimage.ViewBigImageActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.anim.OptAnimationLoader;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.ScreenUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
/**
 * @author zxl on 2018/06/28.
 *         discription: 仿pic超好看的照片墙,可编辑修改--------
 */
public class GalleryNiceActivity extends BaseActivity {
    @BindView(R.id.picRcv)
    RecyclerView mPicRcv;
    @BindView(R.id.picture_id_preview)
    TextView mPictureIdPreview;
    @BindView(R.id.picture_tv_ok)
    TextView mPictureTvOk;
    @BindView(R.id.id_ll_ok)
    LinearLayout mIdLlOk;
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tvAdd)
    TextView mTvAdd;
    @BindView(R.id.picture_tv_img_num)
    TextView mPictureTvImgNum;
    private Animation animation;
    private List<PhotoResponse.DataBean> mTotalList = new ArrayList<>();
    private List<PhotoResponse.DataBean> mSelectList = new ArrayList<>();
    private BaseQuickAdapter<PhotoResponse.DataBean, BaseViewHolder> mAdapter;
    private List<LocalMedia> mMulSelectList;
    private ArrayList<String> mTotalStrList = new ArrayList<>();
    private ArrayList<String> mSelectStrList = new ArrayList<>();
    private List<File> mPhotoSubmit = new ArrayList<>();
    @Override
    protected void init() {
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_gallery_nice;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvAdd.setVisibility(View.VISIBLE);
        animation = OptAnimationLoader.loadAnimation(this, R.anim.modal_in);
        mTvToolbarTitle.setText(R.string.title_my_gallery);
        initAdapter();
    }

    private void initAdapter() {
        mPicRcv.setHasFixedSize(true);
        mPicRcv.addItemDecoration(new GridSpacingItemDecoration(3,
                ScreenUtils.dip2px(this, 5), false));
        mPicRcv.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new BaseQuickAdapter<PhotoResponse.DataBean, BaseViewHolder>(R.layout.item_gallery_nice, mTotalList) {
            @Override
            protected void convert(BaseViewHolder helper, PhotoResponse.DataBean item) {
                ImageView imageView = helper.getView(R.id.iv_picture);
                ImageView ivCheck = helper.getView(R.id.ivCheck);
                if (item.isChecked()){
                    ivCheck.setImageBitmap(UIUtils.getBitmap(R.drawable.sel));
                    imageView.setColorFilter(UIUtils.getColor(R.color.image_overlay_true), PorterDuff.Mode.SRC_ATOP);
                }else {
                    ivCheck.setImageBitmap(UIUtils.getBitmap(R.drawable.def));
                    imageView.setColorFilter(UIUtils.getColor(R.color.image_overlay_false), PorterDuff.Mode.SRC_ATOP);

                }
                Glide.with(GalleryNiceActivity.this)
                        .asBitmap()
                        .load(item.getImage())
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop().placeholder(R.drawable.image_placeholder))
                        .into(imageView);

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMulBigImages(helper.getLayoutPosition(),mTotalStrList);
                    }
                });

                helper.getView(R.id.ll_check).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /* 改变cb的状态*/
                        boolean isChecked =  item.isChecked();
                        if (isChecked) {
                            for (PhotoResponse.DataBean bean : mSelectList) {
                                if (bean.getImage().equals(item.getImage())) {
                                    mSelectList.remove(bean);
                                    mSelectStrList.remove(bean.getImage());
                                    break;
                                }
                            }
                            mSelectStrList.remove(item.getImage());
                        } else {
                            if (animation != null) {
                                ivCheck.startAnimation(animation);
                            }
                            mSelectList.add(item);
                            mSelectStrList.add(item.getImage());
                            /* 这个设置 color真的牛皮-----------------*/
                        }
                        mTotalList.get(helper.getAdapterPosition()).setChecked(!isChecked);
                        notifyItemChanged(helper.getAdapterPosition());
                        changeBottomState();
                    }
                });
            }
        };

        mAdapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.gallery_empty_view,null));
        mPicRcv.setAdapter(mAdapter);
    }

    /**
     *  改变底部状态栏的状态
     * */
    private void changeBottomState() {
        boolean enable = mSelectList.size() != 0;
        if (enable) {
            mIdLlOk.setEnabled(enable);
            mPictureIdPreview.setEnabled(true);
            mPictureIdPreview.setSelected(true);
            mPictureTvOk.setSelected(true);
            mPictureTvOk.setText("删除");

            mTvAdd.setSelected(true);
            mTvAdd.setEnabled(false);
        } else {
            mIdLlOk.setEnabled(false);
            mPictureIdPreview.setEnabled(false);
            mPictureIdPreview.setSelected(false);
            mPictureTvOk.setSelected(false);
            mPictureTvOk.setText("请选择");

            mTvAdd.setSelected(false);
            mTvAdd.setEnabled(true);
        }
    }


    @Override
    protected void initData() {
        showWaitingDialog(getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().queryAllPhotos(UserCache.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photoResponse -> {
                    hideWaitingDialog();
                    if (photoResponse.getCode() == 1){
                        mTotalList.clear();
                        mTotalList.addAll(photoResponse.getData());
                        filterList();
                        mAdapter.notifyDataSetChanged();
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    @Override
    protected void initListener() {
        mTvAdd.setOnClickListener(v -> chooseMulPictureEvent());
        mPictureTvOk.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<mSelectList.size();i++){
                sb.append(String.valueOf(mSelectList.get(i).getId())).append("-");
            }
            deletePhotoByServer(sb.substring(0,sb.lastIndexOf("-")));
        });

        mPictureIdPreview.setOnClickListener(v -> showMulBigImages(0,mSelectStrList));

        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
    }

    private void showMulBigImages(int nowPosition,ArrayList<String> mList){
        Bundle bundle = new Bundle();
        bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
        bundle.putInt("code", nowPosition);//第几张
        bundle.putStringArrayList("imageuri",mList);
        jumpToActivity(ViewBigImageActivity.class,bundle);
    }

    public void chooseMulPictureEvent() {
        PictureSelector.create(GalleryNiceActivity.this)
                .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(5)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(false)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(true)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(mMulSelectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    mMulSelectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    mPhotoSubmit.clear();
                    for (LocalMedia media : mMulSelectList) {
                        LogUtils.e("图片路径",media.getPath());
                        mPhotoSubmit.add(new File(media.getPath()));
                    }
                    mMulSelectList.clear();
                    submitPhotoToServer();
                    break;
                default:
                    break;
            }
        }
    }
    private void deletePhotoByServer(String ids){
        showWaitingDialog(getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().deleteSMPhotos(ids)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(response -> {
                    if (response !=null && response.getCode() == 1){
                        return ApiRetrofit.getInstance().queryAllPhotos(UserCache.getName());
                    }
                    return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photoResponse -> {
                    hideWaitingDialog();
                    if (photoResponse.getCode() == 1){
                        mTotalList.clear();
                        mTotalList.addAll(photoResponse.getData());
                        filterList();
                        mSelectList.clear();
                        mSelectStrList.clear();
                        changeBottomState();
                        mAdapter.notifyDataSetChanged();
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    private void submitPhotoToServer() {
        showWaitingDialog(getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().addSMPhotos(mPhotoSubmit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photoResponse -> {
                    hideWaitingDialog();
                    if (photoResponse.getCode() == 1){
                        mTotalList.clear();
                        mTotalList.addAll(photoResponse.getData());
                        filterList();
                        mAdapter.notifyDataSetChanged();
                    }else {
                        UIUtils.showToast(photoResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });

    }

    private void filterList() {
        mTotalStrList.clear();
        for (PhotoResponse.DataBean bean:mTotalList){
            mTotalStrList.add(bean.getImage());
        }
    }

}

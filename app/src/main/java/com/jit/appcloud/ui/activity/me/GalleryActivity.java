package com.jit.appcloud.ui.activity.me;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.bean.GyImageBean;
import com.jit.appcloud.model.bean.MulPicBean;
import com.jit.appcloud.ui.activity.news.PublishInfoActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.Check;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.viewbigimage.ViewBigImageActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zxl on 2018/06/27.
 *         discription: 照片墙的界面
 */
public class GalleryActivity extends BaseActivity {
    @BindView(R.id.rvGallery)
    RecyclerView mRvGallery;
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.llToolbarTitle)
    LinearLayout mLlToolbarTitle;
    @BindView(R.id.llAdd)
    LinearLayout mTvAdd;
    @BindView(R.id.llMinus)
    LinearLayout mTvMinus;
    @BindView(R.id.llGallery)
    LinearLayout mLlGallery;
    @BindView(R.id.checkEditor)
    CheckBox mCheckEditor;
    private int mSelectPosition = -1;
    private BaseQuickAdapter<GyImageBean,BaseViewHolder> mGyAdapter;
    private List<GyImageBean> mGyList = new ArrayList<>();
    private ArrayList<String> mStrList = new ArrayList<>();
    private List<LocalMedia> mMulSelectList;
    @Override
    protected void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("个人相册");
        mLlGallery.setVisibility(View.VISIBLE);
        setActionState();
        initAdapter();

    }

    private void initAdapter() {
        mRvGallery.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mGyAdapter = new BaseQuickAdapter<GyImageBean, BaseViewHolder>(R.layout.item_gallery_image,mGyList) {
            @Override
            protected void convert(BaseViewHolder helper, GyImageBean item) {
                if (mSelectPosition == helper.getAdapterPosition()){
                    helper.getView(R.id.hover).setVisibility(View.VISIBLE);
                }else {
                    helper.getView(R.id.hover).setVisibility(View.GONE);
                }

                Glide.with(mContext).load(item.getImagePath()).into((ImageView) helper.getView(R.id.ivGallery));
            }
        };
        mGyAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mCheckEditor.isChecked()){
                mSelectPosition = position;
                mGyAdapter.notifyDataSetChanged();
            }else {
                // 进行展示 --------------
                Bundle bundle = new Bundle();
                bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
                bundle.putInt("code", position);//第几张
                bundle.putStringArrayList("imageuri",mStrList);
                Intent intent = new Intent(GalleryActivity.this, ViewBigImageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRvGallery.setAdapter(mGyAdapter);
    }

    @Override
    protected void initData() {

    }

    public void chooseMulPictureEvent() {
        PictureSelector.create(GalleryActivity.this)
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
    protected void initListener() {
        mCheckEditor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mTvAdd.setVisibility(View.VISIBLE);
                    mTvMinus.setVisibility(View.VISIBLE);
                }else {
                    mTvAdd.setVisibility(View.GONE);
                    mTvMinus.setVisibility(View.GONE);
                    mSelectPosition = -1;
                    mGyAdapter.notifyDataSetChanged();
                }
            }
        });

        mIvToolbarNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMulPictureEvent();
            }
        });

        mTvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectPosition == -1){
                    UIUtils.showToast("请先选择一张图片");
                }else {
                    mGyList.remove(mSelectPosition);
                    mStrList.remove(mSelectPosition);
                    mGyAdapter.notifyDataSetChanged();
                    mSelectPosition = -1;
                }
            }
        });
    }

    private void setActionState(){
        if (mCheckEditor.isChecked()){

            mTvAdd.setVisibility(View.VISIBLE);
            mTvMinus.setVisibility(View.VISIBLE);
        }else {
            mTvAdd.setVisibility(View.GONE);
            mTvMinus.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case PictureConfig.CHOOSE_REQUEST:
                    mMulSelectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的

                    for (LocalMedia media : mMulSelectList) {
                        mGyList.add(new GyImageBean(media.getPath()));
                        mStrList.add(media.getPath());
                    }
                    mGyAdapter.notifyDataSetChanged();
                    mMulSelectList.clear();
                    break;
            }
        }
    }
}

package com.jit.appcloud.ui.activity.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.response.NormalResponse;
import com.jit.appcloud.ui.activity.news.PublishInfoActivity;
import com.jit.appcloud.ui.adapter.multi.CircleTalkAdapter;
import com.jit.appcloud.ui.adapter.multi.TalkPicItem;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.viewbigimage.ViewBigImageActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/08/02.
 *         discription: 发布说说的活动
 */
public class PublishTalkActivity extends BaseActivity {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.etContent)
    EditText mEtContent;
    @BindView(R.id.view_gad)
    View mViewGad;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tvSave)
    TextView mTvSave;
    private List<TalkPicItem> mDataList = new ArrayList<>();
    private CircleTalkAdapter mCircleTalkAdapter;

    private ArrayList<String> mSource = new ArrayList<>();
    private List<LocalMedia> mMulSelectList;
    public int mPicNum = AppConst.TOTAL_TALK_COUNT; // 可选数量
    @Override
    protected void init() {
        mDataList.add(new TalkPicItem());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_publish_talk;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("发表说说");
        initAdapter();
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3,LinearLayoutManager.VERTICAL,false));
        mCircleTalkAdapter = new CircleTalkAdapter(mDataList);
        mCircleTalkAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()){
                case R.id.rlAddTalk:// 添加图片
                    chooseMulPictureEvent();
                    break;
                case R.id.imgPhoto: // 查看图片
                    if (!mCircleTalkAdapter.isShadowStatus()) {
                        showDetailImage(position);
                    }else {
                        mCircleTalkAdapter.setShadowChange();
                    }
                    break;
                case R.id.imgDelete:// 删除图片
                    mSource.remove(position);
                    mCircleTalkAdapter.remove(position);
                    defAddIsNeed();
                    break;
                default:
                    break;
            }
        });
        mCircleTalkAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.imgPhoto){
                mCircleTalkAdapter.setShadowChange();
            }
            return false;
        });

        mRecyclerView.setAdapter(mCircleTalkAdapter);

    }

    private void defAddIsNeed() {
        TalkPicItem talkPicItem = mDataList.get(mDataList.size() - 1);
        if (talkPicItem.getItemType() != TalkPicItem.ADD_PIC){
            mCircleTalkAdapter.addData(new TalkPicItem());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null
                    && getCurrentFocus().getWindowToken() != null) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mTvSave.setOnClickListener(v -> {
            checkData();
        });



    }

    private void checkData() {
        if (TextUtils.isEmpty(mEtContent.getText().toString()) || mSource.size() == 0){
            UIUtils.showToast("发表内容不能为空!");
            return;
        }
        showWaitingDialog(getString(R.string.str_please_waiting));
        List<File> mListContent = new ArrayList<>();
        for (String filePath:mSource){
            mListContent.add(new File(filePath));
        }
        ApiRetrofit.getInstance().publishMyCircleInfo(mEtContent.getText().toString(),mListContent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    hideWaitingDialog();
                    if (response != null && response.getCode() == 1){
                        BroadcastManager.getInstance(PublishTalkActivity.this)
                                .sendBroadcast(AppConst.REFRESH_FRIEND_CIRCLE);
                        finish();
                    }else {
                        UIUtils.showToast(response.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });

    }

    /**
     * 添加数据并且设置显示
     */
    private void addDataAndShow() {
        mDataList.remove(mDataList.size()-1);
        mPicNum = AppConst.TOTAL_TALK_COUNT - mMulSelectList.size();
        for (LocalMedia localMedia:mMulSelectList){
            mDataList.add(new TalkPicItem(new File(localMedia.getPath())));
            mSource.add(localMedia.getPath());
        }
        if (mDataList.size() != AppConst.TOTAL_TALK_COUNT){
            mDataList.add(new TalkPicItem());
        }
        mCircleTalkAdapter.notifyDataSetChanged();
        mMulSelectList.clear();
    }

    /**
     * 展示具体的图片咯
     */
    private void showDetailImage(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
        bundle.putInt("code", position);//第几张
        bundle.putStringArrayList("imageuri",mSource);
        jumpToActivity(ViewBigImageActivity.class,bundle);
    }

    public void chooseMulPictureEvent() {
        PictureSelector.create(PublishTalkActivity.this)
                .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(mPicNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
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
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case PictureConfig.CHOOSE_REQUEST:
                    // 已经选中的图片
                    mMulSelectList = PictureSelector.obtainMultipleResult(data);
                    mPicNum = mPicNum - mMulSelectList.size(); // 设置最大的可选择数量
                    addDataAndShow();
                    break;
                default:
                    break;
            }
        }
    }


}

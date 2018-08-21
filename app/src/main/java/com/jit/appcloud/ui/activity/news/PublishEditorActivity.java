package com.jit.appcloud.ui.activity.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.MyPbNewsBean;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.bean.MulPicBean;
import com.jit.appcloud.model.request.PublishProductRequest;
import com.jit.appcloud.ui.activity.MulPictureShowActivity;
import com.jit.appcloud.ui.activity.message.ShowBigImageActivity;
import com.jit.appcloud.ui.adapter.PicEditorMulAdapter;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.CheckUtils;
import com.jit.appcloud.util.RxDownLoad;
import com.jit.appcloud.util.UIUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/05/28.
 *         discription: 编辑资讯的活动
 */
public class PublishEditorActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.tvPondName)
    TextView mTvPondName;
    @BindView(R.id.etInputTitle)
    EditText mEtInputTitle;
    @BindView(R.id.ivHeadPic)
    ImageView mIvHeadPic;
    @BindView(R.id.cv)
    CardView mCv;
    @BindView(R.id.flAdd)
    FrameLayout mFlAdd;
    @BindView(R.id.delete)
    ImageView mDelete;
    @BindView(R.id.tvAddHeadPic)
    TextView mTvAddHeadPic;
    @BindView(R.id.etAbstract)
    EditText mEtAbstract;
    @BindView(R.id.spNewsHead)
    MaterialSpinner mSpNewsHead;
    @BindView(R.id.etInputPrice)
    EditText mEtInputPrice;
    @BindView(R.id.cbDiscount)
    CheckBox mCbDiscount;
    @BindView(R.id.cbHot)
    CheckBox mCbHot;
    @BindView(R.id.cbNet)
    CheckBox mCbNet;
    @BindView(R.id.cbPic)
    CheckBox mCbPic;
    @BindView(R.id.etNetContent)
    EditText mEtNetContent;
    @BindView(R.id.rlNet)
    RelativeLayout mRlNet;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.rc_pic)
    RecyclerView mRcPic;
    @BindView(R.id.llPic)
    LinearLayout mLlPic;
    private MyPbNewsBean mListBean;
    private PicEditorMulAdapter mPicSelectMulAdapter;
    private List<MulPicBean> mList = new ArrayList<>();
    private List<String> mSource = new ArrayList<>();
    private List<LocalMedia> mMulSelectList;
    public int mPicNum = AppConst.TOTAL_COUNT; // 可选数量
    public List<LocalMedia> mSingleSelectList = new ArrayList<>();
    private boolean isHeadPicAdd = false;
    private String mStrTitle;
    private String mInfoKind;
    private String mStrPrice;
    private String mMStrComment;
    private File mFileImage;
    public List<File> mMyFileList = new ArrayList<>();// 用于提交的mMyFileList
    private String mSummary;
    private String mHeadImagePath;


    public List<String> mTrueList = new ArrayList<>();
    private boolean isPic;


    @Override
    protected void init() {
        if (getIntent() != null) {
            mListBean = (MyPbNewsBean) getIntent().getExtras().
                    getSerializable(AppConst.NEWS_FROM_MY_TO_EDITOR);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_publish_editor;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.title_editor_news);
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(UIUtils.getString(R.string.str_title_save));
        mSpNewsHead.setItems(AppConst.SP_PRODUCT_TYPE);
        setMultiAdapter();
        setDataFromBefore();
    }

    /**
     *  加载上一个活动传递过来的数值  步骤1 先做正常的显示吧
        至于提交后面再进行
     * */
    private void setDataFromBefore() {
        showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        if (!TextUtils.isEmpty(mListBean.getTitle())) {
            mEtInputTitle.setText(mListBean.getTitle());
            mEtInputTitle.setSelection(mListBean.getTitle().length());
        }

        if (!TextUtils.isEmpty(mListBean.getImage())) {
            isHeadPicAdd = true;
            mTvAddHeadPic.setVisibility(View.GONE);
            mFlAdd.setVisibility(View.VISIBLE);
            mDelete.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mListBean.getImage()).into(mIvHeadPic);
            /* 先加载然后再下载 */
            RxDownLoad.saveImageAndGetPathObservable(mContext, mListBean.getImage(), "xx")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(uri -> mHeadImagePath = uri.getPath());
        }

        if (!TextUtils.isEmpty(mListBean.getSummary())){
            mEtAbstract.setText(mListBean.getSummary());
        }

        if (mListBean.getHot() == 1){
            mCbHot.setChecked(true);
        }

        if (mListBean.getDiscount() == 1){
            mCbDiscount.setChecked(true);
        }


        if (AppConst.MSG_TYPE.NET.equals(mListBean.getMsg_type())){
            mLlPic.setVisibility(View.GONE);
            mRlNet.setVisibility(View.VISIBLE);
            mCbNet.setChecked(true);
            mEtNetContent.setText(mListBean.getDescription());
        }else {
            mCbPic.setChecked(true);
            if (!TextUtils.isEmpty(mListBean.getContent())) {
                mList.clear();
                List<String> mFileList = Arrays.asList(mListBean.getContent().split(", "));
                for (String filePath : mFileList) {
                    mList.add(new MulPicBean(filePath, AppConst.PicType.SHOW));
                }
                mPicNum = AppConst.TOTAL_COUNT - mList.size();
                if (mList.size() <= 5) {
                    mList.add(new MulPicBean(null, AppConst.PicType.ADD_BUTTON));
                }
                mPicSelectMulAdapter.notifyDataSetChanged();
                mTrueList.clear();
            /*  =============== 这下面的是真实路径 =============== */
                for (int i = 0; i < mFileList.size(); i++) {
                    RxDownLoad.saveImageAndGetPathObservable(mContext, mFileList.get(i), String.valueOf(i))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(uri -> {
                                mTrueList.add(uri.getPath());
                            });
                }
            }

            if (!TextUtils.isEmpty(mListBean.getDescription())) {
                mEtComment.setText(mListBean.getDescription());
                mEtComment.setSelection(mListBean.getDescription().length());
            }
        }

        if (!TextUtils.isEmpty(mListBean.getType())) {
            List<String> typeList = Arrays.asList(AppConst.SP_PRODUCT_TYPE);
            if (typeList.contains(mListBean.getType())) {
                mSpNewsHead.setSelectedIndex(typeList.indexOf(mListBean.getType()));
            }
        }
        mEtInputPrice.setText(String.valueOf(mListBean.getPrice()));
        hideWaitingDialog();
    }

    private void setMultiAdapter() {
        if (mPicSelectMulAdapter == null) {
            mList.clear();
            mList.add(new MulPicBean(null, AppConst.PicType.ADD_BUTTON));
            mPicSelectMulAdapter = new PicEditorMulAdapter(mContext, mList, this);
            mRcPic.setLayoutManager(new GridLayoutManager(this, 3));
            mPicSelectMulAdapter.setOnItemClickListener((helper, parent, itemView, position) -> {
                if (mList.get(position).getType() == AppConst.PicType.ADD_BUTTON) {
                    if (mPicSelectMulAdapter.isDeleting()) {
                        mPicSelectMulAdapter.setDeleting(false);
                    }
                    chooseMulPictureEvent();
                } else {
                    if (mPicSelectMulAdapter.isDeleting()) {
                        mPicSelectMulAdapter.setDeleting(false);
                        mPicSelectMulAdapter.notifyDataSetChanged();
                    } else {
                        mSource.clear();
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).getType() == AppConst.PicType.SHOW) {
                                mSource.add(mList.get(i).getfilePath());
                            }
                        }
                        MulPictureShowActivity.startImagePagerActivity(PublishEditorActivity.this,
                                mSource, helper.getAdapterPosition());
                    }
                }
            });

            mPicSelectMulAdapter.setOnItemLongClickListener((helper, parent, itemView, position) -> {
                mPicSelectMulAdapter.setDeleting(true);
                mPicSelectMulAdapter.notifyDataSetChanged();
                return false;
            });

        }
        mRcPic.setAdapter(mPicSelectMulAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mIvHeadPic.setOnClickListener(v -> ShowBigImageActivity.startAction(mContext, mIvHeadPic, mHeadImagePath));
        mTvAddHeadPic.setOnClickListener(v -> {
            mSingleSelectList.clear();
            chooseSinglePictureEvent();
        });

        mDelete.setOnClickListener(v -> {
            mDelete.setVisibility(View.GONE);
            mFlAdd.setVisibility(View.GONE);
            mTvAddHeadPic.setVisibility(View.VISIBLE);
            isHeadPicAdd = false;
        });

        mTvPublishNow.setOnClickListener(v -> verifyData());


        // 修正======================================
        mCbPic.setOnClickListener(v -> {
            if (mCbNet.isChecked()){
                mCbNet.setChecked(false);
                mRlNet.setVisibility(View.GONE);
            }
            boolean checked = mCbPic.isChecked();
            if (!checked){
                mLlPic.setVisibility(View.GONE);
            }else {
                mLlPic.setVisibility(View.VISIBLE);
            }
        });

        mCbNet.setOnClickListener(v -> {
            if (mCbPic.isChecked()){
                mCbPic.setChecked(false);
                mLlPic.setVisibility(View.GONE);
            }
            boolean checked = mCbNet.isChecked();
            if (!checked){
                mRlNet.setVisibility(View.GONE);
            }else {
                mRlNet.setVisibility(View.VISIBLE);
            }
        });
    }

    private void verifyData() {
        mStrTitle = mEtInputTitle.getText().toString();
        if (TextUtils.isEmpty(mStrTitle)) {
            UIUtils.showToast(getString(R.string.please_input_news_title));
            return;
        }

        if (!isHeadPicAdd) {
            UIUtils.showToast(getString(R.string.please_input_news_head_image));
            return;
        }

        mSummary = mEtAbstract.getText().toString();
        mStrPrice = mEtInputPrice.getText().toString();
        if (TextUtils.isEmpty(mStrPrice)) {
            UIUtils.showToast(getString(R.string.please_input_price));
            return;
        }


        if (!mCbNet.isChecked() && !mCbPic.isChecked()){
            UIUtils.showToast(getString(R.string.please_input_msg_type));
            return;
        }

        if (mCbNet.isChecked()){
            /* --------- 选择了网址内容 -----------*/
            mMStrComment = mEtNetContent.getText().toString();
            if (TextUtils.isEmpty(mMStrComment)){
                UIUtils.showToast(getString(R.string.please_net_not_null));
                return;
            }else if (!CheckUtils.isNetUrl(mMStrComment)){
                UIUtils.showToast(getString(R.string.invalid_net));
                return;
            }
            isPic = false;
        }else {
            mMStrComment = mEtComment.getText().toString();
            for (int i = 0; i < mTrueList.size(); i++) {
                mMyFileList.add(new File(mTrueList.get(i)));
            }
            if (TextUtils.isEmpty(mMStrComment) && mMyFileList.size() == 0) {
                UIUtils.showToast(getString(R.string.please_pic_content_not_null));
                return;
            }
            isPic = true ;
        }

        mFileImage = new File(mHeadImagePath);
        mInfoKind = AppConst.SP_PRODUCT_TYPE[mSpNewsHead.getSelectedIndex()];
        showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        PublishProductRequest request = new PublishProductRequest();
        request.setTitle(mStrTitle);
        request.setType(mInfoKind);
        request.setDescription(mMStrComment);
        request.setPrice(Float.parseFloat(mStrPrice));
        request.setImage(mFileImage);
                /* ----- 图文内容 ---- */
        if (isPic){
            if (mMyFileList.size()!=0) {
                request.setContent(mMyFileList);
            }
            request.setMsg_type(AppConst.MSG_TYPE.PIC);
        }else {
            request.setMsg_type(AppConst.MSG_TYPE.NET);
        }
        request.setDiscount(mCbDiscount.isChecked()?1:0);
        request.setHot(mCbHot.isChecked()?1:0);
        request.setSummary(TextUtils.isEmpty(mSummary) ?"":mSummary);
        ApiRetrofit.getInstance().updateNewsById(mListBean.getNewsId(),request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(publishProductResponse -> {
                    hideWaitingDialog();
                    if (publishProductResponse.getCode() == 1) {
                        deleteFile();
                        UIUtils.showToast(getString(R.string.str_editor_publish_success));
                        MyPbNewsBean myPbNewsBean = publishProductResponse.getData();
                        myPbNewsBean.setNewsId(myPbNewsBean.getId());
                        DBManager.getInstance().saveOrUpdateNewsPb(myPbNewsBean);
                        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_MY_PB);
                        finish();
                    } else {
                        UIUtils.showToast(publishProductResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    private void deleteFile() {
        File appDir = new File(Environment.getExternalStorageDirectory(), AppConst.PS_SAVE_LEAST_DIR);
        if (appDir.exists()) {
            File[] files = appDir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            appDir.delete();
        }
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }

    public void chooseMulPictureEvent() {
        PictureSelector.create(PublishEditorActivity.this)
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

    public void chooseSinglePictureEvent() {
        PictureSelector.create(PublishEditorActivity.this)
                .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE : PictureConfig.SINGLE
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
                .selectionMedia(mSingleSelectList)// 是否传入已选图片
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
                .forResult(PictureConfig.SINGLE);//结果回调onActivityResult code
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
                    mList.remove(mList.size() - 1); // 移除最后一个尾巴
                    for (LocalMedia media : mMulSelectList) {
                        Log.i("图片-----》", media.getPath());
                        mTrueList.add(media.getPath());
                        mList.add(new MulPicBean(media.getPath(), AppConst.PicType.SHOW));
                        mPicNum--;
                    }
                    if (mList.size() <= 5) {
                        mList.add(new MulPicBean(null, AppConst.PicType.ADD_BUTTON));
                    }
                    mPicSelectMulAdapter.notifyDataSetChanged();
                    mMulSelectList.clear();
                    break;

                case PictureConfig.SINGLE:
                    mSingleSelectList = PictureSelector.obtainMultipleResult(data);
                    mHeadImagePath = mSingleSelectList.get(0).getPath();
                    // 改变图片选择状态
                    isHeadPicAdd = true;
                    if (isHeadPicAdd) {
                        mTvAddHeadPic.setVisibility(View.GONE);
                        mFlAdd.setVisibility(View.VISIBLE);
                        mDelete.setVisibility(View.VISIBLE);
                    }
                    Glide.with(mContext).load(mHeadImagePath).into(mIvHeadPic);
                    // 加载图片
                    break;
                default:
                    break;
            }
        }
    }


}

package com.jit.appcloud.ui.activity.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.ui.activity.me.EditInfoActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.CreateGroupAtPresenter;
import com.jit.appcloud.ui.view.ICreateGroupAtView;
import com.jit.appcloud.util.UIUtils;
import com.lqr.recyclerview.LQRRecyclerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zxl on 2018/07/23.
 *         discription: 发起群聊的活动
 */
public class CreateGroupActivity extends BaseActivity<ICreateGroupAtView, CreateGroupAtPresenter> implements ICreateGroupAtView {
    @BindView(R.id.rvContacts)
    LQRRecyclerView mRvContacts;
    @BindView(R.id.llTop)
    LinearLayout mLlTop;
    @BindView(R.id.llDt)
    LinearLayout mLlDt;
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tvNameSelected)
    EditText mEtName;
    @BindView(R.id.llName)
    LinearLayout mLlNameGroup;

    public List<LocalMedia> mSingleSelectList = new ArrayList<>();
    /**
     * 用于已选择的客户
     */
    public ArrayList<String> mSelectedTeamMemberAccounts;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.vGroupImage)
    ImageView mVGroupImage;
    @BindView(R.id.btnSetImage)
    Button mBtnSetImage;
    @BindView(R.id.btnDelImage)
    Button mBtnDelImage;

    private String mGroupFlag;


    @Override
    protected void init() {
        mGroupFlag = getIntent().getStringExtra(AppConst.EXTRA_FLAG_GROUP);
        mSelectedTeamMemberAccounts = getIntent().getStringArrayListExtra("selectedMember");
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_create_group;
    }

    @Override
    protected CreateGroupAtPresenter createPresenter() {
        return new CreateGroupAtPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mEtName.clearFocus();
        defIsAddMb();
    }

    /**
     * 根据选中的任务和
     */
    private void defIsAddMb() {
        if (AppConst.GROUP_FLAG_CREATE.equals(mGroupFlag)){
            mTvToolbarTitle.setText("发起群聊");
            mTvPublishNow.setVisibility(View.VISIBLE);
            mTvPublishNow.setText("确认发起");
        }else {
            mTvToolbarTitle.setText("选择联系人");
            mLlTop.setVisibility(View.GONE);
            mLlDt.setVisibility(View.GONE);
            mTvPublishNow.setVisibility(View.VISIBLE);
            mTvPublishNow.setText("确定");
        }
    }

    @Override
    protected void initData() {
        mPresenter.loadContacts();
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mTvPublishNow.setOnClickListener(v -> {
            if (AppConst.GROUP_FLAG_CREATE.equals(mGroupFlag)) {
                if (TextUtils.isEmpty(mEtName.getText().toString())){
                    UIUtils.showToast("客官请输入群聊名称!");
                    return;
                }

                if (mSingleSelectList.size() == 0){
                    UIUtils.showToast("请选择让群聊头像!");
                    return;
                }

                mPresenter.createGroup(mEtName.getText().toString(),mSingleSelectList.get(0).getPath());
            } else {
                //添加群成员
                mPresenter.addGroupMembers();
            }
        });

        mBtnSetImage.setOnClickListener(v -> {
            if (mSingleSelectList.size() == 1){
                return;
            }
            chooseSinglePictureEvent();
        });

        mBtnDelImage.setOnClickListener(v -> {
            if (mSingleSelectList.size() == 0){
                return;
            }
            mSingleSelectList.clear();
            mVGroupImage.setImageBitmap(null);
        });
    }


    @Override
    public TextView getTextSend() {
        return mTvPublishNow;
    }

    @Override
    public LQRRecyclerView getRvContacts() {
        return mRvContacts;
    }


    @Override
    public EditText getNameText() {
        return mEtName;
    }


    private void chooseSinglePictureEvent() {
        PictureSelector.create(CreateGroupActivity.this)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.SINGLE:
                    mSingleSelectList = PictureSelector.obtainMultipleResult(data);
                    String filePath = mSingleSelectList.get(0).getPath();
                    Glide.with(this).load(filePath).into(mVGroupImage);
                    break;
                default:
                    break;
            }
        }
    }
}

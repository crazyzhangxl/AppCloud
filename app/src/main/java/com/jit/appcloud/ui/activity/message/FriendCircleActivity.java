package com.jit.appcloud.ui.activity.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.bean.CommentBean;
import com.jit.appcloud.model.bean.CommentConfig;
import com.jit.appcloud.model.bean.CommentItem;
import com.jit.appcloud.model.bean.FavortItem;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.FriendCircleResponse;
import com.jit.appcloud.model.response.NormalResponse;
import com.jit.appcloud.ui.activity.news.PublishInfoActivity;
import com.jit.appcloud.ui.adapter.FriendCircleAdapter;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.CommentListView;
import com.jit.appcloud.widget.ZoneHeaderView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/07/31.
 *         discription: 朋友圈的活动
 */
public class FriendCircleActivity extends BaseActivity implements FriendCircleAdapter.onNeedsIconClickListener {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.rvContent)
    RecyclerView mRvContent;
    @BindView(R.id.circleEt)
    EditText mCircleEt;
    @BindView(R.id.sendIv)
    ImageView mSendIv;
    @BindView(R.id.editTextBodyLl)
    LinearLayout mEditTextBodyLl;
    @BindView(R.id.fabRf)
    FloatingActionButton mFabRf;
    @BindView(R.id.fabChangeGl)
    FloatingActionButton mFabChangeGl;
    @BindView(R.id.fabPublish)
    FloatingActionButton mFabPublish;
    @BindView(R.id.menu_red)
    FloatingActionMenu mMenuRed;

    /**
     * 顶部的View
     */
    private ZoneHeaderView mZoneHeaderView;
    private FriendCircleAdapter mFriendCircleAdapter;
    private List<FriendCircleResponse.DataBean> mData = new ArrayList<>();
    private LinearLayoutManager mManager;

    private CommentConfig mCommentConfig;
    private int mScreenHeight;
    private int mEditTextBodyHeight;
    private int mCurrentKeyboardH;
    private int mSelectCircleItemH;
    private int mSelectCommentItemOffset;
    private List<LocalMedia> mSingleSelectList;


    @Override
    protected void init() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(this).register(AppConst.REFRESH_FRIEND_CIRCLE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initData();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(this).unregister(AppConst.REFRESH_FRIEND_CIRCLE);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_friend_circle;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("朋友圈");
        mZoneHeaderView = new ZoneHeaderView(this);
        mZoneHeaderView.setData(UserCache.getName(),UserCache.getHeadImage());
        // 设置空白区域点击收起 floatActionBar
        mMenuRed.setClosedOnTouchOutside(true);
        initAdapter();
        //监听recyclerview滑动
        setViewTreeObserver();
    }

    private void initAdapter() {
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvContent.setLayoutManager(mManager);

        //监听列表滑动
        mRvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean isSignificantDelta = Math.abs(dy) > ViewConfiguration.getTouchSlop();
                if (isSignificantDelta) {
                    if (dy > 0) {
                        mMenuRed.hideMenuButton(true);
                    } else {
                        mMenuRed.showMenuButton(true);
                    }
                }
            }
        });

        mFriendCircleAdapter = new FriendCircleAdapter(R.layout.item_friend_circle,mData);
        mFriendCircleAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mFriendCircleAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()){
                case R.id.deleteBtn:
                    actionDeleteCircle(position);
                    break;
                default:
                    break;
            }
        });

        // 设置滑动 关闭输入框
        mRvContent.setOnTouchListener((v, event) -> {
            if (mEditTextBodyLl.getVisibility() == View.VISIBLE){
                updateEditTextBodyVisible(View.GONE,null);
            }
            return false;
        });

        mFriendCircleAdapter.setOnNeedsIconClickListener(this);
        mFriendCircleAdapter.addHeaderView(mZoneHeaderView);
        mRvContent.setAdapter(mFriendCircleAdapter);
    }


    /**
     * 发表评论的点击事件
     * 指代对朋友圈的评论
     * @param visibility
     * @param commentConfig
     */
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        mCommentConfig = commentConfig;
        mEditTextBodyLl.setVisibility(visibility);
        measureCircleItemHighAndCommentItemOffset(commentConfig);
        if (commentConfig != null && CommentConfig.Type.REPLY.equals(commentConfig.getCommentType())) {
            mCircleEt.setHint("回复" + commentConfig.getName() + ":");
        } else {
            mCircleEt.setHint("说点什么吧");
        }
        if (View.VISIBLE == visibility) {
            mCircleEt.requestFocus();
            //弹出键盘
            UIUtils.showSoftKeyboard(mCircleEt);
            //隐藏菜单
            mMenuRed.hideMenuButton(true);
        } else if (View.GONE == visibility) {
            //隐藏键盘
            UIUtils.hideSoftKeyboard(mCircleEt);
            //显示菜单
            mMenuRed.showMenuButton(true);
        }
    }

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig){
        if (commentConfig == null){
            return;
        }

        int selectPosition = commentConfig.getCirclePosition()+1;
        View selectViewItem = mManager.findViewByPosition(selectPosition);
        if (selectViewItem != null){
           mSelectCircleItemH =  - UIUtils.dip2Px(48);
            if (commentConfig.commentType == CommentConfig.Type.REPLY) {
                //回复评论的情况
                CommentListView commentLv = (CommentListView) selectViewItem.findViewById(R.id.commentList);
                if (commentLv != null) {
                    //找到要回复的评论view,计算出该view距离所属动态底部的距离
                    View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                    if (selectCommentItem != null) {
                        //选择的commentItem距选择的CircleItem底部的距离
                        mSelectCommentItemOffset = 0;
                        View parentView = selectCommentItem;
                        do {
                            int subItemBottom = parentView.getBottom();
                            parentView = (View) parentView.getParent();
                            if (parentView != null) {
                                mSelectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                            }
                        }
                        while (parentView != null && parentView != selectViewItem);
                    }
                }
            }
        }
    }

    /**
     * 删除 不包含头的（但移除的时候是需要包含头的）
     * @param position
     */
    private void actionDeleteCircle(int position) {
        ApiRetrofit.getInstance().deleteMyFdCircle(mData.get(position).getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getCode() == 1){
                        mFriendCircleAdapter.notifyItemRemoved(position
                                +mFriendCircleAdapter.getHeaderLayout().getChildCount());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

    /**
     * 当视图层改变 也就是检测弹出软键盘时进行调用
     */
    private void setViewTreeObserver() {
        ViewTreeObserver viewTreeObserver = mRvContent.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            mRvContent.getWindowVisibleDisplayFrame(rect);
            /* 获得状态栏的高度*/
            int statusBarHeight = getStatusBarHeight();
            /* 获得屏幕的整体高度 */
            int screenH = mRvContent.getRootView().getHeight();
            /* 可见矩形的高度 */
            if (rect.top != statusBarHeight){
                rect.top = statusBarHeight;
            }
            int keyboardH = screenH - (rect.bottom - rect.top);
            if (keyboardH == mCurrentKeyboardH){
                return;
            }
            mCurrentKeyboardH = keyboardH;
            // 应用屏幕的高度
            mScreenHeight = screenH;
            mEditTextBodyHeight = mEditTextBodyLl.getHeight();
            if (mRvContent != null && mCommentConfig != null && mFriendCircleAdapter != null){
                int index = mCommentConfig.circlePosition + 1;
                /*
                * manager.scrollToPositionWithOffset 即为滑动到第几个(即顶部) ; offset 即为顶部偏移量
                * */
                mManager.scrollToPositionWithOffset(index,getListviewOffset(mCommentConfig));
            }
        });
    }

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null) {
            return 0;
        }
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        int listviewOffset = mScreenHeight - mSelectCircleItemH
                - mCurrentKeyboardH
                - mEditTextBodyHeight
                - UIUtils.dip2Px(45);

        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + mSelectCommentItemOffset - UIUtils.dip2px(this,45);
        }
        return listviewOffset;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier(
                "status_bar_height",
                "dimen",
                "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void initData() {
        showWaitingDialog(getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().queryAllFdCircleInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(friendCircleResponse -> {
                    hideWaitingDialog();
                    if (friendCircleResponse != null && friendCircleResponse.getCode() == 1){
                        mData.clear();
                        mData.addAll(friendCircleResponse.getData());
                        mFriendCircleAdapter.notifyDataSetChanged();
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
        mFabPublish.setOnClickListener(v -> {
            mMenuRed.close(true);
            jumpToActivity(PublishTalkActivity.class);
        });
        mFabRf.setOnClickListener(v -> {
            mMenuRed.close(true);
            FriendCircleActivity.this.initData();
        });

        mFabChangeGl.setOnClickListener(v -> {
            mMenuRed.close(true);
            FriendCircleActivity.this.chooseSinglePictureEvent();
        });

        mSendIv.setOnClickListener(v -> {
            String content = mCircleEt.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                UIUtils.showToast("评论内容不能为空!");
                return;
            }
            if (mCommentConfig == null){
                UIUtils.showToast("config为空!");
                return;
            }

            if (mCommentConfig.commentType ==  CommentConfig.Type.PUBLIC) {
                publishCircle(content);
            }else {
                replayManInfo(content);
            }
        });

    }





    /**
     * 发布的朋友圈信息
     * @param content
     */
    private void publishCircle(String content){
        ApiRetrofit.getInstance().pbCircleComment(mCommentConfig.getPublishId(), content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NormalResponse>() {
                    @Override
                    public void accept(NormalResponse response) throws Exception {
                        if (response != null && response.getCode() == 1) {
                            mData.get(mCommentConfig.getCirclePosition() - 1).getComments()
                                    .add(new CommentBean(UserCache.getName()
                                            , Integer.parseInt(UserCache.getId())
                                            , content, 0));
                            mFriendCircleAdapter.notifyItemChanged(mCommentConfig.getCirclePosition());
                            mCircleEt.setText("");
                            // 这个得放在后面撒 我晕
                            updateEditTextBodyVisible(View.GONE, null);
                        } else {
                            UIUtils.showToast(response.getMsg());
                        }
                    }
                }, throwable -> {
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }


    /**
     * 回复好友信息
     * @param content
     */
    private void replayManInfo(String content){
        ApiRetrofit.getInstance().pbPersonCommit(mCommentConfig.getCommentReplyID(),mCommentConfig.getPublishId(),
                content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NormalResponse>() {
                    @Override
                    public void accept(NormalResponse response) throws Exception {
                        if (response != null && response.getCode() == 1){
                            filterInfoAndRefresh(content);
                            mFriendCircleAdapter.notifyItemChanged(
                                    mCommentConfig.getCirclePosition());
                            mCircleEt.setText("");
                            // 这个得放在后面撒 我晕
                            updateEditTextBodyVisible(View.GONE, null);
                        }else {
                            UIUtils.showToast(response.getMsg());
                        }
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));


    }

    private void filterInfoAndRefresh(String content) {
        filterComment(mData.get(mCommentConfig.getCirclePosition() -1).getComments(),content);
    }

    private void filterComment(List<CommentBean> list,String content) {
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                //  读取第一层，存储至新结构
                CommentBean commentBean = list.get(i);
                List<CommentBean> subComments = commentBean.getSub_comment();
                if (commentBean.getId() == mCommentConfig.getCommentReplyID()){

                    subComments.add(new CommentBean(mCommentConfig.getName(),
                            Integer.parseInt(mCommentConfig.getId()),
                            UserCache.getName(),Integer.parseInt(UserCache.getId()),
                            content,mCommentConfig.getCommentReplyID()));
                    return;
                }
                filterComment(subComments,content);
            }
        }
    }

    /**
     * 添加赞
     * @param position
     */
    @Override
    public void updateAddFavort(int position) {
        mData.get(position-1).getYesUser().add(new FavortItem(Integer.parseInt(UserCache.getId()),UserCache.getName()));
        mFriendCircleAdapter.notifyItemChanged(position);
    }

    /**
     * 减少赞; 这里的position是添加了Head（1）的
     * @param position
     */
    @Override
    public void updateDelFavort(int position) {
        List<FavortItem> yesUser = mData.get(position-1).getYesUser();
        for (int i=0;i<yesUser.size();i++){
            FavortItem favortItem = yesUser.get(i);
            if (favortItem.getUserId() == Integer.parseInt(UserCache.getId())){
                yesUser.remove(i);
                mFriendCircleAdapter.notifyItemChanged(position);
                return;
            }
        }
    }

    @Override
    public void showPublishEdtext(CommentConfig commentConfig) {
        updateEditTextBodyVisible(View.VISIBLE,commentConfig);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case PictureConfig.SINGLE:
                    mSingleSelectList = PictureSelector.obtainMultipleResult(data);
                    String filePath = mSingleSelectList.get(0).getPath();
                    mZoneHeaderView.changeBg(new File(filePath));
                    mSingleSelectList.clear();
                    break;
                default:
                    break;
            }
        }
    }

    public void chooseSinglePictureEvent() {
        PictureSelector.create(FriendCircleActivity.this)
                .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
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

}

package com.jit.appcloud.ui.activity.cultivate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.FeedMeal;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserOtherCache;
import com.jit.appcloud.model.request.InsertFeedRequest;
import com.jit.appcloud.ui.activity.message.ShowBigImageActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/05/21.
 *         discription:日常日志活动
 */
public class SubmitFeedActivity extends BaseActivity {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.tvPondSelected)
    TextView mTvPondSelected;
    @BindView(R.id.ll_pond)
    LinearLayout mLlPond;
    @BindView(R.id.tvGoodsSelected)
    TextView mTvGoodsSelected;
    @BindView(R.id.ll_inputType)
    LinearLayout mLlInputType;
    @BindView(R.id.tvPondArea)
    TextView mTvPondArea;
    @BindView(R.id.etInputNum)
    EditText mEtInputNum;
    @BindView(R.id.spInputUnit)
    MaterialSpinner mSpInputUnit;
    @BindView(R.id.tvWeatherSelected)
    TextView mTvWeatherSelected;
    @BindView(R.id.ll_inputWeather)
    LinearLayout mLlInputWeather;
    @BindView(R.id.tvTimeSelected)
    TextView mTvTimeSelected;
    @BindView(R.id.ll_inputTime)
    LinearLayout mLlInputTime;
    @BindView(R.id.tvReMarkInput)
    TextView mtvReMarkInput;
    @BindView(R.id.rl_ReMark)
    RelativeLayout mRlReMark;
    @BindView(R.id.ivHeadPic)
    ImageView mIvHeadPic;
    @BindView(R.id.cv)
    CardView mCv;
    @BindView(R.id.flAdd)
    FrameLayout mFlAdd;
    @BindView(R.id.delete)
    ImageView mDelete;
    @BindView(R.id.tvAddLogPic)
    TextView mTvAddLogPic;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.logTab)
    FloatingActionButton mLogTab;
    @BindView(R.id.etFeedType)
    EditText mEtFeedType;
    @BindView(R.id.llFeedMeal)
    LinearLayout mLlFeedMeal;
    @BindView(R.id.tvMealSelected)
    TextView mTvMealSelected;


    /* 模拟的数据*/
    private boolean isPicAdd = false;
    public List<LocalMedia> mSingleSelectList = new ArrayList<>();
    private TimePickerView pvTime;
    private int mPondId = -1;

    private String mSiliaoName;
    private String mSiliaoType;
    private String mTypeFeedType;
    private String mInputNum;
    private String mUnit;

    @Override
    protected void init() {
        initTimePicker();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_submit_feed;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(UIUtils.getString(R.string.str_title_normal_log));
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(UIUtils.getString(R.string.str_submit));
        mSpInputUnit.setItems(AppConst.SP_INPUT_UNIT);
        mSpInputUnit.setSelectedIndex(0);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> finish());

        mTvPublishNow.setOnClickListener(v -> verifyAndSubmit());

        mLlInputTime.setOnClickListener(v -> {
            // 时间选择器
            pvTime.show();
        });

        mLlPond.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,SingleSelectionActivity.class);
            intent.putExtra(AppConst.FLAG_POND,"pond");
            startActivityForResult(intent,AppConst.RECODE_TO_POND_LIST);
        });

        mLlInputWeather.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,SingleSelectionActivity.class);
            intent.putExtra(AppConst.FLAG_WEATHER,"SubmitFeedActivity");// 传入活动名即可
            startActivityForResult(intent,AppConst.RECODE_TO_SINGLE_ITEM_LIST);
        });

        mLlInputType.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,SingleSelectionActivity.class);
            intent.putExtra(AppConst.FLAG_SILIAO,"siliao");
            startActivityForResult(intent,AppConst.RECODE_TO_SILIAO_ITEM_LIST);
        });

        mTvAddLogPic.setOnClickListener(v -> {
            mSingleSelectList.clear();
            chooseSinglePictureEvent();
        });

        mDelete.setOnClickListener(v -> {
            mDelete.setVisibility(View.GONE);
            mFlAdd.setVisibility(View.GONE);
            mTvAddLogPic.setVisibility(View.VISIBLE);
            isPicAdd = false;
        });

        mLlFeedMeal.setOnClickListener(v -> jumpToActivityForResult(MealFeedManageActivity.class,AppConst.RECODE_TO_FEED_MEAL_MANAGE));

        mIvHeadPic.setOnClickListener(v -> ShowBigImageActivity.startAction(mContext,v,mSingleSelectList.get(0).getPath()));

        mRlReMark.setOnClickListener(v -> {
            Intent intent = new Intent(SubmitFeedActivity.this, KeyInputActivity.class);
            intent.putExtra(AppConst.KEY_INPUT_TITLE, UIUtils.getString(R.string.str_title_write_remark));
            intent.putExtra(AppConst.KEY_TO_INPUT_CONTENT, mtvReMarkInput.getText().toString().trim());
            startActivityForResult(intent, AppConst.RCODE_INPUT_FROM_REMARK_DETAIL);
        });

        mLogTab.setOnClickListener(v -> jumpToActivity(LogFeedActivity.class));


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(mContext).register(AppConst.UPDATE_SUBMIT_MEAL_NAME, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String mealName = intent.getStringExtra("String");
                Log.e("套餐名", mealName);
                if (TextUtils.isEmpty(mealName)){
                    mTvMealSelected.setText("");
                }else {
                    FeedMeal feedMeal = DBManager.getInstance().getFeedMealByName(mealName);
                     mSiliaoName = feedMeal.getFeedName();
                     mSiliaoType = feedMeal.getFeedBrand();
                     mTypeFeedType = feedMeal.getFeedType();
                     mInputNum = String.valueOf(feedMeal.getInputNum());
                     mUnit = feedMeal.getInputUnit();
                     mTvMealSelected.setText(feedMeal.getMealName());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(mContext).unregister(AppConst.UPDATE_SUBMIT_MEAL_NAME);
    }

    private void verifyAndSubmit() {
        // 验证并提交日常日志
        if (mPondId == -1){
            UIUtils.showToast("请选择塘口");
            return;
        }

        if (TextUtils.isEmpty(mTvMealSelected.getText().toString().trim())){
            UIUtils.showToast("请选择投放套餐");
            return;
        }


        String time = mTvTimeSelected.getText().toString().trim();
        if (TextUtils.isEmpty(time)){
            UIUtils.showToast("请选择投放时间");
            return;
        }


        String weather = mTvWeatherSelected.getText().toString().trim();
        if (TextUtils.isEmpty(weather)){
            UIUtils.showToast("请输入天气");
            return;
        }

        showWaitingDialog(getString(R.string.str_please_waiting));
        InsertFeedRequest insertFeedRequest = new InsertFeedRequest();
        insertFeedRequest.setName(mSiliaoName);
        insertFeedRequest.setPound_id(mPondId);
        insertFeedRequest.setUnit(mUnit);
        insertFeedRequest.setCount(Double.parseDouble(mInputNum));
        insertFeedRequest.setFeed_brand(mSiliaoType);
        insertFeedRequest.setTime(time);
        insertFeedRequest.setFeed_type(mTypeFeedType);
        insertFeedRequest.setWeather(weather);
        ApiRetrofit.getInstance().insertFeed(UserCache.getToken(),insertFeedRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    hideWaitingDialog();
                    if (response.getCode() == 1) {
                        UIUtils.showToast("提交成功");
                        mTvPondSelected.setText("");
                        mTvMealSelected.setText("");
                        mTvWeatherSelected.setText("");
                        mTvTimeSelected.setText("");
                        mtvReMarkInput.setText("");
                        UserOtherCache.setFeedMealSelected("");
                        /* 初始化图片 */
                        mDelete.setVisibility(View.GONE);
                        mFlAdd.setVisibility(View.GONE);
                        mTvAddLogPic.setVisibility(View.VISIBLE);
                        isPicAdd = false;
                        mSingleSelectList.clear();
                    }else {
                        UIUtils.showToast(response.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.SINGLE:
                    mSingleSelectList = PictureSelector.obtainMultipleResult(data);
                    String filePath = mSingleSelectList.get(0).getPath();
                    isPicAdd = true; // 改变图片选择状态
                    if (isPicAdd) {
                        mTvAddLogPic.setVisibility(View.GONE);
                        mFlAdd.setVisibility(View.VISIBLE);
                        mDelete.setVisibility(View.VISIBLE);
                    }
                    Glide.with(mContext).load(filePath).into(mIvHeadPic);
                    break;
                case AppConst.RECODE_TO_POND_LIST:
                    String pondSelected = data.getStringExtra(AppConst.SINGLE_ITEM_SELECTED);
                    mPondId = data.getIntExtra(AppConst.POND_ID_SELECTED,-1);
                    mTvPondSelected.setText(pondSelected);
                    break;
                case AppConst.RECODE_TO_SINGLE_ITEM_LIST:
                    String weatherSelected = data.getStringExtra(AppConst.SINGLE_ITEM_SELECTED);
                    mTvWeatherSelected.setText(weatherSelected);
                    break;
                case AppConst.RCODE_INPUT_FROM_REMARK_DETAIL:
                    mtvReMarkInput.setText(data.getStringExtra(AppConst.KEY_FROM_INPUT));
                    break;
                case AppConst.RECODE_TO_SILIAO_ITEM_LIST:
                    String result = data.getStringExtra(AppConst.SINGLE_ITEM_SELECTED);
                    mTvGoodsSelected.setText(result);
                    String[] siliaoBoud = result.split("-");
                    mSiliaoType = siliaoBoud[0];
                    mSiliaoName = siliaoBoud[1];
                    break;
                case AppConst.RECODE_TO_FEED_MEAL_MANAGE:
                    FeedMeal feedMeal = (FeedMeal) data.getExtras().getSerializable(AppConst.FEED_MEAL);
                    mTvMealSelected.setText(feedMeal.getMealName());
                    mSiliaoType = feedMeal.getFeedBrand();
                    mSiliaoName = feedMeal.getFeedName();
                    mInputNum = String.valueOf(feedMeal.getInputNum());
                    mUnit = feedMeal.getInputUnit();
                    mTypeFeedType = feedMeal.getFeedType();
                    break;
            }
        }
    }

    private void chooseSinglePictureEvent() {
        PictureSelector.create(SubmitFeedActivity.this)
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

    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR)-1, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedDate.get(Calendar.YEAR)+1, 11, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, (date, v) -> {//选中事件回调
            // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
            mTvTimeSelected.setText(TimeUtil.date2String(date,"yyyy-MM-dd HH:mm:ss"));
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年", "月", "日", "点", "分", "秒")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
    }
}

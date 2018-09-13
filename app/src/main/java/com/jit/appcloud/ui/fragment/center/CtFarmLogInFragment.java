package com.jit.appcloud.ui.fragment.center;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.bean.MulPicBean;
import com.jit.appcloud.model.request.EpInsertFeedRequest;
import com.jit.appcloud.model.request.EpInsertWaterRequest;
import com.jit.appcloud.model.response.PondGetByMGResponse;
import com.jit.appcloud.ui.activity.cultivate.KeyInputActivity;
import com.jit.appcloud.ui.activity.cultivate.SingleSelectionActivity;
import com.jit.appcloud.ui.adapter.PicPutSelectMulAdapter;
import com.jit.appcloud.ui.base.BaseFragment;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/6/20.
 *         discription: 养殖户录入的Fragment
 */

public class CtFarmLogInFragment extends BaseFragment {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.ll_pond)
    LinearLayout mLlPond;
    @BindView(R.id.tvTimeSelected)
    TextView mTvTimeSelected;
    @BindView(R.id.ll_inputTime)
    LinearLayout mLlInputTime;
    @BindView(R.id.etNano2Input)
    EditText mEtNano2Input;
    @BindView(R.id.ll_nano2)
    LinearLayout mLlNano2;
    @BindView(R.id.etPHInput)
    EditText mEtPHInput;
    @BindView(R.id.ll_nh)
    LinearLayout mLlNh;
    @BindView(R.id.etO2Input)
    EditText mEtO2Input;
    @BindView(R.id.ll_o2)
    LinearLayout mLlO2;
    @BindView(R.id.tvTypeDefine)
    TextView mTvTypeDefine;
    @BindView(R.id.rl_typeDefine)
    RelativeLayout mRlTypeDefine;
    @BindView(R.id.tvReMarkInput)
    TextView mTvReMarkInput;
    @BindView(R.id.rl_ReMark)
    RelativeLayout mRlReMark;
    @BindView(R.id.tvAddFeed)
    TextView mTvAddFeed;
    @BindView(R.id.tvDelFeed)
    TextView mTvDelFeed;
    @BindView(R.id.llFeedIncrease)
    LinearLayout mLlFeedIncrease;
    @BindView(R.id.rcPic)
    RecyclerView mRcPic;
    @BindView(R.id.tvPondSelected)
    TextView mTvPondSelected;
    @BindView(R.id.tvWeatherSelected)
    TextView mTvWeatherSelected;
    @BindView(R.id.ll_inputWeather)
    LinearLayout mLlInputWeather;

    private PicPutSelectMulAdapter mPicSelectMulAdapter;
    private List<MulPicBean> mList = new ArrayList<>();
    private List<String> mSource = new ArrayList<>();
    public int mPicNum = AppConst.TOTAL_COUNT; // 可选数量
    public List<LocalMedia> mShowSelectList = new ArrayList<>();
    private List<LocalMedia> mMulSelectList = new ArrayList<>();
    private List<PondGetByMGResponse.DataBean> mMEpPondList; // 获取的塘口列表
    private List<String> mPondStrList = new ArrayList<>();
    private int mSelectPosition = 0;

    @Override
    public void init() {

    }


    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_main_center_fm;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void initView(View rootView) {
        mIvToolbarNavigation.setVisibility(View.GONE);
        mTvToolbarTitle.setText("养殖录入");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(getString(R.string.str_title_save));
        setAdapter();
        initPondAndTime();
        initFeed();
        initPond();
    }

    /***
     * 获得塘口---
     * */
    private void initPond() {
        ApiRetrofit.getInstance().getPondByEp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pondGetByMGResponse -> {
                    if (pondGetByMGResponse != null && pondGetByMGResponse.getCode() == 1) {
                        mMEpPondList = pondGetByMGResponse.getData();
                        mPondStrList.clear();
                        for (PondGetByMGResponse.DataBean bean : mMEpPondList) {
                            mPondStrList.add(bean.getNumber());
                        }
                        // ==============
                        if (mPondStrList.size() != 0) {
                            mTvPondSelected.setText(mPondStrList.get(0));
                        }

                    } else {
                        UIUtils.showToast(pondGetByMGResponse.getMsg());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }


    private void setAdapter() {
            mList.clear();
            mList.add(new MulPicBean(null, AppConst.PicType.ADD_BUTTON));
            mPicSelectMulAdapter = new PicPutSelectMulAdapter(getActivity(), mList, this);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
            mRcPic.setLayoutManager(manager);
            manager.setSmoothScrollbarEnabled(true);//*
            manager.setAutoMeasureEnabled(true);    //*
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
                        PictureSelector.create(CtFarmLogInFragment.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, mShowSelectList);
                    }
                }
            });

            mPicSelectMulAdapter.setOnItemLongClickListener((helper, parent, itemView, position) -> {
                mPicSelectMulAdapter.setDeleting(true);
                mPicSelectMulAdapter.notifyDataSetChanged();
                return false;
            });
        mRcPic.setHasFixedSize(true); //*
        mRcPic.setNestedScrollingEnabled(false); //*
        mRcPic.setAdapter(mPicSelectMulAdapter);
    }


    private void initPondAndTime() {
        mTvTimeSelected.setText(TimeUtil.date2String(new Date(System.currentTimeMillis()), "yyyy-MM-dd"));
    }

    private void initFeed() {
        // 增加三餐
        addFeed();
        addFeed();
        addFeed();
    }

    @Override
    public void initData() {

    }


    @Override
    public void initListener() {
        mTvAddFeed.setOnClickListener(v -> addFeed());

        mTvDelFeed.setOnClickListener(v -> delFeed());

        mLlPond.setOnClickListener(v -> showPondList());

        mLlInputTime.setOnClickListener(v -> showCustomDatePicker());
        mRlReMark.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), KeyInputActivity.class);
            intent.putExtra(AppConst.KEY_INPUT_TITLE, UIUtils.getString(R.string.str_title_write_remark));
            intent.putExtra(AppConst.KEY_TO_INPUT_CONTENT, mTvReMarkInput.getText().toString());
            startActivityForResult(intent, AppConst.RCODE_INPUT_FROM_REMARK_DETAIL);
        });

        mRlTypeDefine.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), KeyInputActivity.class);
            intent.putExtra(AppConst.KEY_INPUT_TITLE, UIUtils.getString(R.string.str_title_write_type_define));
            intent.putExtra(AppConst.KEY_TO_INPUT_CONTENT, mTvTypeDefine.getText().toString());
            startActivityForResult(intent, AppConst.RCODE_INPUT_FROM_TYPEDEFINE);
        });


        mTvPublishNow.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            // 隐藏软键盘
            if (isSoftShowing()) {
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
            }
            submitData();
        });

        mLlInputWeather.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),SingleSelectionActivity.class);
            intent.putExtra(AppConst.FLAG_WEATHER,"CtFarmLogInFragment");// 传入活动名即可
            startActivityForResult(intent,AppConst.RECODE_TO_SINGLE_ITEM_LIST);
        });

    }

    private void submitData() {
        if (TextUtils.isEmpty(mTvPondSelected.getText().toString())) {
            UIUtils.showToast(getString(R.string.please_no_pound_can_choose));
            return;
        }
        EpInsertWaterRequest requestWater = new EpInsertWaterRequest();
        requestWater.setNh(mEtNano2Input.getText().toString());  // NH  ->氨氮
        requestWater.setAlkali(mEtPHInput.getText().toString()); // Alkali -> PH
        requestWater.setO2(mEtO2Input.getText().toString());     // O2  -> 溶解氧
        requestWater.setRemark(mTvReMarkInput.getText().toString());
        requestWater.setMedicine(mTvTypeDefine.getText().toString());
        requestWater.setPound_id(mMEpPondList.get(mSelectPosition).getId());
        requestWater.setData(mTvTimeSelected.getText().toString());
        requestWater.setWeather(mTvWeatherSelected.getText().toString());
        EpInsertFeedRequest requestFeed = new EpInsertFeedRequest();
        requestFeed.setPound_id(mMEpPondList.get(mSelectPosition).getId());
        requestFeed.setDate(mTvTimeSelected.getText().toString());
        LogUtils.e("日志提交",mMEpPondList.get(mSelectPosition).getId()+"  ");
        if (mLlFeedIncrease.getChildCount() != 0) {
            for (int i = 0; i < mLlFeedIncrease.getChildCount(); i++) {
                View view = mLlFeedIncrease.getChildAt(i);
                EditText etNum = view.findViewById(R.id.tvFeedNum);
                etNum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                long seedNum = 0;
                String trim = etNum.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)){
                    seedNum = Long.parseLong(trim);
                }
                switch (i) {
                    case 0:
                        requestFeed.setCount1(seedNum);
                        break;
                    case 1:
                        requestFeed.setCount2(seedNum);
                        break;
                    case 2:
                        requestFeed.setCount3(seedNum);
                        break;
                    case 3:
                        requestFeed.setCount4(seedNum);
                        break;
                    case 4:
                        requestFeed.setCount5(seedNum);
                        break;
                    case 5:
                        requestFeed.setCount6(seedNum);
                        break;
                    default:
                        break;
                }
            }
        }
        ApiRetrofit.getInstance().epInsertWater(requestWater)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(response -> {
                    if (response != null && response.getCode() == 1) {
                        return ApiRetrofit.getInstance().epInsertFeed(requestFeed);
                    }
                    return null;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getCode() == 1) {
                        UIUtils.showToast(response.getMsg());
                        initViewToOrigin();
                    } else {
                        UIUtils.showToast(getString(R.string.please_log_sbm_fail));
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

   /**
    *设置View为初始状态---
    * */
    private void initViewToOrigin() {
        mTvPondSelected.setText("");
        mTvTimeSelected.setText(TimeUtil.date2String
                (new Date(System.currentTimeMillis()), "yyyy-MM-dd"));
        mEtNano2Input.setText("");
        mEtO2Input.setText("");
        mEtPHInput.setText("");
        mTvTypeDefine.setText("");
        mTvReMarkInput.setText("");
        mLlFeedIncrease.removeAllViews();
        initFeed();
        mList.clear();
        mList.add(new MulPicBean(null, AppConst.PicType.ADD_BUTTON));
        mPicSelectMulAdapter.notifyDataSetChanged();
        mSource.clear();
        mPicNum = AppConst.TOTAL_COUNT;
        mShowSelectList.clear();
        mMulSelectList.clear();
    }

    public void showCustomDatePicker() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_time_pick)
                .customView(R.layout.dialog_datepicker, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive((dialog, which) -> {
                    DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                    mTvTimeSelected.setText(String.format("%d-%02d-%d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                })
                .show();
    }

    public void showPondList() {
        new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.str_title_pond))
                .items(mPondStrList)
                .itemsCallback((dialog, view, which, text) -> {
                    mSelectPosition = which;
                    mTvPondSelected.setText(text);
                })
                .positiveText(android.R.string.cancel)
                .show();
    }

   /***
    * 减少餐数
    * */
    private void delFeed() {
        if (mLlFeedIncrease.getChildCount() != 0) {
            mLlFeedIncrease.removeViewAt(mLlFeedIncrease.getChildCount() - 1);
        } else {
            UIUtils.showToast("无餐数可减少");
        }
    }

    /**
     * 增加餐数
     * */
    private void addFeed() {
        if (mLlFeedIncrease.getChildCount() >= AppConst.MAX_FEED_NUM) {
            UIUtils.showToast("每日仅可投放6餐");
        } else {
            View feedView = LayoutInflater.from(getActivity()).inflate(R.layout.item_feed_mg, null);
            TextView tvID = feedView.findViewById(R.id.tvID);
            tvID.setText(String.valueOf(mLlFeedIncrease.getChildCount() + 1));
            mLlFeedIncrease.addView(feedView);
        }
    }

    public void chooseMulPictureEvent() {
        PictureSelector.create(CtFarmLogInFragment.this)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    mList.remove(mList.size() - 1);
                    for (LocalMedia media : mMulSelectList) {
                        mShowSelectList.add(media);
                        mList.add(new MulPicBean(media.getPath(), AppConst.PicType.SHOW));
                        mPicNum--;
                    }
                    if (mList.size() <= 5) {
                        mList.add(new MulPicBean(null, AppConst.PicType.ADD_BUTTON));
                    }
                    mPicSelectMulAdapter.notifyDataSetChanged();
                    mMulSelectList.clear();
                    break;
                case AppConst.RCODE_INPUT_FROM_REMARK_DETAIL:
                    mTvReMarkInput.setText(data.getStringExtra(AppConst.KEY_FROM_INPUT));
                    break;
                case AppConst.RCODE_INPUT_FROM_TYPEDEFINE:
                    mTvTypeDefine.setText(data.getStringExtra(AppConst.KEY_FROM_INPUT));
                    break;
                case AppConst.RECODE_TO_SINGLE_ITEM_LIST:
                    String weatherSelected = data.getStringExtra(AppConst.SINGLE_ITEM_SELECTED);
                    mTvWeatherSelected.setText(weatherSelected);
                    break;
                default:
                    break;
            }
        }
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getActivity().getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(getActivity()).register(AppConst.UPDATE_EP_CUL_POUND, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initPond();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.UPDATE_EP_CUL_POUND);
    }
}

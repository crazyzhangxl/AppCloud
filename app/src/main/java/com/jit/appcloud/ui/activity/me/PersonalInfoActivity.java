package com.jit.appcloud.ui.activity.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.bean.JsonBean;
import com.jit.appcloud.model.bean.MulFlowBean;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserNormalCache;
import com.jit.appcloud.model.request.UserInfoUpRequest;
import com.jit.appcloud.ui.activity.cultivate.SingleSelectionActivity;
import com.jit.appcloud.ui.activity.cultivate.UpdateList;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.fragment.ShowEnjoyDialogFragment;
import com.jit.appcloud.util.GetJsonDataUtil;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.MyTagFlowLayout;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.silencedut.router.Router;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import org.json.JSONArray;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/05/25.
 *         discription: 个人信息的界面
 *          暂时用不到 -----------------------------------
 */
public class PersonalInfoActivity extends BaseActivity implements UpdateList, AMapLocationListener {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.ivHeadImage)
    CircleImageView mIvHeadImage;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.tvSignature)
    TextView mTvSignature;
    @BindView(R.id.tvRealName)
    TextView mTvRealName;
    @BindView(R.id.ivEditor)
    ImageView mIvEditor;
    @BindView(R.id.tvRegisterTime)
    TextView mTvRegisterTime;
    @BindView(R.id.tvDetailAds)
    TextView mTvDetailAds;
    @BindView(R.id.rlDetailAds)
    RelativeLayout mRlDetailAds;
    @BindView(R.id.tvCity)
    TextView mTvCity;
    @BindView(R.id.rlCity)
    RelativeLayout mRlCity;
    @BindView(R.id.cv)
    CardView mCv;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.rlPhone)
    RelativeLayout mRlPhone;
    @BindView(R.id.tvEmail)
    TextView mTvEmail;
    @BindView(R.id.rlEmail)
    RelativeLayout mRlEmail;
    @BindView(R.id.tvCompany)
    TextView mTvCompany;
    @BindView(R.id.rlRelName)
    RelativeLayout mRlRealName;
    @BindView(R.id.flowlayout)
    MyTagFlowLayout mFlowlayout;
    @BindView(R.id.RlFlow)
    RelativeLayout mRlFlow;
    @BindView(R.id.ivHobby)
    ImageView mIvHobby;
    @BindView(R.id.rlCompany)
    RelativeLayout mRlCompany;
    public List<LocalMedia> mSingleSelectList = new ArrayList<>();
    @BindView(R.id.tvInCome)
    TextView mTvInCome;
    @BindView(R.id.rlIncome)
    RelativeLayout mRlIncome;
    @BindView(R.id.tvArea)
    TextView mTvArea;
    @BindView(R.id.rlArea)
    RelativeLayout mRlArea;
    @BindView(R.id.tvCategory)
    TextView mTvCategory;
    @BindView(R.id.rlCategory)
    RelativeLayout mRlCategory;
    /* 省市区-分开来-后面可能需要进行提交*/
    private String mProvince;
    private String mCity;
    private String mDistrict;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private boolean isLoaded = false;

    private ArrayList<MulFlowBean> mHobbyFlowList = new ArrayList<>();
    private ArrayList<String> mHobbyStrList = new ArrayList<>();
    private TagAdapter<MulFlowBean> mHobbyTagAdapter;

    public AMapLocationClient mAMapLocationClient;
    private List<HotCity> mHotCities;

    @Override
    protected void init() {
        Router.instance().register(this);
        Observable.create(this::initJsonData).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> isLoaded = aBoolean);
        mHotCities = new ArrayList<>();
        mHotCities.add(new HotCity("北京", "北京", "101010100"));
        mHotCities.add(new HotCity("上海", "上海", "101020100"));
        mHotCities.add(new HotCity("广州", "广东", "101280101"));
        mHotCities.add(new HotCity("深圳", "广东", "101280601"));
        mHotCities.add(new HotCity("杭州", "浙江", "101210101"));
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.str_title_personal_info);
        setHobbyFlowAdapter();
        if (AppConst.ROLE_GENERAL_AGENCY.equals(UserCache.getRole())){
            mRlIncome.setVisibility(View.VISIBLE);
        }else if (AppConst.ROLE_AGENCY.equals(UserCache.getRole())){
            mRlCategory.setVisibility(View.VISIBLE);
            mRlArea.setVisibility(View.VISIBLE);
            mRlCompany.setVisibility(View.VISIBLE);
        }

    }

    public void setHobbyFlowAdapter() {
        mHobbyTagAdapter = new TagAdapter<MulFlowBean>(mHobbyFlowList) {
            @Override
            public View getView(FlowLayout parent, int position, MulFlowBean bean) {
                TextView textView = null;
                if (bean.isContent()) {
                    textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flow_text, mFlowlayout, false);
                    textView.setText(bean.getContent());
                } else {
                    textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flow_text_add, mFlowlayout, false);
                }
                return textView;
            }
        };
        mFlowlayout.setAdapter(mHobbyTagAdapter);
    }

    @Override
    protected void initData() {
        updateViews();
    }

    private void updateViews() {
        if (!TextUtils.isEmpty(UserCache.getHeadImage())) {
            Glide.with(this).load(UserCache.getHeadImage())
                    .apply(new RequestOptions().error(R.mipmap.default_header)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)).into(mIvHeadImage);
        }
        mTvUserName.setText(UserCache.getName());
        mTvRegisterTime.setText(UserCache.getRegisterTime());

        if (UserNormalCache.getProvince() != null) {
            mTvCity.setText(UIUtils.getString(R.string.str_format_city_bond
                    , UserNormalCache.getProvince(), UserNormalCache.getCity(), UserNormalCache.getCountry()));
        }

        if (UserNormalCache.getAddress() != null) {
            mTvDetailAds.setText(UserNormalCache.getAddress());
        }

        if (UserNormalCache.getRealName() != null) {
            mTvRealName.setText(UserNormalCache.getRealName());
        }


        if (UserNormalCache.getTel() != null) {
            mTvPhone.setText(UserNormalCache.getTel());
        }

        if (UserNormalCache.getEmail() != null) {
            mTvEmail.setText(UserNormalCache.getEmail());
        }

        if (UserNormalCache.getSignature() != null) {
            mTvSignature.setText(UserNormalCache.getSignature());
        }

        if (UserNormalCache.getRealName() != null){
            mTvRealName.setText(UserNormalCache.getRealName());
        }

        if (AppConst.ROLE_GENERAL_AGENCY.equals(UserCache.getRole())){
            if (UserNormalCache.getIncome() != 0){
                mTvInCome.setText(String.valueOf(UserNormalCache.getIncome()));
            }

        }else if (AppConst.ROLE_AGENCY.equals(UserCache.getRole())){
            if (UserNormalCache.getArea() != null)
                mTvArea.setText(UserNormalCache.getArea());
            if (UserNormalCache.getCategory() != null)
                mTvCategory.setText(UserNormalCache.getCategory());
            if (UserNormalCache.getDepartment() != null)
                mTvCompany.setText(UserNormalCache.getDepartment());
        }

        if (UserNormalCache.getHobby() != null){
            String hobbyStr = UserNormalCache.getHobby();
            String[] hobbyArray = hobbyStr.split(",");
            for (String hobby:hobbyArray){
                mHobbyFlowList.add(new MulFlowBean(hobby, true));
                mHobbyStrList.add(hobby);

            }
            if (mHobbyFlowList.size() < AppConst.HOBBY_MAX_NUM) {
                mHobbyFlowList.add(new MulFlowBean(null, false));
            } else {
                mIvHobby.setVisibility(View.VISIBLE);
            }
            mHobbyTagAdapter.notifyDataChanged();
        }else {
            mHobbyFlowList.add(new MulFlowBean(null, false));
            mHobbyTagAdapter.notifyDataChanged();
        }


    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mIvEditor.setOnClickListener(v -> {
            // 编辑信息
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.CONTENT_FROM_SIGNATURE, mTvSignature.getText().toString().trim());
            jumpToActivityForResult(EditSignatureActivity.class, bundle, AppConst.RECODE_EDIT_FROM_SIGNATURE);
        });

        mIvHeadImage.setOnClickListener(v -> {
            // 点击回调成功则上传头像,之后才是加载
            mSingleSelectList.clear();
            chooseSinglePictureEvent();
        });

        mRlRealName.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.CONTENT_FROM_REAL_NAME, mTvRealName.getText().toString().trim());
            jumpToActivityForResult(EditInfoActivity.class, bundle, AppConst.RECODE_EDIT_FROM_REAL_NAME);
        });

        mRlCity.setOnClickListener(v -> {
            if (isLoaded) {
                showPickerView();
            } else {
                UIUtils.showToast("sorry,城市数据未加载!!!");
            }
        });

        mRlFlow.setOnClickListener(v -> {
            ShowEnjoyDialogFragment showEnjoyDialogFragment = new ShowEnjoyDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(AppConst.HOBBY_KEY_ENJOY, mHobbyStrList);
            showEnjoyDialogFragment.setArguments(bundle);
            showEnjoyDialogFragment.show(getFragmentManager(), "showEnjoyDialogFragment");
        });

        mRlEmail.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.CONTENT_FORM_EMAIL, mTvEmail.getText().toString().trim());
            jumpToActivityForResult(EditInfoActivity.class, bundle, AppConst.RECODE_EDIT_FROM_EMAIL);
        });

        mRlPhone.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.CONTENT_FROM_PHONE, mTvPhone.getText().toString().trim());
            jumpToActivityForResult(EditInfoActivity.class, bundle, AppConst.RECODE_EDIT_FROM_PHONE);
        });

        mRlCompany.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.CONTENT_FROM_COMPANY, mTvCompany.getText().toString().trim());
            jumpToActivityForResult(EditInfoActivity.class, bundle, AppConst.RECODE_EDIT_FROM_COMPANY);
        });

        mRlDetailAds.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.CONTENT_FROM_DETAIL_ADS, mTvDetailAds.getText().toString().trim());
            jumpToActivityForResult(EditInfoActivity.class, bundle, AppConst.RECODE_EDIT_FROM_DETAILADS);
        });

        mRlArea.setOnClickListener(v -> showCityChoosed());


        mRlIncome.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.CONTENT_FROM_INCOME, mTvInCome.getText().toString().trim());
            jumpToActivityForResult(EditInfoActivity.class, bundle, AppConst.RECODE_EDIT_FROM_INCOME);
        });

        mRlCategory.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,SingleSelectionActivity.class);
            intent.putExtra(AppConst.FLAG_AGENCY_CATEGORY,"PersonalInfoActivity");// 传入活动名即可
            startActivityForResult(intent,AppConst.RECODE_TO_SINGLE_ITEM_LIST);
        });
    }

    public void showCityChoosed(){
        CityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())	//此方法必须调用
                .enableAnimation(true)	//启用动画效果
                .setAnimationStyle(R.style.AnimBottom)	//自定义动画
                .setHotCities(mHotCities)	//指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        if (data != null) {
                            UserInfoUpRequest userInfoUpRequest = UserNormalCache.getUserInfoUpRequest();
                            userInfoUpRequest.setArea(data.getName());
                            ApiRetrofit.getInstance().updateUserInfo(UserCache.getToken()
                                    ,userInfoUpRequest)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(userUpdateInfoResponse -> {
                                        if (userUpdateInfoResponse.getCode() == 1){
                                            UserNormalCache.setArea(data.getName());
                                            mTvArea.setText(data.getName());
                                        }else {
                                            UIUtils.showToast(userUpdateInfoResponse.getMsg());
                                        }
                                    }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
                        }
                    }

                    @Override
                    public void onLocate() {
                        locateEvent();
                    }
                })
                .show();
        locateEvent();
    }

    public void locateEvent(){
        mAMapLocationClient = new AMapLocationClient(mContext);
        AMapLocationClientOption mAMapLocationClientOption = new AMapLocationClientOption();
        mAMapLocationClient.setLocationListener(PersonalInfoActivity.this);
        mAMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mAMapLocationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Router.instance().unregister(this);
    }

    /*  ====== 单选图片 =====*/
    private void chooseSinglePictureEvent() {
        PictureSelector.create(PersonalInfoActivity.this)
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
                case PictureConfig.SINGLE:
                    mSingleSelectList = PictureSelector.obtainMultipleResult(data);
                    String filePath = mSingleSelectList.get(0).getPath();
                    showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
                    ApiRetrofit.getInstance().postHeadImage(UserCache.getToken(), new File(filePath))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(userHeadPostResponse -> {
                                hideWaitingDialog();
                                if (userHeadPostResponse.getCode() == 1) {
                                    UserCache.setHeadImage(filePath);
                                    Glide.with(mContext).load(filePath)
                                            .apply(new RequestOptions().error(R.mipmap.default_header)
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                                            .into(mIvHeadImage);
                                    UserCache.setHeadImage(userHeadPostResponse.getData().getImage());
                                    BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_ME_FG_INFO);
                                } else {
                                    UIUtils.showToast(userHeadPostResponse.getMsg());
                                }
                            }, throwable -> {
                                hideWaitingDialog();
                                UIUtils.showToast(throwable.getLocalizedMessage());
                            });
                    break;
                case AppConst.RECODE_EDIT_FROM_EMAIL:
                    mTvEmail.setText(data.getStringExtra(AppConst.RESULT_FROM_EDIT));
                    break;
                case AppConst.RECODE_EDIT_FROM_PHONE:
                    mTvPhone.setText(data.getStringExtra(AppConst.RESULT_FROM_EDIT));
                    break;
                case AppConst.RECODE_EDIT_FROM_COMPANY:
                    mTvCompany.setText(data.getStringExtra(AppConst.RESULT_FROM_EDIT));
                    break;
                case AppConst.RECODE_EDIT_FROM_DETAILADS:
                    mTvDetailAds.setText(data.getStringExtra(AppConst.RESULT_FROM_EDIT));
                    break;
                case AppConst.RECODE_EDIT_FROM_REAL_NAME:
                    mTvRealName.setText(data.getStringExtra(AppConst.RESULT_FROM_EDIT));
                    break;
                case AppConst.RECODE_EDIT_FROM_SIGNATURE:
                    mTvSignature.setText(data.getStringExtra(AppConst.RESULT_FROM_EDIT));
                    break;
                case AppConst.RECODE_TO_SINGLE_ITEM_LIST:
                    mTvCategory.setText(data.getStringExtra(AppConst.SINGLE_ITEM_SELECTED));
                    break;
                case AppConst.RECODE_EDIT_FROM_INCOME:
                    mTvInCome.setText(data.getStringExtra(AppConst.RESULT_FROM_EDIT));
                    break;


            }
        }
    }

    /* ============ 地区选择 ==========*/
    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            mProvince = options1Items.get(options1).getPickerViewText();
            mCity = options2Items.get(options1).get(options2);
            mDistrict = options3Items.get(options1).get(options2).get(options3);
            UserInfoUpRequest userInfoUpRequest = UserNormalCache.getUserInfoUpRequest();
            userInfoUpRequest.setProvince(mProvince);
            userInfoUpRequest.setCity(mCity);
            userInfoUpRequest.setCountry(mDistrict);
            ApiRetrofit.getInstance().updateUserInfo(UserCache.getToken()
                    ,userInfoUpRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userUpdateInfoResponse -> {
                        if (userUpdateInfoResponse.getCode() == 1) {
                            UserNormalCache.setProvince(mProvince);
                            UserNormalCache.setCity(mCity);
                            UserNormalCache.setCountry(mDistrict);
                            mTvCity.setText(String.format(UIUtils.getString(R.string.str_format_city_bond), mProvince, mCity, mDistrict));
                        } else {
                            UIUtils.showToast(userUpdateInfoResponse.getMsg());
                        }
                    }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
        })
                .setTitleText(UIUtils.getString(R.string.str_pk_title_city))
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData(ObservableEmitter<Boolean> emitter) {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(mContext, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData, emitter);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> mProvince_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                mProvince_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(mProvince_AreaList);
        }

        emitter.onNext(true);
        emitter.onComplete();

    }

    public ArrayList<JsonBean> parseData(String result, ObservableEmitter<Boolean> emitter) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            emitter.onNext(false);
            emitter.onComplete();
        }
        return detail;
    }

    @Override
    public void updateFlowList(ArrayList<String> mList) {
        LogUtils.e("头像", " ============ 执行啦 ");
        mHobbyFlowList.clear();
        mHobbyStrList.clear();
        mHobbyStrList.addAll(mList);
        for (String str : mList) {
            mHobbyFlowList.add(new MulFlowBean(str, true));
        }
        if (mHobbyFlowList.size() < AppConst.HOBBY_MAX_NUM) {
            mIvHobby.setVisibility(View.GONE);
            mHobbyFlowList.add(new MulFlowBean(null, false));
        } else {
            mIvHobby.setVisibility(View.VISIBLE);
        }
        LogUtils.e("头像+爱好", mHobbyFlowList.size() + "");
        mHobbyTagAdapter.notifyDataChanged();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            CityPicker.getInstance()
                    .locateComplete(new LocatedCity(aMapLocation.getCity(), aMapLocation.getProvince(), aMapLocation.getCityCode()), LocateState.SUCCESS);
            mAMapLocationClient.stopLocation();
            mAMapLocationClient.onDestroy();
        } else {
            Toast.makeText(mContext, "定位失败", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.jit.appcloud.ui.activity.me;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.commom.ResourceProvider;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.CityWeatherBean;
import com.jit.appcloud.event.UpdateSign;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.bean.WeatherBasicData;
import com.jit.appcloud.model.response.HeWeatherResponse;
import com.jit.appcloud.ui.adapter.HoursForecastAdapter;
import com.jit.appcloud.ui.adapter.WeatherPagerAdapter;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.fragment.me.WeatherCityFragment;
import com.jit.appcloud.ui.fragment.me.WeatherFragment;
import com.jit.appcloud.ui.presenter.WeatherAtPresenter;
import com.jit.appcloud.ui.view.IWeatherAtView;
import com.jit.appcloud.util.FormatUtils;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.SharePreferenceUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import com.silencedut.taskscheduler.TaskScheduler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.animation.ObjectAnimator.ofFloat;
/**
 * @author zxl
 */
public class WeatherActivity extends BaseActivity<IWeatherAtView, WeatherAtPresenter> implements AMapLocationListener, AppBarLayout.OnOffsetChangedListener, IWeatherAtView {
    public AMapLocationClient mAMapLocationClient;
    @BindView(R.id.main_temp)
    TextView mMainTemp;
    @BindView(R.id.main_info)
    TextView mMainInfo;
    @BindView(R.id.container_layout)
    LinearLayout mContainerLayout;
    @BindView(R.id.main_hours_forecast_recyclerView)
    RecyclerView mMainHoursForecastRecyclerView;
    @BindView(R.id.main_bg)
    FrameLayout mMainBg;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.title_icon)
    ImageView mTitleIcon;
    @BindView(R.id.title_temp)
    TextView mTitleTemp;
    @BindView(R.id.main_location)
    TextView mMainLocation;
    @BindView(R.id.refresh_status)
    ImageView mRefreshStatus;
    @BindView(R.id.main_post_time)
    TextView mMainPostTime;
    @BindView(R.id.main_toolbar)
    Toolbar mMainToolbar;
    @BindView(R.id.float_action)
    FloatingActionButton mFloatAction;
    @BindView(R.id.main_layout)
    CoordinatorLayout mMainLayout;
    private static final int ROTATION_DURATION = 1000;
    private static final int POSTTIME_DURATION = 500;
    private static final float DEFAULT_PERCENTAGE = 0.8f;
    @BindView(R.id.tv_wind)
    TextView mTvWind;
    @BindView(R.id.tv_sc)
    TextView mTvSc;
    private float percentageOfShowTitle = DEFAULT_PERCENTAGE;
    private float mWeatherInfoContainerLeft;
    private ObjectAnimator mActionRotate;
    private Drawable mDrawableLocation;
    private ValueAnimator mSucceedAnimator;
    protected float mTitlePercentage;
    private String mWeatherStatus;
    private String mTemperature;
    private String mDistrictSuffixCity; //代表了地图定位的地址
    private long mStartRefresh;
    private String mUpdateTime = "";
    private static final int MIN_REFRESH_MILLS = 500;
    private ArrayList<HeWeatherResponse.HeWeather6Bean.HourlyBean> mForecastBeanArrayList = new ArrayList<>();
    private HoursForecastAdapter mHoursForecastAdapter;

    @Override
    protected void initListener() {
        mFloatAction.setOnClickListener(view -> {
            queryWeather(SharePreferenceUtils.getInstance(mContext).getString(AppConst.LOCATION_CITY_NOWS_ID, ""));
        });
    }



    @Override
    protected void initData() {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (TimeUtil.isNight()) {
            mMainBg.setBackgroundResource(R.mipmap.ic_weather_bg_night);
        } else {
            mMainBg.setBackgroundResource(R.mipmap.ic_weather_bg_day);
        }
        setSupportActionBar(mMainToolbar);
        mMainToolbar.setNavigationIcon(null);
        getSupportActionBar().setTitle("");
        mAppBarLayout.addOnOffsetChangedListener(this);
        setupViewPager();
        setupHoursForecast();
        mContainerLayout.post(() -> {
            mWeatherInfoContainerLeft = mContainerLayout.getX();
            percentageOfShowTitle = (mContainerLayout.getBottom()) * 1.0f / mAppBarLayout.getTotalScrollRange();
            if (percentageOfShowTitle == 0) {
                mWeatherInfoContainerLeft = DEFAULT_PERCENTAGE;
            }
        });
        mActionRotate = ObjectAnimator.ofFloat(mRefreshStatus, "rotation", 0, 360);
        mActionRotate.setDuration(ROTATION_DURATION);
        mActionRotate.setRepeatCount(-1);

        mDrawableLocation = ContextCompat.getDrawable(mContext, R.mipmap.ic_location);
        mDrawableLocation.setBounds(0, 0, UIUtils.dipToPx(this, R.dimen.common_dimension_14), UIUtils.dipToPx(this, R.dimen.common_dimension_14));
        mSucceedAnimator = ofFloat(mMainPostTime, "scaleX", 1, 0, 1).setDuration(POSTTIME_DURATION);
        mSucceedAnimator.setStartDelay(ROTATION_DURATION);
    }

    private void setupHoursForecast() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMainHoursForecastRecyclerView.setLayoutManager(linearLayoutManager);
        mForecastBeanArrayList.clear();
        mHoursForecastAdapter = new HoursForecastAdapter(R.layout.item_weather_hour_forecast, mForecastBeanArrayList);
        mMainHoursForecastRecyclerView.setAdapter(mHoursForecastAdapter);
    }

    private void setupViewPager() {
        WeatherPagerAdapter weatherPagerAdapter = new WeatherPagerAdapter(mContext, getSupportFragmentManager());
        weatherPagerAdapter.addFrag(new Pair<>(WeatherCityFragment.newInstance(), R.drawable.selector_city_tab));
        weatherPagerAdapter.addFrag(new Pair<>(WeatherFragment.getInstance(), R.drawable.selector_weather_tab));
        mViewPager.setAdapter(weatherPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int index = 0; index < weatherPagerAdapter.getCount(); index++) {
            mTabLayout.getTabAt(index).setCustomView(weatherPagerAdapter.getTabView(index, mTabLayout));
        }

        mViewPager.setOffscreenPageLimit(weatherPagerAdapter.getCount());
        mViewPager.setCurrentItem(weatherPagerAdapter.getCount() / 2);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_weather;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mSucceedAnimator.removeAllListeners();
        mActionRotate.removeAllListeners();
        mSucceedAnimator.removeAllUpdateListeners();
        mActionRotate.removeAllUpdateListeners();
    }

    @Override

    protected void init() {
        // 获取天气相关的地图读写权限
        getLocatedCityId();// 获得请求的地址
    }


    // 定位相关
    public void getLocatedCityId() {
        mAMapLocationClient = new AMapLocationClient(this);
        AMapLocationClientOption mAMapLocationClientOption = new AMapLocationClientOption();
        mAMapLocationClient.setLocationListener(this);
        mAMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mAMapLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            String city = aMapLocation.getCity();
            String district = aMapLocation.getDistrict();
            LogUtils.e("位置", FormatUtils.getDistrictName(city, district));
            mDistrictSuffixCity = FormatUtils.getDistrictName(city, district);
            // 获得到了之后直接查询即可
            SharePreferenceUtils.getInstance(mContext).putString(AppConst.LOCATE_CITY_str, district.substring(0, district.length() - 1));
            queryWeather(mDistrictSuffixCity);
            // 在这里进行位置的存储 有可能
            mAMapLocationClient.stopLocation();
            mAMapLocationClient.onDestroy();
        } else {
            UIUtils.showToast(getString(R.string.str_toast_location_error));
        }
    }


    // 上下滑动相关
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        mTitlePercentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        handleInfoAnimate(mTitlePercentage);
    }

    private void handleInfoAnimate(float percentage) {
        mMainToolbar.getBackground().mutate().setAlpha((int) (255 * percentage));
        mContainerLayout.setAlpha(1 - percentage);
        mContainerLayout.setScaleX(1 - percentage);
        mContainerLayout.setScaleY(1 - percentage);
        mMainHoursForecastRecyclerView.setAlpha(1 - percentage);

        if (mWeatherInfoContainerLeft > 0) {
            mContainerLayout.setX(mWeatherInfoContainerLeft * (1 - percentage));
        }

        if (!(percentage < percentageOfShowTitle)) {
            mTitleIcon.setImageResource(ResourceProvider.getIconId(mWeatherStatus));
            mTitleTemp.setText(mTemperature + "°");
            if (mFloatAction.isShown()) {
                mFloatAction.hide();
            }
        } else {
            if (!mFloatAction.isShown() && !mActionRotate.isRunning()) {
                mFloatAction.show();
            }
            mTitleIcon.setImageDrawable(null);
            mTitleTemp.setText("");
        }
    }


    // 数据刷新相关
    private void startRefresh() {
        mMainPostTime.setText(R.string.weather_refreshing);
        mRefreshStatus.setVisibility(View.VISIBLE);
        mActionRotate.start();
        mFloatAction.hide();
        mStartRefresh = SystemClock.currentThreadTimeMillis();
    }

    private void stopRefreshing() {
        mActionRotate.end();
        mRefreshStatus.setVisibility(View.GONE);
        mFloatAction.show();
    }

    // 请求成功进行处理
    @Override
    public void querySuccessful(HeWeatherResponse heWeatherResponse) {
        //解析数据
        HeWeatherResponse.HeWeather6Bean heWeather6Bean = heWeatherResponse.getHeWeather6().get(0);
        HeWeatherResponse.HeWeather6Bean.BasicBean basicBean = heWeather6Bean.getBasic();
        HeWeatherResponse.HeWeather6Bean.NowBean nowBean = heWeather6Bean.getNow();
        HeWeatherResponse.HeWeather6Bean.UpdateBean updateBean = heWeather6Bean.getUpdate();
        List<HeWeatherResponse.HeWeather6Bean.HourlyBean> hourlyBeans = heWeather6Bean.getHourly();
        mForecastBeanArrayList.clear();
        mForecastBeanArrayList.addAll(hourlyBeans);
        WeatherBasicData weatherBasicData = new WeatherBasicData();
        // 这是代表定位城市的意思
        weatherBasicData.setLocationIsCurrent(mDistrictSuffixCity.equals(basicBean.getLocation() + "," + basicBean.getParent_city()));
        weatherBasicData.setTemp(nowBean.getTmp());
        weatherBasicData.setUpdateTime(updateBean.getLoc());
        weatherBasicData.setWeather(nowBean.getCond_txt());
        weatherBasicData.setLocation(basicBean.getLocation());
        weatherBasicData.setWind(nowBean.getWind_dir());
        weatherBasicData.setWind_sc(nowBean.getWind_sc());

        // 当前查询的城市保存入内
        SharePreferenceUtils.getInstance(mContext).putString(AppConst.LOCATION_CITY_NOWS_ID, basicBean.getCid());
        // 判断是否有,没有的话就插入
        DBManager.getInstance().saveOrUpdateCity(new CityWeatherBean(basicBean.getCid(), basicBean.getLocation(), nowBean.getTmp(), nowBean.getCond_txt()));
        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.SIGN_UPDATE_VIEW);
        EventBus.getDefault().post(heWeather6Bean);

        if (SystemClock.currentThreadTimeMillis() - mStartRefresh > MIN_REFRESH_MILLS) {
            // 最小刷新时间
            onWeatherUpdate(weatherBasicData);

        } else {
            TaskScheduler.runOnUIThread(() -> onWeatherUpdate(weatherBasicData), MIN_REFRESH_MILLS + SystemClock.currentThreadTimeMillis() - mStartRefresh);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSignUpdate(UpdateSign mUpdateSign) {
        if (mUpdateSign != null && mUpdateSign.getCode() == AppConst.UPDATE_WEATHER_FROM_FG) {
            queryWeather(mUpdateSign.getStr());
        }
    }

    private void onWeatherUpdate(WeatherBasicData weatherBasicData) {
        updateBaseWeatherInfo(weatherBasicData);
        updateSucceed(mUpdateTime);
        mHoursForecastAdapter.notifyDataSetChanged();
        stopRefreshing();
    }

    private void updateSucceed(final String postTime) {

        mMainPostTime.setText(R.string.weather_refresh_succeed);

        mSucceedAnimator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if (fraction >= 0.5f) {
                mMainPostTime.setText(postTime);
            }
        });
        mSucceedAnimator.start();
    }

    private void updateBaseWeatherInfo(WeatherBasicData basicBean) {
        if (basicBean == null) {
            return;
        }

        mMainLocation.setCompoundDrawables(basicBean.isLocationIsCurrent() ? mDrawableLocation : null, null, null, null);
        mMainLocation.setText(basicBean.getLocation());
        mUpdateTime = String.format(getString(R.string.weather_post), TimeUtil.getTimeTips(basicBean.getUpdateTime()));
        mTemperature = basicBean.getTemp();
        mWeatherStatus = basicBean.getWeather();
        mMainTemp.setText(mTemperature + "°");
        mMainInfo.setText(mWeatherStatus);
        mTvWind.setText(basicBean.getWind());
        mTvSc.setText(basicBean.getWind_sc()+"级");
        if (TimeUtil.isNight()) {
            if (ResourceProvider.sunny(mWeatherStatus)) {
                mMainBg.setBackgroundResource(R.mipmap.ic_weather_bg_night);
            } else {
                mMainBg.setBackgroundResource(R.mipmap.ic_weather_bg_night_dark);
            }
        } else {
            mMainBg.setBackgroundResource(R.mipmap.ic_weather_bg_day);
        }
    }

    public void queryWeather(String location) {
        startRefresh();
        mPresenter.queryWeatherStatue(location);

    }

    @Override
    public void queryFailed() {
        stopRefreshing();
    }


    @Override
    protected WeatherAtPresenter createPresenter() {
        return new WeatherAtPresenter(this);
    }

}

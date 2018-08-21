package com.jit.appcloud.ui.activity.cultivate;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.bean.JsonBean;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.request.UserInfoUpRequest;
import com.jit.appcloud.model.response.NormalResponse;
import com.jit.appcloud.model.response.UserInfoResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.CheckUtils;
import com.jit.appcloud.util.GetJsonDataUtil;
import com.jit.appcloud.util.UIUtils;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/06/23.
 *         discription: 客户明细
 *         经销商身份登陆
 */
public class CusDtRoleMgActivity extends BaseActivity {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.iv_save)
    ImageView mIvSave;
    @BindView(R.id.tvLoginName)
    TextView mTvLoginName;
    @BindView(R.id.etLoginName)
    TextView mEtLoginName;
    @BindView(R.id.btnInitPsd)
    Button mBtnInitPsd;
    @BindView(R.id.tvCustomName)
    TextView mTvCustomName;
    @BindView(R.id.etCustomName)
    EditText mEtCustomName;
    @BindView(R.id.tvCompanyName)
    TextView mTvCompanyName;
    @BindView(R.id.etCompanyName)
    EditText mEtCompanyName;
    @BindView(R.id.tvAddress)
    TextView mTvAddress;
    @BindView(R.id.tvLocation)
    TextView mTvLocation;
    @BindView(R.id.rlLocation)
    RelativeLayout mRlLocation;
    @BindView(R.id.tvStreetDetail)
    TextView mTvStreetDetail;
    @BindView(R.id.etStreetDetail)
    EditText mEtStreetDetail;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.etPhone)
    EditText mEtPhone;
    @BindView(R.id.tvArea)
    TextView mTvArea;
    @BindView(R.id.etArea)
    EditText mEtArea;
    @BindView(R.id.tvYearIncome)
    TextView mTvYearIncome;
    @BindView(R.id.etYearIncome)
    EditText mEtYearIncome;
    @BindView(R.id.tvBreadCy)
    TextView mTvBreadCy;
    @BindView(R.id.llAgCategory)
    LinearLayout mLlAgCategory;
    @BindView(R.id.etBreadCg)
    EditText mEtBreadCg;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.svRgCommonInfo)
    ScrollView mSvRgCommonInfo;

    private String mProvince;
    private String mCity;
    private String mDistrict;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private boolean isLoaded = false;

    private String mUserName; //用于查询的用户名
    private int mCustomId;    //用于上级更新下级的用户信息 =======

    @Override
    protected void init() {
        Observable.create(this::initJsonData).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> isLoaded = aBoolean);
        if (getIntent() != null && getIntent().hasExtra(AppConst.TURN_TO_SHOW_AGENCY)) {
            mUserName = getIntent().getStringExtra(AppConst.INFO_SHOW_DETAIL);
            mCustomId = getIntent().getIntExtra(AppConst.INFO_CUS_ID, 0);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_cus_dt_role_mg;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(UIUtils.getString(R.string.str_title_save));
        mTvToolbarTitle.setText(R.string.title_custom_detail);
    }

    @Override
    protected void initData() {
        ApiRetrofit.getInstance().getUserInfoByName(mUserName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserInfoResponse>() {
                    @Override
                    public void accept(UserInfoResponse userInfoResponse) throws Exception {
                        hideWaitingDialog();
                        if (userInfoResponse.getCode() == 1){
                            updateViews(userInfoResponse.getData());
                        }else {
                            UIUtils.showToast(userInfoResponse.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideWaitingDialog();
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    }
                });
    }

    private void updateViews(UserInfoResponse.DataBean bean) {
        mProvince = bean.getProvince();
        mCity = bean.getCity();
        mDistrict = bean.getCountry();
        mEtLoginName.setText(bean.getUsername());
        mEtCustomName.setText(bean.getRealname());
        if (!TextUtils.isEmpty(bean.getDepartment()))
            mEtCompanyName.setText(bean.getDepartment());
        if (!TextUtils.isEmpty(bean.getProvince())){
            mTvLocation.setText(String.format(getString(R.string.str_format_city_bond),bean.getProvince(),bean.getCity(),bean.getCountry()));
        }

        if (!TextUtils.isEmpty(bean.getAddress())){
            mEtStreetDetail.setText(bean.getAddress());
        }

        if (!TextUtils.isEmpty(bean.getTel())){
            mEtPhone.setText(bean.getTel());
        }

        if (bean.getIncome() != 0){
            mEtYearIncome.setText(String.valueOf(bean.getIncome()));
        }

        if (!TextUtils.isEmpty(bean.getArea())){
            mEtArea.setText(bean.getArea());
        }

        if (!TextUtils.isEmpty(bean.getCategory())){
            mEtBreadCg.setText(bean.getCategory());
        }
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnInitPsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPwdInitDialog();
            }
        });

                /* 联系地址的三级菜单选择 */
        mRlLocation.setOnClickListener(v -> {
            // 隐藏软键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            if (isLoaded)
                showCitySelected();
            else
                UIUtils.showToast("sorry,城市数据未加载!!!");
        });

        mTvPublishNow.setOnClickListener(v -> doDefAndSubmit());
    }

    private void doDefAndSubmit() {
        String customName = mEtCustomName.getText().toString();
        if (TextUtils.isEmpty(customName)){
            UIUtils.showToast("客户名称不能为空!");
            return;
        }
        String phone = mEtPhone.getText().toString();
        if (!TextUtils.isEmpty(phone) && !CheckUtils.isPhone(phone)){
            UIUtils.showToast(getString(R.string.please_phone_error));
            return;
        }

        if (TextUtils.isEmpty(phone)){
            UIUtils.showToast(getString(R.string.phone));
            return;
        }

        showWaitingDialog(getString(R.string.str_please_waiting));
        UserInfoUpRequest request = new UserInfoUpRequest();
        request.setRealname(customName);
        request.setCategory(mEtBreadCg.getText().toString());
        if (!TextUtils.isEmpty(mEtYearIncome.getText().toString()))
            request.setIncome(Integer.parseInt(mEtYearIncome.getText().toString()));
        request.setCity(mCity);
        request.setCountry(mDistrict);
        request.setProvince(mProvince);
        request.setAddress(mEtStreetDetail.getText().toString());
        request.setDepartment(mEtCompanyName.getText().toString());
        request.setArea(mEtArea.getText().toString());
        request.setTel(phone);
        ApiRetrofit.getInstance().updateCustomInfo(mCustomId,request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NormalResponse>() {
                    @Override
                    public void accept(NormalResponse response) throws Exception {
                        hideWaitingDialog();
                        if (response.getCode() == 1){
                            BroadcastManager.getInstance(CusDtRoleMgActivity.this).sendBroadcast(AppConst.UPDATE_FM_CUSTOM);
                            finish();
                        }else {
                            UIUtils.showToast(response.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideWaitingDialog();
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    }
                });
    }


    private void showPwdInitDialog() {
        View rgScsView = LayoutInflater.from(mContext).inflate(R.layout.dialog_init_pwd, null);
        Dialog delDialog = new Dialog(mContext, R.style.MyDialog);
        Window window = delDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.BottomDialog);
        window.getDecorView().setPadding(50, 0, 50, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        delDialog.setContentView(rgScsView);
        EditText etPsd = delDialog.findViewById(R.id.etPsd);
        TextView tvCancel = delDialog.findViewById(R.id.tvCancel);
        TextView tvSub =  delDialog.findViewById(R.id.tvSubmit);
        TextView tvCusName = (TextView) delDialog.findViewById(R.id.tvCustomName);
        tvCusName.setText(mUserName);
        tvCancel.setOnClickListener(v -> delDialog.dismiss());
        tvSub.setOnClickListener(v -> {
            if (!UserCache.getPassword().equals(etPsd.getText().toString())){
                UIUtils.showToast("密码错误");
            }else {
                ApiRetrofit.getInstance().initPassword(mUserName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<NormalResponse>() {
                            @Override
                            public void accept(NormalResponse response) throws Exception {
                                if (response.getCode() == 1){
                                    UIUtils.showToast("初始化密码成功!");
                                }else{
                                    UIUtils.showToast(response.getMsg());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                UIUtils.showToast(throwable.getLocalizedMessage());
                            }
                        });
                delDialog.dismiss();
            }
        });
        delDialog.show();
    }

    private void showCitySelected() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置

            mProvince = options1Items.get(options1).getPickerViewText();
            mCity = options2Items.get(options1).get(options2);
            mDistrict = options3Items.get(options1).get(options2).get(options3);
            mTvLocation.setText(String.format(UIUtils.getString(R.string.str_format_city_bond), mProvince, mCity, mDistrict));
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
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

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



}

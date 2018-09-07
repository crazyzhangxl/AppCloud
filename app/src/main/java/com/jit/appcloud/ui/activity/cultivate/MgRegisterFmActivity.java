package com.jit.appcloud.ui.activity.cultivate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.AgDeviceBean;
import com.jit.appcloud.db.db_model.AgPondBean;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.bean.AgAddPondBean;
import com.jit.appcloud.model.bean.DeviceBatchInsert;
import com.jit.appcloud.model.bean.JsonBean;
import com.jit.appcloud.model.request.AgAddPondResponse;
import com.jit.appcloud.model.request.RgCustomRequest;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.CheckUtils;
import com.jit.appcloud.util.GetJsonDataUtil;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/06/23.
 *         discription:
 *         role: 经销商
 *         经销商注册养殖户的界面
 */
public class MgRegisterFmActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tvFrontStep)
    TextView mTvFrontStep;
    @BindView(R.id.tvNextStep)
    TextView mTvNextStep;
    @BindView(R.id.llTwoSteps)
    LinearLayout mLlTwoSteps;
    @BindView(R.id.tvRgCommonInfo)
    TextView mTvRgCommonInfo;
    @BindView(R.id.lineCommonInfo)
    View mLineCommonInfo;
    @BindView(R.id.lineCommonInfoS)
    View mLineCommonInfoS;
    @BindView(R.id.tvRgPondInfo)
    TextView mTvRgPondInfo;
    @BindView(R.id.lineRgPondInfoS)
    View mLineRgPondInfoS;
    @BindView(R.id.lineRgPondInfo)
    View mLineRgPondInfo;
    @BindView(R.id.tvRgDeviceInfo)
    TextView mTvRgDeviceInfo;
    @BindView(R.id.lineRgDeviceInfoS)
    View mLineRgDeviceInfoS;
    @BindView(R.id.lineRgDeviceInfo)
    View mLineRgDeviceInfo;
    @BindView(R.id.tvLoginName)
    TextView mTvLoginName;
    @BindView(R.id.etLoginName)
    EditText mEtLoginName;
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
    @BindView(R.id.svRgCommonInfo)
    ScrollView mSvRgCommonInfo;
    @BindView(R.id.rlRgPondInfo)
    RelativeLayout mRlRgPondInfo;
    @BindView(R.id.rlDeviceInfo)
    RelativeLayout mSvRgDeviceInfo;
    @BindView(R.id.lqrPondManage)
    RecyclerView mLqrPondManage;
    @BindView(R.id.btnAddPond)
    Button mBtnAddPond;
    @BindView(R.id.rvDeviceMg)
    RecyclerView mRvDeviceMg;
    @BindView(R.id.btnAddDevice)
    Button mBtnAddDevice;
    /**
     *用于记录选中的注册信息 0: 基本信息 ; 1:鱼塘 ; 2:设备
     */
    private int mRgInfoPosition;
    private String mProvince;
    private String mCity;
    private String mDistrict;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private boolean isLoaded = false;
    private BaseQuickAdapter<AgPondBean, BaseViewHolder> mPondManageAp;
    private List<AgPondBean> mAgPondBeanList = new ArrayList<>();
    private BaseQuickAdapter<AgDeviceBean, BaseViewHolder> mDeviceManageAp;
    private List<AgDeviceBean> mAgDeviceBeanList = new ArrayList<>();
    private String mMUserName;

    @Override
    protected void init() {
        /* 很好使用了rxjava来进行加载了*/
        Observable.create(this::initJsonData).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> isLoaded = aBoolean);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_mg_register_fm;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("注册");
        mLlTwoSteps.setVisibility(View.VISIBLE);
        setTwoSteps();
        initPond();
        initDevice();

    }

    private void setTwoSteps() {
        switch (mRgInfoPosition) {
            case 0:
                mTvFrontStep.setText("");
                mTvNextStep.setText("下一步");
                break;
            case 1:
                mTvFrontStep.setText("上一步");
                mTvNextStep.setText("下一步");
                break;
            case 2:
                mTvFrontStep.setText("上一步");
                mTvNextStep.setText("确定");
                break;
            default:
                break;
        }
    }

    /**
     *  设置二级标题的状态
     */
    private void setSmallTitleState() {
        switch (mRgInfoPosition) {
            case 0:
                mTvRgCommonInfo.setTextColor(UIUtils.getColor(R.color.black));
                mLineCommonInfoS.setVisibility(View.VISIBLE);
                mLineCommonInfo.setVisibility(View.GONE);
                mTvRgPondInfo.setTextColor(UIUtils.getColor(R.color.gray2));
                mLineRgPondInfoS.setVisibility(View.GONE);
                mLineRgPondInfo.setVisibility(View.VISIBLE);
                mTvRgDeviceInfo.setTextColor(UIUtils.getColor(R.color.gray2));
                mLineRgDeviceInfo.setVisibility(View.VISIBLE);
                mLineRgDeviceInfoS.setVisibility(View.GONE);
                break;
            case 1:
                mTvRgCommonInfo.setTextColor(UIUtils.getColor(R.color.gray2));
                mLineCommonInfoS.setVisibility(View.GONE);
                mLineCommonInfo.setVisibility(View.VISIBLE);
                mTvRgPondInfo.setTextColor(UIUtils.getColor(R.color.black));
                mLineRgPondInfoS.setVisibility(View.VISIBLE);
                mLineRgPondInfo.setVisibility(View.GONE);
                mTvRgDeviceInfo.setTextColor(UIUtils.getColor(R.color.gray2));
                mLineRgDeviceInfo.setVisibility(View.VISIBLE);
                mLineRgDeviceInfoS.setVisibility(View.GONE);
                break;
            case 2:
                mTvRgCommonInfo.setTextColor(UIUtils.getColor(R.color.gray2));
                mLineRgDeviceInfo.setVisibility(View.VISIBLE);
                mLineRgDeviceInfoS.setVisibility(View.GONE);
                mTvRgPondInfo.setTextColor(UIUtils.getColor(R.color.gray2));
                mLineRgPondInfoS.setVisibility(View.GONE);
                mLineRgPondInfo.setVisibility(View.VISIBLE);
                mTvRgDeviceInfo.setTextColor(UIUtils.getColor(R.color.black));
                mLineRgDeviceInfo.setVisibility(View.GONE);
                mLineRgDeviceInfoS.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void setContentState() {
        switch (mRgInfoPosition) {
            case 0:
                mSvRgCommonInfo.setVisibility(View.VISIBLE);
                mRlRgPondInfo.setVisibility(View.GONE);
                mSvRgDeviceInfo.setVisibility(View.GONE);
                break;
            case 1:
                mSvRgCommonInfo.setVisibility(View.GONE);
                mRlRgPondInfo.setVisibility(View.VISIBLE);
                mSvRgDeviceInfo.setVisibility(View.GONE);
                break;
            case 2:
                mSvRgCommonInfo.setVisibility(View.GONE);
                mRlRgPondInfo.setVisibility(View.GONE);
                mSvRgDeviceInfo.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> {
            MgRegisterFmActivity.this.onBackPressed();
        });

        mTvFrontStep.setOnClickListener(v -> {
            switch (mRgInfoPosition) {
                case 0:
                    /* 未呈现哦*/
                    break;
                case 1:
                    mRgInfoPosition = 0;
                    setTwoSteps();
                    setSmallTitleState();
                    setContentState();
                    break;
                case 2:
                    mRgInfoPosition = 1;
                    setTwoSteps();
                    setSmallTitleState();
                    setContentState();
                    break;
                default:
                    break;
            }
        });

        mTvNextStep.setOnClickListener(v -> {
            switch (mRgInfoPosition) {
                case 0:
                    /* 提交基本信息 失败则返回*/
                    /* */
                    checkDataAndSubmit();
                    break;
                case 1:
                    /* 提交塘口信息 失败则提醒*/
                    checkPondAndSubmit();

                    break;
                case 2:
                    /* 提交设备信息 失败则提醒 */
                    checkDeviceAndSubmit();
                    break;
                default:
                    break;
            }

        });

        mRlLocation.setOnClickListener(v -> {
            if (isLoaded) {
                showCitySelected();
            } else{
                UIUtils.showToast("sorry,城市数据未加载!!!");
            }
        });


        /* 塘口管理 */
        // 添加塘口点击事件
        mBtnAddPond.setOnClickListener(v -> jumpToActivityForResult(AgPondAddActivity.class, AppConst.RECODE_FROM_POND_MANAGE));

        mBtnAddDevice.setOnClickListener(v -> jumpToActivityForResult(AgDeviceAddActivity.class,AppConst.RECODE_FROM_DEVICE_MANAGE ));
    }

    private void checkDeviceAndSubmit() {
        List<AgDeviceBean> agDeviceBeans = DBManager.getInstance().queryAllAgDevice();
        if (agDeviceBeans == null ||agDeviceBeans.size() == 0){
            UIUtils.showToast("请添加设备!!!");
            return;
        }

        List<DeviceBatchInsert> mDeviceBeans = new ArrayList<>();
        for (AgDeviceBean agDeviceBean:agDeviceBeans){
            DeviceBatchInsert batchInsert = new DeviceBatchInsert();

            batchInsert.setDevice_no(agDeviceBean.getDeviceID());
            //batchInsert.setAddress(agDeviceBean.getWorkAddress());
            //batchInsert.setMac_ip(agDeviceBean.getMacAddress());
            batchInsert.setPound_id(agDeviceBean.getPondId());
            batchInsert.setType(agDeviceBean.getType());
            mDeviceBeans.add(batchInsert);
        }
        ApiRetrofit.getInstance().deviciceBatchInsert(mDeviceBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                       if (response.getCode() == 1){
                           BroadcastManager.getInstance(MgRegisterFmActivity.this).sendBroadcast(AppConst.UPDATE_FM_CUSTOM);
                           finish();
                       }else {
                           UIUtils.showToast(response.getMsg());
                       }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

    private void checkPondAndSubmit() {
        // 转化数据 ================
        List<AgPondBean> agPondBeans = DBManager.getInstance().queryAllAgPond();
        if (agPondBeans == null || agPondBeans.size() == 0){
            UIUtils.showToast("请添加塘口!!!!");
            return;
        }
        List<AgAddPondBean> mPondBeans = new ArrayList<>();
        for (AgPondBean pondBean:agPondBeans){
            AgAddPondBean bean = new AgAddPondBean();
            bean.setNumber(pondBean.getPondId());
            bean.setType(pondBean.getPondCg());
            bean.setFeed_brand(pondBean.getLiaoType());
            bean.setFeed_type(pondBean.getLiaoName());
            bean.setSeed_time(pondBean.getSeedTime());
            bean.setSeed_number(pondBean.getSeedNum());
            bean.setUsername(mMUserName);
            if (!TextUtils.isEmpty(pondBean.getSeedArea())) {
                bean.setSquare(Integer.parseInt(pondBean.getSeedArea()));
            }
            bean.setSeed_type(pondBean.getPondVy());
            bean.setSeed_brand(pondBean.getSeedBread());
            mPondBeans.add(bean);
        }
        ApiRetrofit.getInstance().addPondBratch(mPondBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getCode() == 1) {
                        // 转换数据
                        DBManager.getInstance().clearAllAgPond();
                        List<AgAddPondResponse.DataBean> data = response.getData();
                        for (AgAddPondResponse.DataBean dataBean:data){
                            AgPondBean agPondBean = new AgPondBean();
                            agPondBean.setPondId(dataBean.getNumber());
                            agPondBean.setPondCg(dataBean.getType());
                            agPondBean.setSeedArea(String.valueOf(dataBean.getSquare()));
                            agPondBean.setSeedTime(dataBean.getSeed_time());
                            agPondBean.setPondVy(dataBean.getSeed_brand());
                            agPondBean.setLiaoName(dataBean.getFeed_type());
                            agPondBean.setLiaoType(dataBean.getFeed_brand());
                            agPondBean.setSeedNum(dataBean.getSeed_number());
                            agPondBean.setPondVy(dataBean.getSeed_brand());
                            agPondBean.setPondPosition(dataBean.getId());
                            DBManager.getInstance().saveAgPond(agPondBean);
                        }
                        mRgInfoPosition = 2;
                        setTwoSteps();
                        setSmallTitleState();
                        setContentState();
                    }else {
                        UIUtils.showToast(response.getMsg());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

    private void checkDataAndSubmit() {
        String mLoginName = mEtLoginName.getText().toString();
        String customName = mEtCustomName.getText().toString();
        String phone = mEtPhone.getText().toString();
        if (TextUtils.isEmpty(mLoginName)){
            UIUtils.showToast(getString(R.string.please_user_name));
            return;
        }

        if (TextUtils.isEmpty(customName)){
            UIUtils.showToast(getString(R.string.please_real_name));
            return;
        }

        if (TextUtils.isEmpty(phone)){
            UIUtils.showToast(getString(R.string.please_input_phone));
            return;
        }

        if (!CheckUtils.isPhone(phone)){
            UIUtils.showToast(getString(R.string.please_phone_error));
            return;
        }
        showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        RgCustomRequest upRequest = new RgCustomRequest(mLoginName,customName,phone);
        upRequest.setArea(mEtArea.getText().toString());
        upRequest.setProvince(mProvince);
        upRequest.setCountry(mDistrict);
        upRequest.setCity(mCity);
        upRequest.setAddress(mEtStreetDetail.getText().toString());
        if (!TextUtils.isEmpty(mEtYearIncome.getText().toString())) {
            upRequest.setIncome(Integer.parseInt(mEtYearIncome.getText().toString()));
        }
        upRequest.setCategory(mEtBreadCg.getText().toString());
        upRequest.setDepartment(mEtCompanyName.getText().toString());
        ApiRetrofit.getInstance().registerNextUser(upRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rgCustomResponse -> {
                    hideWaitingDialog();
                    if (rgCustomResponse.getCode() == 1){
                        mMUserName = mLoginName;
                        mRgInfoPosition = 1;
                        setTwoSteps();
                        setSmallTitleState();
                        setContentState();
                        BroadcastManager.getInstance(MgRegisterFmActivity.this).sendBroadcast(AppConst.UPDATE_FM_CUSTOM);
                        BroadcastManager.getInstance(MgRegisterFmActivity.this).sendBroadcast(AppConst.UPDATE_MG_EP_LIST);

                    }else {
                        UIUtils.showToast(rgCustomResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }


   /**
    * 塘口管理初始化 ----
    * */
    private void initPond() {
        LinearLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mLqrPondManage.setLayoutManager(manager);
        mPondManageAp = new BaseQuickAdapter<AgPondBean, BaseViewHolder>(R.layout.item_pond_mg_show, mAgPondBeanList) {
            @Override
            protected void convert(BaseViewHolder helper, AgPondBean item) {
                helper.setText(R.id.tvPondId, String.format("塘号:%s", item.getPondId()));
                helper.setText(R.id.tvPondCg, String.format("分类:%s", item.getPondCg()));
                helper.setText(R.id.tvPondVy, String.format("品牌:%s", item.getPondVy()));
                helper.getView(R.id.llDelete).setOnClickListener(v -> {
                    mAgPondBeanList.remove(helper.getAdapterPosition());
                    DBManager.getInstance().deleteAgPond(item.getId());
                    mPondManageAp.notifyDataSetChanged();
                });

                helper.getView(R.id.llEdit).setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppConst.EXTRA_SER_AG_POND_EDITOR, item);
                    jumpToActivityForResult(AgPondEditorActivity.class, bundle, AppConst.RECODE_FROM_POND_MANAGE);
                });

            }
        };
        mLqrPondManage.setAdapter(mPondManageAp);
    }

    /**
     * 设备管理初始化
     * */
    private void initDevice() {
        LinearLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvDeviceMg.setLayoutManager(manager);
        mDeviceManageAp = new BaseQuickAdapter<AgDeviceBean, BaseViewHolder>(R.layout.item_device_mg_show, mAgDeviceBeanList) {
            @Override
            protected void convert(BaseViewHolder helper, AgDeviceBean item) {
                helper.setText(R.id.tvDeviceId, String.format("设备ID:%s", item.getDeviceID()));
                //helper.setText(R.id.tvMacAddress, String.format("Mac地址:%s", item.getMacAddress()));
                helper.setText(R.id.tvDeviceFunc, String.format("功能:%s", AppConst.DEVICE_KIND[item.getType()]));
                helper.setText(R.id.tvDevicePond, String.format("鱼塘:%s", item.getPondName()));
                helper.getView(R.id.llDelete).setOnClickListener(v -> {
                    mAgDeviceBeanList.remove(helper.getAdapterPosition());
                    DBManager.getInstance().deleteAgDevice(item.getId());
                    DBManager.getInstance().deleteDvDtByDeviceId(item.getDeviceID());
                    mDeviceManageAp.notifyDataSetChanged();
                });

                helper.getView(R.id.llEdit).setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppConst.EXTRA_SER_AG_DEVICE_EDITOR, item);
                    jumpToActivityForResult(AgDeviceEditorActivity.class, bundle, AppConst.RECODE_FROM_DEVICE_MANAGE);
                });

            }
        };
        mRvDeviceMg.setAdapter(mDeviceManageAp);
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
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();
    }

    private void initJsonData(ObservableEmitter<Boolean> emitter) {//解析数据

        /*
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");

        ArrayList<JsonBean> jsonBean = parseData(JsonData, emitter);

        /*
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {
            ArrayList<String> cityList = new ArrayList<>();
            ArrayList<ArrayList<String>> mProvinceAreaList = new ArrayList<>();

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);
                ArrayList<String> cityAreaList = new ArrayList<>();

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    cityAreaList.add("");
                } else {
                    cityAreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                mProvinceAreaList.add(cityAreaList);
            }

            options2Items.add(cityList);

            options3Items.add(mProvinceAreaList);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConst.RECODE_FROM_POND_MANAGE:
                    /* 表示从塘口管理转向*/
                    mAgPondBeanList.clear();
                    mAgPondBeanList.addAll(DBManager.getInstance().queryAllAgPond());
                    mPondManageAp.notifyDataSetChanged();
                    break;
                case AppConst.RECODE_FROM_DEVICE_MANAGE:
                    /* 表示从塘口管理转向*/
                    mAgDeviceBeanList.clear();
                    mAgDeviceBeanList.addAll(DBManager.getInstance().queryAllAgDevice());
                    mDeviceManageAp.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        DBManager.getInstance().clearAllAgPond();
        DBManager.getInstance().clearAllAgDevice();
        DBManager.getInstance().clearAllAgDeviceDt();
        super.onDestroy();
    }
}

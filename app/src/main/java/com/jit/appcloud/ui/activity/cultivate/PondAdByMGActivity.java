package com.jit.appcloud.ui.activity.cultivate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.PondBean;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.bean.JsonBean;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserOtherCache;
import com.jit.appcloud.model.request.AddPondRequest;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.GetJsonDataUtil;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
/**
 * @author zxl on 2018/05/23.
 *         discription:经销商添加塘口活动
 */
public class PondAdByMGActivity extends BaseActivity {

    @BindView(R.id.rlDetailLocation)
    RelativeLayout mRlDetailLocation;
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.etPondName)
    EditText mEtPondName;
    @BindView(R.id.spPondKind)
    MaterialSpinner mSpPondKind;
    @BindView(R.id.etPondArea)
    EditText mEtPondArea;
    @BindView(R.id.spPondUnit)
    MaterialSpinner mSpPondUnit;
    @BindView(R.id.etPondLength)
    EditText mEtPondLength;
    @BindView(R.id.etPondWidth)
    EditText mEtPondWidth;
    @BindView(R.id.etPondDepth)
    EditText mEtPondDepth;
    @BindView(R.id.rlChooseMan)
    RelativeLayout mRlChooseMan;
    @BindView(R.id.etPondRSMan)
    TextView mTvPondRSMan;
    @BindView(R.id.etPondPhone)
    EditText mEtPondPhone;
    @BindView(R.id.etPondEmail)
    EditText mEtPondEmail;
    @BindView(R.id.tvAShowLocation)
    TextView mTvAShowLocation;
    @BindView(R.id.rlModifyLocation)
    RelativeLayout mRlModifyLocation;
    @BindView(R.id.tvDetailLocation)
    TextView mTvDetailLocation;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.tvTimeAdded)
    TextView mTvTimeAdded;
    @BindView(R.id.rlAddTime)
    RelativeLayout mRlAddTime;
    private String mFarmerName = "";

    /**
     * 省市区分开---方便提交
     */
    private String mProvince;
    private String mCity;
    private String mDistrict;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private boolean isLoaded = false;
    private TimePickerView pvTime;
    @Override
    protected void init() {
        initTimePicker();
        /* 很好使用了rxjava来进行加载了*/
        Observable.create(this::initJsonData).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> isLoaded = aBoolean);

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_pond_ad_by_mg;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(UIUtils.getString(R.string.title_add_pond));
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(UIUtils.getString(R.string.str_title_submit));
        mSpPondKind.setItems(AppConst.SP_POND_KIND);
        mSpPondKind.setSelectedIndex(0);
        mSpPondUnit.setItems(AppConst.SP_AREA_UNIT);
        mSpPondUnit.setSelectedIndex(0);

        mTvPondRSMan.setText(UserOtherCache.getFarmSelectedName());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mTvPublishNow.setOnClickListener(v -> verifyData());

        mRlModifyLocation.setOnClickListener(v -> {
            if (isLoaded) {
                showPickerView();
            } else {
                UIUtils.showToast("sorry,城市数据未加载!!!");
            }
        });

        mRlDetailLocation.setOnClickListener(v -> {
            Intent intent = new Intent(PondAdByMGActivity.this, KeyInputActivity.class);
            intent.putExtra(AppConst.KEY_INPUT_TITLE, UIUtils.getString(R.string.str_title_address_detail));
            intent.putExtra(AppConst.KEY_TO_INPUT_CONTENT, mTvDetailLocation.getText().toString().trim());
            startActivityForResult(intent, AppConst.RCODE_INPUT_FROM_AREA_DETAIL);
        });

        mRlAddTime.setOnClickListener(v -> pvTime.show());
    }

    private void verifyData() {
        String mPondName = mEtPondName.getText().toString().trim();
        if (TextUtils.isEmpty(mPondName)){
            UIUtils.showToast("请输入塘号");
            return;
        }

        String mPondArea = mEtPondArea.getText().toString().trim();
        if (TextUtils.isEmpty(mPondArea)){
            UIUtils.showToast("请输入池塘面积");
            return;
        }

        String mPondLength = mEtPondLength.getText().toString().trim();
        if (TextUtils.isEmpty(mPondLength)){
            UIUtils.showToast("请输入池塘长度");
            return;
        }

        String mPondWidth = mEtPondWidth.getText().toString().trim();
        if (TextUtils.isEmpty(mPondWidth)){
            UIUtils.showToast("请输入池塘宽度");
            return;
        }

        String mPondDepth = mEtPondDepth.getText().toString().trim();
        if (TextUtils.isEmpty(mPondDepth)){
            UIUtils.showToast("请输入池塘深度");
            return;
        }

        mFarmerName = mTvPondRSMan.getText().toString();
        if (TextUtils.isEmpty(mProvince)){
            UIUtils.showToast("请选择地区");
            return;
        }

        String location = mTvDetailLocation.getText().toString();
        if (TextUtils.isEmpty(location)){
            UIUtils.showToast("请输入详细地址");
            return;
        }
        showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        AddPondRequest request = new AddPondRequest();
        request.setNumber(mPondName);
        request.setType(AppConst.SP_POND_KIND[mSpPondKind.getSelectedIndex()]);
        request.setUnit(AppConst.SP_AREA_UNIT[mSpPondUnit.getSelectedIndex()]);
        request.setLength(Double.parseDouble(mPondLength));
        request.setWidth(Double.parseDouble(mPondWidth));
        request.setDepth(Double.parseDouble(mPondDepth));
        request.setSquare(Double.parseDouble(mPondArea));
        request.setProvince(mProvince);
        request.setCity(mCity);
        request.setCountry(mDistrict);
        request.setAddress(location);
        request.setUsername(mFarmerName);
        /* 进行提交*/
        ApiRetrofit.getInstance().addPondByManager(UserCache.getToken(),
                request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addPondResponse -> {
                    hideWaitingDialog();
                    if (addPondResponse.getCode() == 1){
                        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_POND_MG);
                        DBManager.getInstance().savePond(new PondBean(addPondResponse.getData().getId(),addPondResponse.getData().getNumber()));
                        finish();
                    }else {
                        UIUtils.showToast(addPondResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConst.RCODE_INPUT_FROM_AREA_DETAIL:
                    mTvDetailLocation.setText(data.getStringExtra(AppConst.KEY_FROM_INPUT));
                    break;
            }

        }
    }

    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置

            mProvince = options1Items.get(options1).getPickerViewText();
            mCity = options2Items.get(options1).get(options2);
            mDistrict = options3Items.get(options1).get(options2).get(options3);
            mTvAShowLocation.setText(String.format(UIUtils.getString(R.string.str_format_city_bond), mProvince, mCity, mDistrict));
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
            mTvTimeAdded.setText(TimeUtil.date2String(date,"yyyy-MM-dd HH:mm"));
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "点", "分", "")
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

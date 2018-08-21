package com.jit.appcloud.ui.activity.me;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.event.UpdateSign;
import com.jit.appcloud.model.bean.JsonBean;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.GetJsonDataUtil;
import com.jit.appcloud.util.UIUtils;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
/**
 * @author zxl on 2018/5/14
 *  description: 添加塘口界面
 *               经销商可操作
* */
public class AddFarmPondActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.tvPondName)
    TextView mTvPondName;
    @BindView(R.id.etPondName)
    EditText mEtPondName;
    @BindView(R.id.tvPondKind)
    TextView mTvPondKind;
    @BindView(R.id.spPondKind)
    MaterialSpinner mSpPondKind;
    @BindView(R.id.tvPondArea)
    TextView mTvPondArea;
    @BindView(R.id.etPondArea)
    EditText mEtPondArea;
    @BindView(R.id.spPondUnit)
    MaterialSpinner mSpPondUnit;
    @BindView(R.id.tvPondLocation)
    TextView mTvPondLocation;
    @BindView(R.id.tvAShowLocation)
    TextView mTvAShowLocation;
    @BindView(R.id.rlModifyLocation)
    RelativeLayout mRlModifyLocation;
    @BindView(R.id.etDetailLocation)
    EditText mEtDetailLocation;
    private String mProvince;
    private String mCity;
    private String mDistrict;
    private String mStrPondNum;
    private String mStrPondKind;
    private String mStrAreaSize;
    private String mStrareaUint;
    private String mStrLoction;
    private String mStrDetailLocation;
    /**
     * 联动菜单相关
     ***/
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private boolean isLoaded = false;
    private Thread thread;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_LOAD_DATA:
                    //如果已创建就不再重新创建子线程了
                    if (thread == null) {
                        thread = new Thread(() -> {
                            // 写子线程中的操作,解析省市区数据
                            initJsonData();
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_FAILED:
                    isLoaded = false;
                    break;
                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    break;
                default:
            }
        }
    };

    @Override
    protected void init() {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_add_farm_pond;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(UIUtils.getString(R.string.title_add_pond));
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(UIUtils.getString(R.string.title_next));

        mSpPondUnit.setItems(AppConst.SP_AREA_UNIT);
        mSpPondUnit.setSelectedIndex(0);
        mSpPondKind.setItems(AppConst.SP_POND_KIND);
        mSpPondKind.setSelectedIndex(0);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> finish());

        mRlModifyLocation.setOnClickListener(v -> {
            if (isLoaded){
                showPickerView();
            }else {
                UIUtils.showToast("sorry,城市数据未加载!!!");
            }
        });

        mTvPublishNow.setOnClickListener(v -> verifyData());
    }

    private void verifyData() {
        mStrPondNum = mEtPondName.getText().toString();
        if (TextUtils.isEmpty(mStrPondNum)){
            UIUtils.showToast("请输入塘口号");
            return;
        }

        mStrAreaSize = mEtPondArea.getText().toString();
        if (TextUtils.isEmpty(mStrAreaSize)){
            UIUtils.showToast("请输入池塘面积");
            return;
        }

        mStrLoction = mTvAShowLocation.getText().toString();
        if (TextUtils.isEmpty(mStrLoction)){
            UIUtils.showToast("请选择塘口地址");
            return;
        }

        mStrDetailLocation = mEtDetailLocation.getText().toString();
        if (TextUtils.isEmpty(mStrDetailLocation)){
            UIUtils.showToast("请输入塘口的详细地址");
            return;
        }

        mStrPondKind = AppConst.SP_POND_KIND[mSpPondKind.getSelectedIndex()];
        mStrareaUint = AppConst.SP_AREA_UNIT[mSpPondUnit.getSelectedIndex()];
        UIUtils.showToast("点击了下一步!!!");
    }


    public void showPickerView() {// 弹出选择器
        @SuppressLint("SetTextI18n") OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置

            mProvince = options1Items.get(options1).getPickerViewText();
            mCity = options2Items.get(options1).get(options2);
            mDistrict = options3Items.get(options1).get(options2).get(options3);
            mTvAShowLocation.setText(mProvince+" "+mCity+" "+mDistrict);
        }).setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();
        // setTextColorCenter(Color.BLACK) 设置选中项文字颜色
        // setOutSideCancelable(false) default is true
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        //获取assets目录下的json文件数据
        String jsonData = new GetJsonDataUtil().getJson(mContext, "province.json");
        //用Gson 转成实体
        ArrayList<JsonBean> jsonBean = parseData(jsonData);

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {
            //遍历省份
            ArrayList<String> cityList = new ArrayList<>();
            //该省的城市列表（第二级）
            ArrayList<ArrayList<String>> mProvinceAreaList = new ArrayList<>();
            //该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {
                //遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);
                //添加城市

                ArrayList<String> cityAreaList = new ArrayList<>();
                //该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    cityAreaList.add("");
                } else {

                    //该城市对应地区所有数据
                    //添加该城市所有地区数据
                    cityAreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                mProvinceAreaList.add(cityAreaList);
                //添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(mProvinceAreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {
        //Gson 解析
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
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

}

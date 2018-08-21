package com.jit.appcloud.ui.fragment.me;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.event.UpdateSign;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.db.db_model.CityWeatherBean;
import com.jit.appcloud.model.bean.JsonBean;
import com.jit.appcloud.ui.activity.me.WeatherActivity;
import com.jit.appcloud.ui.adapter.multi.CityAddAdapter;
import com.jit.appcloud.ui.adapter.multi.MultiItem;
import com.jit.appcloud.ui.base.BaseFragment;
import com.jit.appcloud.ui.presenter.WhCityFgPresenter;
import com.jit.appcloud.ui.view.IWeatherCityView;
import com.jit.appcloud.util.FormatUtils;
import com.jit.appcloud.util.GetJsonDataUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 张先磊 on 2018/4/23.
 */

public class WeatherCityFragment extends BaseFragment<IWeatherCityView, WhCityFgPresenter> implements IWeatherCityView{

    @BindView(R.id.city_follow_list)
    RecyclerView mCityFollowList;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private boolean isLoaded = false;
    private Thread thread;
    private ArrayList<MultiItem> mMultiItemList =  new ArrayList<>();
    //private WeatherInfoDao mWeatherInfoDao;
    private CityAddAdapter mCityAddAdapter;

    public static BaseFragment newInstance() {
        WeatherCityFragment cityFragment;
        cityFragment = new WeatherCityFragment();
        return cityFragment;
    }

    @SuppressLint("HandlerLeak")
    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        thread = new Thread(() -> {
                            // 写子线程中的操作,解析省市区数据
                            initJsonData();
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_FAILED:
                    Toast.makeText(_mActivity, "数据解析失败", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    break;
            }
        }
    };

    @Override
    public void init() {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
    }

    @Override
    public void initView(View rootView) {
        mCityFollowList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mCityFollowList.setBackgroundResource(R.color.white);
        mMultiItemList.clear();
        MultiItem multiItem = new MultiItem();
        mMultiItemList.add(multiItem);
        mCityAddAdapter = new CityAddAdapter(mMultiItemList,getActivity());
        mCityAddAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.image){
                // 弹出城市选择了
                if (isLoaded) {
                    showPickerView();
                }
            }else if (view.getId() == R.id.delete){
                DBManager.getInstance().deleteCityById(mMultiItemList.get(position).cityId);
                mCityAddAdapter.remove(position);
                mCityAddAdapter.notifyDataSetChanged();
            }
        });

        mCityAddAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mCityAddAdapter.isDeleting()) {
                mCityAddAdapter.setDeleting(false);
                mCityAddAdapter.notifyDataSetChanged();
            }else {
                EventBus.getDefault().post(new UpdateSign(mMultiItemList.get(position).cityId, AppConst.UPDATE_WEATHER_FROM_FG));
            }
        });

        mCityAddAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            mCityAddAdapter.setDeleting(true);
            mCityAddAdapter.notifyDataSetChanged();
            return false;
        });


        mCityFollowList.setAdapter(mCityAddAdapter);
        BroadcastManager.getInstance(_mActivity).register(AppConst.SIGN_UPDATE_VIEW, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onEvent();
            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void onEvent(){
        List<CityWeatherBean> cityWeatherBeans = DBManager.getInstance().queryAllCity();
        mMultiItemList.clear();
            for (int i=0;i<cityWeatherBeans.size();i++){
                mMultiItemList.add(new MultiItem(cityWeatherBeans.get(i),CityAddAdapter.BLUR_IMAGE[i % CityAddAdapter.BLUR_IMAGE.length]));
            }
            mMultiItemList.add(new MultiItem());
            mCityAddAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(_mActivity).unregister(AppConst.SIGN_UPDATE_VIEW);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    protected WhCityFgPresenter createPresenter() {
        return new WhCityFgPresenter((WeatherActivity) getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_wh_city;
    }


    @Override
    public RecyclerView getRecyclerView() {
        return mCityFollowList;
    }



    public void showPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(_mActivity, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String districtName = FormatUtils.getDistrictName(options2Items.get(options1).get(options2), options3Items.get(options1).get(options2).get(options3));
            Toast.makeText(_mActivity, districtName, Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new UpdateSign(districtName, AppConst.UPDATE_WEATHER_FROM_FG));
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(getActivity(), "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

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
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
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

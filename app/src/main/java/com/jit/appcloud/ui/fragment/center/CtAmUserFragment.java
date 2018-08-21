package com.jit.appcloud.ui.fragment.center;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.maps.model.Text;
import com.bigkoo.pickerview.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.bean.JsonBean;
import com.jit.appcloud.model.request.RgCustomRequest;
import com.jit.appcloud.model.request.UserInfoUpRequest;
import com.jit.appcloud.model.response.NormalResponse;
import com.jit.appcloud.model.response.RgCustomResponse;
import com.jit.appcloud.ui.adapter.decoration.GridSpacingItemDecoration;
import com.jit.appcloud.ui.base.BaseFragment;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.CheckUtils;
import com.jit.appcloud.util.GetJsonDataUtil;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/6/20.
 *         discription:中间的总代理进行注册
 */
public class CtAmUserFragment extends BaseFragment {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.tvLoginName)
    TextView mTvLoginName;
    @BindView(R.id.etLoginName)
    EditText mEtLoginName;
    @BindView(R.id.tvCustomName)
    TextView mTvCustomName;
    @BindView(R.id.etCustomName)
    EditText mEtCustomName;
    @BindView(R.id.tvStoreName)
    TextView mTvStoreName;
    @BindView(R.id.etStoreName)
    EditText mEtStoreName;
    @BindView(R.id.tvStreetDetail)
    TextView mTvStreetDetail;
    @BindView(R.id.etStreetDetail)
    EditText mEtStreetDetail;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.etPhone)
    EditText mEtPhone;
    @BindView(R.id.tvYearIncome)
    TextView mTvYearIncome;
    @BindView(R.id.etYearIncome)
    EditText mEtYearIncome;
    @BindView(R.id.tvSellAddress)
    TextView mTvSellAddress;
    @BindView(R.id.etSellAddress)
    EditText mEtSellAddress;
    @BindView(R.id.tvAgencyCy)
    TextView mTvAgencyCy;
    @BindView(R.id.llAgCategory)
    LinearLayout mLlAgCategory;
    @BindView(R.id.etAgencyCg)
    EditText mEtAgencyCg;
    @BindView(R.id.tvLocation)
    TextView mTvLocation;
    @BindView(R.id.rlLocation)
    RelativeLayout mRlLocation;

    /* 省市区-分开来-后面可能需要进行提交
    *   用于地址选择
    * */
    private String mProvince;
    private String mCity;
    private String mDistrict;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private boolean isLoaded = false;
    private String mLoginName;

    @Override
    public void init() {
        /* 很好使用了rxjava来进行加载了*/
        Observable.create(this::initJsonData).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> isLoaded = aBoolean);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_main_center_admin;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void initView(View rootView) {
        mIvToolbarNavigation.setVisibility(View.GONE);
        mTvToolbarTitle.setText("注册");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText("确定注册");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mTvPublishNow.setOnClickListener(v -> checkDataAndSubmit());

        /* 经销类别的选择*/
        mLlAgCategory.setOnClickListener(v -> showAgencyCgDialog());

        /* 联系地址的三级菜单选择 */
        mRlLocation.setOnClickListener(v -> {
            // 隐藏软键盘
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
            if (isLoaded) {
                showCitySelected();
            } else {
                UIUtils.showToast("sorry,城市数据未加载!!!");
            }
        });
    }


    private void showRgScsDialog() {
        View rgScsView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_register_ok, null);
        Dialog rgDialog = new Dialog(getActivity(), R.style.MyDialog);
        Window window = rgDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.BottomDialog);
        window.getDecorView().setPadding(20, 0, 20, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        rgDialog.setContentView(rgScsView);
        TextView tvLoginName = rgScsView.findViewById(R.id.tvLoginName);
        tvLoginName.setText(String.format("登录名称:%s", mLoginName));
        rgDialog.show();
    }

    /* 弹出经销类别选择的活动*/
    private void showAgencyCgDialog() {
        View agencyCgView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_agency_cg, null);
        Dialog cgDialog = new Dialog(getActivity(), R.style.BottomDialog);
        cgDialog.setCancelable(true); /* 外部点击取消*/
        Window window = cgDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.AnimBottom);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        cgDialog.setContentView(agencyCgView);
        initCgDialog(cgDialog);
        cgDialog.show();
    }

    /* 用作初始化的*/
    private void initCgDialog(Dialog cgDialog) {
        RecyclerView mRv = (RecyclerView) cgDialog.findViewById(R.id .rv);
        TextView mTVSubmit = (TextView) cgDialog.findViewById(R.id.tvBtn);
        TextView mTvFive = (TextView) cgDialog.findViewById(R.id.tvFive);

        BaseQuickAdapter<String, BaseViewHolder> mBaseAdapter = null;
        List<Integer> mSelected = new ArrayList<>();
        List<String> mList = new ArrayList<>();

        mList.addAll(Arrays.asList(AppConst.AGENCY_CATEGORY));
        if (mBaseAdapter == null) {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
            mRv.setLayoutManager(layoutManager);
            mRv.addItemDecoration(new GridSpacingItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.common_dimension_10), true));
            mBaseAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_enjoy_text, mList) {
                @Override
                protected void convert(BaseViewHolder helper, String item) {
                    int position = helper.getAdapterPosition();
                    TextView tvEnjoy = helper.getView(R.id.tvEnjoy);
                    if (mSelected != null && mSelected.size() != 0 && mSelected.contains((Integer) position)) {
                        tvEnjoy.setTextColor(UIUtils.getColor(R.color.red0));
                        tvEnjoy.setBackground(UIUtils.getDrawable(R.drawable.bg_enjoy_pink));
                    }
                    tvEnjoy.setText(item);
                    tvEnjoy.setOnClickListener(v -> {
                        if (mSelected != null && mSelected.size() <= 5) {
                            if (mSelected.contains(position)) {
                                mSelected.remove((Integer) position);
                                tvEnjoy.setTextColor(UIUtils.getColor(R.color.black));
                                tvEnjoy.setBackground(UIUtils.getDrawable(R.drawable.bg_enjoy_normal));
                            } else {
                                if (mSelected.size() == 5) {
                                    UIUtils.showToast("不超过5个");
                                    return;
                                }
                                mSelected.add(position);
                                tvEnjoy.setTextColor(UIUtils.getColor(R.color.red0));
                                tvEnjoy.setBackground(UIUtils.getDrawable(R.drawable.bg_enjoy_pink));
                            }
                        }
                        if (mSelected.size() == 5) {
                            mTvFive.setVisibility(View.VISIBLE);
                        } else {
                            mTvFive.setVisibility(View.GONE);
                        }
                    });

                }
            };
        }
        mRv.setAdapter(mBaseAdapter);

        mTVSubmit.setOnClickListener(v -> {
            if (mSelected.size() == 0) {
                UIUtils.showToast("客官请至少选择一个兴趣");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mSelected.size(); i++) {
                Integer integer = mSelected.get(i);
                if (i == mSelected.size() - 1) {
                    sb.append(mList.get(integer));
                } else {
                    sb.append(mList.get(integer)).append(",");
                }
            }

            mEtAgencyCg.setText(sb.toString());
            cgDialog.dismiss();
        });

    }


    // 核实并提交数据!
    private void checkDataAndSubmit() {
        mLoginName = mEtLoginName.getText().toString();
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
        upRequest.setArea(mEtSellAddress.getText().toString());
        upRequest.setProvince(mProvince);
        upRequest.setCountry(mDistrict);
        upRequest.setCity(mCity);
        upRequest.setAddress(mEtStreetDetail.getText().toString());
        if (!TextUtils.isEmpty(mEtYearIncome.getText().toString())) {
            upRequest.setIncome(Integer.parseInt(mEtYearIncome.getText().toString()));
        }
        upRequest.setCategory(mEtAgencyCg.getText().toString());
        upRequest.setDepartment(mEtStoreName.getText().toString());
        ApiRetrofit.getInstance().registerNextUser(upRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RgCustomResponse>() {
                    @Override
                    public void accept(RgCustomResponse rgCustomResponse) throws Exception {
                        hideWaitingDialog();
                        if (rgCustomResponse.getCode() ==1){
                            BroadcastManager.getInstance(getActivity()).sendBroadcast(AppConst.UPDATE_CUL_CUSTOM);
                            cleanAllViews();
                            showRgScsDialog();
                        }else {
                            UIUtils.showToast(rgCustomResponse.getMsg());
                        }
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }
    /* 初始化各个view到最初 ===*/
    public void cleanAllViews() {
        mEtAgencyCg.setText("");
        mEtCustomName.setText("");
        mEtLoginName.setText("");
        mEtPhone.setText("");
        mEtSellAddress.setText("");
        mEtStoreName.setText("");
        mEtYearIncome.setText("");
        mEtStreetDetail.setText("");
        mTvLocation.setText("");
    }

    private void showCitySelected() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getActivity(), (options1, options2, options3, v) -> {
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
        String JsonData = new GetJsonDataUtil().getJson(getActivity(), "province.json");//获取assets目录下的json文件数据

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

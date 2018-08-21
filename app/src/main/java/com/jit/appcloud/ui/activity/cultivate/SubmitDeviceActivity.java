package com.jit.appcloud.ui.activity.cultivate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.request.InsertDeviceLogRequest;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;

import java.util.Calendar;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/06/05.
 *         discription: 设备信息提交的活动
 */
public class SubmitDeviceActivity extends BaseActivity {


    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.tvDvNameSelected)
    TextView mTvDvNameSelected;
    @BindView(R.id.llDeviceName)
    LinearLayout mLlDeviceName;
    @BindView(R.id.tvDevicceSelected)
    TextView mTvDevicceSelected;
    @BindView(R.id.llDevice)
    LinearLayout mLlDevice;
    @BindView(R.id.etLowIn)
    EditText mEtLowIn;
    @BindView(R.id.etLowOut)
    EditText mEtLowOut;
    @BindView(R.id.etHighIn)
    EditText mEtHighIn;
    @BindView(R.id.etHighOut)
    EditText mEtHighOut;
    @BindView(R.id.tvTimeSelected)
    TextView mTvTimeSelected;
    @BindView(R.id.llInputTime)
    LinearLayout mLlInputTime;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.scrollview)
    NestedScrollView mScrollview;
    @BindView(R.id.logTab)
    FloatingActionButton mLogTab;
    @BindView(R.id.tvSmallRange)
    TextView mTvSmallRange;
    @BindView(R.id.tvLargeRange)
    TextView mTvLargeRange;


    private TimePickerView pvTime;
    private String mFun_name;
    private int mPondId = -1;
    private int mDeviceId = -1;

    @Override
    protected void init() {
        initTimePicker();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_submit_device;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("设备");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(getString(R.string.str_title_save));
    }


    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mLogTab.setOnClickListener(v -> jumpToActivity(LogDeviceActivity.class));

        mLlDeviceName.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,SingleSelectionActivity.class);
            intent.putExtra(AppConst.FLAG_DEVICE,"device");// 传入活动名即可
            startActivityForResult(intent,AppConst.RECODE_TO_SINGLE_ITEM_LIST);
        });

        mLlDevice.setOnClickListener(v -> jumpToActivityForResult(DeviceSelectedActivity.class,AppConst.RECODE_TO_DEVICE_ITEM_LIST));

        mLlInputTime.setOnClickListener(v -> pvTime.show());

        mTvPublishNow.setOnClickListener(v -> verifyAndSubmitData());
    }

    private void verifyAndSubmitData() {
        if (TextUtils.isEmpty(mFun_name)){
            UIUtils.showToast("请选择设备名称");
            return;
        }

        if (mPondId == -1 || mDeviceId == -1){
            UIUtils.showToast("请选择设备号");
            return;
        }

        String strLowIn = mEtLowIn.getText().toString().trim();
        String strLowOut = mEtLowOut.getText().toString().trim();
        String strHighIn = mEtHighIn.getText().toString().trim();
        String strHighOut = mEtHighOut.getText().toString().trim();
        if (TextUtils.isEmpty(strLowIn) || TextUtils.isEmpty(strLowOut)
                || TextUtils.isEmpty(strHighIn) || TextUtils.isEmpty(strHighOut)){
            UIUtils.showToast("数值未完整录入");
            return;
        }
        String time = mTvTimeSelected.getText().toString().trim();
        if (TextUtils.isEmpty(time)){
            UIUtils.showToast("请录入时间");
            return;
        }
        showWaitingDialog(getString(R.string.str_please_waiting));
        InsertDeviceLogRequest request = new InsertDeviceLogRequest();
        request.setFun_name(mFun_name);
        request.setDevice_id(mDeviceId);
        request.setPound_id(mPondId);
        request.setLow_in(Double.parseDouble(strLowIn));
        request.setLow_out(Double.parseDouble(strLowOut));
        request.setHigh_in(Double.parseDouble(strHighIn));
        request.setHigh_out(Double.parseDouble(strHighOut));
        request.setTime(time);
        ApiRetrofit.getInstance().insertDeviceRecord(UserCache.getToken(),request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    hideWaitingDialog();
                    if (response.getCode() == 1){
                        finish();
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
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case AppConst.RECODE_TO_SINGLE_ITEM_LIST:
                    mTvDvNameSelected.setText(data.getStringExtra(AppConst.SINGLE_ITEM_SELECTED));
                    mFun_name = data.getStringExtra(AppConst.SINGLE_ITEM_SELECTED_ID);
                    setRangeUnit();
                    break;
                case AppConst.RECODE_TO_DEVICE_ITEM_LIST:
                    /* 获得塘口设备的信息*/
                    mPondId = data.getIntExtra(AppConst.DEVICE_POND_ID, -1);
                    mDeviceId = data.getIntExtra(AppConst.DEVICE_ID, -1);
                    String pondName = data.getStringExtra(AppConst.DEVICE_POND_NAME);
                    String deviceNo = data.getStringExtra(AppConst.DEVICE_NO);
                    mTvDevicceSelected.setText(String.format("%s--%s",pondName,deviceNo));
                    break;
            }
        }
    }

    private void setRangeUnit() {
        /*"o2","ph","temperature"*/
        if (AppConst.LIST_DEVICE_EN[0].equals(mFun_name)){
            mTvSmallRange.setText(R.string.str_device_small_range_unit);
            mTvLargeRange.setText(R.string.str_device_large_range_unit);
        }else if (AppConst.LIST_DEVICE_EN[1].equals(mFun_name)){
            mTvSmallRange.setText(R.string.str_device_small_range);
            mTvLargeRange.setText(R.string.str_device_large_range);
        }else if(AppConst.LIST_DEVICE_EN[2].equals(mFun_name)){
            mTvSmallRange.setText(R.string.str_device_small_range_unit_o2);
            mTvLargeRange.setText(R.string.str_device_large_range_unit_o2);
        }
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

package com.jit.appcloud.ui.activity.cultivate;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.allen.library.SuperTextView;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserOtherCache;
import com.jit.appcloud.model.request.DeviceInsertRequest;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
/**
 * @author zxl on 2018/06/05.
 *         discription:
 */
public class DeviceAdByMGActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.etDeviceNumber)
    EditText mEtDeviceNumber;
    @BindView(R.id.etMacIP)
    EditText mEtMacIP;
    @BindView(R.id.tvPondID)
    TextView mTvPondID;
    @BindView(R.id.rlChoosePond)
    RelativeLayout mRlChoosePond;
    @BindView(R.id.stvStatus)
    SuperTextView mStvStatus;
    @BindView(R.id.tvPondRSMan)
    TextView mTvPondMan;
    private int status = 1;
    private int mPondId = -1;
    @Override
    protected void init() {
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_device_ad_by_mg;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("增加设备");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(getString(R.string.str_submit));
        mTvPondMan.setText(UserOtherCache.getFarmSelectedName());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mTvPublishNow.setOnClickListener(v -> verifyAndSubmitData());
        mStvStatus.setSwitchIsChecked(true);
        mStvStatus.setSwitchCheckedChangeListener((compoundButton, b) -> {
             if (b){
                 status = 1;
             }else {
                 status = 0;
             }
        });

        mRlChoosePond.setOnClickListener(v -> {
            if (DBManager.getInstance().queryAllPond().size() == 0){
                UIUtils.showToast(String.format("请先为养殖户:%s 添加塘口", UserOtherCache.getFarmSelectedName()));
                return;
            }
            Intent intent = new Intent(mContext,SingleSelectionActivity.class);
            intent.putExtra(AppConst.FLAG_POND,"pond");
            startActivityForResult(intent,AppConst.RECODE_TO_POND_LIST);
        });

        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case AppConst.RECODE_TO_POND_LIST:
                    String pondSelected = data.getStringExtra(AppConst.SINGLE_ITEM_SELECTED);
                    mPondId = data.getIntExtra(AppConst.POND_ID_SELECTED,-1);
                    mTvPondID.setText(pondSelected);
                    UIUtils.showToast(String.valueOf(mPondId));
                    break;
                default:
                    break;
            }
        }
    }

    private void verifyAndSubmitData() {
        if (mPondId == -1){
            UIUtils.showToast("请选择塘口");
            return;
        }

        String deviceNumber = mEtDeviceNumber.getText().toString().trim();
        if (TextUtils.isEmpty(deviceNumber)){
            UIUtils.showToast("请录入设备号");
            return;
        }

        String deviceIP = mEtMacIP.getText().toString().trim();
        if (TextUtils.isEmpty(deviceIP)){
            UIUtils.showToast("请录入Mac地址");
            return;
        }
        showWaitingDialog(getString(R.string.str_please_waiting));
        DeviceInsertRequest request = new DeviceInsertRequest();
        request.setDevice_no(Integer.parseInt(deviceNumber));
        request.setMac_ip(deviceIP);
        request.setStatus(status);
        request.setPound_id(mPondId);
        request.setUsername(mTvPondMan.getText().toString().trim());
        ApiRetrofit.getInstance().deviceInsert(UserCache.getToken(),request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getCode() == 1){
                        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.DEVICE_UPDATE);
                        finish();
                        // 这里需要进行更新
                    }else {
                        UIUtils.showToast(response.getMsg());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }


}

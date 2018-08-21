package com.jit.appcloud.ui.activity.me;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bigkoo.pickerview.TimePickerView;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.request.EpPondRequest;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import java.util.Calendar;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
/**
 * @author zxl on 2018/07/12.
 *         discription: 养殖户添加塘口 ======
 */
public class EpAddPondActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.etPondId)
    EditText mEtPondId;
    @BindView(R.id.etPondCg)
    EditText mEtPondCg;
    @BindView(R.id.etVy)
    EditText mEtVy;
    @BindView(R.id.etBreedBrand)
    EditText mEtBreedBrand;
    @BindView(R.id.etBreedNum)
    EditText mEtBreedNum;
    @BindView(R.id.etArea)
    EditText mEtArea;
    @BindView(R.id.tvBreedTime)
    TextView mTvBreedTime;
    @BindView(R.id.rlBreedTime)
    RelativeLayout mRlBreedTime;
    @BindView(R.id.etLiaoName)
    EditText mEtLiaoName;
    @BindView(R.id.etLiaoType)
    EditText mEtLiaoType;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    private TimePickerView pvTime;
    @Override
    protected void init() {
        initTimePicker();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_ag_pond_add;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("新建鱼塘信息");
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(getString(R.string.str_title_save));
        mTvBreedTime.setText(TimeUtil.getCurrentTime());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        /* 选择时间*/
        mRlBreedTime.setOnClickListener(v -> pvTime.show());
        mTvPublishNow.setOnClickListener(v -> {
            String pondId = mEtPondId.getText().toString();
            if (TextUtils.isEmpty(pondId)){
                UIUtils.showToast(getString(R.string.pound_not_null));
                return;
            }

            showWaitingDialog(getString(R.string.str_please_waiting));
            EpPondRequest epPondRequest = new EpPondRequest();
            epPondRequest.setNumber(pondId);
            epPondRequest.setType(mEtPondCg.getText().toString());
            if (!TextUtils.isEmpty(mEtArea.getText().toString())) {
                epPondRequest.setSquare(Integer.parseInt(mEtArea.getText().toString()));
            }
            epPondRequest.setSeed_brand(mEtBreedBrand.getText().toString());
            epPondRequest.setSeed_type(mEtVy.getText().toString());
            if (!TextUtils.isEmpty(mEtBreedNum.getText().toString())) {
                epPondRequest.setSeed_number(Integer.parseInt(mEtBreedNum.getText().toString()));
            }
            epPondRequest.setFeed_brand(mEtLiaoName.getText().toString());
            epPondRequest.setFeed_type(mEtLiaoType.getText().toString());
            epPondRequest.setUsername(UserCache.getName());

            ApiRetrofit.getInstance().epAddPound(epPondRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        hideWaitingDialog();
                        if (response.getCode() == 1){
                            UIUtils.showToast(getString(R.string.success_add_pound));
                            BroadcastManager.getInstance(EpAddPondActivity.this).sendBroadcast(AppConst.UPDATE_EP_POUND);
                            BroadcastManager.getInstance(EpAddPondActivity.this).sendBroadcast(AppConst.UPDATE_EP_CUL_POUND);
                            finish();
                        }else {
                            UIUtils.showToast(response.getMsg());
                        }
                    }, throwable -> {
                        hideWaitingDialog();
                        UIUtils.showToast(throwable.getLocalizedMessage());
                    });

        });
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
            mTvBreedTime.setText(TimeUtil.date2String(date,"yyyy-MM-dd HH:mm:ss"));
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

package com.jit.appcloud.ui.activity.cultivate;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jit.appcloud.R;
import com.jit.appcloud.model.bean.CultivateLogBean;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.recyclerview.LQRRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author zxl on 2018/05/23.
 *         discription: 经销商  养殖日志的查看和塘口的删除等操作
 */
public class AgencyCulLogActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.lqr_cul_manage)
    LQRRecyclerView mLqrCulManage;

    private List<CultivateLogBean> mList = new ArrayList<>();
    private LQRAdapterForRecyclerView<CultivateLogBean> mAdapter;
    @Override
    protected void init() {
        mList.add(new CultivateLogBean(R.mipmap.ic_fish_pond_log,"塘口管理","合理管理塘口"));
        mList.add(new CultivateLogBean(R.mipmap.ic_manage_device,"设备管理","合理分配设备"));
        mList.add(new CultivateLogBean(R.mipmap.ic_pond_log,"投放日志","审查工作记录"));
        mList.add(new CultivateLogBean(R.mipmap.ic_pond_put,"种苗日志","审查种苗投放"));
        mList.add(new CultivateLogBean(R.mipmap.ic_pond_medicine,"用药日志","审查用药情况"));
        mList.add(new CultivateLogBean(R.mipmap.ic_pond_water_eq,"水检日志","审查水质环境"));
        mList.add(new CultivateLogBean(R.mipmap.ic_log_device,"设备日志","审查生产环境"));

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_agency_cul_log;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("管理与日志");
        setAdapter();
    }

    @Override
    protected void initData() {

    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new LQRAdapterForRecyclerView<CultivateLogBean>(mContext, mList, R.layout.item_pond_manage) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, CultivateLogBean item, int position) {
                    ImageView ivManage = helper.getView(R.id.ivManage);
                    TextView tvManageTitle = helper.getView(R.id.tvManageTitle);
                    TextView tvManageDes = helper.getView(R.id.tvManageDes);
                    ivManage.setImageResource(item.getImageId());
                    tvManageTitle.setText(item.getTitle());
                    tvManageDes.setText(item.getDes());
                }
            };
        }
        // 首先进行塘口管理吧
        mAdapter.setOnItemClickListener((helper, parent, itemView, position) -> {
            /* 这里先待定 后面根据需要再进行更新*/
            if (position == 0){
                jumpToActivity(PondManageActivity.class);
            }else if (position == 1){
                jumpToActivity(DeviceManageActivity.class);
            }
            else if (position == 2)
            {
                jumpToActivity(LogFeedActivity.class);
            }else if (position == 3){
                jumpToActivity(LogSeedActivity.class);
            }else if( position == 4){
                jumpToActivity(LogDrugActivity.class);
            }else if (position == 5){
                jumpToActivity(LogWaterDtActivity.class);
            }else if (position == 6){
                jumpToActivity(LogDeviceActivity.class);
            }
        });
        mLqrCulManage.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
    }
}

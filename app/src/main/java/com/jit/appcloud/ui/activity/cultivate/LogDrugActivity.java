package com.jit.appcloud.ui.activity.cultivate;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.LogDrugAtPresenter;
import com.jit.appcloud.ui.view.ILogAtView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author zxl on 2018/06/04.
 *         discription: 药品投放记录
 */
public class LogDrugActivity extends BaseActivity<ILogAtView,LogDrugAtPresenter> implements ILogAtView {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.rcvLog)
    RecyclerView mRcvLog;
    private View mPondView;
    private Dialog mPondDialog;
    private List<String> mPondList = new ArrayList<>();
    @Override
    public RecyclerView getLogRecv() {
        return mRcvLog;
    }

    @Override
    protected void init() {
        mPondList.clear();
        mPondList.addAll(DBManager.getInstance().queryPondName());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_log;
    }

    @Override
    protected LogDrugAtPresenter createPresenter() {
        return new LogDrugAtPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvPublishNow.setVisibility(View.VISIBLE);
        mTvPublishNow.setText(R.string.str_choose_pond);
        mPresenter.initAdapter();
        if (mPondList != null && mPondList.size()!=0) {
            mPresenter.refreshAndSetData(DBManager.getInstance().queryPondIdByName(mPondList.get(0)));
        }
        setToolbarTitle(0);
    }


    private void setToolbarTitle(int position){
        if (mPondList != null && mPondList.size()!=0){
            mTvToolbarTitle.setText(String.format(getString(R.string.str_title_drug),mPondList.get(position)));
        }else {
            mTvToolbarTitle.setText(getString(R.string.str_title_drug_empty));

        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mTvPublishNow.setOnClickListener(v -> showPondDialog());
    }

    private void showPondDialog() {
        if (mPondView == null){
            mPondView = LayoutInflater.from(mContext).inflate(R.layout.dialog_bottom_pond, null);
            mPondDialog = new Dialog(mContext, R.style.MyDialog);
            Window window = mPondDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.BottomDialog);
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            mPondDialog.setContentView(mPondView);
            TagFlowLayout mPondLayout = (TagFlowLayout) mPondView.findViewById(R.id.pondTag);
            TagAdapter<String> mHobbyTagAdapter = new TagAdapter<String>(mPondList) {
                @Override
                public View getView(FlowLayout parent, int position, String bean) {
                    TextView textView = null;
                    textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_pond_bottom_selected, mPondLayout, false);
                    textView.setText(bean);
                    return textView;
                }
            };
            mPondLayout.setOnTagClickListener((view, position, parent) -> {
                mPresenter.refreshAndSetData(DBManager.getInstance().queryPondIdByName(mPondList.get(position)));
                mPondDialog.dismiss();
                setToolbarTitle(position);

                return false;
            });
            mPondLayout.setAdapter(mHobbyTagAdapter);

        }
        mPondDialog.show();
    }

}

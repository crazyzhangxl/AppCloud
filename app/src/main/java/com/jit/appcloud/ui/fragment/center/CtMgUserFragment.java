package com.jit.appcloud.ui.fragment.center;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.bean.PersonalBean;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.UserBdAmResponse;
import com.jit.appcloud.ui.activity.MainActivity;
import com.jit.appcloud.ui.activity.cultivate.CusDtRoleMgActivity;
import com.jit.appcloud.ui.activity.cultivate.MgRegisterFmActivity;
import com.jit.appcloud.ui.base.BaseFragment;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.MyDialog;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
/**
 * @author zxl on 2018/6/20.
 *         discription: 该界面用于呈现 经销商身份登录的中间界面
 */
public class CtMgUserFragment extends BaseFragment {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_publish_now)
    TextView mTvPublishNow;
    @BindView(R.id.rvCustomList)
    RecyclerView mRvCustomList;
    private BaseQuickAdapter<PersonalBean,BaseViewHolder> mCustomAdapter;
    private List<PersonalBean > mCustomBeans = new ArrayList<>();
    @Override
    public void init() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_main_center_mg;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void initView(View rootView) {
        mIvToolbarNavigation.setVisibility(View.GONE);
        mTvToolbarTitle.setText("客户列表");
        initAdapter();
        if (UserCache.getRole().equals(AppConst.ROLE_AGENCY)){
            mTvPublishNow.setVisibility(View.VISIBLE);
            mTvPublishNow.setText("注册");
        }
    }

    private void initAdapter() {
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity());
        lManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvCustomList.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, UIUtils.dip2px(getActivity(), 0.5f), UIUtils.getColor(R.color.bg_line_2)));
        mRvCustomList.setLayoutManager(lManager);

        mCustomAdapter = new BaseQuickAdapter<PersonalBean , BaseViewHolder>(R.layout.item_ag_center_cus,mCustomBeans) {
            @Override
            protected void convert(BaseViewHolder helper, PersonalBean  item) {
                ImageView ivHead =  helper.getView(R.id.ivHead);
                Glide.with(getActivity()).load(item.getImage()).apply(new RequestOptions().placeholder(R.mipmap.default_header)).into(ivHead);
                helper.setText(R.id.tvListName,item.getRealname());
                if (item.getDeviceCount() != 0) {
                    helper.setText(R.id.tvEquNum, String.valueOf(item.getDeviceCount()));
                }
                helper.getView(R.id.rlCus).setOnClickListener(v -> {
                    Intent intent = new Intent(mContext,CusDtRoleMgActivity.class);
                    intent.putExtra(AppConst.TURN_TO_SHOW_AGENCY,"agency");
                    intent.putExtra(AppConst.INFO_SHOW_DETAIL,item.getUsername());
                    intent.putExtra(AppConst.INFO_CUS_ID,item.getId());
                    jumpToActivity(intent);
                });

                helper.getView(R.id.tvEquNum).setOnClickListener(v -> {
                    ((MainActivity)getActivity()).turnToCul();
                    BroadcastManager.getInstance(mContext)
                            .sendBroadcast(AppConst.UPDATE_DEVICE_SELECTED,item.getRealname());
                });

                helper.getView(R.id.llDel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserCache.getRole().equals(AppConst.ROLE_VICE_AGENT)
                                || UserCache.getRole().equals(AppConst.ROLE_VICE_ADMIN)
                                || UserCache.getRole().equals(AppConst.ROLE_VICE_MANAGER)){
                            UIUtils.showToast(getString(R.string.please_permission_denied));
                            return;
                        }
                        showDeleteDialog(item.getId(), helper.getAdapterPosition());
                    }
                });
            }
        };
        mRvCustomList.setAdapter(mCustomAdapter);
    }

    private void showDeleteDialog(int id,int position){
        /*  弹出dialog,键入密码进行验证*/
        View rgScsView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete_agency_psd, null);
        MyDialog delDialog = new MyDialog(getActivity(), R.style.MyDialog);
        Window window = delDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.BottomDialog);
        window.getDecorView().setPadding(50, 0, 50, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        delDialog.setContentView(rgScsView);
        EditText etPsd = delDialog.findViewById(R.id.etPsd);
        TextView tvCancel = delDialog.findViewById(R.id.tvCancel);
        TextView tvSub =  delDialog.findViewById(R.id.tvSubmit);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delDialog.dismiss();
            }
        });
        tvSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserCache.getPassword().equals(etPsd.getText().toString())) {
                    ApiRetrofit.getInstance().deleteUser(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                if (response.getCode() == 1) {
                                    UIUtils.showToast(CtMgUserFragment.this.getString(R.string.user_delete_success));
                                    mCustomBeans.remove(position);
                                    mCustomAdapter.notifyDataSetChanged();
                                    BroadcastManager.getInstance(CtMgUserFragment.this.getActivity()).sendBroadcast(AppConst.UPDATE_MG_EP_LIST);
                                } else {
                                    UIUtils.showToast(response.getMsg());
                                }
                            }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
                    delDialog.dismiss();

                } else {
                    UIUtils.showToast(CtMgUserFragment.this.getString(R.string.password_error));
                }
            }
        });
        delDialog.show();
    }

    private void refreshData(){
        showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().getAgencyNextUserInfo(UserCache.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userBdAmResponse -> {
                    hideWaitingDialog();
                    if (userBdAmResponse.getCode() == 1){
                        mCustomBeans.clear();
                        mCustomBeans.addAll(userBdAmResponse.getData());
                        mCustomAdapter.notifyDataSetChanged();
                    }else {
                        UIUtils.showToast(userBdAmResponse.getMsg());
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void initListener() {
        mTvPublishNow.setOnClickListener(v -> jumpToActivity(MgRegisterFmActivity.class));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.getInstance(getActivity()).register(AppConst.UPDATE_FM_CUSTOM, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshData();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.UPDATE_FM_CUSTOM);
    }
}

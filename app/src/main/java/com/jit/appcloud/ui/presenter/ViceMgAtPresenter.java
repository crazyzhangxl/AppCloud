package com.jit.appcloud.ui.presenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.bean.PersonalBean;
import com.jit.appcloud.model.response.UserBBAMResponse;
import com.jit.appcloud.ui.activity.me.ViceUserAddActivity;
import com.jit.appcloud.ui.activity.me.ViceUserEditorActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IViceMGAtView;
import com.jit.appcloud.util.UIUtils;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/7/16.
 *         discription: 平行用户管理的P层
 */

public class ViceMgAtPresenter extends BasePresenter<IViceMGAtView> {


    private List<PersonalBean> mViceUserList = new ArrayList<>();
    private BaseQuickAdapter<PersonalBean,BaseViewHolder> mViceAdapter;
    private int nowPosition = -1;
    private int prePosition = -1;


    public ViceMgAtPresenter(BaseActivity context) {
        super(context);
    }

    /**
     * 查
     */
    public void initViceInfo(){
        initViceAdapter();
        refreshViceData();
    }



    /**
     * 初始化列表
     */
    private void initViceAdapter() {
        getView().getRvViceMG().setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        getView().getRvViceMG().addItemDecoration(new RecycleViewDivider(mContext, LinearLayout.HORIZONTAL,UIUtils.dip2px(mContext,0.5f),UIUtils.getColor(R.color.bg_line_2)));
        mViceAdapter = new BaseQuickAdapter<PersonalBean, BaseViewHolder>(R.layout.item_vice_manage,mViceUserList) {
            @Override
            protected void convert(BaseViewHolder helper, PersonalBean item) {
                helper.itemView.setBackground(UIUtils.getDrawable(R.drawable.selector_item_select_state));
                helper.itemView.setSelected(helper.getAdapterPosition() == nowPosition);
                helper.setText(R.id.tvUserName,item.getUsername());
                helper.setText(R.id.tvRealName,item.getRealname());
                Glide.with(mContext).load(item.getImage()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.default_header)).into((ImageView) helper.getView(R.id.ivHead));
            }
        };
        mViceAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position != nowPosition){
                prePosition = nowPosition;
                nowPosition = position;
                mViceAdapter.notifyItemChanged(prePosition);
                mViceAdapter.notifyItemChanged(nowPosition);
            }else {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConst.EXTRA_VICE_INFO, mViceUserList.get(nowPosition));
                mContext.jumpToActivity(ViceUserEditorActivity.class, bundle);
            }
        });

        getView().getRvViceMG().setAdapter(mViceAdapter);
    }

    /**
     * 刷新数据
     */
    public void refreshViceData() {
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().queryAllViceInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userBBAMResponse -> {
                    mContext.hideWaitingDialog();
                    if (userBBAMResponse.getCode() == 1){
                        mViceUserList.clear();
                        mViceUserList.addAll(userBBAMResponse.getData());
                        mViceAdapter.notifyDataSetChanged();
                    }else {
                        UIUtils.showToast(userBBAMResponse.getMsg());
                    }
                }, throwable -> {
                    mContext.hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    /**
     * 新建
     */
    public void addViceUser(){
        mContext.jumpToActivity(ViceUserAddActivity.class);
    }

    /**
     * 编辑
     */
    public void editorViceUser(){
        if (nowPosition == -1){
            UIUtils.showToast("请选择一个平行用户");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable (AppConst.EXTRA_VICE_INFO,mViceUserList.get(nowPosition));
        mContext.jumpToActivity(ViceUserEditorActivity.class,bundle);
    }

    /**
     * 删除
     */
    public void delViceUser(){
        if (nowPosition == -1){
            UIUtils.showToast("请选择一个平行用户");
            return;
        }
        actionDelVice();
    }

    private void actionDelVice(){
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().deleteViceById(mViceUserList.get(nowPosition).getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    mContext.hideWaitingDialog();
                    if (response.getCode() == 1){
                        mViceUserList.remove(nowPosition);
                        mViceAdapter.notifyItemRemoved(nowPosition);
                        nowPosition = -1;
                    }else {
                        UIUtils.showToast(response.getMsg());
                    }
                }, throwable -> {
                    mContext.hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }
}

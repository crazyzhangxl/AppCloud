package com.jit.appcloud.ui.presenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.db_model.MyPbNewsBean;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.NewsAllInfoResponse;
import com.jit.appcloud.ui.activity.news.PublishDetailShowActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.INewsFgView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.StringUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * @author 张先磊
 * @date 2018/4/17
 */

public class NewsFgPresenter extends BasePresenter<INewsFgView> {
    private BaseQuickAdapter<MyPbNewsBean,BaseViewHolder> mAdapter;
    // 这里应当设置多个数据
    private List<MyPbNewsBean> mListCurrent = new ArrayList<>();
    private int nowPosition ;
    private int prePosition ;
    private BaseActivity mmContext;

    public NewsFgPresenter(BaseActivity context) {
        super(context);
        mmContext = context;
    }

    /**
     * 首次进入加载数据
     * @param flag 标志
     * @param type 筛选类型
     */
    public void loadAllData(String flag,String type){
        initPosition();
        loadData(flag,type);
    }

    public void setRevAdapter(){
        setAdapter();
    }

    private void loadData(String flag,String type) {
        ApiRetrofit.getInstance().getNewsByType(flag,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsAllInfoResponse -> {
                    if (newsAllInfoResponse.getCode() == 1){
                        mListCurrent.clear();
                        mListCurrent.addAll(newsAllInfoResponse.getData());
                        mAdapter.setNewData(mListCurrent);
                    }else {
                        UIUtils.showToast(newsAllInfoResponse.getMsg());
                    }
                }, throwable -> {
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }


    private void initPosition(){
        nowPosition = -1;
        prePosition = -1;
    }

    private void setAdapter() {
        getView().getRCVNewsBody().setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mAdapter = new BaseQuickAdapter<MyPbNewsBean, BaseViewHolder>(R.layout.item_list_news,mListCurrent) {
            @Override
            protected void convert(BaseViewHolder helper, MyPbNewsBean item) {
                helper.itemView.setBackground(UIUtils.getDrawable(R.drawable.selector_item_select_state));
                helper.itemView.setSelected(helper.getAdapterPosition() == nowPosition);
                ImageView ivFlag = helper.getView(R.id.ivNewsFlag);
                if (AppConst.MSG_TYPE.PIC.equals(item.getMsg_type())) {
                    ivFlag.setImageResource(R.mipmap.ic_news_flag_pic);
                }else {
                    ivFlag.setImageResource(R.mipmap.ic_news_flag_net);
                }
                    /*如果可以的话 还是比较有意义的------------*/
                ImageView ivNewsHead = helper.getView(R.id.ivNewsHead);
                TextView mTvNewsTitle =  helper.getView(R.id.tvNewTitle);
                TextView mTvTypeHot = helper.getView(R.id.tvTypeHot);
                TextView mTvNewDes = helper.getView(R.id.tvNewsDes);
                TextView mTvPrice = helper.getView(R.id.tvNewsAccount);
                TextView mTvTime = helper.getView(R.id.tvNewsTime);
                Glide.with(mContext).load(item.getImage()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(ivNewsHead);
                mTvNewsTitle.setText(item.getTitle());
                mTvNewDes.setText(item.getDescription());
                mTvPrice.setText("¥"+item.getPrice());
                mTvTime.setText(TimeUtil.getMyTime(item.getTime()));
                StringBuilder sb = new StringBuilder();
                if (!TextUtils.isEmpty(item.getType())){
                    sb.append(item.getType()).append("·");
                    if (item.getDiscount() == 1){
                        sb.append(UIUtils.getString(R.string.discount));
                        if (item.getHot() == 1){
                            sb.append("/").append(UIUtils.getString(R.string.hot));
                        }
                    }else {
                        if (item.getHot() == 1){
                            sb.append(UIUtils.getString(R.string.hot));
                        }
                    }
                }else {
                    sb.append("·");
                    if (item.getDiscount() == 1){
                        sb.append(UIUtils.getString(R.string.discount));
                        if (item.getHot() == 1){
                            sb.append("/").append(UIUtils.getString(R.string.hot));
                        }
                    }else {
                        if (item.getHot() == 1){
                            sb.append(UIUtils.getString(R.string.hot));
                        }
                    }
                }
                if (!TextUtils.isEmpty(sb)){
                    mTvTypeHot.setText(sb.toString());
                }
                helper.getView(R.id.llNews).setOnClickListener(new MyItemClicker(helper.getAdapterPosition(),helper.getView(R.id.ivNewsHead)));
            }
        };
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        getView().getRCVNewsBody().setAdapter(mAdapter);
    }

    private void jumpToWebView(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.jumpToActivity(intent);
    }

    private class  MyItemClicker implements View.OnClickListener {
        private View view;
        private int position;
        MyItemClicker(int position, View view){
            this.position = position;
            this.view = view;
        }

        @Override
        public void onClick(View v) {
            if (position != nowPosition){
                prePosition = nowPosition;
                nowPosition = position;
                mAdapter.notifyItemChanged(prePosition);
                mAdapter.notifyItemChanged(nowPosition);
            }
            if (AppConst.MSG_TYPE.PIC.equals(mListCurrent.get(position).getMsg_type())) {
                PublishDetailShowActivity.startAction(mmContext,mListCurrent.get(position), view);
            }else if (AppConst.MSG_TYPE.NET.equals(mListCurrent.get(position).getMsg_type())){
                jumpToWebView(mListCurrent.get(position).getDescription());
            }

        }
    }


}

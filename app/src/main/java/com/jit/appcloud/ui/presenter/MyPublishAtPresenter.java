package com.jit.appcloud.ui.presenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.MyPbNewsBean;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserOtherCache;
import com.jit.appcloud.model.response.NormalResponse;
import com.jit.appcloud.model.response.MyPublishResponse;
import com.jit.appcloud.ui.activity.news.PublishDetailShowActivity;
import com.jit.appcloud.ui.activity.news.PublishEditorActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IMyPublishAtView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.StringUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
/**
 * @author zxl on 2018/5/25.
 *         discription: 我的发布的P层
 */

public class MyPublishAtPresenter extends BasePresenter<IMyPublishAtView> {
    private BaseActivity mmContext;
    private List<MyPbNewsBean> mMyPbBeanList = new ArrayList<>();
    private BaseQuickAdapter<MyPbNewsBean,BaseViewHolder> mAdapter;
    private int nowPosition = -1;
    private ArrayList<String> mSortList = new ArrayList<>();
    public MyPublishAtPresenter(BaseActivity context) {
        super(context);
        mmContext = context;
    }

    public void setRcvAdapter(){
        setAdapter();
    }

    public void setMyPublish(){

        if (UserOtherCache.getNewsIsCached()){
            getDataFromDb();
        }else {
            getData();
        }
    }

    private void getDataFromDb() {
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        Observable.create((ObservableOnSubscribe<List<MyPbNewsBean>>) emitter -> {
            mSortList.clear();
            if (!TextUtils.isEmpty(UserOtherCache.getNewsPbSort())) {
                mSortList.addAll(UIUtils.strToList(UserOtherCache.getNewsPbSort()));
                emitter.onNext(DBManager.getInstance().queryAllNewsPbSorted());
            }else {
                List<String> list = DBManager.getInstance().queryOriginStrList();
                if (list != null && list.size()!=0 )
                    mSortList.addAll(list);
                emitter.onNext(DBManager.getInstance().queryAllNewsNormal());
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listBeans -> {
                    mContext.hideWaitingDialog();
                    mMyPbBeanList.clear();
                    mMyPbBeanList.addAll(listBeans);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    mContext.hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    public void addMyPublish(String newsId){
        if (!TextUtils.isEmpty(UserOtherCache.getNewsPbSort())) {
            List<String> stringList = UIUtils.strToList(UserOtherCache.getNewsPbSort());
            stringList.add(newsId);
            UserOtherCache.setNewsPbSort(UIUtils.listToStr(stringList));
        }
        setMyPublish();
    }

    public void updatePublish(){
        setMyPublish();
    }

    private void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        getView().getRVPublish().setLayoutManager(linearLayoutManager);
        if (mAdapter == null){
            mAdapter = new BaseQuickAdapter<MyPbNewsBean, BaseViewHolder>(R.layout.item_publish_my,mMyPbBeanList) {
                @SuppressLint("SetTextI18n")
                @Override
                protected void convert(BaseViewHolder helper, MyPbNewsBean item) {
                    /*如果可以的话 还是比较有意义的------------*/
                    LogUtils.e("图片",item.getImage());
                    ImageView ivFlag = helper.getView(R.id.ivNewsFlag);
                    if (AppConst.MSG_TYPE.PIC.equals(item.getMsg_type())) {
                        ivFlag.setImageResource(R.mipmap.ic_news_flag_pic);
                    }else {
                        ivFlag.setImageResource(R.mipmap.ic_news_flag_net);
                    }
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
                    LogUtils.e("查看====","优惠: "+item.getDiscount()+"  热销:  "+item.getHot());
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

                    if (nowPosition == helper.getAdapterPosition()) {
                        helper.getView(R.id.llPBDetail).setBackgroundColor(UIUtils.getColor(R.color.item_select_color));
                    }else {
                        helper.getView(R.id.llPBDetail).setBackgroundColor(UIUtils.getColor(R.color.white));
                    }

                    helper.getView(R.id.llPBDetail).setOnClickListener(v -> {
                        if (helper.getAdapterPosition() == nowPosition){
                            if (AppConst.MSG_TYPE.PIC.equals(item.getMsg_type())) {
                                PublishDetailShowActivity.startAction(mmContext, item, ivNewsHead);
                            }else if (AppConst.MSG_TYPE.NET.equals(item.getMsg_type())){
                                jumpToWebView(item.getDescription());
                            }
                            return;
                        }
                        nowPosition = helper.getAdapterPosition();
                        mAdapter.notifyDataSetChanged();
                    });
                    helper.getView(R.id.llDelete).setOnClickListener(v -> showDeletingDialog(item.getNewsId(),helper.getAdapterPosition()));
                    helper.getView(R.id.llEdit).setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.NEWS_FROM_MY_TO_EDITOR,item);
                        mmContext.jumpToActivity(PublishEditorActivity.class,bundle);
                    });
                }
            };

            mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
            View view = LayoutInflater.from(mContext).inflate(R.layout.gallery_empty_view, null);
            TextView tvEmpty = view.findViewById(R.id.tv_empty);
            tvEmpty.setText("客官:暂未发布资讯哦，点击页面下方按钮进行发布吧!");
            mAdapter.setEmptyView(view);
        }

        getView().getRVPublish().setAdapter(mAdapter);

    }

    private void jumpToWebView(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.jumpToActivity(intent);
    }

    private void showDeletingDialog(int id,int positionDeleted) {
        mContext.showMaterialDialog(null, "确定删除该资讯?", UIUtils.getString(R.string.ensure), UIUtils.getString(R.string.cancel)
                , (dialog, which) -> {
                    dialog.dismiss();
                    mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
                    ApiRetrofit.getInstance().getPbDeleteCode(UserCache.getToken(),String.valueOf(id))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(normalResponse -> {
                                mContext.hideWaitingDialog();
                                if (normalResponse.getCode() == 1){
                                    if (positionDeleted == nowPosition)
                                        nowPosition = -1;
                                    deleteByIdAction(id);
                                    mMyPbBeanList.remove(positionDeleted);
                                    mAdapter.notifyDataSetChanged();
                                }else {
                                    UIUtils.showToast(normalResponse.getMsg());
                                }
                            }, throwable -> {
                                mContext.hideWaitingDialog();
                                UIUtils.showToast(throwable.getLocalizedMessage());
                            });
                }, (dialog, which) -> dialog.dismiss());
    }

    /* 删除资讯需要执行的数据持久操作 */
    private void deleteByIdAction(int id) {
        DBManager.getInstance().deleteNewsPbByNewsId(id);
        mSortList.remove(String.valueOf(id));
        if (!TextUtils.isEmpty(UserOtherCache.getNewsPbSort())) {
            List<String> stringList = UIUtils.strToList(UserOtherCache.getNewsPbSort());
            stringList.remove(String.valueOf(id));
            UserOtherCache.setNewsPbSort(UIUtils.listToStr(stringList));
        }
        UIUtils.showToast("删除成功");
    }

    private void getData() {
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().getMyPublish(UserCache.getToken(),UserCache.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(myPublishResponse -> {
                    mContext.hideWaitingDialog();
                    /* 存储数据------- 执行数据库存储*/
                    mSortList.clear();
                    for (MyPbNewsBean newsBean:myPublishResponse.getData()){
                        mSortList.add(String.valueOf(newsBean.getId()));
                        newsBean.setNewsId(newsBean.getId());
                        DBManager.getInstance().saveNewsMyPb(newsBean);
                    }
                    /* --------表示已经请求过了的 -------------*/
                    UserOtherCache.setNewsIsCached(true);
                    if (myPublishResponse.getCode() == 1){
                        mMyPbBeanList.clear();
                        mMyPbBeanList.addAll(myPublishResponse.getData());
                        mAdapter.setNewData(mMyPbBeanList);

                    }else {
                        UIUtils.showToast(myPublishResponse.getMsg());
                    }
                }, throwable -> {
                    mContext.hideWaitingDialog();
                    TextView tvError = mAdapter.getEmptyView().findViewById(R.id.tv_empty);
                    tvError.setText("抱歉,未链接到服务器,请检查网络退出重试!");
                    LogUtils.e("我的发布",throwable.getLocalizedMessage());
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }

    public void actionMoveToTop(){
        if (mMyPbBeanList.size() == 0){
            UIUtils.showToast(UIUtils.getString(R.string.new_cannot_action));
            return;
        }

        if (nowPosition == -1){
            UIUtils.showToast(UIUtils.getString(R.string.please_choose_news_item_first));
            return;
        }

        if (nowPosition == 0){
            UIUtils.showToast(UIUtils.getString(R.string.cannot_move_top));
            return;
        }

        sortMoveToTop(nowPosition,mSortList); // 交换 ----------
        MyPbNewsBean listBean = mMyPbBeanList.get(nowPosition);
        int prePosition = nowPosition -1;
        mMyPbBeanList.remove(nowPosition);
        mMyPbBeanList.add(prePosition,listBean);
        nowPosition = prePosition;
        mAdapter.notifyDataSetChanged();
        /* ===================  排序数字的--------交换 ======================*/

    }
    public void actionMoveTopBottom(){
        if (mMyPbBeanList.size() == 0){
            UIUtils.showToast(UIUtils.getString(R.string.new_cannot_action));
            return;
        }
        if (nowPosition == -1){
            UIUtils.showToast(UIUtils.getString(R.string.please_choose_news_item_first));
            return;
        }

        if (nowPosition == mMyPbBeanList.size() -1){
            UIUtils.showToast(UIUtils.getString(R.string.cannot_move_bottom));
            return;
        }
        sortMoveToBottom(nowPosition,mSortList);
        MyPbNewsBean listBean = mMyPbBeanList.get(nowPosition);
        int nextPosition = nowPosition + 1;
        mMyPbBeanList.remove(nowPosition);
        mMyPbBeanList.add(nextPosition,listBean);
        nowPosition = nextPosition;
        mAdapter.notifyDataSetChanged();
    }

    public void actionMoveToTrick(){
        if (mMyPbBeanList.size() == 0){
            UIUtils.showToast(UIUtils.getString(R.string.new_cannot_action));
            return;
        }
        if (nowPosition == -1){
            UIUtils.showToast(UIUtils.getString(R.string.please_choose_news_item_first));
            return;
        }

        if (nowPosition == 0){
            UIUtils.showToast(UIUtils.getString(R.string.cannot_move_trick));
            return;
        }
        sortMoveToStick(nowPosition,mSortList);
        MyPbNewsBean listBean = mMyPbBeanList.get(nowPosition);
        mMyPbBeanList.remove(nowPosition);
        mMyPbBeanList.add(0,listBean);
        nowPosition = 0;
        mAdapter.notifyDataSetChanged();
    }

    public void actionMoveToSaveSort(){
        if (mMyPbBeanList.size() == 0){
            UIUtils.showToast(UIUtils.getString(R.string.new_cannot_action));
            return;
        }

        List<Integer> list = new ArrayList<>();
        for (String num:mSortList){
            list.add(Integer.parseInt(num));
        }
        ApiRetrofit.getInstance().saveMyPbPosition(Integer.parseInt(mSortList.get(0)),list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getCode() == 1){
                        UIUtils.showToast(UIUtils.getString(R.string.my_news_save_success));
                        UserOtherCache.setNewsPbSort(UIUtils.listToStr(mSortList));
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));

    }

    /* 顺序保存 ------------- */
    private   void sortMoveToTop(int position,ArrayList<String> list){
        if (position ==0){
            return;
        }
        String value = list.get(position);
        int prePosition = position -1;
        list.remove(position);
        list.add(prePosition,value);
    }

    private   void sortMoveToBottom(int position,ArrayList<String> list){
        if (position ==list.size()-1){
            return;
        }
        String value = list.get(position);
        int nextPosition = position + 1;
        list.remove(position);
        list.add(nextPosition,value);
    }

    private   void sortMoveToStick(int position, ArrayList<String> list){
        if (position == 0){
            return;
        }
        String value = list.get(position);
        list.remove(position);
        list.add(0,value);
    }
}

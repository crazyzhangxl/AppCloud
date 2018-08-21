package com.jit.appcloud.ui.presenter;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.Friend;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.ContactNotificationMessageData;
import com.jit.appcloud.model.response.FriendAddGetResponse;
import com.jit.appcloud.model.response.UserInfoResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.INewFriendAtView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.NetUtils;
import com.jit.appcloud.util.PinyinUtils;
import com.jit.appcloud.util.UIUtils;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;

/**
 * @author zxl on 2018/6/11.
 *         discription: 新朋友将在这里呈现
 *         好友共计分为3种状态 已经添加/请求已发送/被请求添加为好友
 *
 */

public class NewFriendAtPresenter extends BasePresenter<INewFriendAtView> {
    private List<FriendAddGetResponse.DataBean> mData = new ArrayList<>();
    private LQRAdapterForRecyclerView<FriendAddGetResponse.DataBean> mAdapter;

    public NewFriendAtPresenter(BaseActivity context) {
        super(context);
    }

    public void loadNewFriendData() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            UIUtils.showToast(UIUtils.getString(R.string.please_check_net));
            return;
        }
        setAdapter();
        loadData();
    }

    public void refreshFriendData(){
        if (!NetUtils.isNetworkAvailable(mContext)) {
            UIUtils.showToast(UIUtils.getString(R.string.please_check_net));
            return;
        }
        loadData();
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new LQRAdapterForRecyclerView<FriendAddGetResponse.DataBean>(mContext, mData, R.layout.item_new_friends) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, FriendAddGetResponse.DataBean item, int position) {

                    ImageView ivHeader = helper.getView(R.id.ivHeader);
                    helper.setText(R.id.tvName, item.getFriendname()) // 好友姓名
                            .setText(R.id.tvMsg, item.getMessage());

                    if (item.getStatus() == 1) {//已经是好友
                        helper.setViewVisibility(R.id.tvAdded, View.VISIBLE)
                                .setViewVisibility(R.id.tvWait, View.GONE)
                                .setViewVisibility(R.id.btnAck, View.GONE);
                    } else if (item.getStatus() == 3) {//别人发来的添加好友请求
                        helper.setViewVisibility(R.id.tvAdded, View.GONE)
                                .setViewVisibility(R.id.tvWait, View.GONE)
                                .setViewVisibility(R.id.btnAck, View.VISIBLE);
                    } else if (item.getStatus() == 2) {//我发起的添加好友请求
                        helper.setViewVisibility(R.id.tvAdded, View.GONE)
                                .setViewVisibility(R.id.tvWait, View.VISIBLE)
                                .setViewVisibility(R.id.btnAck, View.GONE);
                    }

                    String portraitUri = item.getImage();
                    if (TextUtils.isEmpty(portraitUri)) {
                        portraitUri = DBManager.getInstance().getPortraitUri(item.getUsername(),String.valueOf(item.getId()));
                    }
                    Glide.with(mContext).load(portraitUri).into(ivHeader);
                    helper.getView(R.id.btnAck).setOnClickListener(v -> {
                        agreeFriends(String.valueOf(item.getId()),item.getFriendname(), helper);
                    });

                }
            };
        }
        getView().getRvNewFriend().setAdapter(mAdapter);
    }

    private void agreeFriends(String friendId,String friendName, LQRViewHolderForRecyclerView helper) {
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().agreeFriend(UserCache.getToken(),friendName)
                .subscribeOn(Schedulers.io())  // 子线程中发请求
                .observeOn(AndroidSchedulers.mainThread()) // 主线程中更新view
                .doOnNext(response -> {
                    if (response != null && response.getCode() ==1) {
                        helper.setViewVisibility(R.id.tvAdded,View.VISIBLE)
                                .setViewVisibility(R.id.btnAck,View.GONE);
                    }

                })
                .observeOn(Schedulers.io()) // 子线程中发送请求
                .flatMap(response -> {
                    if (response != null && response.getCode() ==1){
                        return ApiRetrofit.getInstance().getFriendInfoByName(UserCache.getToken(),friendName);
                    }
                    return Observable.error(new Exception(UIUtils.getString(R.string.agree_friend_fail)));
                })
                .observeOn(AndroidSchedulers.mainThread()) // 主线程中做处理
                .subscribe(userInfoResponse -> {
                    if (userInfoResponse != null && userInfoResponse.getCode() == 1) {
                        UserInfoResponse.DataBean dataBean = userInfoResponse.getData();
                        UserInfo userInfo = new UserInfo(
                                String.valueOf(dataBean.getId()),
                                dataBean.getUsername(),
                                dataBean.getImage() == null ? null : Uri.parse(dataBean.getImage())
                        );
                        if (userInfo.getPortraitUri() == null || TextUtils.isEmpty(userInfo.getPortraitUri().toString())) {
                            userInfo.setPortraitUri(Uri.parse(DBManager.getInstance().getPortraitUri(userInfo)));
                        }
                        /* 这后面都没有得到执行啊!!*/
                        Friend friend = new Friend(
                                userInfo.getUserId(),
                                userInfo.getName(),
                                userInfo.getPortraitUri().toString(),
                                userInfo.getName(),
                                null, null, null, null,
                                PinyinUtils.getPinyin(userInfo.getName()),
                                PinyinUtils.getPinyin(userInfo.getName())
                        );

                        DBManager.getInstance().saveOrUpdateFriend(friend);
                        // 更新联系人列表
                        /*  发送体同意添加好友 =======*/
                        ContactNotificationMessage requestMessage = ContactNotificationMessage.obtain(ContactNotificationMessage.CONTACT_OPERATION_ACCEPT_RESPONSE, UserCache.getId(), friendId, null);
                        ContactNotificationMessageData c = new ContactNotificationMessageData();
                        c.setSourceUserNickname(UserCache.getName());
                        c.setVersion(101);
                        requestMessage.setExtra(new Gson().toJson(c));
                        RongIMClient.getInstance().sendMessage(Message.obtain(friendId, Conversation.ConversationType.PRIVATE, requestMessage), "", "", null, null);

                        UIUtils.postTaskDelay(() -> {
                            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_FRIEND);
                            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                        }, 500);
                    }
                    mContext.hideWaitingDialog();
                }, throwable -> {
                    mContext.hideWaitingDialog();
                    LogUtils.e("错误",throwable.getLocalizedMessage());
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }
    /* 加载好友数据*/
    private void loadData() {
        ApiRetrofit.getInstance().getAllUserRelationship(UserCache.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(friendAddGetResponse -> {
                    if (friendAddGetResponse.getCode() == 1){
                        List<FriendAddGetResponse.DataBean> dataBeans = friendAddGetResponse.getData();
                        if (dataBeans != null && dataBeans.size() != 0) {
                            for (int i = 0; i < dataBeans.size(); i++) {
                                FriendAddGetResponse.DataBean re = dataBeans.get(i);
                                if (re.getStatus() == 2) {//是我发起的添加好友请求
                                    dataBeans.remove(re);
                                    i--;
                                }
                            }
                        }

                        if (dataBeans != null && dataBeans.size() != 0){
                            getView().getNewFriendText().setVisibility(View.VISIBLE);
                        }else {
                            getView().getNewFriendText().setVisibility(View.GONE);
                        }
                        mData.clear();
                        mData.addAll(dataBeans);
                        mAdapter.notifyDataSetChanged();

                    }else {
                       UIUtils.showToast("加载失败!");
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }
}

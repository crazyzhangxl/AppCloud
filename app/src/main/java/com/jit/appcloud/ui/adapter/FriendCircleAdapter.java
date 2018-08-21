package com.jit.appcloud.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.model.bean.CommentBean;
import com.jit.appcloud.model.bean.CommentConfig;
import com.jit.appcloud.model.bean.CommentItem;
import com.jit.appcloud.model.bean.FavortItem;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.response.FriendCircleResponse;
import com.jit.appcloud.ui.activity.MulPictureShowActivity;
import com.jit.appcloud.ui.activity.message.FriendCircleActivity;
import com.jit.appcloud.util.GlideLoaderUtils;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.CommentListView;
import com.jit.appcloud.widget.ExpandableTextView;
import com.jit.appcloud.widget.FavortListView;
import com.jit.appcloud.widget.GoodView;
import com.jit.appcloud.widget.MultiImageView;
import com.jit.appcloud.widget.spannable.ISpanClick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/8/2.
 *         discription: 朋友圈Adapter
 */

public class FriendCircleAdapter extends BaseQuickAdapter<FriendCircleResponse.DataBean,BaseViewHolder> {
    private GoodView mGoodView;
    private onNeedsIconClickListener mOnNeedsIconClickListener;
    public void setOnNeedsIconClickListener(onNeedsIconClickListener onNeedsIconClickListener){
        this.mOnNeedsIconClickListener = onNeedsIconClickListener;
    }
    public FriendCircleAdapter(int layoutResId, @Nullable List<FriendCircleResponse.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendCircleResponse.DataBean item) {
        setCommonView(helper,item);
        setPictures(helper,item);
        setFavourite(helper,item);
        setCommentViews(helper,item);
    }

    /**
     * 设置普通的View 包括头像;昵称;时间;删除等等
     * @param helper
     * @param item
     */
    private void setCommonView(BaseViewHolder helper, FriendCircleResponse.DataBean item) {
        // 我自己
        ImageView ivHead =  helper.getView(R.id.headIv);
        if (UserCache.getId().equals(String.valueOf(item.getMessageUserId()))){
            helper.setText(R.id.nameTv,item.getMessageUsername());
            helper.getView(R.id.deleteBtn).setVisibility(View.VISIBLE);
        }else {
            // 好友
            String displayName = DBManager.getInstance().getFriendDisplayName(String.valueOf(item.getMessageUserId()));
            if (displayName != null){
                helper.setText(R.id.nameTv,displayName);
            }
            helper.getView(R.id.deleteBtn).setVisibility(View.GONE);
        }
        helper.setText(R.id.timeTv,item.getCreateTime());
        ExpandableTextView contentTv = helper.getView(R.id.contentTv);
        contentTv.setText(item.getContent(),helper.getAdapterPosition());
        GlideLoaderUtils.display(mContext,ivHead,item.getMessageImage());
        // 设置删除的
        helper.addOnClickListener(R.id.deleteBtn);

    }

    /**
     * 设置图片和点击事件
     * @param helper
     * @param item
     */
    private void setPictures(BaseViewHolder helper, FriendCircleResponse.DataBean item) {
        String picture = item.getPicture();
        // 图片为空,则不加载
        if (TextUtils.isEmpty(picture)){
            return;
        }
        List<String> mData = Arrays.asList(picture.split(", "));
        MultiImageView mulItemView =  helper.getView(R.id.mulItemView);
        if (mData != null && mData.size() !=0){
            mulItemView.setList(mData);
            mulItemView.setOnItemClickListener((view, position) -> {
                MulPictureShowActivity.startImagePagerActivity((FriendCircleActivity)mContext,mData,position);
            });
        }
    }

    /**
     * 设置
     * 点赞点击事件;
     * 评论点击事件;
     * 点赞的列别;
     * @param helper
     * @param item
     *
     * 确定？？ 草拟吗, help.getAdapterPosition 是从第一个加载的？？？？？？？？
     * viewHolder.getLayoutPosition() 获取当前item的position 才是啊
     *
     * 更替数据的时候要注意了啊; 该修改数据是从0开始的,但是加载数据是从1开始的撒
     */

    private void setFavourite(BaseViewHolder helper, FriendCircleResponse.DataBean item) {
        FavortListView favortListTv = helper.getView(R.id.favortListTv);
        FavortListAdapter favortListAdapter = new FavortListAdapter();
        TextView tvFavourite = helper.getView(R.id.favortBtn);
        List<FavortItem> yesUser = item.getYesUser();
        favortListTv.setAdapter(favortListAdapter);

        if (yesUser != null && yesUser.size() != 0){
            favortListTv.setVisibility(View.VISIBLE);
            favortListTv.setSpanClickListener(new ISpanClick() {
                @Override
                public void onClick(int position) {
                    // 待定 -----------------------
                }
            });
            favortListAdapter.setDatas(yesUser);
            favortListAdapter.notifyDataSetChanged();
        }else {
            favortListTv.setVisibility(View.GONE);
            tvFavourite.setText("");

        }
        tvFavourite.setOnClickListener
                (new MyFavortOnClick(yesUser,item.getId(),helper.getAdapterPosition()));
    }

    /**
     * 这里就比较难了; 设置的是评论
     * @param helper
     * @param item
     */
    private void setCommentViews(BaseViewHolder helper, FriendCircleResponse.DataBean item) {
        CommentListView commentListView = helper.getView(R.id.commentList);
        CommentAdapter commentAdapter = new CommentAdapter(mContext);
        commentListView.setAdapter(commentAdapter);
        List<CommentItem> mCommentItemList = new ArrayList<>();
        mCommentItemList.clear();
        mCommentItemList = filterComment(item.getComments(),null);
        if (mCommentItemList.size() != 0){
            commentAdapter.setDatas(mCommentItemList);
            commentAdapter.notifyDataSetChanged();
            commentListView.setVisibility(View.VISIBLE);
            commentListView.setOnItemClick(new MyCommentClick(mCommentItemList,item.getId(),helper.getAdapterPosition()));
        }else {
            commentListView.setVisibility(View.GONE);
        }
        helper.getView(R.id.ll_comment).setOnClickListener
                (new MyPblishEdtextClick(item.getId(),helper.getAdapterPosition()));
        helper.getView(R.id.commentBtn).setOnClickListener
                (new MyPblishEdtextClick(item.getId(),helper.getAdapterPosition()));
    }

    /**
     * 递归转换筛选列表数据
     * @param list
     */
    private List<CommentItem> filterComment(List<CommentBean> list,List<CommentItem> mComment) {
        if (mComment == null){
            mComment = new ArrayList<>();
        }
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                //  读取第一层，存储至新结构
                CommentBean commentBean = list.get(i);
                mComment.add(new CommentItem(
                        commentBean.getId(),
                        commentBean.getCommentedUsername(),
                        String.valueOf(commentBean.getCommentedUserId()),
                        commentBean.getContent(),
                                String.valueOf(commentBean.getId()),
                                String.valueOf(commentBean.getUserId()),
                                commentBean.getUsername()));
                List<CommentBean> subComments = commentBean.getSub_comment();
                filterComment(subComments,mComment);
            }
        }
        return mComment;
    }

    /**
     * 评论的ID
     * @param publishId
     * @param circlePosition
     */
    private void comment(int publishId, int circlePosition) {
        CommentConfig config = new CommentConfig();
        config.circlePosition = circlePosition;
        config.commentType = CommentConfig.Type.PUBLIC;
        config.setPublishId(publishId);
        // 发布者是我
        config.setPublishUserId(UserCache.getId());
        if (mOnNeedsIconClickListener != null){
            mOnNeedsIconClickListener.showPublishEdtext(config);
        }
    }

    /**
     * //点赞、取消点赞
     */
    private long mLasttime = 0;
    private void favort(int publishId,int circlePosition, String mTitle, View view) {
        //防止快速点击操作
        if (System.currentTimeMillis() - mLasttime < 700) {
            return;
        }
        mLasttime = System.currentTimeMillis();
            if ("赞".equals(mTitle)) {
                addFavort(publishId, circlePosition,view);
            } else {//取消点赞
                deleteFavort(publishId, circlePosition);
            }
        }

    private void deleteFavort(int publishId, int circlePosition) {
        ApiRetrofit.getInstance().cancelClickYes(publishId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getCode() == 1){
                        if (mOnNeedsIconClickListener != null){
                            LogUtils.e("点赞","------ 取消赞 -----");
                            mOnNeedsIconClickListener.updateDelFavort(circlePosition);
                        }
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

    private void addFavort(int publishId, int circlePosition, View view) {
        // 进行网络请求
        ApiRetrofit.getInstance().addClickYes(publishId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getCode() == 1){
                        if (mGoodView == null){
                            mGoodView = new GoodView(mContext);
                        }
                        mGoodView.setImage(R.drawable.dianzan);
                        mGoodView.show(view);
                        // 设置回调------
                        if (mOnNeedsIconClickListener != null){
                            mOnNeedsIconClickListener.updateAddFavort(circlePosition);
                        }
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }

    private boolean defIsClickYes(List<FavortItem> yesUserIds){
        for (FavortItem yesID : yesUserIds){
            if (yesID.getUserId() == Integer.parseInt(UserCache.getId())){
                return true;
            }
        }
        return false;
    }

    /**
     * 必要的元素点击回调
     */
    public interface onNeedsIconClickListener{
        void updateAddFavort(int position);
        void updateDelFavort(int position);
        void showPublishEdtext(CommentConfig commentConfig);
    }

    private class MyFavortOnClick implements View.OnClickListener{
        private List<FavortItem> mFavortItems;
        private int msgPosition;
        private int circlePosition;

        public MyFavortOnClick(List<FavortItem> favortItems, int msgPosition, int circlePosition) {
            mFavortItems = favortItems;
            this.msgPosition = msgPosition;
            this.circlePosition = circlePosition;
        }

        @Override
        public void onClick(View v) {
            if (defIsClickYes(mFavortItems)){
                favort(msgPosition,circlePosition,"取消",v);
            }else {
                favort(msgPosition,circlePosition,"赞",v);
            }
        }
    }

    private class MyPblishEdtextClick implements View.OnClickListener{
        private int publishID;
        private int position;

        public MyPblishEdtextClick(int publishID, int position) {
            this.publishID = publishID;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            comment(publishID,position);
        }
    }

    /**
     * 传递的方式 防止错位导致的回调错误
     */
    private class MyCommentClick implements CommentListView.OnItemClickListener{
        private int publishID;
        private int adapterPosition;
        private List<CommentItem> mCommentItemList;
        public MyCommentClick(List<CommentItem> mCommentItemList, int publishID, int adapterPosition) {
            this.mCommentItemList = mCommentItemList;
            this.publishID = publishID;
            this.adapterPosition = adapterPosition;
        }
        @Override
        public void onItemClick(int position) {
            CommentItem mCommentItem = mCommentItemList.get(position);
            if (UserCache.getId().equals(mCommentItem.getUserId())){
            }else {
                CommentConfig config = new CommentConfig();
                config.circlePosition = adapterPosition;
                config.commentPosition = position;
                config.commentType = CommentConfig.Type.REPLY;
                config.setPublishId(publishID);
                config.setCommentReplyID(mCommentItem.getCommentID());
                config.setId(mCommentItem.getUserId());
                config.setName(mCommentItem.getUserNickname());
                if (mOnNeedsIconClickListener != null){
                    mOnNeedsIconClickListener.showPublishEdtext(config);
                }
            }
        }
    }
}

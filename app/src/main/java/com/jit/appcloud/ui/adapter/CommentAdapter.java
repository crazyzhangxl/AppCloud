package com.jit.appcloud.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.model.bean.CommentItem;
import com.jit.appcloud.widget.CommentListView;
import com.jit.appcloud.widget.spannable.CircleMovementMethod;
import com.jit.appcloud.widget.spannable.NameClickListener;
import com.jit.appcloud.widget.spannable.NameClickable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxl on 2018/8/2.
 *         discription: 评论的适配器
 *         这里写的就是一个ListView的适配器罢了
 */

public class CommentAdapter {
    private Context mContext;
    private CommentListView mListview;
    private List<CommentItem> mDatas;

    public CommentAdapter(Context context){
        mContext = context;
        mDatas = new ArrayList<CommentItem>();
    }

    public CommentAdapter(Context context, List<CommentItem> datas){
        mContext = context;
        setDatas(datas);
    }

    public void bindListView(CommentListView listView){
        if(listView == null){
            throw new IllegalArgumentException("CommentListView is null....");
        }
        mListview = listView;
    }

    public void setDatas(List<CommentItem> datas){
        if(datas == null ){
            datas = new ArrayList<CommentItem>();
        }
        mDatas = datas;
    }

    public List<CommentItem> getDatas(){
        return mDatas;
    }

    public int getCount(){
        if(mDatas == null){
            return 0;
        }
        return mDatas.size();
    }

    public CommentItem getItem(int position){
        if(mDatas == null){
            return null;
        }
        if(position < mDatas.size()){
            return mDatas.get(position);
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private View getView(final int position){
        System.out.println("CommentAdapter getView-----------------------" + position);
        View convertView = View.inflate(mContext,
                R.layout.item_social_comment, null);
        TextView commentTv = convertView.findViewById(R.id.commentTv);
        final CircleMovementMethod circleMovementMethod =
                new CircleMovementMethod(R.color.circle_name_selector_color,
                R.color.circle_name_selector_color);
        final CommentItem bean = mDatas.get(position);
        String name = bean.getUserNickname();
        String toReplyName = "";
        if (bean.getAppointUserNickname() != null ) {
            toReplyName = bean.getAppointUserNickname();
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();

        // 第一个 ------------------
        builder.append(setClickableSpan(name,bean.getUserId(), 0));

        if (!TextUtils.isEmpty(toReplyName) && !"0".equals(toReplyName)) {
            builder.append(" 回复 ");
            String  displayName = DBManager.getInstance().getFriendDisplayName(bean.getAppointUserid());
            if (displayName == null){
                displayName = bean.getAppointUserNickname();
            }
            builder.append(setClickableSpan(toReplyName,displayName, 1));
        }
        builder.append(": ");
        //转换表情字符
        String contentBodyStr = bean.getContent();
        //SpannableString contentSpanText = new SpannableString(contentBodyStr);
        //contentSpanText.setSpan(new UnderlineSpan(), 0, contentSpanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(contentBodyStr);

        commentTv.setText(builder);
        commentTv.setMovementMethod(circleMovementMethod);

        /*
         * 短按事件
         */
        commentTv.setOnClickListener(v -> {
            if (circleMovementMethod.isPassToTv() && mListview.getOnItemClickListener() != null) {
                mListview.getOnItemClickListener().onItemClick(position);
            }
        });
        /*
         * 长按事件
         */
        commentTv.setOnLongClickListener(v -> {
            if (circleMovementMethod.isPassToTv() && mListview.getOnItemLongClickListener() != null) {
                mListview.getOnItemLongClickListener().onItemLongClick(position);
                return true;
            }
            return false;
        });
        return convertView;
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, String userId, int position) {
        // 字体内容填充入 spannableString
        SpannableString subjectSpanText = new SpannableString(textStr);
        // 改变文本样式
        subjectSpanText.setSpan(new NameClickable(new NameClickListener(
                        subjectSpanText,userId), position), 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    /**
     * 刷新数据 -------
     */
    public void notifyDataSetChanged(){
        if(mListview == null){
            throw new NullPointerException("listview is null, please bindListView first...");
        }
        mListview.removeAllViews();
        if(mDatas == null || mDatas.size() == 0){
            return;
        }
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0; i<mDatas.size(); i++){
            final int index = i;
            View view = getView(index);
            if(view == null){
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }

            mListview.addView(view, index, layoutParams);
        }

    }
}

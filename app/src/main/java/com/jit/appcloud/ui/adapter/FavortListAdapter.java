package com.jit.appcloud.ui.adapter;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import com.jit.appcloud.R;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.model.bean.FavortItem;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.FavortListView;
import com.jit.appcloud.widget.spannable.CircleMovementMethod;
import com.jit.appcloud.widget.spannable.NameClickable;
import java.util.List;

/**
 * @author zxl on 2018/8/2.
 *         discription:
 */

public class FavortListAdapter {
    private FavortListView mListView;
    private List<FavortItem> datas;

    public List<FavortItem> getDatas() {
        return datas;
    }

    public void setDatas(List<FavortItem> datas) {
        this.datas = datas;
    }

    @NonNull
    public void bindListView(FavortListView listview){
        if(listview == null){
            throw new IllegalArgumentException("FavortListView is null ....");
        }
        mListView = listview;
    }


    public int getCount() {
        if(datas != null && datas.size() > 0){
            return datas.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        if(datas != null && datas.size() > position){
            return datas.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged(){
        if(mListView == null){
            throw new NullPointerException("listview is null, please bindListView first...");
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if(datas != null && datas.size() > 0){
            //添加点赞图标
            builder.append(setImageSpan());
            FavortItem item ;
            String nickName;
            /* for循环遍历加载 */
            for (int i=0; i<datas.size(); i++){
                item = datas.get(i);
                if(item != null){
                    nickName = item.getUsername();
                    if (item.getUserId() != Integer.parseInt(UserCache.getId())){
                        String friendDisplayName =
                                DBManager.getInstance().getFriendDisplayName(String.valueOf(item.getUserId()));
                        if (!TextUtils.isEmpty(friendDisplayName)){
                            nickName = friendDisplayName;
                        }
                    }
                    builder.append(setClickableSpan(nickName, i));
                    if(i != datas.size()-1){
                        builder.append(", ");
                    }
                }
            }
        }
        mListView.setText(builder);
        mListView.setMovementMethod(new CircleMovementMethod(R.color.circle_name_selector_color));
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, int position) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new NameClickable(mListView.getSpanClickListener(), position), 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    private SpannableString setImageSpan(){
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new ImageSpan(UIUtils.getContext(), R.drawable.dianzansmal, DynamicDrawableSpan.ALIGN_BASELINE),
                0 , 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }
}

package com.jit.appcloud.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.bean.MulPicBean;
import com.jit.appcloud.ui.activity.news.PublishInfoActivity;
import com.jit.appcloud.ui.fragment.center.CtFarmLogInFragment;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;

import java.util.List;

/**
 * Created by 张先磊 on 2018/5/11.
 */

public class PicPutSelectMulAdapter extends LQRAdapterForRecyclerView<MulPicBean> {
    private List<MulPicBean> mData;
    private Context mContext;
    private static final int PIC_SHOW = R.layout.item_pic_show;
    private static final int PIC_NONE = R.layout.item_pic_add;
    private boolean mIsDeleting = false;
    private CtFarmLogInFragment mCtFarmLogInFragment;

    public boolean isDeleting() {
        return mIsDeleting;
    }

    public void setDeleting(boolean deleting) {
        mIsDeleting = deleting;
    }
    public PicPutSelectMulAdapter(Context context, List<MulPicBean> data , CtFarmLogInFragment mCtFarmLogInFragment) {
        super(context, data);
        mContext = context;
        mData = data;
        this.mCtFarmLogInFragment = mCtFarmLogInFragment;
    }

    @Override
    public void convert(LQRViewHolderForRecyclerView helper, MulPicBean item, int position) {
        // 超过6张就不提供啦 只能删除啦

        if (item.getType() == AppConst.PicType.SHOW){
            ImageView iv = helper.getView(R.id.image);
            Glide.with(mContext).load(item.getfilePath()).into(iv);
            helper.getView(R.id.hover).setVisibility(isDeleting()?View.VISIBLE : View.GONE);
            helper.getView(R.id.delete).setVisibility(isDeleting()?View.VISIBLE : View.GONE);
            helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.remove(position);
                    mCtFarmLogInFragment.mPicNum ++;
                    mCtFarmLogInFragment.mShowSelectList.remove(position);
                    // 如果末尾不是以+结尾那么就添加
                    if (mData.get(mData.size()-1).getType()!= AppConst.PicType.ADD_BUTTON){
                        mData.add(new MulPicBean(null,AppConst.PicType.ADD_BUTTON));
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).getType()== AppConst.PicType.SHOW){
           return PIC_SHOW;
        }else {
            return PIC_NONE;
        }
    }
}
package com.jit.appcloud.ui.adapter.multi;

import android.graphics.PorterDuff;
import android.opengl.Visibility;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.util.GlideLoaderUtils;
import com.jit.appcloud.util.UIUtils;

import java.util.List;

/**
 * @author zxl on 2018/8/2.
 *         discription:
 */

public class CircleTalkAdapter extends BaseMultiItemQuickAdapter<TalkPicItem,BaseViewHolder> {
    private boolean shadowStatus = false;

    public boolean isShadowStatus() {
        return shadowStatus;
    }

    public void setShadowStatus(boolean shadowStatus) {
        this.shadowStatus = shadowStatus;
    }

    public void setShadowChange(){
        shadowStatus = !shadowStatus;
        notifyDataSetChanged();
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */



    public CircleTalkAdapter(List<TalkPicItem> data) {
        super(data);
        addItemType(TalkPicItem.ADD_PIC, R.layout.item_grid_pb_talk_empty);
        addItemType(TalkPicItem.SHOW_PIC,R.layout.item_grid_pb_talk);
    }


    @Override
    protected void convert(BaseViewHolder helper, TalkPicItem item) {
        switch (helper.getItemViewType()){
            case TalkPicItem.ADD_PIC:
                helper.addOnClickListener(R.id.rlAddTalk);
                break;
            case TalkPicItem.SHOW_PIC:
                // 加载图片
                ImageView ivTalkPic =  helper.getView(R.id.imgPhoto);
                GlideLoaderUtils.display(mContext,ivTalkPic,item.getImageFile());
                if (shadowStatus){
                    ivTalkPic.setColorFilter(UIUtils.getColor(R.color.image_overlay_true), PorterDuff.Mode.SRC_ATOP);
                    helper.setVisible(R.id.imgDelete, true);
                }else {
                    ivTalkPic.setColorFilter(UIUtils.getColor(R.color.image_overlay_false), PorterDuff.Mode.SRC_ATOP);
                    helper.setVisible(R.id.imgDelete, false);
                }
                helper.addOnClickListener(R.id.imgDelete);
                helper.addOnClickListener(R.id.imgPhoto);
                helper.addOnLongClickListener(R.id.imgPhoto);
                break;
            default:
                break;
        }
    }
}

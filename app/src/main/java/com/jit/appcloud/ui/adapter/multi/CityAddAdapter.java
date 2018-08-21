package com.jit.appcloud.ui.adapter.multi;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.commom.ResourceProvider;
import com.jit.appcloud.util.SharePreferenceUtils;
import com.jit.appcloud.util.UIUtils;

import java.util.List;

/**
 * Created by 张先磊 on 2018/4/23.
 * 要记住 在这里使用butterknife是没有效果的 一定要记得了啊 别再犯傻LA
 */

public class CityAddAdapter extends BaseMultiItemQuickAdapter<MultiItem, BaseViewHolder> {
    public static final int[] BLUR_IMAGE = {R.mipmap.ic_city_blur0, R.mipmap.ic_city_blur1, R.mipmap.ic_city_blur2, R.mipmap.ic_city_blur3, R.mipmap.ic_city_blur4, R.mipmap.ic_city_blur5};
    private Drawable mDrawableLocation;
    private Context context;
    private ImageView mImageView;
    private TextView mTv_status;
    private ImageView mView_del;
    private View mView_hover;
    private TextView mText_city_name;
    private boolean mIsDeleting = false;
    private ImageView mMChecked;


    public boolean isDeleting() {
        return mIsDeleting;
    }

    public void setDeleting(boolean deleting) {
        mIsDeleting = deleting;
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CityAddAdapter(List<MultiItem> data, Context context) {
        super(data);
        addItemType(MultiItem.CITY, R.layout.item_city_followed_city);
        addItemType(MultiItem.ADDSYMBOL, R.layout.item_city_add_city);
        this.context = context;
        initViews();
    }

    private void initViews() {
        mDrawableLocation = ContextCompat.getDrawable(context,R.mipmap.ic_location);
        mDrawableLocation.setBounds(0, 0, UIUtils.dipToPx(context, R.dimen.common_dimension_14), UIUtils.dipToPx(context, R.dimen.common_dimension_14));
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItem item) {
        switch (helper.getItemViewType()) {
            case MultiItem.CITY:
                if (item == null) {
                    return;
                }
                mImageView = helper.getView(R.id.image);
                mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mImageView.setImageResource(item.backgroundId);
                helper.setText(R.id.city_temp,item.temp);
                mText_city_name = helper.getView(R.id.city_name);
                mText_city_name.setText(item.cityName);

                mTv_status = helper.getView(R.id.city_status);
                Drawable drawableLeft = UIUtils.getDrawable(context, ResourceProvider.getIconId(item.weatherStatus));
                drawableLeft.setBounds(0, 0, UIUtils.dipToPx(context, R.dimen.common_dimension_16),
                        UIUtils.dipToPx(context, R.dimen.common_dimension_16));
                mTv_status.setCompoundDrawables(drawableLeft,null,null,null);
                mTv_status.setText(item.weatherStatus);

                mView_del = helper.getView(R.id.delete);
                helper.addOnClickListener(R.id.delete);

                mView_hover = helper.getView(R.id.hover);
                Log.e("长按数据加载", "convert"+isDeleting() );
                mView_del.setVisibility(isDeleting()?View.VISIBLE : View.GONE);
                mView_hover.setVisibility(isDeleting()?View.VISIBLE : View.GONE);
                if (item.cityName.equals(SharePreferenceUtils.getInstance(context).getString(AppConst.LOCATE_CITY_str,""))){
                    mView_del.setVisibility(View.GONE);
                    mView_hover.setVisibility(View.GONE);
                    mText_city_name.setCompoundDrawables(mDrawableLocation, null, null, null);
                }else {
                    mText_city_name.setCompoundDrawables(null, null, null, null);
                }
                // 设置默认
                mMChecked = helper.getView(R.id.checked);
                boolean isDefault = item.cityId.equals(SharePreferenceUtils.getInstance(context).getString(AppConst.LOCATION_CITY_NOWS_ID,""));
                mMChecked.setVisibility(isDefault?View.VISIBLE:View.GONE);
                break;
            case MultiItem.ADDSYMBOL:
                helper.addOnClickListener(R.id.image);
                break;
            default:
                break;
        }


    }


}

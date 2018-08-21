package com.jit.appcloud.ui.activity.me;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.model.bean.GalleryBean;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.widget.ListPopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author zxl on 2018/06/28.
 *         discription:  示例-------------
 */
public class PopNiceActivity extends BaseActivity implements ListPopWindow.OnPopItemSelectedListener{

    @BindView(R.id.picture_left_back)
    ImageView mPictureLeftBack;
    @BindView(R.id.picture_title)
    TextView mPictureTitle;
    @BindView(R.id.picture_right)
    TextView mPictureRight;
    @BindView(R.id.rl_picture_title)
    RelativeLayout mRlPictureTitle;
    private List<GalleryBean> mList = new ArrayList<>();
    private ListPopWindow mListPopWindow;
    @Override
    protected void init() {
        mList.add(new GalleryBean("第一条数据"));
        mList.add(new GalleryBean("第二条数据"));
        mList.add(new GalleryBean("第三条数据"));
        mList.add(new GalleryBean("第四条数据"));
        mList.add(new GalleryBean("第五条数据"));
        mList.add(new GalleryBean("第六条数据"));
        mList.add(new GalleryBean("第七条数据"));
        mList.add(new GalleryBean("第八条数据"));
        mList.add(new GalleryBean("第九条数据"));
        mList.add(new GalleryBean("第十条数据"));
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_pop_nice;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mListPopWindow = new ListPopWindow(this,mList);
        mListPopWindow.setPictureTitleView(mPictureTitle);
        mListPopWindow.setOnItemSelectedListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mPictureTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListPopWindow.isShowing()) {
                    mListPopWindow.dismiss();
                } else {
                    mListPopWindow.showAsDropDown(mRlPictureTitle);
                }
            }
        });

        mPictureRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToActivity(GalleryNiceActivity.class);
            }
        });
    }

    @Override
    public void popItemSelected(String title) {
        mPictureTitle.setText(title);
        mListPopWindow.dismiss();
    }
}

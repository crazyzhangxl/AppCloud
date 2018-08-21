package com.jit.appcloud.ui.presenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.Groups;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.ui.activity.message.SessionActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IGroupListAtView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zxl on 2018/7/24.
 *         discription: 群聊列表展示的P层
 */

public class GroupListAtPresenter extends BasePresenter<IGroupListAtView>{
    private List<Groups> mData = new ArrayList<>();
    private BaseQuickAdapter<Groups,BaseViewHolder> mAdapter;
    public GroupListAtPresenter(BaseActivity context) {
        super(context);
    }

    /**
     * 加载数据
     */
    public void loadGroups() {
        loadData();
        setAdapter();
    }

    private void loadData() {
        List<Groups> groups = DBManager.getInstance().getGroups();
        Log.e("群组同步","加载群聊:"+groups.size());
        if (groups != null && groups.size() > 0) {
            mData.clear();
            mData.addAll(groups);
            setAdapter();
            getView().getLlGroups().setVisibility(View.VISIBLE);
        } else {
            getView().getLlGroups().setVisibility(View.GONE);
        }
    }

    private void setAdapter() {
        if (mAdapter == null){
            getView().getRvGroupList().setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
            mAdapter = new BaseQuickAdapter<Groups, BaseViewHolder>(R.layout.item_contact,mData) {
                @Override
                protected void convert(BaseViewHolder helper, Groups item) {
                    // 直接设置群聊头像和群聊名称就可以了
                    ImageView imageView = helper.getView(R.id.ivHeader);
                    Glide.with(mContext).load(item.getPortraitUri()).into(imageView);
                    helper.setText(R.id.tvName,item.getName());
                }
            };
            // 设置了点击事件
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                Intent intent = new Intent(mContext, SessionActivity.class);
                intent.putExtra("sessionId", mData.get(position).getGroupId());
                intent.putExtra("sessionType", AppConst.SESSION_TYPE_GROUP);
                mContext.jumpToActivity(intent);
                mContext.finish();
            });

            getView().getRvGroupList().setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }
}

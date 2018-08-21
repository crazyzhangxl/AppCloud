package com.jit.appcloud.ui.activity.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jit.appcloud.R;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.GroupMember;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.UIUtils;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.recyclerview.LQRRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.UserInfo;

import static com.jit.appcloud.commom.AppConst.EXTRA_FRIEND_INFO;

/**
 * @author zxl on 2018/07/30.
 *         discription:
 */
public class RemoveGpMemberActivity extends BaseActivity {


    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.btnToolbarSend)
    Button mBtnToolbarSend;
    @BindView(R.id.rvMember)
    LQRRecyclerView mRvMember;
    private LQRAdapterForRecyclerView<GroupMember> mAdapter;
    private String mGroupId;
    private List<GroupMember> mData = new ArrayList<>();
    private List<GroupMember> mSelectedData = new ArrayList<>();
    @Override
    protected void init() {
        mGroupId = getIntent().getStringExtra("sessionId");
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_remove_gp_member;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (TextUtils.isEmpty(mGroupId)) {
            finish();
            return;
        }

        mBtnToolbarSend.setText(UIUtils.getString(R.string.delete));
        mBtnToolbarSend.setVisibility(View.VISIBLE);
        mBtnToolbarSend.setBackgroundResource(R.drawable.shape_btn_delete);
        mBtnToolbarSend.setEnabled(false);
    }

    @Override
    protected void initData() {
        List<GroupMember> groupMembers = DBManager.getInstance().getGroupMembers(mGroupId);
        mData.clear();
        mData.addAll(groupMembers);
        setAdapter();
        updateTitle();
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new LQRAdapterForRecyclerView<GroupMember>(this, mData, R.layout.item_contact) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, GroupMember item, int position) {
                    helper.setText(R.id.tvName, item.getName()).setViewVisibility(R.id.cb, item.getUserId().equals(UserCache.getId())?View.GONE:View.VISIBLE);
                    ImageView ivHeader = helper.getView(R.id.ivHeader);
                    Glide.with(RemoveGpMemberActivity.this).load(item.getPortraitUri()).apply(new RequestOptions().placeholder(R.mipmap.default_header)).into(ivHeader);

                    CheckBox cb = helper.getView(R.id.cb);
                    cb.setClickable(true);
                    cb.setChecked(mSelectedData.contains(item));
                    cb.setOnClickListener(v -> {
                        if (cb.isChecked()) {
                            mSelectedData.add(item);
                        } else {
                            mSelectedData.remove(item);
                        }
                        if (mSelectedData.size() > 0) {
                            mBtnToolbarSend.setEnabled(true);
                            mBtnToolbarSend.setText(UIUtils.getString(R.string.delete) + "(" + mSelectedData.size() + ")");
                        } else {
                            mBtnToolbarSend.setEnabled(false);
                            mBtnToolbarSend.setText(UIUtils.getString(R.string.delete));
                        }
                    });
                }
            };
            mAdapter.setOnItemClickListener((helper, parent, itemView, position) -> {
                UserInfo userInfo = DBManager.getInstance().getUserInfo(mData.get(position).getUserId());
                if (userInfo != null) {
                    Intent intent = new Intent(RemoveGpMemberActivity.this, UserInfoBySearchActivity.class);
                    intent.putExtra(EXTRA_FRIEND_INFO, userInfo);
                    jumpToActivity(intent);
                }
            });
            mRvMember.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChangedWrapper();
        }
    }

    private void updateTitle(){
        mTvToolbarTitle.setText(String.format("聊天信息(%d)",mData.size()));
    }

    @Override
    protected void initListener() {
        mBtnToolbarSend.setOnClickListener(v -> {
            ArrayList<String> selectedIds = new ArrayList<>(mSelectedData.size());
            for (int i = 0; i < mSelectedData.size(); i++) {
                GroupMember groupMember = mSelectedData.get(i);
                selectedIds.add(groupMember.getUserId());
            }
            Intent data = new Intent();
            data.putStringArrayListExtra("selectedIds", selectedIds);
            setResult(Activity.RESULT_OK, data);
            finish();
        });

        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());
    }


}

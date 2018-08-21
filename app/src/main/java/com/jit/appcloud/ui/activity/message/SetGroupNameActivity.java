package com.jit.appcloud.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.GroupMember;
import com.jit.appcloud.db.db_model.Groups;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.manager.JsonMananger;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.message.GroupNotificationMessageData;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.GroupNotificationMessage;

/**
 * @author zxl on 2018/07/24.
 *         discription: 设置群聊名称的活动
 */
public class SetGroupNameActivity extends BaseActivity {

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.btnToolbarSend)
    Button mBtnToolbarSend;
    @BindView(R.id.etName)
    EditText mEtName;
    private String mGroupId;

    @Override
    protected void init() {
        mGroupId = getIntent().getStringExtra("groupId");
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_set_group_name;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("修改群聊名称");
        if (TextUtils.isEmpty(mGroupId)){
            // 如果为空直接返回咯
            finish();
            return;
        }

        mBtnToolbarSend.setVisibility(View.VISIBLE);
        mBtnToolbarSend.setText(R.string.str_title_save);
    }

    @Override
    protected void initData() {
        // ---------- 通过Api查询群号等信息
        Observable.just(DBManager.getInstance().getGroupsById(mGroupId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(groups -> {
                    if (groups != null) {
                        mEtName.setText(groups.getName());
                        mEtName.setSelection(groups.getName().length());
                        mBtnToolbarSend.setEnabled(groups.getName().length() > 0);
                    }
                });

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBtnToolbarSend.setEnabled(mEtName.getText().toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBtnToolbarSend.setOnClickListener(v -> {
            String groupName = mEtName.getText().toString().trim();
            if (!TextUtils.isEmpty(groupName)) {
                showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
                ApiRetrofit.getInstance().updateGroupName(groupName,Integer.parseInt(mGroupId))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(setGroupNameResponse -> {
                            if (setGroupNameResponse != null && setGroupNameResponse.getCode() == 1) {
                                Groups groups = DBManager.getInstance().getGroupsById(mGroupId);
                                if (groups != null) {
                                    groups.setName(groupName);
                                    groups.saveOrUpdate("groupid=?", groups.getGroupId());
                                }
                                List<String> mTargetName = new ArrayList<>();
                                List<String> mTargetIDS = new ArrayList<>();
                                List<GroupMember> groupMembers = DBManager.getInstance().queryGroupMembers(mGroupId);
                                for (GroupMember groupMember : groupMembers){
                                    mTargetIDS.add(groupMember.getUserId());
                                    mTargetName.add(groupMember.getDisplayName());
                                }
                                // 发送修改群聊的通知 -------------
                                // 发送了创建群聊的通知
                                GroupNotificationMessageData messageData = new GroupNotificationMessageData();
                                messageData.setOperatorNickname(DBManager.getInstance().getGroupMemberById(mGroupId,UserCache.getId()).getDisplayName());
                                messageData.setTargetUserDisplayNames(mTargetName);
                                messageData.setTargetUserIds(mTargetIDS);
                                messageData.setTargetGroupName(groupName);
                                GroupNotificationMessage requestMessage = GroupNotificationMessage.
                                        obtain(UserCache.getId(),
                                                GroupNotificationMessage.GROUP_OPERATION_RENAME,
                                                JsonMananger.beanToJson(messageData), "哈哈");
                                RongIMClient.getInstance().sendMessage(Message.obtain(String.valueOf(mGroupId),
                                        Conversation.ConversationType.GROUP, requestMessage),
                                        "RC:GrpNtf", "", new IRongCallback.ISendMessageCallback() {
                                    @Override
                                    public void onAttached(Message message) {

                                    }

                                    @Override
                                    public void onSuccess(Message message) {
                                        LogUtils.e("群组消息的通知","----------发送成功了");
                                    }

                                    @Override
                                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                        LogUtils.e("群组消息的通知","----------发送失败了");
                                    }
                                });

                                BroadcastManager.getInstance(SetGroupNameActivity.this).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                                BroadcastManager.getInstance(SetGroupNameActivity.this).sendBroadcast(AppConst.UPDATE_CURRENT_SESSION_NAME);
                                BroadcastManager.getInstance(SetGroupNameActivity.this).sendBroadcast(AppConst.REFRESH_CURRENT_SESSION);
                                UIUtils.showToast(UIUtils.getString(R.string.success_update_name));

                                // 返回前一个活动 --------
                                Intent data = new Intent();
                                data.putExtra("group_name", groupName);
                                setResult(RESULT_OK, data);
                                hideWaitingDialog();
                                finish();
                            } else {
                                hideWaitingDialog();
                                UIUtils.showToast(UIUtils.getString(R.string.fail_update_name));
                            }
                        }, throwable -> {
                            hideWaitingDialog();
                            UIUtils.showToast(throwable.getLocalizedMessage());
                        });
            }

        });
    }


}

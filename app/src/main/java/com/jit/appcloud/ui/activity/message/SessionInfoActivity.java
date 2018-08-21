package com.jit.appcloud.ui.activity.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jit.appcloud.R;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.presenter.SessionInfoAtPresenter;
import com.jit.appcloud.ui.view.IFarmLogAtView;
import com.jit.appcloud.ui.view.ISessionInfoAtView;
import com.jit.appcloud.util.UIUtils;
import com.kyleduo.switchbutton.SwitchButton;
import com.lqr.optionitemview.OptionItemView;

import java.util.ArrayList;

import butterknife.BindView;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static com.jit.appcloud.commom.AppConst.SESSION_TYPE_GROUP;
import static com.jit.appcloud.commom.AppConst.SESSION_TYPE_PRIVATE;

/**
 * @author zxl on 2018/07/24.
 *         discription: 会话用户信息的界面
 */
public class SessionInfoActivity extends BaseActivity<ISessionInfoAtView, SessionInfoAtPresenter> implements ISessionInfoAtView {
    public static final int REQ_ADD_MEMBERS = 1000;
    public static final int REQ_REMOVE_MEMBERS = 1001;
    public static final int  REQ_SET_GROUP_NAME = 1002;
    public static final int REQ_CRETE_GROUP = 1003;

    private String mSessionId = "";
    private Conversation.ConversationType mConversationType = Conversation.ConversationType.PRIVATE;
    private int mSessionType;

    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.rvMember)
    RecyclerView mRvMember;
    @BindView(R.id.oivGroupName)
    OptionItemView mOivGroupName;
    @BindView(R.id.oivQRCordCard)
    OptionItemView mOivQRCordCard;
    @BindView(R.id.tvAnnouncement)
    TextView mTvAnnouncement;
    @BindView(R.id.llAnnouncement)
    LinearLayout mLlAnnouncement;
    @BindView(R.id.vLineTeamManage)
    View mVLineTeamManage;
    @BindView(R.id.oivTeamManage)
    OptionItemView mOivTeamManage;
    @BindView(R.id.llGroupPart1)
    LinearLayout mLlGroupPart1;
    @BindView(R.id.sbToTop)
    SwitchButton mSbToTop;
    @BindView(R.id.oivNickNameInGroup)
    OptionItemView mOivNickNameInGroup;
    @BindView(R.id.sbShowNickName)
    SwitchButton mSbShowNickName;
    @BindView(R.id.llShowNickName)
    LinearLayout mLlShowNickName;
    @BindView(R.id.llGroupPart2)
    LinearLayout mLlGroupPart2;
    @BindView(R.id.oivClearMsgRecord)
    OptionItemView mOivClearMsgRecord;
    @BindView(R.id.btnQuit)
    Button mBtnQuit;
    private boolean mIsTop;

    @Override
    protected void init() {
        Intent intent = getIntent();
        mSessionId = intent.getStringExtra("sessionId");
        mSessionType = intent.getIntExtra("sessionType", SESSION_TYPE_PRIVATE);
        mIsTop = intent.getBooleanExtra(AppConst.CONVERSATION_IS_TOP, false);
        switch (mSessionType) {
            case SESSION_TYPE_PRIVATE:
                mConversationType = Conversation.ConversationType.PRIVATE;
                break;
            case SESSION_TYPE_GROUP:
                mConversationType = Conversation.ConversationType.GROUP;
                break;
            default:
                break;
        }
        registerBR();
    }


    private void registerBR() {
        BroadcastManager.getInstance(this).register(AppConst.UPDATE_GROUP_MEMBER, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String groupId = intent.getStringExtra("String");
                if (mSessionId.equalsIgnoreCase(groupId)) {
                    mPresenter.loadMembers();
                }
            }
        });
    }

    private void unRegisterBR() {
        BroadcastManager.getInstance(this).unregister(AppConst.UPDATE_GROUP_MEMBER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBR();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_session_info;
    }

    @Override
    protected SessionInfoAtPresenter createPresenter() {
        return new SessionInfoAtPresenter(this,mSessionId,mConversationType);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        switch (mSessionType) {
            case SESSION_TYPE_PRIVATE:
                mLlGroupPart1.setVisibility(View.GONE);
                mLlGroupPart2.setVisibility(View.GONE);
                mBtnQuit.setVisibility(View.GONE);
                mTvToolbarTitle.setText(R.string.title_single_cheat);
                break;
            case SESSION_TYPE_GROUP:
                mLlGroupPart1.setVisibility(View.VISIBLE);
                mLlGroupPart2.setVisibility(View.VISIBLE);
                mBtnQuit.setVisibility(View.VISIBLE);
                mTvToolbarTitle.setText(R.string.title_group_cheat);
                break;
            default:
                break;
        }
        // 设置默认的置顶消息
        mSbToTop.setChecked(mIsTop);
    }

    @Override
    protected void initData() {
        mPresenter.loadMembers();
        mPresenter.loadOtherInfo(mSessionType, mSessionId);
    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mOivGroupName.setOnClickListener(v -> {
            Intent intent = new Intent(SessionInfoActivity.this, SetGroupNameActivity.class);
            intent.putExtra("groupId", mSessionId);
            startActivityForResult(intent, REQ_SET_GROUP_NAME);
        });

        mOivQRCordCard.setOnClickListener(v -> {
            Intent intent = new Intent(SessionInfoActivity.this, QRCodeCardActivity.class);
            intent.putExtra(AppConst.EXTRA_GROUP_ID, mSessionId);
            jumpToActivity(intent);
        });

        mOivNickNameInGroup.setOnClickListener(v -> mPresenter.setDisplayName());

        mSbToTop.setOnCheckedChangeListener((buttonView, isChecked) -> RongIMClient.getInstance().setConversationToTop(mConversationType, mSessionId, isChecked, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (isChecked) {
                    UIUtils.showToast(getString(R.string.msg_to_top_success));
                } else {
                    UIUtils.showToast(getString(R.string.msg_not_to_top_success));
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                if (isChecked) {
                    UIUtils.showToast(getString(R.string.msg_to_top_fail));
                } else {
                    UIUtils.showToast(getString(R.string.msg_not_to_top_fail));
                }
            }
        }));

        mSbShowNickName.setOnCheckedChangeListener((buttonView, isChecked) -> mPresenter.changeShowNickName(isChecked));

        mOivClearMsgRecord.setOnClickListener(v -> mPresenter.clearConversationMsg());
        mBtnQuit.setOnClickListener(v -> mPresenter.quit());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> selectedIds;
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQ_ADD_MEMBERS:
                     selectedIds = data.getStringArrayListExtra("selectedIds");
                    mPresenter.addGroupMember(selectedIds);
                    break;
                case REQ_REMOVE_MEMBERS:
                    selectedIds = data.getStringArrayListExtra("selectedIds");
                    mPresenter.deleteGroupMembers(selectedIds);
                    break;
                case REQ_SET_GROUP_NAME:
                    String groupName = data.getStringExtra("group_name");
                    mOivGroupName.setRightText(groupName);
                case REQ_CRETE_GROUP:
                    selectedIds = data.getStringArrayListExtra("selectedIds");
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public RecyclerView getRvMember() {
        return mRvMember;
    }

    @Override
    public OptionItemView getOivGroupName() {
        return mOivGroupName;
    }

    @Override
    public OptionItemView getOivNickNameInGroup() {
        return mOivNickNameInGroup;
    }

    @Override
    public SwitchButton getSbToTop() {
        return mSbToTop;
    }

    @Override
    public SwitchButton getSbShowNickView() {
        return mSbShowNickName;
    }

    @Override
    public Button getBtnQuit() {
        return mBtnQuit;
    }
}

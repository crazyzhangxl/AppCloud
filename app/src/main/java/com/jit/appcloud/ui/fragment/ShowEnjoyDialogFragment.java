package com.jit.appcloud.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.cache.UserNormalCache;
import com.jit.appcloud.model.request.UserInfoUpRequest;
import com.jit.appcloud.ui.activity.cultivate.UpdateList;
import com.jit.appcloud.ui.adapter.decoration.GridSpacingItemDecoration;
import com.jit.appcloud.util.UIUtils;
import com.silencedut.router.Router;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/5/22.
 *         discription:弹出的喜欢的Fragment
 */

public class ShowEnjoyDialogFragment extends DialogFragment{
    private RecyclerView mRv;
    private TextView mTVSubmit;
    private TextView mTvFive;
    private List<String> mList = new ArrayList<>();
    private List<Integer> mSelected = new ArrayList<>();

    private BaseQuickAdapter<String,BaseViewHolder> mBaseAdapter;
    private String mResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.fragment_dialog_show_enjoy);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 2/ 3;
        window.setAttributes(lp);
        getDataFromBefore();
        initView(dialog);
        // 窗口初始化后 请求网络数据
        return dialog;
    }

    private void getDataFromBefore() {
        mList = Arrays.asList(AppConst.ENJOY_ARRAYS);
        Bundle bundle = getArguments();
        if (bundle != null){
            ArrayList<String> enjoy = bundle.getStringArrayList(AppConst.HOBBY_KEY_ENJOY);
            for (String str:enjoy){
                mSelected.add(mList.indexOf(str));
            }
        }
    }

    private void initView(Dialog dialog) {
        mRv =  (RecyclerView)dialog.findViewById(R.id.rv);
        mTVSubmit = (TextView) dialog.findViewById(R.id.tvBtn);
        mTvFive =(TextView)dialog.findViewById(R.id.tvFive);
        setAdapter();
        setListener();

    }

    private void setListener() {
        mTVSubmit.setOnClickListener(v -> {
            if (mSelected.size() == 0){
                UIUtils.showToast("客官请至少选择一个兴趣");
                return;
            }
            StringBuilder sb = new StringBuilder();
            ArrayList<String> mArrays = new ArrayList<>();
            for (Integer integer:mSelected){
                mArrays.add(mList.get(integer));
                sb.append(mList.get(integer)).append(",");
            }
            mResult = sb.toString();
          UserInfoUpRequest userInfoUpRequest = UserNormalCache.getUserInfoUpRequest();
            userInfoUpRequest.setHobby(mResult);
            ApiRetrofit.getInstance().updateUserInfo(UserCache.getToken()
                    ,userInfoUpRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userUpdateInfoResponse -> {
                        if (userUpdateInfoResponse.getCode() == 1){
                            UserNormalCache.setHobby(mResult);
                            Router.instance().getReceiver(UpdateList.class).updateFlowList(mArrays);
                            dismiss();
                        }else {
                            UIUtils.showToast(userUpdateInfoResponse.getMsg());
                        }
                    }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
        });

    }

    private void setAdapter() {
        if (mBaseAdapter == null) {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
            mRv.setLayoutManager(layoutManager);
            mRv.addItemDecoration(new GridSpacingItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.common_dimension_10), true));
            mBaseAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_enjoy_text,mList) {
                @Override
                protected void convert(BaseViewHolder helper, String item) {
                    int position = helper.getAdapterPosition();
                    TextView tvEnjoy = helper.getView(R.id.tvEnjoy);
                    if (mSelected != null&&mSelected.size()!=0 && mSelected.contains((Integer)position)){
                        tvEnjoy.setTextColor(UIUtils.getColor(R.color.red0));
                        tvEnjoy.setBackground(UIUtils.getDrawable(R.drawable.bg_enjoy_pink));
                    }
                    tvEnjoy.setText(item);
                    tvEnjoy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSelected != null && mSelected.size()<=5){
                                if (mSelected.contains(position)){
                                    mSelected.remove((Integer)position);
                                    tvEnjoy.setTextColor(UIUtils.getColor(R.color.black));
                                    tvEnjoy.setBackground(UIUtils.getDrawable(R.drawable.bg_enjoy_normal));
                                }else {
                                    if (mSelected.size() == 5) {

                                        UIUtils.showToast("不超过5个");
                                        return;
                                    }
                                    mSelected.add(position);
                                    tvEnjoy.setTextColor(UIUtils.getColor(R.color.red0));
                                    tvEnjoy.setBackground(UIUtils.getDrawable(R.drawable.bg_enjoy_pink));
                                }
                            }
                            if (mSelected.size() == 5){
                                mTvFive.setVisibility(View.VISIBLE);
                            }else {
                                mTvFive.setVisibility(View.GONE);
                            }
                        }
                    });

                }
            };
        }
        if (mSelected.size() == 5) {
            mTvFive.setVisibility(View.VISIBLE);
        }
        mRv.setAdapter(mBaseAdapter);
    }
}

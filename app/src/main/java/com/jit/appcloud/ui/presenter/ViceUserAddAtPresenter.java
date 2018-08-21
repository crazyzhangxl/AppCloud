package com.jit.appcloud.ui.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.FarmerNameBean;
import com.jit.appcloud.manager.BroadcastManager;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.request.AllocateLookUserRequest;
import com.jit.appcloud.ui.activity.me.ViceUserAddActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IViceUserAddView;
import com.jit.appcloud.util.UIUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/5/19.
 *         discription:添加经销商的Presenter
 */

public class ViceUserAddAtPresenter extends BasePresenter<IViceUserAddView> {

    public ViceUserAddAtPresenter(BaseActivity context) {
        super(context);
    }


    public void addViceUser(){
        String strUserName = getView().getUserName().getText().toString().trim();
        if (TextUtils.isEmpty(strUserName)) {
            UIUtils.showToast(mContext.getString(R.string.please_user_name));
            return;
        }
        String strRealName =  getView().getUserRealName().getText().toString().trim();
        if (TextUtils.isEmpty(strRealName)) {
            UIUtils.showToast(mContext.getString(R.string.please_real_name));
            return;
        }

        String strPwd = getView().getPwdEt().getText().toString().trim();
        if (TextUtils.isEmpty(strPwd)){
            UIUtils.showToast(mContext.getString(R.string.please_in_pwd));
            return;
        }
        String strCfPwd = getView().getPwdCfEt().getText().toString().trim();
        if (TextUtils.isEmpty(strCfPwd)){
            UIUtils.showToast(mContext.getString(R.string.please_in_cf_pwd));
            return;
        }

        if (!strCfPwd.equals(strPwd)){
            UIUtils.showToast(mContext.getString(R.string.please_in_two_error));
            getView().getPwdEt().setText("");
            getView().getPwdCfEt().setText("");
            return;
        }

        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(mContext.getWindow().getDecorView().getWindowToken(), 0);
        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));

        ApiRetrofit.getInstance().getAllocateUserCode(UserCache.getToken(),
                new AllocateLookUserRequest(strUserName,strRealName,strPwd))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(allocateUserResponse -> {
                    mContext.hideWaitingDialog();
                    if (allocateUserResponse.getCode() == 1){
                        BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_VICE_INFO);
                        mContext.finish();
                    }else {
                        UIUtils.showToast(allocateUserResponse.getMsg());
                    }
                }, throwable -> {
                    mContext.hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                });
    }
}

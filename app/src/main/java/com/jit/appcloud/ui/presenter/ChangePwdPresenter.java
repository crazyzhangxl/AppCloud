package com.jit.appcloud.ui.presenter;

import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.request.ChangePwdRequest;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IChangePwdAtView;
import com.jit.appcloud.util.UIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author zxl on 2018/5/19.
 *         discription: 修改密码的presenter
 */

public class ChangePwdPresenter extends BasePresenter<IChangePwdAtView> {

    public ChangePwdPresenter(BaseActivity context) {
        super(context);
    }

    public void changePwd(){
        String oldPwd =  getView().getOldPwdText().getText().toString().trim();
        String newPwd = getView().getConfirmPwdText().getText().toString().trim();
        ApiRetrofit.getInstance().getChangePwdCode(UserCache.getToken(),new ChangePwdRequest(oldPwd,newPwd))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(changePwdResponse -> {
                    if (changePwdResponse.getCode() == 1){
                        UIUtils.showToast(UIUtils.getString(R.string.str_change_pwd_success));
                        mContext.finish();
                    }else {
                        UIUtils.showToast(changePwdResponse.getMsg());
                    }
                }, throwable -> UIUtils.showToast(throwable.getLocalizedMessage()));
    }
}

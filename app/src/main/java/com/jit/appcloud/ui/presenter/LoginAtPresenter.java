package com.jit.appcloud.ui.presenter;

import android.text.TextUtils;

import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.db.db_model.FeedMeal;
import com.jit.appcloud.db.db_model.SeedMeal;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.request.LoginRequest;
import com.jit.appcloud.ui.activity.MainActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.ILoginAtView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * @author 张先磊
 * @date 2018/5/10
 */

public class LoginAtPresenter extends BasePresenter<ILoginAtView> {
    public LoginAtPresenter(BaseActivity context) {
        super(context);
    }

    public void login(){
        String userName = getView().getUserNameText().getText().toString();
        String userPsd = getView().getUserPsdText().getText().toString();
        if (TextUtils.isEmpty(userName)){
            getView().getRealName().setBackground(UIUtils.getDrawable(R.drawable.bg_border_color_cutmaincolor));
            getView().getUserNameIcon().setAnimation(UIUtils.shakeAnimation(2));
            return;
        }

        if (TextUtils.isEmpty(userPsd)){
            getView().getRealPsd().setBackground(UIUtils.getDrawable(R.drawable.bg_border_color_cutmaincolor));
            getView().getUserPsdIcon().setAnimation(UIUtils.shakeAnimation(2));
            return;
        }

        mContext.showWaitingDialog("请稍等...");
        ApiRetrofit.getInstance().getLoginCode(new LoginRequest(userName,userPsd))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataBeanUserLoginResponse -> {
                    mContext.hideWaitingDialog();
                    if (dataBeanUserLoginResponse.getCode() == 1){
                        /*  这里是有缺口的 */
                        LogUtils.e("身份", dataBeanUserLoginResponse.getData().getRole());
                        LogUtils.e("身份",String.valueOf(dataBeanUserLoginResponse.getData().getUser_id()));
                        LogUtils.e("身份",String.valueOf(dataBeanUserLoginResponse.getData().getRong_token()));

                        UserCache.save(String.valueOf(dataBeanUserLoginResponse.getData().getUser_id()),
                                dataBeanUserLoginResponse.getData().getUsername(),
                                dataBeanUserLoginResponse.getData().getRole(),
                                "Bearer "+ dataBeanUserLoginResponse.getData().getToken(),
                                dataBeanUserLoginResponse.getData().getRong_token(),
                                TimeUtil.getMyTime(dataBeanUserLoginResponse.getData().getRegister_time()),userPsd,
                                dataBeanUserLoginResponse.getData().getImage());
                        if (AppConst.ROLE_FARMER.equals(dataBeanUserLoginResponse.getData().getRole())){
                            DBManager.getInstance().saveOrUpdateFeedMeal(
                                    new FeedMeal("套餐A","虾类","大豆牌","豆柏",3.5,"斤"));
                            DBManager.getInstance().saveOrUpdateFeedMeal(
                                    new FeedMeal("套餐B","鱼类","大豆牌","豆柏",3.5,"斤"));
                            DBManager.getInstance().saveOrUpdateSeedMeal(
                                    new SeedMeal("套餐A","虾类","品牌1","水草",3.5,"斤"));
                            DBManager.getInstance().saveOrUpdateSeedMeal(
                                    new SeedMeal("套餐B","鱼类","品牌2","金龙鱼",3.5,"斤"));
                        }
                        mContext.jumpToActivity(MainActivity.class);
                        mContext.finish();
                    }else{
                        UIUtils.showToast(dataBeanUserLoginResponse.getMsg());
                        getView().getUserPsdText().setText("");
                        getView().getUserNameText().setText("");
                    }
                },this::loginError);
    }

    private void loginError(Throwable throwable) {
        mContext.hideWaitingDialog();
        UIUtils.showToast(throwable.getLocalizedMessage());
    }
}

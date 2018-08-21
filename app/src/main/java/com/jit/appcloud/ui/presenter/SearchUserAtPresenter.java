package com.jit.appcloud.ui.presenter;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.ui.activity.message.UserInfoBySearchActivity;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.ISearchUserAtView;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.UIUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.model.UserInfo;

/**
 * @author zxl on 2018/6/11.
 *         discription: 搜索用于进行添加的活动
 */

public class SearchUserAtPresenter extends BasePresenter<ISearchUserAtView> {
    public SearchUserAtPresenter(BaseActivity context) {
        super(context);
    }

    public void searchUser(){
        String content = getView().getEtSearchContent().getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            UIUtils.showToast(UIUtils.getString(R.string.content_no_empty));
            return;
        }

        if (UserCache.getName().equals(content)){
            UIUtils.showToast("这不就是你自己嘛");
            getView().getEtSearchContent().setText("");
            return;
        }

        mContext.showWaitingDialog(UIUtils.getString(R.string.str_please_waiting));
        ApiRetrofit.getInstance().searchFriend(UserCache.getToken(),content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchFriendResponse -> {
                    mContext.hideWaitingDialog();
                    if (searchFriendResponse.getCode() == 1){
                        UserInfo userInfo = new UserInfo(String.valueOf(searchFriendResponse.getData().getId()),searchFriendResponse.getData().getUsername(), searchFriendResponse.getData().getImage()== null?null:Uri.parse(searchFriendResponse.getData().getImage()));
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(AppConst.EXTRA_FRIEND_INFO,userInfo);
                        bundle.putString(AppConst.EXTRA_SIGNATURE,searchFriendResponse.getData().getSign() == null ? "":searchFriendResponse.getData().getSign());
                        bundle.putString(AppConst.EXTRA_AREA,searchFriendResponse.getData().getProvince() == null ? "":
                                String.format(UIUtils.getString(R.string.str_format_city_bond),searchFriendResponse.getData().getProvince()
                                        ,searchFriendResponse.getData().getCity()
                                        ,searchFriendResponse.getData().getCountry()
                                ));
                        mContext.jumpToActivity(UserInfoBySearchActivity.class,bundle);

                    }else {
                        getView().getRlNoResultTip().setVisibility(View.VISIBLE);
                        getView().getLlSearch().setVisibility(View.GONE);
                    }
                }, throwable -> {
                    mContext.hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());
                    getView().getRlNoResultTip().setVisibility(View.VISIBLE);
                    getView().getLlSearch().setVisibility(View.GONE);
                });
        // tips: 根据用户名进行用户的搜索
    }
}

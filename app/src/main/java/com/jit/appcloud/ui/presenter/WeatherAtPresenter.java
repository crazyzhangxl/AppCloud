package com.jit.appcloud.ui.presenter;

import android.widget.Toast;

import com.jit.appcloud.api.HeWeatherRetrofit;
import com.jit.appcloud.model.response.HeWeatherResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.IWeatherAtView;
import com.google.gson.Gson;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 张先磊 on 2018/4/23.
 */

public class WeatherAtPresenter extends BasePresenter<IWeatherAtView> {
    public WeatherAtPresenter(BaseActivity context) {
        super(context);
    }

    public void queryWeatherStatue(String districtSuffixCity){
        HeWeatherRetrofit.getInstance().getWeath(districtSuffixCity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                if (o.toString().length()>100){
                    HeWeatherResponse heWeatherResponse = new Gson().fromJson
                            (new Gson().toJson(o), HeWeatherResponse.class);
                    getView().querySuccessful(heWeatherResponse);
                }else if (o.toString().contains("unknown city")){
                    Toast.makeText(mContext, "您输入的地区还没提供天气服务", Toast.LENGTH_SHORT).show();
                    getView().queryFailed();
                }else {
                    Toast.makeText(mContext, "其他返回错误", Toast.LENGTH_SHORT).show();
                    getView().queryFailed();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

}

package com.jit.appcloud.api;

import com.jit.appcloud.api.base.BaseApiRetrofit;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author zxl
 * @date 2018/4/23
 */

public class HeWeatherRetrofit extends BaseApiRetrofit {
    public HeWeatherApi mApi;
    public static HeWeatherRetrofit mInstance;
    private HeWeatherRetrofit(){
        //在构造方法中完成对Retrofit接口的初始化

        mApi = new Retrofit.Builder()
                .baseUrl(HeWeatherApi.BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(HeWeatherApi.class);
    }

    public static HeWeatherRetrofit getInstance(){
        if (mInstance == null){
            synchronized (HeWeatherRetrofit.class){
                if (mInstance == null){
                    mInstance = new HeWeatherRetrofit();
                }
            }
        }
        return  mInstance;
    }

    public Observable<Object> getWeath(String districtSuffixCity){
        return mApi.getWeather(HeWeatherApi.sHeyWeatherKey,districtSuffixCity);
    }


}

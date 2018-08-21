package com.jit.appcloud.api.base;



import com.jit.appcloud.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 *
 * @author zxl
 * @date 2018/4/10
 */

public class BaseApiRetrofit {
    private final OkHttpClient mClient;

    protected OkHttpClient getClient() {
        return mClient;
    }

    protected BaseApiRetrofit(){
        //OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }
        mClient = builder.build();
    }

}

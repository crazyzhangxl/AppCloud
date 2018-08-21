package com.jit.appcloud.api;
import com.jit.appcloud.api.base.BaseApi2Retrofit;
import com.jit.appcloud.model.request.TokenBean;
import com.jit.appcloud.model.response.RongTokenResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author zxl
 * @date 2018/4/26
 */

public class RongCloudRetrofit extends BaseApi2Retrofit {

    public RongCloudApi mApi;
    public static RongCloudRetrofit mInstance;
    private RongCloudRetrofit(){
        //在构造方法中完成对Retrofit接口的初始化

        mApi = new Retrofit.Builder()
                .baseUrl(RongCloudApi.BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RongCloudApi.class);
    }

    public static RongCloudRetrofit getInstance(){
        if (mInstance == null){
            synchronized (RongCloudRetrofit.class){
                if (mInstance == null){
                    mInstance = new RongCloudRetrofit();
                }
            }
        }
        return  mInstance;
    }
        public Observable<RongTokenResponse> getRongToken(TokenBean mBean){
        return mApi.getRongToken(getRongTokenRequestBy(mBean));
    }

    private Map<String,String> getRongTokenRequestBy(TokenBean mBean){
            Map<String,String > params = new HashMap<>();
            params.put("userId",mBean.userId);
            params.put("name",mBean.name);
            params.put("portraitUri",mBean.portraitUri);
            return params;
    }

    private RequestBody getRequestBody(Object obj) {
        String route = new Gson().toJson(obj);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),route);
        return body;
    }
}

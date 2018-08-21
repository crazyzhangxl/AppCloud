package com.jit.appcloud.api;


import com.jit.appcloud.model.response.RongTokenResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 *
 * @author zxl
 * @date 2018/4/26
 */

public interface RongCloudApi {
    String BASE_URL = "https://api.cn.ronghub.com/";

    /**
     * 获得token 之后验证是否可从本地服务器获取

     * @param params 参数列表
     * @return
     */
    @FormUrlEncoded
    @POST("user/getToken.json")
    Observable<RongTokenResponse> getRongToken(@FieldMap Map<String,String> params);
}

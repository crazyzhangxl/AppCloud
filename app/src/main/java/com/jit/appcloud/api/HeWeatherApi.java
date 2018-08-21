package com.jit.appcloud.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * @author zxl
 * @date 2018/4/23
 */

public interface HeWeatherApi {
    String BASE_URL = "https://free-api.heweather.com/s6/";
    String sHeyWeatherKey = "db2c63dc76b442a0b60cc6a4718b6359";

    @GET("weather")
    Observable<Object> getWeather(@Query("key") String key, @Query("location") String location);
}

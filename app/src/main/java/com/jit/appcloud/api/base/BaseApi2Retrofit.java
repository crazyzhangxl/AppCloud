package com.jit.appcloud.api.base;




import com.jit.appcloud.BuildConfig;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 *
 * @author zxl
 * @date 2018/4/10
 */

public class BaseApi2Retrofit {
    private final OkHttpClient mClient;

    protected OkHttpClient getClient() {
        return mClient;
    }

    protected BaseApi2Retrofit(){
        //OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
            builder.addInterceptor(REWRITE_HEADER_CONTROL_INTERCEPTOR);
        }
        mClient = builder.build();
    }

    /**
     *  head配置
     */
    Interceptor REWRITE_HEADER_CONTROL_INTERCEPTOR = chain -> {
        String key = "vnroth0kv8oao";
        String keySecret = "T2EICRilLhknIJ";
        String data = String.valueOf(System.currentTimeMillis()/1000);
        String random = String.valueOf(Math.floor(Math.random() * 1000000));
        String shaHex =  shaEncrypt(keySecret+random+data);
        Request request = chain.request()
                .newBuilder()
               .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("App-Key",key)
                .addHeader("Nonce",random)
                .addHeader("Timestamp",data)
                .addHeader("Signature",shaHex)
                .build();
        return chain.proceed(request);
    };


    private String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    private String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}

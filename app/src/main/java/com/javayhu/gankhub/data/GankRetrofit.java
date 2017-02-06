package com.javayhu.gankhub.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.javayhu.gankhub.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Gank retrofit
 * <p>
 * Created by javayhu on 1/21/17.
 */
public class GankRetrofit {

    private static final String BASE_URL = "http://gank.io/api/";
    private static GankService sGankService;

    public static GankService getGankService() {
        if (sGankService == null) {
            Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

            OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
            clientBuilder.readTimeout(10, TimeUnit.SECONDS);
            clientBuilder.connectTimeout(9, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {//开发调试期间打印okhttp的日志输出http请求和响应内容
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilder.addInterceptor(interceptor);
            }

            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            sGankService = retrofit.create(GankService.class);
        }
        return sGankService;
    }

}

package com.sugarya.closet.Data;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {

    private static final String URL_TEST = "http://192.168.156.53:8031/";

    private static Retrofit retrofit;
    private static NetworkHelper helper;

    private NetworkHelper() {

    }

    public static NetworkHelper getHelper() {
        if (helper == null) {
            helper = new NetworkHelper();
        }
        return helper;
    }

    public Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_TEST)
                    .addConverterFactory(GsonConverterFactory.create(GsonHelper.getGson()))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        return retrofit;
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.addInterceptor(getLogInterceptor());
//        builder.addNetworkInterceptor(getHeadOperateInterceptor());
        builder.addNetworkInterceptor(new StethoInterceptor());
        builder.retryOnConnectionFailure(true);
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        return builder.build();
    }

    //输出http交互内容
//    private HttpLoggingInterceptor getLogInterceptor() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(ConfigProvider.getConfigInstance().isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
//        return interceptor;
//    }

    //添加请求头
//    private Interceptor getHeadOperateInterceptor() {
//        return new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request.Builder builder = chain.request().newBuilder();
//                for (String key : HeaderHelper.getInstance().getHeaders().keySet()) {
//                    builder.addHeader(key, HeaderHelper.getInstance().getHeaders().get(key));
//                }
//                return chain.proceed(builder.build());
//            }
//        };
//    }


}

package com.thxy.skytalk_client.factory.retrofit;

import android.text.TextUtils;
import com.thxy.skytalk_client.factory.Factory;
import com.thxy.skytalk_client.factory.Account;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit简单的封装类
 */

public class RetrofitUtil {

    private static final String BASE_URL="http://120.79.130.236:8080/";//120.79.130.236

    private RemoteService mRemoteService;

    private static RetrofitUtil mInstance;

    /**
     *  私有构造方法
     */
    private RetrofitUtil(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                if (!TextUtils.isEmpty(Account.getToken())) {
                    builder.addHeader("token",Account.getToken());
                }
                builder.addHeader("Content-Type","application/json");
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
        mRemoteService = mRetrofit.create(RemoteService.class);

    }

    public static RemoteService getApiService(){
        if (mInstance == null){
            synchronized (RetrofitUtil.class){
                if (mInstance == null) {
                    mInstance = new RetrofitUtil();
                }
            }
        }
        return mInstance.mRemoteService;
    }
}




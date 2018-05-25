package com.ltt.overseasuser.http;

import com.ltt.overseasuser.XApplication;
import com.ltt.overseasuser.base.Constants;
import com.ltt.overseasuser.http.convert.FastJsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by Administrator on 2016/5/20.
 */
public class RetrofitUtil {
    private static APIService mAPIService;

    public static APIService getAPIService() {
        if (mAPIService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(RetrofitUtil.genericClient())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            mAPIService = retrofit.create(APIService.class);
        }
        return mAPIService;
    }


    public static OkHttpClient genericClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(mTokenInterceptor)
                .build();
        return httpClient;
    }

    static Interceptor mTokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request authorised = originalRequest.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", getAuthorzation())
                    .build();
            return chain.proceed(authorised);
        }
    };

    private static String getAuthorzation(){
        if (XApplication.globalUserBean == null || XApplication.globalUserBean.getAccess_token() == null){
            return "";
        }else{
//            return Constants.GET_MESSAGELIST;
            return "Bearer " + XApplication.globalUserBean.getAccess_token();
        }
    }

}

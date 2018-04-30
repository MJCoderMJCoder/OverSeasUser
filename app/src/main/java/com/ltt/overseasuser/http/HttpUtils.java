package com.ltt.overseasuser.http;


import com.ltt.overseasuser.core.GlobalUser;
import com.ltt.overseasuser.utils.L;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
import java.util.List;

import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class HttpUtils {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String TAG = "HttpUtils";

    private static Retrofit retrofit;

    public static void init(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(genericClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Retrofit get() {
        if (retrofit == null)
            throw new ExceptionInInitializerError();
        return retrofit;
    }

    private static OkHttpClient genericClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List cookies) {
                        L.e("save cookies:", cookies.toString());
                    }

                    @Override
                    public List loadForRequest(HttpUrl url) {
                        return Collections.emptyList();
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original;
                        if (original.body() instanceof FormBody) {
                            FormBody.Builder newFormBody = new FormBody.Builder();
                            FormBody originalBody = (FormBody) original.body();
                            for (int i = 0; i < originalBody.size(); i++)
                                newFormBody.addEncoded(originalBody.encodedName(i), originalBody.encodedValue(i));
//                            if(GlobalUser.getInstance().getUserId()!=null&&GlobalUser.getInstance().getToken()!=null){
//                                newFormBody.add("user_id", GlobalUser.getInstance().getUserId());
//                                newFormBody.add("token", GlobalUser.getInstance().getToken());
//                            }
                            request = original.newBuilder()
                                    .addHeader("bearer",GlobalUser.getInstance().getToken())
                                    .method(original.method(), newFormBody.build()).build();
                        } else {
                            request = original.newBuilder().addHeader("bearer",GlobalUser.getInstance().getToken())
                                    .url(original.url().newBuilder()
                                    .build()).build();
                        }
                        Response response = chain.proceed(request);
                        log(request, response);
                        return response;
                    }

                })
                .build();
        return okHttpClient;
    }

    private static void log(Request request, Response response) {
        L.e(TAG, "--> " + request.method() + ' ' + request.url());
        try {
            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    L.e(TAG, "Couldn't decode the response body; charset is likely malformed.");
                    return;
                }
            }
            if (contentLength != 0) {
                L.e(TAG, "<-- " + buffer.clone().readString(charset));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

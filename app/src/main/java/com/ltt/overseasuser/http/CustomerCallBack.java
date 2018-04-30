package com.ltt.overseasuser.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.google.gson.Gson;
import com.ltt.overseasuser.XApplication;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.utils.GenericsUtils;
import com.ltt.overseasuser.utils.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/5/21.
 */
public abstract class CustomerCallBack<T> implements Callback<T> {
    private Class<T> returnType;
    private static final int error = 1;
    private String errorMessage = null;
    private boolean isNetError = false;

    public CustomerCallBack(){
        returnType = GenericsUtils.getSuperClassGenricType(CustomerCallBack.class, 0);
    }

    public static Handler httpHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case error:
                    ToastUtils.showToast((String) msg.obj);
                    break;
            }
        }
    };

    public abstract void onResponseResult(T response);
    public abstract void onResponseError(BaseBean errorMessage, boolean isNetError);

    BaseBean errBaseData;
    @Override
    public void onResponse(Call<T> call, final Response<T> response) {
        if (response.isSuccessful()) {
            final int responseCode = response.raw().code();
            if (responseCode < 400){
                if(response.body() != null){
                    final String responseStr = response.body().toString();
                    Log.d("retJson", "---------------" + responseStr);
                    //如果返回类型未设置，直接返回 HttpResponseBean
                    if (returnType == null) {
                        errorMessage = "未设置返回类型";
                        return;
                    }
                    try {
                        httpHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                onResponseResult(response.body());
                            }
                        });
                    } catch (JSONException e) {
                        errorMessage = e.getMessage();
                    } catch (Exception e) {
                        errorMessage = e.getMessage();
                    }
                }else{
                    try {
                        httpHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                onResponseResult((T) "成功");
                            }
                        });
                    } catch (JSONException e) {
                        errorMessage = e.getMessage();
                    } catch (Exception e) {
                        errorMessage = e.getMessage();
                    }
                }
            }else{
                if (responseCode == 401){
                    XApplication.getApplication().authHandler.sendEmptyMessage(1);
                    return;
                }
            }
        } else {
            if (response.code() == 401){
                XApplication.getApplication().authHandler.sendEmptyMessage(1);
                return;
            }
            String msg = "";
            try {
                msg = inputStreamTOString(response.errorBody().byteStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(msg.equals("")){
                msg = response.code() + "" + response.raw().message();
            }else{
                Log.d("LD","返回数据："+msg);
                try {
                    BaseBean resBean = new Gson().fromJson(msg, BaseBean.class);
                    if(resBean.getMsg() instanceof String){
                        msg = (String) resBean.getMsg();
                    }else {
                        errBaseData = resBean;
                    }
                }catch (Exception e){

                }
            }
            Log.d("retJson", "---------------" + msg);
            isNetError = true;
            errorMessage = msg;
        }
        if (!TextUtils.isEmpty(errorMessage)) {
            httpHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(errBaseData == null){
                        ToastUtils.showToast(errorMessage);
                    }
                    onResponseError(errBaseData, isNetError);
                }
            });
        }
    }

    final static int BUFFER_SIZE = 4096;
    public static String inputStreamTOString(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(),"UTF-8");
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("retJson", "onFailure"+t.getMessage());
        String msg = t.getMessage();
        httpHandler.post(new Runnable() {
            @Override
            public void run() {
                errorMessage = "网络异常请稍后重试";
                ToastUtils.showToast(errorMessage);
                onResponseError(null, true);
            }
        });
    }

}

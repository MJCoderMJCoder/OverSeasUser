package com.ltt.overseasuser.http.convert;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by zhangbowen on 2016/5/13.
 */
public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Type type;

    public FastJsonResponseBodyConverter(Type type) {
        this.type = type;
    }

    /*
    * 转换方法
    */
    @Override
    public T convert(ResponseBody value) throws IOException {
        byte[] bytes = value.bytes();
        if(type == String.class){
            return (T) new String(bytes,"utf-8");
        }
        try {
            return JSON.parseObject(bytes, type);
        }catch (Exception e){
            return (T) bytes;
        }
    }
}

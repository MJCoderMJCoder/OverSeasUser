package com.ltt.overseasuser.http;

import com.ltt.overseasuser.model.GsonUserBean;
import com.ltt.overseasuser.model.LoginBean;
import com.ltt.overseasuser.model.PhoneListBean;
import com.ltt.overseasuser.model.UserBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/5/20.
 */
public interface APIService {

    //Login
    @POST("auth/login")
    Call<GsonUserBean> login(@Body LoginBean userParams);

    //https://dev.popmach.com/api/auth/forget
    @POST("auth/forget")
    Call<GsonUserBean> forget(@Body UserBean userParams);

    @POST("auth/register")
    Call<GsonUserBean> register(@Body UserBean userParams);

    //Get profile
    @GET("user")
    Call<GsonUserBean> getProfile();

    //Update profile
    @POST("user/update_profile")
    Call<String> modifyProfile(@Body UserBean userParams);

    //Get Country id
    @GET("country/phone_list")
    Call<PhoneListBean> getCountryIds();


//
//    //
//    @PUT("users/changePwd")
//    Call<String> changePwd(@Body UserParams userParams);

//    //
//    @DELETE("address/{addressId}")
//    Call<String> delAdddress(@Path("addressId") String addressId);

}
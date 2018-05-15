package com.ltt.overseasuser.http;

import com.ltt.overseasuser.model.GsonUserBean;
import com.ltt.overseasuser.model.LoginBean;
import com.ltt.overseasuser.model.MessageListBean;
import com.ltt.overseasuser.model.PhoneListBean;
import com.ltt.overseasuser.model.PreferenceListBean;
import com.ltt.overseasuser.model.QuestionBean;
import com.ltt.overseasuser.model.QuestionDataBean;
import com.ltt.overseasuser.model.SectionListBean;
import com.ltt.overseasuser.model.TypeListBean;
import com.ltt.overseasuser.model.TypeSectionBean;
import com.ltt.overseasuser.model.UserBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/5/20.
 */
public interface APIService {

    //Login
    @POST("auth/login")
    Call<GsonUserBean> login(@Body LoginBean userParams);

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
    //Get message list
    @GET("service/message/user")
    Call<MessageListBean> getMessageLists(@Query("page") int page);

    @POST("auth/forget")
    Call<String> forgetPwd(@Body UserBean userParams);

    //GET List all type
    @GET("service/main/list_type")
    Call<TypeListBean> getTypeList();

    //Get Country id
    @GET("service/main/list_section/{type_id}")
    Call<SectionListBean> getSectionList(@Path("type_id") String typeId);

    //Get question
    @GET("service/main/list_question/{section_id}")
    Call<QuestionDataBean> getQuestionList(@Path("section_id") String sectionid);
//
//    //
//    @PUT("users/changePwd")
//    Call<String> changePwd(@Body UserParams userParams);

//    //
//    @DELETE("address/{addressId}")
//    Call<String> delAdddress(@Path("addressId") String addressId);
    @GET("user/list_preference")
    Call<PreferenceListBean> getPreferenceLists();
    //Get question

}
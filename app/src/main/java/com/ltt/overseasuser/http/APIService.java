package com.ltt.overseasuser.http;

import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.model.CitiesListBean;
import com.ltt.overseasuser.model.CountriesListBean;
import com.ltt.overseasuser.model.GsonUserBean;
import com.ltt.overseasuser.model.LoginBean;
import com.ltt.overseasuser.model.MessageListBean;
import com.ltt.overseasuser.model.MyRequestDetailListBean;
import com.ltt.overseasuser.model.MyRequestListBean;
import com.ltt.overseasuser.model.MyResponseListBean;
import com.ltt.overseasuser.model.PWBean;
import com.ltt.overseasuser.model.PhoneListBean;
import com.ltt.overseasuser.model.PreferenceListBean;
import com.ltt.overseasuser.model.QuestionDataBean;
import com.ltt.overseasuser.model.SectionBean;
import com.ltt.overseasuser.model.SectionInitQuestionBean;
import com.ltt.overseasuser.model.SectionListBean;
import com.ltt.overseasuser.model.SignTokenBean;
import com.ltt.overseasuser.model.StatesListBean;
import com.ltt.overseasuser.model.TypeListBean;
import com.ltt.overseasuser.model.UpdatePWBean;
import com.ltt.overseasuser.model.UploadSucessBean;
import com.ltt.overseasuser.model.UserBean;
import com.ltt.overseasuser.model.UserProfileBean;
import com.ltt.overseasuser.model.ViewRequestBean;
import com.ltt.overseasuser.model.postRequestBean;
import com.ltt.overseasuser.model.updateUserBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/5/20.
 */
public interface APIService {
    //social_auth/facebook/callback social_auth/facebook
    @GET("social_auth/facebook/callback")
    Call<GsonUserBean> callback(@Query("access_token") String access_token);

    //Login
    @POST("auth/login")
    Call<GsonUserBean> login(@Body LoginBean userParams);

    @GET("service/message")
    Call<SignTokenBean> getSignToken();

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

    //Get question
    @POST("service/user/request/init_question")
    Call<QuestionDataBean> getInitQuestionList(@Body SectionInitQuestionBean section, @Header("Authorization") String authorization);
    //Update image audio pdf
    @Multipart
    @POST("service/user/request/upload")
    Call<UploadSucessBean> uploadflie(@Part("upload_id") RequestBody uploadId, @Part("create_request_token") RequestBody requestToken, @Part MultipartBody.Part multipartBody, @Header("Authorization") String authorization);

    @GET("service/user/request")
    Call<MyRequestListBean> getRequestList(@Query("page") String page, @Header("Authorization") String authorization);

    @GET("country")
    Call<CountriesListBean> getCountries();

    @GET("country/getStates")
    Call<StatesListBean> getStates(@Query("country_id") String countryId);

    @GET("country/getCities")
    Call<CitiesListBean> getCities(@Query("state_id") String stateId);
    //Update answer
    @POST("service/user/request/create")
    Call<BaseBean> requestcreate(@Body postRequestBean userParams, @Header("Authorization") String authorization);

    @GET("service/user/request/list_response")
    Call<MyResponseListBean> getResponseList(@Query("request_id") String requestId, @Query("page") String page, @Header("Authorization") String authorization);

    @GET("service/main/view_request/{section_id}")
    Call<MyRequestDetailListBean> getRequestDetail(@Path("section_id") String sectionId);

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
    @GET("user")
    Call<UserProfileBean> getUserProfileLists();

    @POST("user/update_profile")
    Call<BaseBean> updateUserProfileLists(@Body updateUserBean userParams);

    @POST("user/change_password")
    Call<UpdatePWBean> updatePW(@Body PWBean pwParams);


    @GET("service/main/view_request/{section_id}")
    Call<ViewRequestBean> getQuestions(@Path("section_id") String sectionId);

    @GET("social_auth/google")
    Call<GsonUserBean> googlelogin(@Query("id_token") String id_token);
}
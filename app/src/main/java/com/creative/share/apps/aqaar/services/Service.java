package com.creative.share.apps.aqaar.services;


import com.creative.share.apps.aqaar.models.App_Data_Model;
import com.creative.share.apps.aqaar.models.PlaceGeocodeData;
import com.creative.share.apps.aqaar.models.PlaceMapDetailsData;
import com.creative.share.apps.aqaar.models.UserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);
    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("user_phone") String user_phone,
                          @Field("user_phone_code") String phone_code,
                          @Field("user_pass") String user_pass);
    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> signUp(@Field("user_name") String user_name,
                           @Field("user_username") String user_username,
                           @Field("user_email") String user_email,
                           @Field("user_pass") String user_pass,
                           @Field("user_phone") String user_phone,
                           @Field("user_phone_code") String user_phone_code

    );

    @GET("api/terms")
    Call<App_Data_Model> getterms();

    @GET("api/about_us")
    Call<App_Data_Model> getabout();
    @FormUrlEncoded
    @POST("api/contact_us")
    Call<ResponseBody> sendContact(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("subject") String subject,
                                   @Field("message") String message
    );


}



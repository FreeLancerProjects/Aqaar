package com.creative.share.apps.aqaar.services;


import com.creative.share.apps.aqaar.models.AdDataModel;
import com.creative.share.apps.aqaar.models.AllMessageModel;
import com.creative.share.apps.aqaar.models.App_Data_Model;
import com.creative.share.apps.aqaar.models.BankDataModel;
import com.creative.share.apps.aqaar.models.CategoryDataModel;
import com.creative.share.apps.aqaar.models.CityDataModel;
import com.creative.share.apps.aqaar.models.MessageModel;
import com.creative.share.apps.aqaar.models.PlaceGeocodeData;
import com.creative.share.apps.aqaar.models.PlaceMapDetailsData;
import com.creative.share.apps.aqaar.models.TypeDataModel;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.models.UserRoomModelData;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    @FormUrlEncoded
    @POST("api/check_code")
    Call<ResponseBody> confirmCode(@Field("user_id") int user_id,
                                   @Field("confirm_code") String confirm_code
    );

    @FormUrlEncoded
    @POST("api/rest_code")
    Call<ResponseBody> resendCode(@Field("user_id") int user_id
    );
    @GET("api/all_cities")
    Call<CityDataModel> getAllCities();

    @GET("api/terms")
    Call<App_Data_Model> getterms();

    @FormUrlEncoded
    @POST("api/fillter")
    Call<AdDataModel> getAdsByCityAndCategory(@Field("city_id") String city_id,
                                              @Field("lat") String lat,
                                              @Field("lng") String lng,
                                              @Field("category_id") String category_id
    );

    @GET("api/aqarat_category")
    Call<CategoryDataModel> getAllCategories();
    @GET("api/about_us")
    Call<App_Data_Model> getabout();
    @FormUrlEncoded
    @POST("api/contact_us")
    Call<ResponseBody> sendContact(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("subject") String subject,
                                   @Field("message") String message
    );
    @GET("api/banks_accounts")
    Call<BankDataModel> getBanks();
    @Multipart
    @POST("api/user_image")
    Call<UserModel> editUserImage(@Part("user_id") RequestBody user_id,
                                  @Part MultipartBody.Part image);
    @FormUrlEncoded
    @POST("api/allRoms")
    Call<UserRoomModelData> getRooms(@Field("user_id") String user_id
    );
    @FormUrlEncoded
    @POST("api/send_messsage")
    Call<MessageModel> sendmessagetext
            (
                    @Field("send_message_id") String send_message_id,
                    @Field("receive_message_id") String receive_message_id,
                    @Field("message") String message

//
            );
    @FormUrlEncoded
    @POST("api/my_message")
    Call<AllMessageModel> getMessge(
            @Field("receive_message_id") String receive_message_id,
            @Field("send_message_id") String send_message_id
    );

    @GET("api/advertisement_types")
    Call<TypeDataModel> gettype();

    @Multipart
    @POST("api/add_advertisement")
    Call<ResponseBody> Sendorder
            (@Part("user_id") RequestBody user_id,
             @Part("main_cat_id_fk") RequestBody main_cat_id_fk,
             @Part("city") RequestBody city,
             @Part("other_type_sakn") RequestBody other_type_sakn,
             @Part("aqar_title") RequestBody aqar_title,
             @Part("other_aqar_text") RequestBody other_aqar_text,
             @Part("other_metr_price") RequestBody other_metr_price,
             @Part("aqar_makan") RequestBody aqar_makan,
             @Part("aqar_long") RequestBody aqar_long,
             @Part("aqar_lat") RequestBody aqar_lat,
             @Part("other_interface") RequestBody other_interface,
             @Part("other_street_width") RequestBody other_street_width,
             @Part("other_size") RequestBody other_size,

             @Part List<MultipartBody.Part> partimageInsideList

//
            );
    @Multipart
    @POST("api/add_advertisement")
    Call<ResponseBody> Sendorder2
            (@Part("user_id") RequestBody user_id,
             @Part("main_cat_id_fk") RequestBody main_cat_id_fk,
             @Part("city") RequestBody city,
             @Part("type_sakn") RequestBody type_skan,
             @Part("type_aqar") RequestBody type_aqar,

             @Part("aqar_title") RequestBody aqar_title,
             @Part("aqar_text") RequestBody other_aqar_text,
             @Part("metr_price") RequestBody other_metr_price,
             @Part("metr_price_to") RequestBody other_metr_price_to,
             @Part("aqar_makan") RequestBody aqar_makan,
             @Part("aqar_long") RequestBody aqar_long,
             @Part("aqar_lat") RequestBody aqar_lat,
             @Part("type_advertise") RequestBody type_advertise,
             @Part("complement") RequestBody complement,
             @Part("garag") RequestBody garag,
             @Part("egar_peroid") RequestBody egar_peroid,
             @Part("num_bathroom") RequestBody num_bathroom,
             @Part("num_salon") RequestBody num_salon,
             @Part("num_rooms") RequestBody num_rooms,
             @Part("kitchen") RequestBody kitchen,
             @Part("lift") RequestBody lift,
             @Part("aqar_size") RequestBody aqaar_size,
             @Part("aqar_size_to") RequestBody aqaar_size_to,
             @Part List<MultipartBody.Part> partimageInsideList
            );
    @FormUrlEncoded
    @POST("api/my_account")
    Call<UserModel> getmyprofile(
            @Field("user_id") String user_id

    );
}



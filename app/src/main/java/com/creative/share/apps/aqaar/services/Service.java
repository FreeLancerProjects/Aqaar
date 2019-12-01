package com.creative.share.apps.aqaar.services;


import com.creative.share.apps.aqaar.models.AdDataModel;
import com.creative.share.apps.aqaar.models.CategoryDataModel;
import com.creative.share.apps.aqaar.models.CityDataModel;
import com.creative.share.apps.aqaar.models.PlaceGeocodeData;
import com.creative.share.apps.aqaar.models.PlaceMapDetailsData;

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


    @GET("api/all_cities")
    Call<CityDataModel> getAllCities();


    @FormUrlEncoded
    @POST("api/fillter")
    Call<AdDataModel> getAdsByCityAndCategory(@Field("city_id") String city_id,
                                              @Field("lat") String lat,
                                              @Field("lng") String lng,
                                              @Field("category_id") String category_id
    );

    @GET("api/aqarat_category")
    Call<CategoryDataModel> getAllCategories();
}



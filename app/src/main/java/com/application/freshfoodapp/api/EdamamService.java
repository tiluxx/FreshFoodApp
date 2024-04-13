package com.application.freshfoodapp.api;

import com.application.freshfoodapp.model.SearchRecipes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EdamamService {
    @GET("api/recipes/v2")
    Call<SearchRecipes> searchRecipes(@Query("app_id") String app_id,
                                      @Query("app_key") String app_key,
                                      @Query("type") String type,
                                      @Query("health") String[] health,
                                      @Query("q") String q);

    @GET("api/recipes/v2")
    Call<SearchRecipes> searchDishes(@Query("app_id") String app_id,
                                     @Query("app_key") String app_key,
                                     @Query("type") String type,
                                     @Query("q") String q);

    @GET("api/recipes/v2/by-uri")
    Call<SearchRecipes> loadDish(@Query("app_id") String app_id,
                                 @Query("app_key") String app_key,
                                 @Query("type") String type,
                                 @Query("uri") String uri);

    @GET("api/recipes/v2")
    Call<SearchRecipes> loadDishBaseOnCountry(@Query("app_id") String app_id,
                                              @Query("app_key") String app_key,
                                              @Query("type") String type,
                                              @Query("cuisineType") String cuisineType);
}
package com.application.freshfoodapp.api;

import com.application.freshfoodapp.model.SearchRecipes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    APIService apiService = new Retrofit.Builder()
            .baseUrl("https://api.edamam.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    @GET("api/recipes/v2")
    Call<SearchRecipes> searchRecipes (@Query("app_id") String app_id,
                                       @Query("app_key") String app_key,
                                       @Query("type") String type,
                                       @Query("health") String[] health,
                                       @Query("q") String q);

    @GET("api/recipes/v2")
    Call<SearchRecipes> searchDishes (@Query("app_id") String app_id,
                                      @Query("app_key") String app_key,
                                      @Query("type") String type,
                                      @Query("q") String q);

    @GET("api/recipes/v2/by-uri")
    Call<SearchRecipes> loadDish (@Query("app_id") String app_id,
                                  @Query("app_key") String app_key,
                                  @Query("type") String type,
                                  @Query("uri") String uri);

    @GET("api/recipes/v2")
    Call<SearchRecipes> loadDishBaseOnCountry (@Query("app_id") String app_id,
                                               @Query("app_key") String app_key,
                                               @Query("type") String type,
                                               @Query("cuisineType") String cuisineType);
}

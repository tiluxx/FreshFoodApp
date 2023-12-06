package com.application.freshfoodapp.api;

import com.application.freshfoodapp.model.SearchRecipes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    @GET("search")
    Call<SearchRecipes> searchRecipes (@Query("app_id") String app_id,
                                       @Query("app_key") String app_key,
                                       @Query("q") String q);
    @GET("search")
    Call<SearchRecipes> loadDish(@Query("app_id") String app_id,
                                 @Query("app_key") String app_key);
}

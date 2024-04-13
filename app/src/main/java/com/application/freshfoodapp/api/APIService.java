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

public class APIService implements EdamamService {
    private static APIService instance;
    private final EdamamService edamamService;
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private APIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.edamam.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        edamamService = retrofit.create(EdamamService.class);
    }

    public static APIService getInstance() {
        if (instance == null) {
            synchronized (APIService.class) {
                if (instance == null) {
                    instance = new APIService();
                }
            }
        }
        return instance;
    }

    @Override
    public Call<SearchRecipes> searchRecipes(String app_id, String app_key, String type, String[] health, String q) {
        return edamamService.searchRecipes(app_id, app_key, type, health, q);
    }

    @Override
    public Call<SearchRecipes> searchDishes(String app_id, String app_key, String type, String q) {
        return edamamService.searchDishes(app_id, app_key, type, q);
    }

    @Override
    public Call<SearchRecipes> loadDish(String app_id, String app_key, String type, String uri) {
        return edamamService.loadDish(app_id, app_key, type, uri);
    }

    @Override
    public Call<SearchRecipes> loadDishBaseOnCountry(String app_id, String app_key, String type, String cuisineType) {
        return edamamService.loadDishBaseOnCountry(app_id, app_key, type, cuisineType);
    }
}
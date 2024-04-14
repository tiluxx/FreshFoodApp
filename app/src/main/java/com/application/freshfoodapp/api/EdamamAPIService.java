package com.application.freshfoodapp.api;

import com.application.freshfoodapp.model.SearchEdamamRecipes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EdamamAPIService implements EdamamService {
    private static EdamamAPIService instance;
    private final EdamamService edamamService;
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private EdamamAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.edamam.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        edamamService = retrofit.create(EdamamService.class);
    }

    public static EdamamAPIService getInstance() {
        if (instance == null) {
            synchronized (EdamamAPIService.class) {
                if (instance == null) {
                    instance = new EdamamAPIService();
                }
            }
        }
        return instance;
    }

    @Override
    public Call<SearchEdamamRecipes> searchRecipes(String app_id, String app_key, String type, String[] health, String q) {
        return edamamService.searchRecipes(app_id, app_key, type, health, q);
    }

    @Override
    public Call<SearchEdamamRecipes> searchDishes(String app_id, String app_key, String type, String q) {
        return edamamService.searchDishes(app_id, app_key, type, q);
    }

    @Override
    public Call<SearchEdamamRecipes> loadDish(String app_id, String app_key, String type, String uri) {
        return edamamService.loadDish(app_id, app_key, type, uri);
    }

    @Override
    public Call<SearchEdamamRecipes> loadDishBaseOnCountry(String app_id, String app_key, String type, String cuisineType) {
        return edamamService.loadDishBaseOnCountry(app_id, app_key, type, cuisineType);
    }
}
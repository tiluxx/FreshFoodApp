package com.application.freshfoodapp.api;

import com.application.freshfoodapp.model.SearchSpoonacularRecipes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpoonacularAPIService implements SpoonacularService {
    private static SpoonacularAPIService instance;
    private final SpoonacularService spoonacularService;
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private SpoonacularAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        spoonacularService = retrofit.create(SpoonacularService.class);
    }

    public static SpoonacularAPIService getInstance() {
        if (instance == null) {
            synchronized (SpoonacularAPIService.class) {
                if (instance == null) {
                    instance = new SpoonacularAPIService();
                }
            }
        }
        return instance;
    }

    @Override
    public Call<SearchSpoonacularRecipes[]> searchRecipes(String ingredients) {
        return spoonacularService.searchRecipes(ingredients);
    }

    @Override
    public Call<SearchSpoonacularRecipes> getRecipe(String id) {
        return spoonacularService.getRecipe(id);
    }
}

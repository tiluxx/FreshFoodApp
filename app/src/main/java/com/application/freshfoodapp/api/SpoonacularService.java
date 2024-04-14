package com.application.freshfoodapp.api;

import com.application.freshfoodapp.model.SearchEdamamRecipes;
import com.application.freshfoodapp.model.SearchSpoonacularRecipes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpoonacularService {
    @Headers({"x-api-key: b3710752c9de4189a3cc93f38c4bfc65"})
    @GET("recipes/findByIngredients")
    Call<SearchSpoonacularRecipes[]> searchRecipes(@Query("ingredients") String ingredients);

    @Headers({"x-api-key: b3710752c9de4189a3cc93f38c4bfc65"})
    @GET("recipes/{id}/information?includeNutrition=false")
    Call<SearchSpoonacularRecipes> getRecipe(@Path("id") String id);
}

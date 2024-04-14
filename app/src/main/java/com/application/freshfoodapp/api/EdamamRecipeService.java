package com.application.freshfoodapp.api;

import androidx.annotation.NonNull;

import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.model.SearchEdamamRecipes;
import com.application.freshfoodapp.ui.recipes.RecipesViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EdamamRecipeService implements RecipeServiceProvider {
    private static final String APP_ID = "fa0eb92f";
    private static final String APP_KEY = "f49ace5bca4a053d1b877b4d369a673a";

    @Override
    public void fetchRecipes(RecipesViewModel vm, String ingredients) {
    }

    @Override
    public void fetchRecipes(RecipesViewModel vm, String ingredients, String[] healthyLabels) {
        List<RootObjectModel> recipesList = new ArrayList<>();
         EdamamAPIService.getInstance().searchRecipes(APP_ID, APP_KEY,"public", healthyLabels, ingredients).enqueue(new Callback<SearchEdamamRecipes>() {
            @Override
            public void onResponse(@NonNull Call<SearchEdamamRecipes> call, @NonNull Response<SearchEdamamRecipes> response) {
                if (response.body() != null) {
                    recipesList.addAll(Arrays.asList(response.body().getFoodRecipes()));
                    vm.getRecipes().postValue(recipesList);
                    vm.updateRecipes(recipesList);
                }
            }
            @Override
            public void onFailure(@NonNull Call<SearchEdamamRecipes> call, @NonNull Throwable t) {

            }
        });
    }
}

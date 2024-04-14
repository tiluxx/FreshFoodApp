package com.application.freshfoodapp.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.application.freshfoodapp.model.Ingredient;
import com.application.freshfoodapp.model.MissedIngredient;
import com.application.freshfoodapp.model.RecipeModel;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.model.SearchEdamamRecipes;
import com.application.freshfoodapp.model.SearchSpoonacularRecipes;
import com.application.freshfoodapp.model.UsedIngredients;
import com.application.freshfoodapp.ui.recipes.RecipesViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpoonacularServiceAdapter implements RecipeServiceProvider {
    private final SpoonacularAPIService apiService = SpoonacularAPIService.getInstance();

    @Override
    public void fetchRecipes(RecipesViewModel vm, String ingredients) {
        List<SearchSpoonacularRecipes> spoonacularRecipesList = new ArrayList<>();
        apiService.searchRecipes(ingredients).enqueue(new Callback<SearchSpoonacularRecipes[]>() {
            @Override
            public void onResponse(
                    @NonNull Call<SearchSpoonacularRecipes[]> call,
                    @NonNull Response<SearchSpoonacularRecipes[]> response) {
                if (response.body() != null) {
                    spoonacularRecipesList.addAll(Arrays.asList(response.body()));
                    List<RootObjectModel> covertedList = new ArrayList<>();
                    for (SearchSpoonacularRecipes spoonacularRecipe: spoonacularRecipesList) {
                        Log.i("TAG", spoonacularRecipe.getTitle());
                        String uri = "https://api.spoonacular.com/recipes/"+ spoonacularRecipe.getId() + "/information?includeNutrition=false&apiKey=b3710752c9de4189a3cc93f38c4bfc65";
                        String label = spoonacularRecipe.getTitle();
                        String image = spoonacularRecipe.getImage();
                        String calories = "0.0";
                        String totalTime = "0.0";
                        String[] cuisineType = new String[]{};
                        String[] mealType = new String[]{};
                        String[] dishType = new String[]{};
                        String[] healthLabels = new String[]{};
                        List<Ingredient> ingredientsOfRecipe = new ArrayList<>();
                        for (MissedIngredient ingr : spoonacularRecipe.getMissedIngredients()){
                            ingredientsOfRecipe.add(new Ingredient(String.valueOf(ingr.getAmount()), ingr.getUnit(), ingr.getName(), "0.0", ingr.getAisle()));
                        }
                        for (UsedIngredients ingr : spoonacularRecipe.getUsedIngredients()){
                            ingredientsOfRecipe.add(new Ingredient(String.valueOf(ingr.getAmount()), ingr.getUnit(), ingr.getName(), "0.0", ingr.getAisle()));
                        }
                        Ingredient[] ingrArray = new Ingredient[ingredientsOfRecipe.size()];
                        ingredientsOfRecipe.toArray(ingrArray);
                        RecipeModel recipeModel = new RecipeModel(uri, label, image, calories, totalTime, cuisineType, mealType, dishType, healthLabels, ingrArray);
                        covertedList.add(new RootObjectModel(recipeModel));
                    }
                    vm.getRecipes().postValue(covertedList);
                    vm.updateRecipes(covertedList);
                }
            }
            @Override
            public void onFailure(@NonNull Call<SearchSpoonacularRecipes[]> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void fetchRecipes(RecipesViewModel vm, String ingredients, String[] healthyLabels) {
    }
}

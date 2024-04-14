package com.application.freshfoodapp.ui.recipes.recipesdetail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.application.freshfoodapp.api.EdamamAPIService;
import com.application.freshfoodapp.api.SpoonacularAPIService;
import com.application.freshfoodapp.model.Ingredient;
import com.application.freshfoodapp.model.MissedIngredient;
import com.application.freshfoodapp.model.RecipeModel;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.model.SearchEdamamRecipes;
import com.application.freshfoodapp.model.SearchSpoonacularRecipes;
import com.application.freshfoodapp.model.UsedIngredients;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientViewModel extends ViewModel {
    private static final String APP_ID = "fa0eb92f";
    private static final String APP_KEY = "f49ace5bca4a053d1b877b4d369a673a";
    private MutableLiveData<List<RootObjectModel>> mRecipes;
    private List<RootObjectModel> recipe;

    public IngredientViewModel() {
        this.mRecipes = new MutableLiveData<>();
    }
    public MutableLiveData<List<RootObjectModel>> getRecipes() {
        return mRecipes;
    }

    public void prepareData(String keyToRecipe) {
        if (keyToRecipe.contains("spoonacular")) {
            SpoonacularAPIService.getInstance().getRecipe(keyToRecipe).enqueue(new Callback<SearchSpoonacularRecipes>() {
                @Override
                public void onResponse(Call<SearchSpoonacularRecipes> call, Response<SearchSpoonacularRecipes> response) {
                    if (response.body() != null) {
                        recipe = new ArrayList<>();

                        SearchSpoonacularRecipes resRecipe = response.body();
                        String uri = "https://api.spoonacular.com/recipes/"+ resRecipe.getId() + "/information?includeNutrition=false&apiKey=b3710752c9de4189a3cc93f38c4bfc65";
                        String label = resRecipe.getTitle();
                        String image = resRecipe.getImage();
                        String calories = "0.0";
                        String totalTime = "0.0";
                        String[] cuisineType = new String[]{};
                        String[] mealType = new String[]{};
                        String[] dishType = new String[]{};
                        String[] healthLabels = new String[]{};
                        List<Ingredient> ingredientsOfRecipe = new ArrayList<>();
                        for (MissedIngredient ingr : resRecipe.getMissedIngredients()){
                            ingredientsOfRecipe.add(new Ingredient(String.valueOf(ingr.getAmount()), ingr.getUnit(), ingr.getName(), "0.0", ingr.getAisle()));
                        }
                        for (UsedIngredients ingr : resRecipe.getUsedIngredients()){
                            ingredientsOfRecipe.add(new Ingredient(String.valueOf(ingr.getAmount()), ingr.getUnit(), ingr.getName(), "0.0", ingr.getAisle()));
                        }
                        Ingredient[] ingrArray = new Ingredient[ingredientsOfRecipe.size()];
                        ingredientsOfRecipe.toArray(ingrArray);
                        RecipeModel recipeModel = new RecipeModel(uri, label, image, calories, totalTime, cuisineType, mealType, dishType, healthLabels, ingrArray);
                        recipe.add(new RootObjectModel(recipeModel));

                        mRecipes.postValue(recipe);
                    }
                }

                @Override
                public void onFailure(Call<SearchSpoonacularRecipes> call, Throwable t) {
                    mRecipes.postValue(null);
                }
            });
        } else {
            EdamamAPIService.getInstance().loadDish(APP_ID, APP_KEY,"public", keyToRecipe).enqueue(new Callback<SearchEdamamRecipes>() {
                @Override
                public void onResponse(Call<SearchEdamamRecipes> call, Response<SearchEdamamRecipes> response) {
                    recipe = new ArrayList<>(Arrays.asList(response.body().getFoodRecipes()));
                    mRecipes.postValue(Arrays.asList(response.body().getFoodRecipes()));
                }
                @Override
                public void onFailure(Call<SearchEdamamRecipes> call, Throwable t) {
                    mRecipes.postValue(null);
                }
            });
        }
    }
}
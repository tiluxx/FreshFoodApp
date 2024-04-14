package com.application.freshfoodapp.api;

import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.ui.recipes.RecipesViewModel;

import java.util.List;

public interface RecipeServiceProvider {
    void fetchRecipes(RecipesViewModel vm, String ingredients);
    void fetchRecipes(RecipesViewModel vm, String ingredients, String[] healthyLabels);
}

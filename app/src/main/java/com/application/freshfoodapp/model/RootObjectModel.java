package com.application.freshfoodapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RootObjectModel {
    @Expose()
    @SerializedName("recipe")
    private RecipeModel recipeModel;

    public RootObjectModel(RecipeModel recipeModel) {
        this.recipeModel = recipeModel;
    }

    public RecipeModel getRecipeModel() {
        return recipeModel;
    }

    public void setRecipeModel(RecipeModel recipeModel) {
        this.recipeModel = recipeModel;
    }
}

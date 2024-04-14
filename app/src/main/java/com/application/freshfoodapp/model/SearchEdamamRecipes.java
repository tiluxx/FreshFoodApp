package com.application.freshfoodapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchEdamamRecipes {
    @SerializedName("q")
    private String query;
    @SerializedName("hits")
    @Expose()
    private RootObjectModel[] foodRecipes;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public RootObjectModel[] getFoodRecipes() {
        return foodRecipes;
    }

    public void setFoodRecipes(RootObjectModel[] foodRecipes) {
        this.foodRecipes = foodRecipes;
    }
}

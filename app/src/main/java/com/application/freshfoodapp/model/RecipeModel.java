package com.application.freshfoodapp.model;

import com.google.gson.annotations.Expose;

public class RecipeModel {
    private String label;
    private String image;
    private String calories;
    private String totalTime;
    @Expose()
    private String cuisineType[];
    @Expose()
    private String mealType[];
    @Expose()
    private String dishType[];
    @Expose()
    private Ingredient ingredients[];

    public RecipeModel(String label, String image, String calories, String totalTime, String[] cuisineType, String[] mealType, String[] dishType, Ingredient[] ingredients) {
        this.label = label;
        this.image = image;
        this.calories = calories;
        this.totalTime = totalTime;
        this.cuisineType = cuisineType;
        this.mealType = mealType;
        this.dishType = dishType;
        this.ingredients = ingredients;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String[] getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String[] cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String[] getMealType() {
        return mealType;
    }

    public void setMealType(String[] mealType) {
        this.mealType = mealType;
    }

    public String[] getDishType() {
        return dishType;
    }

    public void setDishType(String[] dishType) {
        this.dishType = dishType;
    }

    public Ingredient[] getIngredient() {
        return ingredients;
    }

    public void setIngredient(Ingredient[] ingredient) {
        this.ingredients = ingredient;
    }
}

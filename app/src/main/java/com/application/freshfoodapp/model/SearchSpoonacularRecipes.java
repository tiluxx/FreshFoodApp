package com.application.freshfoodapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchSpoonacularRecipes {
    @SerializedName("id")
    @Expose()
    private float id;
    @SerializedName("image")
    @Expose()
    private String image;
    @SerializedName("imageType")
    @Expose()
    private String imageType;
    @SerializedName("likes")
    @Expose()
    private float likes;
    @SerializedName("missedIngredientCount")
    @Expose()
    private float missedIngredientCount;
    @SerializedName("missedIngredients")
    @Expose()
    MissedIngredient[] missedIngredients;
    @SerializedName("title")
    @Expose()
    private String title;
    @SerializedName("usedIngredientCount")
    @Expose()
    private float usedIngredientCount;
    @SerializedName("usedIngredients")
    @Expose()
    UsedIngredients[] usedIngredients;


    // Getter Methods

    public float getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getImageType() {
        return imageType;
    }

    public float getLikes() {
        return likes;
    }

    public float getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public MissedIngredient[] getMissedIngredients() {return missedIngredients;}

    public String getTitle() {
        return title;
    }

    public float getUsedIngredientCount() {
        return usedIngredientCount;
    }

    public UsedIngredients[] getUsedIngredients() {return usedIngredients;}

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public void setLikes(float likes) {
        this.likes = likes;
    }

    public void setMissedIngredientCount(float missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsedIngredientCount(float usedIngredientCount) {
        this.usedIngredientCount = usedIngredientCount;
    }
}


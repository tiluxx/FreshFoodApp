package com.application.freshfoodapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissedIngredient {
    @SerializedName("aisle")
    @Expose()
    private String aisle;
    @SerializedName("amount")
    @Expose()
    private float amount;
    @SerializedName("id")
    @Expose()
    private float id;
    @SerializedName("image")
    @Expose()
    private String image;
    @SerializedName("name")
    @Expose()
    private String name;
    @SerializedName("original")
    @Expose()
    private String original;
    @SerializedName("originalName")
    @Expose()
    private String originalName;
    @SerializedName("unit")
    @Expose()
    private String unit;
    @SerializedName("unitLong")
    @Expose()
    private String unitLong;
    @SerializedName("unitShort")
    @Expose()
    private String unitShort;


    // Getter Methods

    public String getAisle() {
        return aisle;
    }

    public float getAmount() {
        return amount;
    }

    public float getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getOriginal() {
        return original;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getUnit() {
        return unit;
    }

    public String getUnitLong() {
        return unitLong;
    }

    public String getUnitShort() {
        return unitShort;
    }

    // Setter Methods

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setId(float id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUnitLong(String unitLong) {
        this.unitLong = unitLong;
    }

    public void setUnitShort(String unitShort) {
        this.unitShort = unitShort;
    }
}

package com.application.freshfoodapp.model;

import com.google.gson.annotations.SerializedName;

public class BarcodeMetadata {
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("countries")
    public String countries;
    @SerializedName("ingredients")
    public String ingredients;
}

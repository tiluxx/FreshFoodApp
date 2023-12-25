package com.application.freshfoodapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanForMeal {
    @SerializedName("ownerId")
    private String ownerId;
    @SerializedName("dishUri")
    private String dishUri;
    @SerializedName("dateOfPlan")
    private long dateOfPlan;
    @SerializedName("typeOfMeal")
    private String typeOfMeal;
    @SerializedName("numOfServings")
    private String numOfServings;

    public PlanForMeal() {
    }

    public PlanForMeal(String ownerId, String dishUri, long dateOfPlan, String typeOfMeal, String numOfServings) {
        this.ownerId = ownerId;
        this.dishUri = dishUri;
        this.dateOfPlan = dateOfPlan;
        this.typeOfMeal = typeOfMeal;
        this.numOfServings = numOfServings;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDishUri() {
        return dishUri;
    }

    public void setDishUri(String dishUri) {
        this.dishUri = dishUri;
    }

    public long getDateOfPlan() {
        return dateOfPlan;
    }

    public void setDateOfPlan(long dateOfPlan) {
        this.dateOfPlan = dateOfPlan;
    }

    public String getTypeOfMeal() {
        return typeOfMeal;
    }

    public void setTypeOfMeal(String typeOfMeal) {
        this.typeOfMeal = typeOfMeal;
    }

    public String getNumOfServings() {
        return numOfServings;
    }

    public void setNumOfServings(String numOfServings) {
        this.numOfServings = numOfServings;
    }
}

package com.application.freshfoodapp.model;

public class Ingredient {
    private String quantity;
    private String measure;
    private String food;
    private String weight;
    private String foodCategory;

    public Ingredient(String quantity, String measure, String food, String weight, String foodCategory) {
        this.quantity = quantity;
        this.measure = measure;
        this.food = food;
        this.weight = weight;
        this.foodCategory = foodCategory;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }
}

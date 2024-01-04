package com.application.freshfoodapp.model;

import java.util.ArrayList;
import java.util.List;

public class WeeklyDish {
    private String mealType;
    private List<PlanForMeal> dishesInDayOfWeek;

    public WeeklyDish(String mealType) {
        this.mealType = mealType;
        this.dishesInDayOfWeek = new ArrayList<>();
    }

    public WeeklyDish(String mealType, List<PlanForMeal> dishesInDayOfWeek) {
        this.mealType = mealType;
        this.dishesInDayOfWeek = dishesInDayOfWeek;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public List<PlanForMeal> getDishesInDayOfWeek() {
        return dishesInDayOfWeek;
    }

    public void setDishesInDayOfWeek(List<PlanForMeal> dishesInDayOfWeek) {
        this.dishesInDayOfWeek = dishesInDayOfWeek;
    }
}

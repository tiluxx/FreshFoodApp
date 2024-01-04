package com.application.freshfoodapp.model;

import android.content.ClipData;

import java.util.List;

public class DailyMeal {
    private String typeOfMeal;
    private List<ClipData.Item> itemList;

    public DailyMeal() {
    }

    public DailyMeal(String typeOfMeal, List<ClipData.Item> itemList) {
        this.typeOfMeal = typeOfMeal;
        this.itemList = itemList;
    }

    public String getTypeOfMeal() {
        return typeOfMeal;
    }

    public void setTypeOfMeal(String typeOfMeal) {
        this.typeOfMeal = typeOfMeal;
    }

    public List<ClipData.Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<ClipData.Item> itemList) {
        this.itemList = itemList;
    }
}

package com.application.freshfoodapp.ui.recipes.cuisinetypefactory;

import com.application.freshfoodapp.ui.recipes.CountriesRecipe;

public class ChineseRecipe implements CuisineType {
    public ChineseRecipe() {}

    @Override
    public String getCuisineType() {
        return "chinese";
    }
}

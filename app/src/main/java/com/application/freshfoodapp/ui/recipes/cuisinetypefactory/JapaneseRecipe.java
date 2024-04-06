package com.application.freshfoodapp.ui.recipes.cuisinetypefactory;

import com.application.freshfoodapp.ui.recipes.CountriesRecipe;

public class JapaneseRecipe implements CuisineType {
    public JapaneseRecipe() {}

    @Override
    public String getCuisineType() {
        return "japanese";
    }
}

package com.application.freshfoodapp.ui.recipes.cuisinetypefactory;

import com.application.freshfoodapp.ui.recipes.CountriesRecipe;

public class ItalianRecipe implements CuisineType {
    public ItalianRecipe() {}

    @Override
    public String getCuisineType() {
        return "italian";
    }
}

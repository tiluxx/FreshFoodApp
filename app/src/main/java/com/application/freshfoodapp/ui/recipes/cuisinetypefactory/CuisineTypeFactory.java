package com.application.freshfoodapp.ui.recipes.cuisinetypefactory;

import com.application.freshfoodapp.ui.recipes.CountriesRecipe;

public class CuisineTypeFactory {
    public static CuisineType createCuisineType(CountriesRecipe countriesRecipe) {
        if (countriesRecipe == CountriesRecipe.JAPANESE) {
            return new JapaneseRecipe();
        } else if (countriesRecipe == CountriesRecipe.CHINESE) {
            return new ChineseRecipe();
        } else if (countriesRecipe == CountriesRecipe.ITALIAN) {
            return new ItalianRecipe();
        } else {
            return new KoreanRecipe();
        }
    }
}

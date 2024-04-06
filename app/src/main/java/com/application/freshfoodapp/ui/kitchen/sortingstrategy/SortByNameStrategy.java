package com.application.freshfoodapp.ui.kitchen.sortingstrategy;

import com.application.freshfoodapp.model.Product;

import java.util.Comparator;
import java.util.List;

public class SortByNameStrategy implements SortStrategy{
    @Override
    public void sort(List<Product> foodItems) {
        foodItems.sort(Comparator.comparing(Product::getTitle));
    }
}

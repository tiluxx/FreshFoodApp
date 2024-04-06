package com.application.freshfoodapp.ui.kitchen.sortingstrategy;

import com.application.freshfoodapp.model.Product;

import java.util.List;

public interface SortStrategy {
    void sort(List<Product> foodItems);
}

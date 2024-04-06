package com.application.freshfoodapp.ui.kitchen.sortingstrategy;

import com.application.freshfoodapp.model.Product;

import java.util.List;

public class ProductSorter {
    private SortStrategy sortStrategy;

    public ProductSorter(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public void setSortStrategy(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public void sort(List<Product> productList) {
        sortStrategy.sort(productList);
    }
}

package com.application.freshfoodapp.model;

import java.util.List;

public class ProductSubKitchen {
    private SubKitchen subKitchen;
    private List<Product> productList;

    public ProductSubKitchen() {
    }

    public ProductSubKitchen(SubKitchen subKitchens, List<Product> productList) {
        this.subKitchen = subKitchens;
        this.productList = productList;
    }

    public SubKitchen getSubKitchens() {
        return subKitchen;
    }

    public void setSubKitchens(SubKitchen subKitchen) {
        this.subKitchen = subKitchen;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}

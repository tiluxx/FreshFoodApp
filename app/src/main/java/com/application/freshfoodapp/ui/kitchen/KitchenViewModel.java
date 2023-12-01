package com.application.freshfoodapp.ui.kitchen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.freshfoodapp.model.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KitchenViewModel extends ViewModel {
    private final MutableLiveData<List<Product>> mProducts;

    public KitchenViewModel() {
        this.mProducts =  new MutableLiveData<>();
        List<Product> products = new ArrayList<>();
        products.add(new Product("Avocado", LocalDateTime.now(), 2, 200, "g"));
        products.add(new Product("Avocado", LocalDateTime.now(), 2, 200, "g"));
        products.add(new Product("Avocado", LocalDateTime.now(), 2, 200, "g"));
        mProducts.setValue(products);
    }

    public LiveData<List<Product>> getProducts() {
        return mProducts;
    }
}
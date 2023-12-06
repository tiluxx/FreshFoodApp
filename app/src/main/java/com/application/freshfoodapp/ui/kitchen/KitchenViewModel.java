package com.application.freshfoodapp.ui.kitchen;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.freshfoodapp.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class KitchenViewModel extends ViewModel {
    private MutableLiveData<List<Product>> mProducts;
    FirebaseFirestore db;

    public KitchenViewModel() {
        this.mProducts = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        List<Product> products = new ArrayList<>();
        mProducts.setValue(products);
    }

    public MutableLiveData<List<Product>> getProducts() {
        return mProducts;
    }
}
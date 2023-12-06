package com.application.freshfoodapp.ui.kitchen.kitchentype;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.freshfoodapp.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TabKitchenTypeViewModel extends ViewModel {
    private MutableLiveData<List<Product>> mProducts;
    FirebaseFirestore db;

    public TabKitchenTypeViewModel() {
        this.mProducts = new MutableLiveData<>();
    }

    public void fetchProductsByKitchenType(String kitchenType, String kitchenId) {
        db = FirebaseFirestore.getInstance();
        List<Product> products = new ArrayList<>();

        db.collection("products")
                .whereEqualTo("kitchenId", kitchenId)
                .whereEqualTo("pantry", kitchenType)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Product product;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            product = document.toObject(Product.class);
                            product.setProductId(document.getId());
                            products.add(product);
                            Log.d("RES", document.getId() + " => " + document.getData());
                        }
                        mProducts.setValue(products);

                    } else {
                        Log.d("ERROR", "Error getting documents: ", task.getException());
                    }
                });
    }

    public MutableLiveData<List<Product>> getProductsByKitchenType() {
        return mProducts;
    }
}
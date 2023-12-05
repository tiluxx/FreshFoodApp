package com.application.freshfoodapp.ui.kitchen;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class KitchenViewModel extends ViewModel {
    private MutableLiveData<List<Product>> mProducts;
    FirebaseFirestore db;

    public KitchenViewModel() {
        this.mProducts = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        List<Product> products = new ArrayList<>();

        db.collection("products")
                .whereEqualTo("ownerId", MainActivity.getCurUser().getUid())
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
                    } else {
                        Log.d("ERROR", "Error getting documents: ", task.getException());
                    }
                });
        mProducts.setValue(products);
    }

    public MutableLiveData<List<Product>> getProducts() {
        return mProducts;
    }

    public void deleteProductById(String productId) {
        boolean isSuccess = false;


    }
}
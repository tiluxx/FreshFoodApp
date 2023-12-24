package com.application.freshfoodapp.ui.productdetail;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.freshfoodapp.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetailViewModel extends ViewModel {
    private MutableLiveData<Product> mProduct;
    FirebaseFirestore db;

    public ProductDetailViewModel() {
        this.mProduct = new MutableLiveData<>();
    }

    public void fetchProductsByProductId(String productId) {
        db = FirebaseFirestore.getInstance();

        db.collection("products")
                .document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Product product = documentSnapshot.toObject(Product.class);
                    if (product != null) {
                        product.setProductId(productId);
                    }
                    Log.i("TAG", product.getTitle() + product.getProductId());
                    mProduct.setValue(product);
                });
    }

    public MutableLiveData<Product> getProductByProductId() {
        return mProduct;
    }
}
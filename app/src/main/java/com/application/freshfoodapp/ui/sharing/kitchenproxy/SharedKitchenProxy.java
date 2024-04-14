package com.application.freshfoodapp.ui.sharing.kitchenproxy;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.adapter.KitchenTypeAdapter;
import com.application.freshfoodapp.model.Kitchen;
import com.application.freshfoodapp.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class SharedKitchenProxy implements KitchenService {
    FirebaseFirestore db;
    private KitchenService kitchenService;

    public SharedKitchenProxy() {
        this.kitchenService = new SharedKitchenFoodService();
        db = FirebaseFirestore.getInstance();
    }

    public SharedKitchenProxy(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void accessKitchen(Context context, String kitchenId) {
        db.collection("kitchens")
                .whereArrayContains("subOwnerIds", MainActivity.getCurUser().getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        kitchenService.accessKitchen(context, kitchenId);
                    } else {
                        Toast.makeText(context, "You are not authorized to access this", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void removeProductItem(Context context, Product product, List<Product> data, KitchenTypeAdapter adapter, int deletePosition) {
        db.collection("kitchens")
                .whereArrayContains("subOwnerIds", MainActivity.getCurUser().getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        kitchenService.removeProductItem(context, product, data, adapter, deletePosition);
                    } else {
                        Toast.makeText(context, "You are not authorized to remove product", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

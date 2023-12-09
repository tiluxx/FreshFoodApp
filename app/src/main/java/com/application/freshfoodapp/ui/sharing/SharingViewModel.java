package com.application.freshfoodapp.ui.sharing;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.model.Kitchen;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SharingViewModel extends ViewModel {
    private MutableLiveData<List<Kitchen>> mKitchens;
    FirebaseFirestore db;

    public SharingViewModel() {
        this.mKitchens = new MutableLiveData<>();
    }

    public void fetchSharingKitchensByUser() {
        db = FirebaseFirestore.getInstance();
        List<Kitchen> kitchens = new ArrayList<>();

        db.collection("kitchens")
                .whereArrayContains("subOwnerIds", MainActivity.getCurUser().getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Kitchen kitchen;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            kitchen = document.toObject(Kitchen.class);
                            kitchen.setKitchenId(document.getId());
                            kitchens.add(kitchen);
                            Log.d("RES", document.getId() + " => " + document.getData());
                        }
                        mKitchens.setValue(kitchens);

                    } else {
                        Log.d("ERROR", "Error getting documents: ", task.getException());
                    }
                });
    }

    public MutableLiveData<List<Kitchen>> getSharingKitchens() {
        return mKitchens;
    }
}
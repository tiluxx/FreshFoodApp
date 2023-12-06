package com.application.freshfoodapp.ui.notification;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.model.Invitation;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationViewModel extends ViewModel {
    private MutableLiveData<List<Invitation>> mNotifications;
    FirebaseFirestore db;

    public NotificationViewModel() {
        this.mNotifications = new MutableLiveData<>();
    }

    public void fetchInvitationsByUser() {
        db = FirebaseFirestore.getInstance();
        List<Invitation> invitations = new ArrayList<>();

        db.collection("invitations")
                .whereEqualTo("receiverId", MainActivity.getCurUser().getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Invitation invitation;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            invitation = document.toObject(Invitation.class);
                            invitation.setInvitationId(document.getId());
                            invitations.add(invitation);
                            Log.d("RES", document.getId() + " => " + document.getData());
                        }
                        mNotifications.setValue(invitations);

                    } else {
                        Log.d("ERROR", "Error getting documents: ", task.getException());
                    }
                });
    }

    public MutableLiveData<List<Invitation>> getNotifications() {
        return mNotifications;
    }
}
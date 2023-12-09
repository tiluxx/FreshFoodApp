package com.application.freshfoodapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.databinding.NotificationItemBinding;
import com.application.freshfoodapp.model.Invitation;
import com.application.freshfoodapp.viewholder.NotificationViewHolder;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private List<Invitation> data = new ArrayList<>();

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationItemBinding binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Invitation invitation = data.get(position);

        Picasso.get()
                .load(invitation.getOwnerPhotoUrl())
                .resize(60, 60)
                .centerCrop()
                .into(holder.getSenderAvatarView());
        holder.getSenderNameTextView().setText(invitation.getOwnerDisplayName());
        holder.getMessageTextView().setText(invitation.getMessage());

        holder.getAcceptBtn().setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("kitchens")
                    .document(invitation.getSharingKitchenId())
                    .update("subOwnerIds", FieldValue.arrayUnion(invitation.getReceiverId()))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            removeInvitationInDB(invitation.getInvitationId(), position, true, v.getContext());
                        } else {
                            Toast.makeText(v.getContext(), "Cannot accept this invitation", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        holder.getDeclineBtn().setOnClickListener(v -> {
            removeInvitationInDB(invitation.getInvitationId(), position, false, v.getContext());
        });
    }

    private void removeInvitationInDB(String invitationId, int curPosition, boolean isAccepted, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("invitations")
                .document(invitationId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String message =
                                isAccepted ? "Both of you are connected" : "You have just declined this invitation";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        data.remove(curPosition);
                        notifyItemRemoved(curPosition);
                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateInvitationList(List<Invitation> data) {
        this.data = data;
        notifyItemRangeChanged(0, data.size());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

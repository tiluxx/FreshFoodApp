package com.application.freshfoodapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.databinding.SharingItemBinding;
import com.application.freshfoodapp.model.Kitchen;
import com.application.freshfoodapp.ui.auth.AuthActivity;
import com.application.freshfoodapp.ui.kitchen.KitchenFragment;
import com.application.freshfoodapp.ui.sharing.SharedKitchen.SharedKitchenFragment;
import com.application.freshfoodapp.ui.sharing.kitchenproxy.KitchenService;
import com.application.freshfoodapp.ui.sharing.kitchenproxy.SharedKitchenProxy;
import com.application.freshfoodapp.viewholder.SharingViewHolder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SharingAdapter extends RecyclerView.Adapter<SharingViewHolder> {
    private List<Kitchen> data = new ArrayList<>();
    private KitchenService kitchenService = new SharedKitchenProxy();

    @NonNull
    @Override
    public SharingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SharingItemBinding binding = SharingItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new SharingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SharingViewHolder holder, int position) {
        Kitchen kitchen = data.get(position);

        Picasso.get()
                .load(kitchen.getOwnerPhotoUrl())
                .resize(36, 36)
                .centerCrop()
                .into(holder.getOwnerAvatarView());
        holder.getOwnerNameTextView().setText(kitchen.getOwnerDisplayName());

        int numOfMembers = kitchen.getSubOwnerIds().size() + 1;
        String numOfMembersTv = "x" + numOfMembers + " members";
        holder.getNumOfMemberTextView().setText(numOfMembersTv);

        holder.getItemView().setOnClickListener(v -> {
            kitchenService.accessKitchen(v.getContext(), kitchen.getKitchenId());
//            Bundle args = new Bundle();
//            args.putString(SharedKitchenFragment.ARG_SHARED_KITCHEN, kitchen.getKitchenId());
//            MainActivity.getNavController().navigate(R.id.nav_shared_kitchen, args);
        });

        MaterialAlertDialogBuilder leaveKitchenConfirmDialog = new MaterialAlertDialogBuilder(holder.getItemView().getContext())
                .setTitle("Leave " + kitchen.getOwnerDisplayName() + "'s kitchen?")
                .setMessage("Are you sure you want to leave this kitchen? The action cannot undo.")
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .setPositiveButton("Leave", (dialog, which) -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("kitchens")
                            .document(kitchen.getKitchenId())
                            .update("subOwnerIds", FieldValue.arrayRemove(MainActivity.getCurUser().getEmail()))
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(
                                            holder.getItemView().getContext(), "You've just leaved this kitchen successfully", Toast.LENGTH_SHORT).show();
                                    data.remove(position);
                                    notifyItemRemoved(position);
                                } else {
                                    Toast.makeText(
                                            holder.getItemView().getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                });

        holder.getMoreOptionsBtn().setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.getMoreOptionsBtn());
            popupMenu.getMenuInflater().inflate(R.menu.sharing_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                leaveKitchenConfirmDialog.show();
                return true;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateSharingKitchenList(List<Kitchen> data) {
        this.data = data;
        notifyItemRangeChanged(0, data.size());
    }

    private void leaveKitchenHandler(String kitchenId, Context context) {

    }
}

package com.application.freshfoodapp.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.databinding.SharingItemBinding;
import com.application.freshfoodapp.model.Kitchen;
import com.application.freshfoodapp.ui.kitchen.KitchenFragment;
import com.application.freshfoodapp.viewholder.SharingViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SharingAdapter extends RecyclerView.Adapter<SharingViewHolder> {
    private List<Kitchen> data = new ArrayList<>();

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
        String numOfMembersTv = "x" + numOfMembers + "members";
        holder.getNumOfMemberTextView().setText(numOfMembersTv);

        holder.getItemView().setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString(KitchenFragment.ARG_SUB_KITCHEN, kitchen.getKitchenId());
            MainActivity.getNavController().navigate(R.id.nav_kitchen, args);
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
}

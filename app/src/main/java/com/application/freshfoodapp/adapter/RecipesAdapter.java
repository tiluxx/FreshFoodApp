package com.application.freshfoodapp.adapter;

import static com.google.firebase.firestore.util.Assert.fail;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.databinding.RecipesItemCardBinding;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.ui.kitchen.KitchenViewModel;
import com.application.freshfoodapp.viewholder.RecipesViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesViewHolder> {
    private List<RootObjectModel> data;
    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipesItemCardBinding binding = RecipesItemCardBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new RecipesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        RootObjectModel recipes = data.get(position);

        holder.getDishName().setText(recipes.getRecipeModel().getLabel());
        Glide.with(holder.getRecipesImage().getContext()).load(recipes.getRecipeModel()
                        .getImage())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.getRecipesImage());

        if (Float.valueOf(recipes.getRecipeModel().getTotalTime()) <= 0) {
            holder.getTotalTime().setText("15min");
        } else {
            float originTime = Float.valueOf(recipes.getRecipeModel().getTotalTime());
            int time = (int) originTime;
            holder.getTotalTime().setText(time + "min");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateRecipesList(List<RootObjectModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
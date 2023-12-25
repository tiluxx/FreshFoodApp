package com.application.freshfoodapp.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.databinding.RecipesItemVerticalCardBinding;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.ui.planner.searchdishes.SearchDishFragment;
import com.application.freshfoodapp.ui.recipes.recipesdetail.IngredientFragment;
import com.application.freshfoodapp.viewholder.RecipesVerticalViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class RecipesVerticalAdapter extends RecyclerView.Adapter<RecipesVerticalViewHolder> {
    private List<RootObjectModel> data;
    public RecipesVerticalAdapter() {
        this.data = new ArrayList<>();
    }
    @NonNull
    @Override
    public RecipesVerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipesItemVerticalCardBinding binding = RecipesItemVerticalCardBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new RecipesVerticalViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesVerticalViewHolder holder, int position) {
        RootObjectModel recipes = data.get(position);

        if(recipes.getRecipeModel().getLabel().length() > 19) {
            holder.getDishName().setText(recipes.getRecipeModel().getLabel().substring(0, 20) + "...");
        } else {
            holder.getDishName().setText(recipes.getRecipeModel().getLabel());
        }

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

        float originCalories = Float.valueOf(recipes.getRecipeModel().getCalories());
        int calories = (int) originCalories;
        holder.getCalories().setText(calories + " calories");

        holder.getItemView().setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString(IngredientFragment.ARG_INGREDIENTS_DETAIL, recipes.getRecipeModel().getUri());
            MainActivity.getNavController().navigate(R.id.nav_ingredient, args);
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void updateRecipesList(List<RootObjectModel> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }
}
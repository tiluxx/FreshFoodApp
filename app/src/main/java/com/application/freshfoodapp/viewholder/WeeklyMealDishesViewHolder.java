package com.application.freshfoodapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.databinding.WeeklyMealListItemBinding;
import com.google.android.material.imageview.ShapeableImageView;

public class WeeklyMealDishesViewHolder extends RecyclerView.ViewHolder {
    private ShapeableImageView recipeImageView;
    private TextView recipeTitleTextView;

    public WeeklyMealDishesViewHolder(WeeklyMealListItemBinding binding) {
        super(binding.getRoot());
        recipeImageView = binding.recipeImageView;
        recipeTitleTextView = binding.recipeTitleTextView;
    }

    public ShapeableImageView getRecipeImageView() {
        return recipeImageView;
    }

    public TextView getRecipeTitleTextView() {
        return recipeTitleTextView;
    }
}

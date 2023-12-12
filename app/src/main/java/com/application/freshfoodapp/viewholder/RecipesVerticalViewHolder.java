package com.application.freshfoodapp.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.databinding.RecipesItemCardBinding;
import com.application.freshfoodapp.databinding.RecipesItemVerticalCardBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.carousel.MaskableFrameLayout;

public class RecipesVerticalViewHolder extends RecyclerView.ViewHolder {
    private ImageView recipesImage;
    private TextView dishName, calories, totalTime;
    private MaterialCardView itemView;

    public RecipesVerticalViewHolder(RecipesItemVerticalCardBinding binding) {
        super(binding.getRoot());
        recipesImage = binding.dishImageView;
        dishName = binding.dishNameTextView;
        calories = binding.caloriesTextView;
        totalTime = binding.totalTimeTextView;
        itemView = binding.card;
    }

    public MaterialCardView getItemView() {
        return itemView;
    }

    public void setItemView(MaterialCardView itemView) {
        this.itemView = itemView;
    }

    public ImageView getRecipesImage() {
        return recipesImage;
    }

    public void setRecipesImage(ImageView recipesImage) {
        this.recipesImage = recipesImage;
    }

    public TextView getDishName() {
        return dishName;
    }

    public void setDishName(TextView dishName) {
        this.dishName = dishName;
    }

    public TextView getCalories() {
        return calories;
    }

    public void setCalories(TextView calories) {
        this.calories = calories;
    }

    public TextView getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(TextView totalTime) {
        this.totalTime = totalTime;
    }
}

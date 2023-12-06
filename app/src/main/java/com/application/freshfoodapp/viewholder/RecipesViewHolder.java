package com.application.freshfoodapp.viewholder;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.databinding.RecipesItemCardBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;

public class RecipesViewHolder extends RecyclerView.ViewHolder {
    private ImageView recipesImage;
    private TextView dishName, missingIngredient, totalTime;

    public RecipesViewHolder(RecipesItemCardBinding binding) {
        super(binding.getRoot());
        recipesImage = binding.dishImageView;
        dishName = binding.dishNameTextView;
        missingIngredient = binding.missIngredientTextView;
        totalTime = binding.totalTimeTextView;
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

    public TextView getMissingIngredient() {
        return missingIngredient;
    }

    public void setMissingIngredient(TextView missingIngredient) {
        this.missingIngredient = missingIngredient;
    }

    public TextView getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(TextView totalTime) {
        this.totalTime = totalTime;
    }
}

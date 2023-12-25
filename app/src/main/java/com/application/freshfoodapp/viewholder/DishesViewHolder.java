package com.application.freshfoodapp.viewholder;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.databinding.DishItemCardBinding;
import com.application.freshfoodapp.databinding.RecipesItemVerticalCardBinding;
import com.google.android.material.card.MaterialCardView;

public class DishesViewHolder extends RecyclerView.ViewHolder {
    private ImageView dishImage;
    private TextView dishName, numOfServings;
    private Button removeDish;
    private MaterialCardView itemView;

    public DishesViewHolder(DishItemCardBinding binding) {
        super(binding.getRoot());
        dishImage = binding.dishImageView;
        removeDish = binding.deleteProductBtn2;
        dishName = binding.dishNameTextView2;
        numOfServings = binding.numOfServings;
        itemView = binding.card;
    }

    public Button getRemoveDish() {
        return removeDish;
    }

    public void setRemoveDish(Button removeDish) {
        this.removeDish = removeDish;
    }

    public ImageView getDishImage() {
        return dishImage;
    }

    public void setDishImage(ImageView dishImage) {
        this.dishImage = dishImage;
    }

    public TextView getDishName() {
        return dishName;
    }

    public void setDishName(TextView dishName) {
        this.dishName = dishName;
    }

    public TextView getNumOfServings() {
        return numOfServings;
    }

    public void setNumOfServings(TextView numOfServings) {
        this.numOfServings = numOfServings;
    }

    public MaterialCardView getItemView() {
        return itemView;
    }

    public void setItemView(MaterialCardView itemView) {
        this.itemView = itemView;
    }
}

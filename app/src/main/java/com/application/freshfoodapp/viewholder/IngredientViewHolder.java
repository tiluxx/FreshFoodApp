package com.application.freshfoodapp.viewholder;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.databinding.IngredientDetailItemCardBinding;
import com.application.freshfoodapp.databinding.ProductItemCardBinding;
import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

public class IngredientViewHolder extends RecyclerView.ViewHolder {
    private TextView food, weight;

    public IngredientViewHolder(IngredientDetailItemCardBinding binding) {
        super(binding.getRoot());
        food = binding.foodTextView;
        weight = binding.weightTextView;
    }

    public TextView getFood() {
        return food;
    }

    public void setFood(TextView food) {
        this.food = food;
    }

    public TextView getWeight() {
        return weight;
    }

    public void setWeight(TextView weight) {
        this.weight = weight;
    }
}

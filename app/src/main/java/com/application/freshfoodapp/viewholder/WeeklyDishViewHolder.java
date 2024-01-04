package com.application.freshfoodapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.adapter.WeeklyMealDishesAdapter;
import com.application.freshfoodapp.databinding.MealCardListItemBinding;
import com.application.freshfoodapp.model.ItemOfMeal;
import com.application.freshfoodapp.model.WeeklyDish;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class WeeklyDishViewHolder extends RecyclerView.ViewHolder {
    private MaterialCardView mealCard;
    private TextView mealTypeTextView;
    private RecyclerView recipesRecyclerView;
    private WeeklyMealDishesAdapter adapter;

    public WeeklyDishViewHolder(MealCardListItemBinding binding) {
        super(binding.getRoot());
        mealCard = binding.card;
        mealTypeTextView = binding.mealTypeTextView;
        adapter = new WeeklyMealDishesAdapter();
        recipesRecyclerView = binding.recipesRecyclerView;
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
//        recipesRecyclerView.setAdapter(adapter);
    }

    public MaterialCardView getMealCard() {
        return mealCard;
    }

    public TextView getMealTypeTextView() {
        return mealTypeTextView;
    }

    public RecyclerView getRecipesRecyclerView() {
        return recipesRecyclerView;
    }

    public void updateAdapter(List<ItemOfMeal> data) {
    }
}

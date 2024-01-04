package com.application.freshfoodapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.api.APIService;
import com.application.freshfoodapp.databinding.WeeklyMealListItemBinding;
import com.application.freshfoodapp.model.ItemOfMeal;
import com.application.freshfoodapp.model.PlanForMeal;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.model.SearchRecipes;
import com.application.freshfoodapp.viewholder.WeeklyMealDishesViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeeklyMealDishesAdapter extends RecyclerView.Adapter<WeeklyMealDishesViewHolder> {
    private List<PlanForMeal> data;
    private List<RootObjectModel> recipe;
    private static final String APP_ID = "fa0eb92f";
    private static final String APP_KEY = "f49ace5bca4a053d1b877b4d369a673a";

    public WeeklyMealDishesAdapter() {
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public WeeklyMealDishesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WeeklyMealListItemBinding binding = WeeklyMealListItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new WeeklyMealDishesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyMealDishesViewHolder holder, int position) {
        int curPosition = holder.getAdapterPosition();
        PlanForMeal planForMeal = data.get(curPosition);

        APIService.apiService.loadDish(APP_ID, APP_KEY,"public", planForMeal.getDishUri()).enqueue(new Callback<SearchRecipes>() {
            @Override
            public void onResponse(@NonNull Call<SearchRecipes> call, @NonNull Response<SearchRecipes> response) {
                recipe = new ArrayList<>(Arrays.asList(response.body().getFoodRecipes()));
                holder.getRecipeTitleTextView().setText(recipe.get(0).getRecipeModel().getLabel());
                Glide.with(holder.getRecipeImageView().getContext()).load(recipe.get(0).getRecipeModel().getImage())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.getRecipeImageView());
                Log.i("TAG", recipe.get(0).getRecipeModel().getLabel());
            }

            @Override
            public void onFailure(Call<SearchRecipes> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateWeeklyMealDishesList(List<PlanForMeal> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyItemRangeRemoved(0, data.size());
    }
}

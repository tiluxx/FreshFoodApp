package com.application.freshfoodapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.api.EdamamAPIService;
import com.application.freshfoodapp.databinding.MealCardListItemBinding;
import com.application.freshfoodapp.model.ItemOfMeal;
import com.application.freshfoodapp.model.PlanForMeal;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.model.SearchEdamamRecipes;
import com.application.freshfoodapp.model.WeeklyDish;
import com.application.freshfoodapp.viewholder.WeeklyDishViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeeklyDishAdapter extends RecyclerView.Adapter<WeeklyDishViewHolder> {
    private List<WeeklyDish> data;
    private List<RootObjectModel> recipe;
    private static final String APP_ID = "fa0eb92f";
    private static final String APP_KEY = "f49ace5bca4a053d1b877b4d369a673a";
    private WeeklyMealDishesAdapter adapter;

    public WeeklyDishAdapter() {
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public WeeklyDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MealCardListItemBinding binding = MealCardListItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new WeeklyDishViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyDishViewHolder holder, int position) {
        int curPosition = holder.getAdapterPosition();
        WeeklyDish weeklyDish = data.get(curPosition);
        holder.getMealTypeTextView().setText(weeklyDish.getMealType());

        // Update list of recipes in meal type (Ex: Breakfast)
        List<PlanForMeal> plans = weeklyDish.getDishesInDayOfWeek();
        List<ItemOfMeal> itemOfMeals = new ArrayList<>();
        for (PlanForMeal plan : plans) {
            EdamamAPIService.getInstance().loadDish(APP_ID, APP_KEY,"public", plan.getDishUri()).enqueue(new Callback<SearchEdamamRecipes>() {
                @Override
                public void onResponse(@NonNull Call<SearchEdamamRecipes> call, @NonNull Response<SearchEdamamRecipes> response) {
                    recipe = new ArrayList<>(Arrays.asList(response.body().getFoodRecipes()));
                    itemOfMeals.add(new ItemOfMeal(
                            plan.getDishUri(),
                            recipe.get(0).getRecipeModel().getLabel(),
                            plan.getNumOfServings(),
                            recipe.get(0).getRecipeModel().getImage()
                    ));
                    Log.i("TAG", recipe.get(0).getRecipeModel().getLabel());
                }

                @Override
                public void onFailure(Call<SearchEdamamRecipes> call, Throwable t) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateWeeklyDishesList(List<WeeklyDish> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyItemRangeChanged(0, data.size());
    }

    private void deleteDocument(DocumentReference documentRef) {
        // Delete the document
        documentRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Document deleted successfully
                } else {
                    // Handle errors
                }
            }
        });
    }
}

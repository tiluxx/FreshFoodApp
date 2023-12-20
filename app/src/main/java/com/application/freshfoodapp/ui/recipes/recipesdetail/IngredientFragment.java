package com.application.freshfoodapp.ui.recipes.recipesdetail;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.application.freshfoodapp.adapter.IngredientAdapter;
import com.application.freshfoodapp.databinding.FragmentIngredientBinding;
import com.application.freshfoodapp.model.Ingredient;
import com.application.freshfoodapp.model.RootObjectModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IngredientFragment extends Fragment {
    private FragmentIngredientBinding binding;
    public static final String ARG_INGREDIENTS_DETAIL = "ingredient_detail";
    private List<RootObjectModel> recipe;
    private List<Ingredient> ingredients;
    private IngredientViewModel mViewModel;
    private String uri = "";
    Bundle args;

    public IngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<RootObjectModel>>() {
            @Override
            public void onChanged(List<RootObjectModel> rootObjectModels) {
                if(rootObjectModels != null) {
                    recipe = new ArrayList<>(rootObjectModels);
                    ingredients = new ArrayList<>(Arrays.asList(recipe.get(0).getRecipeModel().getIngredients()));

                    prepareData();
                    IngredientAdapter adapter = new IngredientAdapter();
                    adapter.updateIngredientList(ingredients);
                    RecyclerView recyclerView = binding.recipesDetailRecyclerView;
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentIngredientBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
        args = getArguments();
        if (args != null) {
            if (args.getString(ARG_INGREDIENTS_DETAIL) != null) {
                uri = args.getString(ARG_INGREDIENTS_DETAIL);
                mViewModel.prepareData(args.getString(ARG_INGREDIENTS_DETAIL));
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
        // TODO: Use the ViewModel
    }

    private void prepareData() {
        TextView dishName, calories, totalTime, cuisine, meal, dish;
        ChipGroup chipGroup;
        ImageView dishImage;

        dishName = binding.dishNameTextView;
        calories = binding.caloriesTextView;
        totalTime = binding.totalTimeTextView;
        dishImage = binding.dishImageView2;
        cuisine = binding.cuisineTextView2;
        meal = binding.mealTextView2;
        dish = binding.dishTextView2;
        chipGroup = binding.chipCategoryGroup;

        String cuisineType = "", mealType = "", dishType = "";
        for(String item: recipe.get(0).getRecipeModel().getCuisineType()) {
            cuisineType += item + ", ";
        }

        for(String item: recipe.get(0).getRecipeModel().getMealType()) {
            mealType += item + ", ";
        }

        for(String item: recipe.get(0).getRecipeModel().getDishType()) {
            dishType += item + ", ";
        }

        cuisine.setText(cuisineType.substring(0, cuisineType.length()-2));
        meal.setText(mealType.substring(0, mealType.length()-2));
        dish.setText(dishType.substring(0, dishType.length()-2));

        List<String> chips = new ArrayList<>(Arrays.asList(recipe.get(0).getRecipeModel().getHealthLabels()));
        Context context = chipGroup.getContext();
        for(String ch: chips) {
            Chip chip = new Chip(context);
            chip.setText(ch);
            chipGroup.addView(chip);
        }

        dishName.setText(recipe.get(0).getRecipeModel().getLabel());

        Glide.with(dishImage.getContext()).load(recipe.get(0).getRecipeModel().getImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(dishImage);

        float originCalories = Float.valueOf(recipe.get(0).getRecipeModel().getCalories());
        int calories_temp= (int) originCalories;
        calories.setText(calories_temp + " calories");

        if (Float.valueOf(recipe.get(0).getRecipeModel().getTotalTime()) <= 0) {
            totalTime.setText("15min");
        } else {
            float originTime = Float.valueOf(recipe.get(0).getRecipeModel().getTotalTime());
            int time = (int) originTime;
            totalTime.setText(time + "min");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
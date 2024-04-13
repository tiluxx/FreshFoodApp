package com.application.freshfoodapp.ui.recipes.recipesdetail;

import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.STORAGE_SERVICE;

import android.graphics.Canvas;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.os.storage.StorageVolume;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.freshfoodapp.R;
import com.application.freshfoodapp.adapter.IngredientAdapter;
import com.application.freshfoodapp.databinding.FragmentIngredientBinding;
import com.application.freshfoodapp.model.Ingredient;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.ui.kitchen.sortingstrategy.SortByExpiryDateStrategy;
import com.application.freshfoodapp.ui.kitchen.sortingstrategy.SortByNameStrategy;
import com.application.freshfoodapp.ui.planner.adddish.AddDishActivity;
import com.application.freshfoodapp.ui.planner.searchdishes.SearchDishFragment;
import com.application.freshfoodapp.ui.recipes.recipesdetail.exporttemplate.FileExporter;
import com.application.freshfoodapp.ui.recipes.recipesdetail.exporttemplate.ImageExporter;
import com.application.freshfoodapp.ui.recipes.recipesdetail.exporttemplate.PdfExporter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IngredientFragment extends Fragment {
    private FragmentIngredientBinding binding;
    public static final String ARG_INGREDIENTS_DETAIL = "ingredient_detail";
    private List<RootObjectModel> recipe;
    private List<Ingredient> ingredients;
    private IngredientViewModel mViewModel;
    public static String uri = "";
    FloatingActionButton fabAddDish;
    private boolean isFABOpen = false;
    private File filePDFOutput, imageFile;
    public static final int RC_ADD_DISH = 1001;
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
                    templateExportFile(recyclerView, recipe.get(0).getRecipeModel().getLabel().toString());
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

    private void templateExportFile(RecyclerView recyclerView, String fileName) {
        Button downloadBtn;
        downloadBtn = binding.downloadBtn;

        downloadBtn.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenuInflater().inflate(R.menu.download_ingredients, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.pdfOptionItem) {
                    FileExporter filePDF = new PdfExporter();
                    if(filePDF.export(getActivity(),recyclerView, fileName))
                        Toast.makeText(getContext(), "Complete PDF export, check on Download storage", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getContext(), "Failure PDF export", Toast.LENGTH_SHORT).show();
                } else {
                    FileExporter fileIMG = new ImageExporter();
                    if(fileIMG.export(getActivity(),recyclerView, fileName))
                        Toast.makeText(getContext(), "Complete IMG export, check on Download storage", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getContext(), "Failure IMG export", Toast.LENGTH_SHORT).show();
                }
                return true;
            });
            popup.show();
        });
    }

    private void prepareData() {
        TextView dishName, calories, totalTime, cuisine, meal, dish;
        ChipGroup chipGroup;
        ImageView dishImage;

        fabAddDish = binding.addForMeal;
        dishName = binding.dishNameTextView;
        calories = binding.caloriesTextView;
        totalTime = binding.totalTimeTextView;
        dishImage = binding.dishImageView2;
        cuisine = binding.cuisineTextView2;
        meal = binding.mealTextView2;
        dish = binding.dishTextView2;
        chipGroup = binding.chipCategoryGroup;

        fabAddDish.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddDishActivity.class);
            intent.putExtra(SearchDishFragment.ARG_TYPE_MEAL, SearchDishFragment.type_of_meal.toString());

            startActivityForResult(intent, RC_ADD_DISH);
        });

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
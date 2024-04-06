package com.application.freshfoodapp.ui.recipes;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.adapter.RecipesAdapter;
import com.application.freshfoodapp.databinding.FragmentRecipesBinding;
import com.application.freshfoodapp.model.RootObjectModel;


import com.application.freshfoodapp.model.SearchRecipes;
import com.application.freshfoodapp.utils.GridSpacingItemDecoration;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.android.material.imageview.ShapeableImageView;


import com.application.freshfoodapp.utils.GridSpacingItemDecoration;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment {
    private List<RootObjectModel> recipe;
    private FragmentRecipesBinding binding;
    private RecipesViewModel mViewModel;
    public static final String ARG_COUNTRY = "country_name";

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecipesAdapter adapter = new RecipesAdapter();

        RecyclerView recyclerView = binding.carouselRecyclerView;
        int spanCount = 2;
        int spacing = 32;
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setAdapter(adapter);

        mViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);
        mViewModel.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<RootObjectModel>>() {
            @Override
            public void onChanged(List<RootObjectModel> rootObjectModels) {
                if(rootObjectModels != null) {
                    recipe = new ArrayList<>(rootObjectModels);

                    adapter.updateRecipesList(recipe);

                    Toast.makeText(getContext(), "Upload data successfully", Toast.LENGTH_SHORT).show();
                }
                if(rootObjectModels == null) {
                    Toast.makeText(getContext(), "No formula found matches your restrictions and ingredients", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipesBinding.inflate(inflater, container, false);
        prepareData();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void prepareData() {
        ShapeableImageView japanese, chinese, italian, korean;
        japanese = binding.JapaneseMealImage;
        chinese = binding.chineseMealImage;
        italian = binding.ItalianMealImage;
        korean = binding.KoreanMealImage;
        japanese.setOutlineSpotShadowColor(Color.BLACK);

        japanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(RecipesFragment.ARG_COUNTRY, CountriesRecipe.JAPANESE);
                MainActivity.getNavController().navigate(R.id.nav_country, args);
            }
        });

        chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(RecipesFragment.ARG_COUNTRY, CountriesRecipe.CHINESE);
                MainActivity.getNavController().navigate(R.id.nav_country, args);
            }
        });

        italian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(RecipesFragment.ARG_COUNTRY, CountriesRecipe.ITALIAN);
                MainActivity.getNavController().navigate(R.id.nav_country, args);
            }
        });

        korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(RecipesFragment.ARG_COUNTRY, CountriesRecipe.KOREAN);
                MainActivity.getNavController().navigate(R.id.nav_country, args);
            }
        });
    }
}
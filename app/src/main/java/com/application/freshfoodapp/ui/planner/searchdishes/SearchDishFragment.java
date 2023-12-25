package com.application.freshfoodapp.ui.planner.searchdishes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.application.freshfoodapp.R;
import com.application.freshfoodapp.adapter.RecipesVerticalAdapter;
import com.application.freshfoodapp.databinding.FragmentCountryBinding;
import com.application.freshfoodapp.databinding.FragmentSearchDishBinding;
import com.application.freshfoodapp.model.Ingredient;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.ui.recipes.RecipesFragment;
import com.application.freshfoodapp.ui.recipes.recipescountry.CountryViewModel;
import com.application.freshfoodapp.utils.GridSpacingItemDecoration;
import com.google.android.material.search.SearchBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchDishFragment extends Fragment {
    private SearchDishViewModel mViewModel;
    private FragmentSearchDishBinding binding;
    private List<RootObjectModel> recipe;
    private List<Ingredient> ingredients;
    public static String type_of_meal = "";
    public static String date_of_plan = "";
    public static String dish_uri = "";
    public static final String ARG_TYPE_MEAL = "type_of_meal";
    public static final String ARG_DATE_PLAN = "date_of_plan";
    public static final String ARG_DISH = "dish_uri";

    Bundle args;

    public SearchDishFragment() {
        // Required empty public constructor
    }

    public SearchDishViewModel getmViewModel() {
        return mViewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchDishBinding.inflate(inflater, container, false);
        args = getArguments();
        if (args != null) {
            if (args.getString(ARG_TYPE_MEAL) != null && args.getString(ARG_DATE_PLAN) != null) {
                type_of_meal = args.getString(ARG_TYPE_MEAL);
                date_of_plan = args.getString(ARG_DATE_PLAN);
            }
        }
        prepareData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create the adapter once
        RecipesVerticalAdapter adapter = new RecipesVerticalAdapter();

        // Set up the RecyclerView
        RecyclerView recyclerView = binding.listDishRecyclerView;
        int spanCount = 2;
        int spacing = 32;
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setAdapter(adapter);

        mViewModel = new ViewModelProvider(this).get(SearchDishViewModel.class);
        mViewModel.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<RootObjectModel>>() {
            @Override
            public void onChanged(List<RootObjectModel> rootObjectModels) {
                if (rootObjectModels != null) {
                    recipe = new ArrayList<>(rootObjectModels);

                    // Update the data in the adapter instead of creating a new adapter
                    adapter.updateRecipesList(recipe);

                    Toast.makeText(getActivity(), "Upload data successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "No formula found matches your dish name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchDishViewModel.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void prepareData() {
        EditText searchEditText;

        searchEditText = binding.searchDishEdtText;
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String searchText = String.valueOf(searchEditText.getText());
                    if(searchText != null) {
                        mViewModel.prepareData(searchText);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
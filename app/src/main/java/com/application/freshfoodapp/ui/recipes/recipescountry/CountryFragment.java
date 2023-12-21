package com.application.freshfoodapp.ui.recipes.recipescountry;

import android.app.ActionBar;
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
import android.widget.LinearLayout;

import com.application.freshfoodapp.R;
import com.application.freshfoodapp.adapter.RecipesVerticalAdapter;
import com.application.freshfoodapp.databinding.FragmentCountryBinding;
import com.application.freshfoodapp.model.Ingredient;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.utils.GridSpacingItemDecoration;
import com.application.freshfoodapp.ui.recipes.RecipesFragment;
import com.application.freshfoodapp.utils.GridSpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class CountryFragment extends Fragment {
    private FragmentCountryBinding binding;
    private List<RootObjectModel> recipe;
    private List<Ingredient> ingredients;
    private CountryViewModel mViewModel;
    private String country_name = null;
    Bundle args;


    public CountryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCountryBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(CountryViewModel.class);
        args = getArguments();
        if (args != null) {
            if (args.getString(RecipesFragment.ARG_COUNTRY) != null) {
                country_name = args.getString(RecipesFragment.ARG_COUNTRY);
                mViewModel.prepareData(args.getString(RecipesFragment.ARG_COUNTRY));
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CountryViewModel.class);
        mViewModel.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<RootObjectModel>>() {
            @Override
            public void onChanged(List<RootObjectModel> rootObjectModels) {
                if(rootObjectModels != null) {
                    recipe = new ArrayList<>(rootObjectModels);
                    RecipesVerticalAdapter adapter = new RecipesVerticalAdapter();
                    adapter.updateRecipesList(recipe);
                    RecyclerView recyclerView = binding.dishBaseOnCountry;
                    int spanCount = 2;
                    int spacing = 32;
                    boolean includeEdge = true;
                    recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CountryViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
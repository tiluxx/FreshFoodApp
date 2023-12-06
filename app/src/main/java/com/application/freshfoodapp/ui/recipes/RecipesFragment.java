package com.application.freshfoodapp.ui.recipes;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.freshfoodapp.R;
import com.application.freshfoodapp.adapter.KitchenAdapter;
import com.application.freshfoodapp.adapter.RecipesAdapter;
import com.application.freshfoodapp.databinding.FragmentKitchenBinding;
import com.application.freshfoodapp.databinding.FragmentRecipesBinding;
import com.application.freshfoodapp.model.APIService;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.model.SearchRecipes;
import com.application.freshfoodapp.ui.kitchen.GridSpacingItemDecoration;
import com.application.freshfoodapp.ui.kitchen.KitchenViewModel;
import com.application.freshfoodapp.viewholder.RecipesViewHolder;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesFragment extends Fragment {
    private static final String APP_ID = "fa0eb92f";
    private static final String APP_KEY = "f49ace5bca4a053d1b877b4d369a673a";
    private List<RootObjectModel> recipe;
    private FragmentRecipesBinding binding;
    private RecipesViewModel mViewModel;
    private KitchenViewModel kitchenViewModel;

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
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
        kitchenViewModel = new ViewModelProvider(this).get(KitchenViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void prepareData() {
        APIService.apiService.searchRecipes(APP_ID, APP_KEY, "salad").enqueue(new Callback<SearchRecipes>() {
            @Override
            public void onResponse(Call<SearchRecipes> call, Response<SearchRecipes> response) {
                recipe = new ArrayList<>(Arrays.asList(response.body().getFoodRecipes()));
                RecipesAdapter adapter = new RecipesAdapter();
                adapter.updateRecipesList(recipe);
                RecyclerView recyclerView = binding.carouselRecyclerView;
                int spanCount = 2;
                int spacing = 32;
                boolean includeEdge = true;
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<SearchRecipes> call, Throwable t) {
                Log.v("Tag: ", t.getMessage());
            }
        });
    }
}
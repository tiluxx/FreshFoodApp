package com.application.freshfoodapp.ui.recipes.recipesdetail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.application.freshfoodapp.api.APIService;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.model.SearchRecipes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientViewModel extends ViewModel {
    private static final String APP_ID = "fa0eb92f";
    private static final String APP_KEY = "f49ace5bca4a053d1b877b4d369a673a";
    private MutableLiveData<List<RootObjectModel>> mRecipes;
    private List<RootObjectModel> recipe;

    public IngredientViewModel() {
        this.mRecipes = new MutableLiveData<>();
    }
    public MutableLiveData<List<RootObjectModel>> getRecipes() {
        return mRecipes;
    }

    public void prepareData(String uri) {
        APIService.getInstance().loadDish(APP_ID, APP_KEY,"public", uri).enqueue(new Callback<SearchRecipes>() {
            @Override
            public void onResponse(Call<SearchRecipes> call, Response<SearchRecipes> response) {
                recipe = new ArrayList<>(Arrays.asList(response.body().getFoodRecipes()));
                mRecipes.postValue(Arrays.asList(response.body().getFoodRecipes()));
            }
            @Override
            public void onFailure(Call<SearchRecipes> call, Throwable t) {
                mRecipes.postValue(null);
            }
        });
    }
}
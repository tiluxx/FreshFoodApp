package com.application.freshfoodapp.ui.recipes;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.application.freshfoodapp.api.APIService;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.model.SearchRecipes;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesViewModel extends ViewModel {
    FirebaseFirestore db;
    private static final String APP_ID = "fa0eb92f";
    private static final String APP_KEY = "f49ace5bca4a053d1b877b4d369a673a";
    private MutableLiveData<List<RootObjectModel>> mRecipes;
    private String query = "";

    public RecipesViewModel() {
        this.mRecipes = new MutableLiveData<>();
        prepareData();
    }

    public MutableLiveData<List<RootObjectModel>> getRecipes() {
        return mRecipes;
    }

    private void prepareData() {
        query = "";
        List<Product> products = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("products")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Product product;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        product = document.toObject(Product.class);
                        product.setProductId(document.getId());
                        products.add(product);

                        // Create a query for search recipes api
                        List<String> ingredients = product.getProductCategorizes();
                        for(int i=0; i<ingredients.size(); i++) {
                            query += ingredients.get(i) + ",";
                        }
                    }

                    // Call api recipes from some ingredients in ProductCategorizes
                    query = query.substring(0, query.length()-1);
                    APIService.apiService.searchRecipes(APP_ID, APP_KEY,"public", query).enqueue(new Callback<SearchRecipes>() {
                        @Override
                        public void onResponse(Call<SearchRecipes> call, Response<SearchRecipes> response) {
                            mRecipes.postValue(Arrays.asList(response.body().getFoodRecipes()));
                        }
                        @Override
                        public void onFailure(Call<SearchRecipes> call, Throwable t) {
                            mRecipes.postValue(null);
                        }
                    });

                }
            });
    }
}
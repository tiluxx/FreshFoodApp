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
import java.util.HashSet;
import java.util.List;
import java.util.Random;

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
                    query = deleteDuplicates(query.substring(0, query.length()-1));

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
    private String deleteDuplicates(String inputString) {
        Random rand = new Random();
        String[] values = inputString.split(",");
        randomizeArray(values);

        String[] valuesDeclineNumberIngredients = new String[3];
        for (int i = 0; i < valuesDeclineNumberIngredients.length; i++) {
            int index = rand.nextInt(values.length - 1);
            valuesDeclineNumberIngredients[i] = values[index];
        }

        HashSet<String> seen = new HashSet<>();
        StringBuilder result = new StringBuilder();

        for (String value : valuesDeclineNumberIngredients) {
            if (!seen.contains(value)) {
                seen.add(value);
                if (result.length() > 0) {
                    result.append(",");
                }
                result.append(value);
            }
        }

        return result.toString();
    }

    private void randomizeArray(String[] array) {
        Random rand = new Random();

        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);

            // Swap elements at i and index
            String temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }
}
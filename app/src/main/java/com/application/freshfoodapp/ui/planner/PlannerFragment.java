package com.application.freshfoodapp.ui.planner;

import static com.application.freshfoodapp.MainActivity.formatDate;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.adapter.DishAdapter;
import com.application.freshfoodapp.adapter.IngredientAdapter;
import com.application.freshfoodapp.adapter.RecipesAdapter;
import com.application.freshfoodapp.api.APIService;
import com.application.freshfoodapp.databinding.AddDishItemCardBinding;
import com.application.freshfoodapp.databinding.FragmentPlannerBinding;
import com.application.freshfoodapp.databinding.FragmentProfileBinding;
import com.application.freshfoodapp.databinding.FragmentRecipesBinding;
import com.application.freshfoodapp.databinding.FragmentSearchDishBinding;
import com.application.freshfoodapp.model.ItemOfMeal;
import com.application.freshfoodapp.model.PlanForMeal;
import com.application.freshfoodapp.model.Restriction;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.model.SearchRecipes;
import com.application.freshfoodapp.ui.planner.searchdishes.SearchDishFragment;
import com.application.freshfoodapp.ui.planner.weeklyplanner.WeeklyPlannerFragment;
import com.application.freshfoodapp.ui.recipes.RecipesFragment;
import com.application.freshfoodapp.utils.GridSpacingItemDecoration;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlannerFragment extends Fragment {
    private PlannerViewModel mViewModel;
    private static FirebaseUser curUser;
    FirebaseFirestore db;
    private FragmentPlannerBinding binding;
    Button expiryDatePickerBtn;
    private List<PlanForMeal> plans;
    private static final String APP_ID = "fa0eb92f";
    private static final String APP_KEY = "f49ace5bca4a053d1b877b4d369a673a";
    private List<RootObjectModel> recipe;
    private List<ItemOfMeal> breakfastMeal;
    private List<ItemOfMeal> breakMeal;
    private List<ItemOfMeal> lunchMeal;
    private List<ItemOfMeal> dinnerMeal;
    public PlannerFragment() {
        curUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    public static PlannerFragment newInstance() {
        return new PlannerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPlannerBinding.inflate(inflater, container, false);
        prepareData();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlannerViewModel.class);
        // TODO: Use the ViewModel
    }

    private void loadData(String date) {
        plans = new ArrayList<>();
        db.collection("plannings")
                .whereEqualTo("ownerId", curUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        PlanForMeal plan;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            plan = document.toObject(PlanForMeal.class);
                            plan.setOwnerId(document.getId());
                            plans.add(plan);
                        }
                        uploadData(plans, date);
                    }
                });
    }

    private void uploadData(List<PlanForMeal> plans, String date) {
        breakfastMeal = new ArrayList<>();
        breakMeal = new ArrayList<>();
        dinnerMeal = new ArrayList<>();
        lunchMeal = new ArrayList<>();
        boolean checkComplete = false;

        DishAdapter adapterBreakfast = new DishAdapter();
        RecyclerView recyclerView = binding.breakfastPlannerRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapterBreakfast);

        DishAdapter adapterBreak = new DishAdapter();
        RecyclerView recyclerView2 = binding.breakPlannerRecyclerView;
        recyclerView2.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView2.setAdapter(adapterBreak);

        DishAdapter adapterDinner = new DishAdapter();
        RecyclerView recyclerView3 = binding.dinnerPlannerRecyclerView;
        recyclerView3.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView3.setAdapter(adapterDinner);

        DishAdapter adapterLunch = new DishAdapter();
        RecyclerView recyclerView4 = binding.lunchPlannerRecyclerView;
        recyclerView4.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView4.setAdapter(adapterLunch);

        for(PlanForMeal plan : plans) {
            Date dateCurrent = parseFormattedDate(date);
            if (convertDateToLong(dateCurrent) == plan.getDateOfPlan()) {
                ItemOfMeal item = new ItemOfMeal();
                APIService.getInstance().loadDish(APP_ID, APP_KEY,"public", plan.getDishUri()).enqueue(new Callback<SearchRecipes>() {
                    @Override
                    public void onResponse(Call<SearchRecipes> call, Response<SearchRecipes> response) {
                        recipe = new ArrayList<>(Arrays.asList(response.body().getFoodRecipes()));
                        if (plan.getTypeOfMeal().equals("breakfast")) {
                            item.setLabelDish(recipe.get(0).getRecipeModel().getLabel());
                            item.setImage(recipe.get(0).getRecipeModel().getImage());
                            item.setNumOfServings(plan.getNumOfServings());
                            item.setUrlDish(plan.getDishUri());
                            breakfastMeal.add(item);


                        } else if (plan.getTypeOfMeal().equals("break")) {
                            item.setLabelDish(recipe.get(0).getRecipeModel().getLabel());
                            item.setImage(recipe.get(0).getRecipeModel().getImage());
                            item.setNumOfServings(plan.getNumOfServings());
                            item.setUrlDish(plan.getDishUri());
                            breakMeal.add(item);


                        } else if (plan.getTypeOfMeal().equals("lunch")) {
                            item.setLabelDish(recipe.get(0).getRecipeModel().getLabel());
                            item.setImage(recipe.get(0).getRecipeModel().getImage());
                            item.setNumOfServings(plan.getNumOfServings());
                            item.setUrlDish(plan.getDishUri());
                            lunchMeal.add(item);


                        } else if (plan.getTypeOfMeal().equals("dinner")) {
                            item.setLabelDish(recipe.get(0).getRecipeModel().getLabel());
                            item.setImage(recipe.get(0).getRecipeModel().getImage());
                            item.setNumOfServings(plan.getNumOfServings());
                            item.setUrlDish(plan.getDishUri());
                            dinnerMeal.add(item);


                        }
                        adapterBreakfast.updateDishesList(breakfastMeal);
                        adapterDinner.updateDishesList(dinnerMeal);
                        adapterBreak.updateDishesList(breakMeal);
                        adapterLunch.updateDishesList(lunchMeal);
                    }

                    @Override
                    public void onFailure(Call<SearchRecipes> call, Throwable t) {
                        Toast.makeText(getContext(), "Upload failure", Toast.LENGTH_SHORT).show();
                    }
                });
                checkComplete = true;
            }
        }

        if(checkComplete) {
            Toast.makeText(getContext(), "Upload successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareData() {
        Button breakfastBtn, breakBtn, lunchBtn, dinnerBtn, weekBtn;
        weekBtn = binding.weekBtn;
        breakfastBtn = binding.breakfastAdd;
        breakBtn = binding.breakAdd;
        lunchBtn = binding.lunchAdd;
        dinnerBtn = binding.dinnerAdd;
        expiryDatePickerBtn = binding.dateOfMealBtn2;
        expiryDatePickerBtn.setText(formatDate(Calendar.getInstance()));
        loadData(expiryDatePickerBtn.getText().toString());

        expiryDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                                String selectedDate = formatDate(selectedYear, monthOfYear, dayOfMonth);
                                expiryDatePickerBtn.setText(selectedDate);
                                loadData(expiryDatePickerBtn.getText().toString());
                            }
                        },
                        year, month, day
                );

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                calendar.add(Calendar.DAY_OF_MONTH, 30);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(WeeklyPlannerFragment.ARG_WEEK_VIEW, expiryDatePickerBtn.getText().toString());
                MainActivity.getNavController().navigate(R.id.nav_weekly_planner, args);
            }
        });

        breakfastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(SearchDishFragment.ARG_TYPE_MEAL, "breakfast");
                args.putString(SearchDishFragment.ARG_DATE_PLAN, expiryDatePickerBtn.getText().toString());
                MainActivity.getNavController().navigate(R.id.searchDishFragment, args);
            }
        });

        breakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(SearchDishFragment.ARG_TYPE_MEAL, "break");
                args.putString(SearchDishFragment.ARG_DATE_PLAN, expiryDatePickerBtn.getText().toString());
                MainActivity.getNavController().navigate(R.id.searchDishFragment, args);
            }
        });

        lunchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(SearchDishFragment.ARG_TYPE_MEAL, "lunch");
                args.putString(SearchDishFragment.ARG_DATE_PLAN, expiryDatePickerBtn.getText().toString());
                MainActivity.getNavController().navigate(R.id.searchDishFragment, args);
            }
        });

        dinnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(SearchDishFragment.ARG_TYPE_MEAL, "dinner");
                args.putString(SearchDishFragment.ARG_DATE_PLAN, expiryDatePickerBtn.getText().toString());
                MainActivity.getNavController().navigate(R.id.searchDishFragment, args);
            }
        });
    }

    private String formatDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private Date parseFormattedDate(String formattedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault());
        try {
            return dateFormat.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    private long convertDateToLong(Date date) {
        if (date != null) {
            return date.getTime();
        } else {
            return -1; // or any default value you want to use for null dates
        }
    }
}
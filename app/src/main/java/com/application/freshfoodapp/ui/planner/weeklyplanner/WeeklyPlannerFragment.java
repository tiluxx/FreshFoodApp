package com.application.freshfoodapp.ui.planner.weeklyplanner;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.adapter.WeeklyMealDishesAdapter;
import com.application.freshfoodapp.databinding.FragmentWeeklyPlannerBinding;
import com.application.freshfoodapp.model.PlanForMeal;
import com.application.freshfoodapp.model.WeeklyDish;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeeklyPlannerFragment extends Fragment {

    private WeeklyPlannerViewModel mViewModel;
    private FragmentWeeklyPlannerBinding binding;
    private static FirebaseUser curUser;
    FirebaseFirestore db;
    private List<PlanForMeal> plans;
    public static String planWeek = "";
    Bundle args;
    TextView mondayTextView, tuesdayTextView, wednesdayTextView, thursdayTextView,
            fridayTextView, saturdayTextView, sundayTextView, currentWeek;
    public static final String ARG_WEEK_VIEW = "plan_week";

    public static WeeklyPlannerFragment newInstance() {
        return new WeeklyPlannerFragment();
    }

    public WeeklyPlannerFragment() {
        curUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWeeklyPlannerBinding.inflate(inflater, container, false);
        args = getArguments();
        if (args != null) {
            if (args.getString(ARG_WEEK_VIEW) != null) {
                planWeek = args.getString(ARG_WEEK_VIEW);
            }
        }
        prepareDate();
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WeeklyPlannerViewModel.class);
        // TODO: Use the ViewModel
    }

    private void prepareDate() {
        Button navigateToPreviousButton, navigateToNextButton;
        navigateToPreviousButton = binding.navigateToPreviousButton;
        navigateToNextButton = binding.navigateToNextButton;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
        LocalDate currentDate = LocalDate.parse(planWeek, formatter);
        updateWeekDates(currentDate);

        navigateToPreviousButton.setOnClickListener(v -> {
            LocalDate date = LocalDate.parse(currentWeek.getText(), formatter);
            LocalDate previousWeekStartDate = date.minusWeeks(1);
            updateWeekDates(previousWeekStartDate);
        });

        navigateToNextButton.setOnClickListener(v -> {
            LocalDate date = LocalDate.parse(currentWeek.getText(), formatter);
            LocalDate nextWeekStartDate = date.plusWeeks(1);
            updateWeekDates(nextWeekStartDate);
        });

        currentWeek.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadData();
            }
        });
    }

    private void updateWeekDates(LocalDate startDate) {
        mondayTextView = binding.mondayTextView;
        tuesdayTextView = binding.tuesdayTextView;
        wednesdayTextView = binding.wednesdayTextView;
        thursdayTextView = binding.thursdayTextView;
        fridayTextView = binding.fridayTextView;
        saturdayTextView = binding.saturdayTextView;
        sundayTextView = binding.sundayTextView;
        currentWeek = binding.currentWeekTextView;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);

        currentWeek.setText(startDate.with(DayOfWeek.MONDAY).format(formatter));
        mondayTextView.setText("Mon, " + startDate.with(DayOfWeek.MONDAY).format(formatter));
        tuesdayTextView.setText("Tue, " + startDate.with(DayOfWeek.TUESDAY).format(formatter));
        wednesdayTextView.setText("Wed, " + startDate.with(DayOfWeek.WEDNESDAY).format(formatter));
        thursdayTextView.setText("Thu, " + startDate.with(DayOfWeek.THURSDAY).format(formatter));
        fridayTextView.setText("Fri, " + startDate.with(DayOfWeek.FRIDAY).format(formatter));
        saturdayTextView.setText("Sat, " + startDate.with(DayOfWeek.SATURDAY).format(formatter));
        sundayTextView.setText("Sun, " + startDate.with(DayOfWeek.SUNDAY).format(formatter));
    }

    private void loadData() {
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
                        updateData(plans);
                    }
                });
    }

    private void updateData(List<PlanForMeal> plans) {
        WeeklyDish monBreakfastDishes = new WeeklyDish("Breakfast");
        WeeklyDish monLunchDishes = new WeeklyDish("Lunch");
        WeeklyDish monBreakDishes = new WeeklyDish("Break");
        WeeklyDish monDinnerDishes = new WeeklyDish("Dinner");

        WeeklyDish tueBreakfastDishes = new WeeklyDish("Breakfast");
        WeeklyDish tueLunchDishes = new WeeklyDish("Lunch");
        WeeklyDish tueBreakDishes = new WeeklyDish("Break");
        WeeklyDish tueDinnerDishes = new WeeklyDish("Dinner");

        WeeklyDish wedBreakfastDishes = new WeeklyDish("Breakfast");
        WeeklyDish wedLunchDishes = new WeeklyDish("Lunch");
        WeeklyDish wedBreakDishes = new WeeklyDish("Break");
        WeeklyDish wedDinnerDishes = new WeeklyDish("Dinner");

        WeeklyDish thuBreakfastDishes = new WeeklyDish("Breakfast");
        WeeklyDish thuLunchDishes = new WeeklyDish("Lunch");
        WeeklyDish thuBreakDishes = new WeeklyDish("Break");
        WeeklyDish thuDinnerDishes = new WeeklyDish("Dinner");

        WeeklyDish friBreakfastDishes = new WeeklyDish("Breakfast");
        WeeklyDish friLunchDishes = new WeeklyDish("Lunch");
        WeeklyDish friBreakDishes = new WeeklyDish("Break");
        WeeklyDish friDinnerDishes = new WeeklyDish("Dinner");

        WeeklyDish satBreakfastDishes = new WeeklyDish("Breakfast");
        WeeklyDish satLunchDishes = new WeeklyDish("Lunch");
        WeeklyDish satBreakDishes = new WeeklyDish("Break");
        WeeklyDish satDinnerDishes = new WeeklyDish("Dinner");

        WeeklyDish sunBreakfastDishes = new WeeklyDish("Breakfast");
        WeeklyDish sunLunchDishes = new WeeklyDish("Lunch");
        WeeklyDish sunBreakDishes = new WeeklyDish("Break");
        WeeklyDish sunDinnerDishes = new WeeklyDish("Dinner");

        // Monday
        WeeklyMealDishesAdapter monBreakfastAdapter = new WeeklyMealDishesAdapter();
        RecyclerView monBreakfastRecipesRecyclerView = binding.breakfastRecipesRecyclerView;
        monBreakfastRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        monBreakfastRecipesRecyclerView.setAdapter(monBreakfastAdapter);

        WeeklyMealDishesAdapter monLunchAdapter = new WeeklyMealDishesAdapter();
        RecyclerView monLunchRecipesRecyclerView = binding.breakfastRecipesRecyclerView;
        monLunchRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        monLunchRecipesRecyclerView.setAdapter(monLunchAdapter);

        WeeklyMealDishesAdapter monBreakAdapter = new WeeklyMealDishesAdapter();
        RecyclerView monBreakRecipesRecyclerView = binding.breakfastRecipesRecyclerView;
        monBreakRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        monBreakRecipesRecyclerView.setAdapter(monBreakAdapter);

        WeeklyMealDishesAdapter monDinnerAdapter = new WeeklyMealDishesAdapter();
        RecyclerView monDinnerRecipesRecyclerView = binding.breakfastRecipesRecyclerView;
        monDinnerRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        monDinnerRecipesRecyclerView.setAdapter(monDinnerAdapter);

        // Tuesday
        WeeklyMealDishesAdapter tueBreakfastAdapter = new WeeklyMealDishesAdapter();
        RecyclerView tueBreakfastRecipesRecyclerView = binding.tueBreakfastRecipesRecyclerView;
        tueBreakfastRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        tueBreakfastRecipesRecyclerView.setAdapter(tueBreakfastAdapter);

        WeeklyMealDishesAdapter tueLunchAdapter = new WeeklyMealDishesAdapter();
        RecyclerView tueLunchRecipesRecyclerView = binding.tueLunchRecipesRecyclerView;
        tueLunchRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        tueLunchRecipesRecyclerView.setAdapter(tueLunchAdapter);

        WeeklyMealDishesAdapter tueBreakAdapter = new WeeklyMealDishesAdapter();
        RecyclerView tueBreakRecipesRecyclerView = binding.tueBreakRecipesRecyclerView;
        tueBreakRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        tueBreakRecipesRecyclerView.setAdapter(tueBreakAdapter);

        WeeklyMealDishesAdapter tueDinnerAdapter = new WeeklyMealDishesAdapter();
        RecyclerView tueDinnerRecipesRecyclerView = binding.tueDinnerRecipesRecyclerView;
        tueDinnerRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        tueDinnerRecipesRecyclerView.setAdapter(tueDinnerAdapter);

        // Wednesday
        WeeklyMealDishesAdapter wedBreakfastAdapter = new WeeklyMealDishesAdapter();
        RecyclerView wedBreakfastRecipesRecyclerView = binding.wedBreakfastRecipesRecyclerView;
        wedBreakfastRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        wedBreakfastRecipesRecyclerView.setAdapter(wedBreakfastAdapter);

        WeeklyMealDishesAdapter wedLunchAdapter = new WeeklyMealDishesAdapter();
        RecyclerView wedLunchRecipesRecyclerView = binding.wedLunchRecipesRecyclerView;
        wedLunchRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        wedLunchRecipesRecyclerView.setAdapter(wedLunchAdapter);

        WeeklyMealDishesAdapter wedBreakAdapter = new WeeklyMealDishesAdapter();
        RecyclerView wedBreakRecipesRecyclerView = binding.wedBreakRecipesRecyclerView;
        wedBreakRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        wedBreakRecipesRecyclerView.setAdapter(wedBreakAdapter);

        WeeklyMealDishesAdapter wedDinnerAdapter = new WeeklyMealDishesAdapter();
        RecyclerView wedDinnerRecipesRecyclerView = binding.wedDinnerRecipesRecyclerView;
        wedDinnerRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        wedDinnerRecipesRecyclerView.setAdapter(wedDinnerAdapter);

        // Thursday
        WeeklyMealDishesAdapter thuBreakfastAdapter = new WeeklyMealDishesAdapter();
        RecyclerView thuBreakfastRecipesRecyclerView = binding.thuBreakfastRecipesRecyclerView;
        thuBreakfastRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        thuBreakfastRecipesRecyclerView.setAdapter(thuBreakfastAdapter);

        WeeklyMealDishesAdapter thuLunchAdapter = new WeeklyMealDishesAdapter();
        RecyclerView thuLunchRecipesRecyclerView = binding.thuLunchRecipesRecyclerView;
        thuLunchRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        thuLunchRecipesRecyclerView.setAdapter(thuLunchAdapter);

        WeeklyMealDishesAdapter thuBreakAdapter = new WeeklyMealDishesAdapter();
        RecyclerView thuBreakRecipesRecyclerView = binding.thuBreakRecipesRecyclerView;
        thuBreakRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        thuBreakRecipesRecyclerView.setAdapter(thuBreakAdapter);

        WeeklyMealDishesAdapter thuDinnerAdapter = new WeeklyMealDishesAdapter();
        RecyclerView thuDinnerRecipesRecyclerView = binding.thuDinnerRecipesRecyclerView;
        thuDinnerRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        thuDinnerRecipesRecyclerView.setAdapter(thuDinnerAdapter);

        // Friday
        WeeklyMealDishesAdapter friBreakfastAdapter = new WeeklyMealDishesAdapter();
        RecyclerView friBreakfastRecipesRecyclerView = binding.friBreakfastRecipesRecyclerView;
        friBreakfastRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        friBreakfastRecipesRecyclerView.setAdapter(friBreakfastAdapter);

        WeeklyMealDishesAdapter friLunchAdapter = new WeeklyMealDishesAdapter();
        RecyclerView friLunchRecipesRecyclerView = binding.friLunchRecipesRecyclerView;
        friLunchRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        friLunchRecipesRecyclerView.setAdapter(friLunchAdapter);

        WeeklyMealDishesAdapter friBreakAdapter = new WeeklyMealDishesAdapter();
        RecyclerView friBreakRecipesRecyclerView = binding.friBreakRecipesRecyclerView;
        friBreakRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        friBreakRecipesRecyclerView.setAdapter(friBreakAdapter);

        WeeklyMealDishesAdapter friDinnerAdapter = new WeeklyMealDishesAdapter();
        RecyclerView friDinnerRecipesRecyclerView = binding.friDinnerRecipesRecyclerView;
        friDinnerRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        friDinnerRecipesRecyclerView.setAdapter(friDinnerAdapter);

        // Saturday
        WeeklyMealDishesAdapter satBreakfastAdapter = new WeeklyMealDishesAdapter();
        RecyclerView satBreakfastRecipesRecyclerView = binding.satBreakfastRecipesRecyclerView;
        satBreakfastRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        satBreakfastRecipesRecyclerView.setAdapter(satBreakfastAdapter);

        WeeklyMealDishesAdapter satLunchAdapter = new WeeklyMealDishesAdapter();
        RecyclerView satLunchRecipesRecyclerView = binding.satLunchRecipesRecyclerView;
        satLunchRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        satLunchRecipesRecyclerView.setAdapter(satLunchAdapter);

        WeeklyMealDishesAdapter satBreakAdapter = new WeeklyMealDishesAdapter();
        RecyclerView satBreakRecipesRecyclerView = binding.satBreakRecipesRecyclerView;
        satBreakRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        satBreakRecipesRecyclerView.setAdapter(satBreakAdapter);

        WeeklyMealDishesAdapter satDinnerAdapter = new WeeklyMealDishesAdapter();
        RecyclerView satDinnerRecipesRecyclerView = binding.satDinnerRecipesRecyclerView;
        satDinnerRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        satDinnerRecipesRecyclerView.setAdapter(satDinnerAdapter);

        // Sunday
        WeeklyMealDishesAdapter sunBreakfastAdapter = new WeeklyMealDishesAdapter();
        RecyclerView sunBreakfastRecipesRecyclerView = binding.sunBreakfastRecipesRecyclerView;
        sunBreakfastRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        sunBreakfastRecipesRecyclerView.setAdapter(sunBreakfastAdapter);

        WeeklyMealDishesAdapter sunLunchAdapter = new WeeklyMealDishesAdapter();
        RecyclerView sunLunchRecipesRecyclerView = binding.sunLunchRecipesRecyclerView;
        sunLunchRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        sunLunchRecipesRecyclerView.setAdapter(sunLunchAdapter);

        WeeklyMealDishesAdapter sunBreakAdapter = new WeeklyMealDishesAdapter();
        RecyclerView sunBreakRecipesRecyclerView = binding.sunBreakRecipesRecyclerView;
        sunBreakRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        sunBreakRecipesRecyclerView.setAdapter(sunBreakAdapter);

        WeeklyMealDishesAdapter sunDinnerAdapter = new WeeklyMealDishesAdapter();
        RecyclerView sunDinnerRecipesRecyclerView = binding.sunDinnerRecipesRecyclerView;
        sunDinnerRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        sunDinnerRecipesRecyclerView.setAdapter(sunDinnerAdapter);

        LocalDate currentDate = LocalDate.now();
        LocalDate dayOfPlannedMeal;
        for (PlanForMeal plan : plans) {
            dayOfPlannedMeal = Instant.ofEpochMilli(plan.getDateOfPlan())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (currentDate.isEqual(dayOfPlannedMeal)) {
                if (currentDate.with(DayOfWeek.MONDAY).getDayOfWeek()
                        .compareTo(dayOfPlannedMeal.getDayOfWeek()) == 0) {
                    switch (plan.getTypeOfMeal()) {
                        case "breakfast":
                            monBreakfastDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "break":
                            monBreakDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "lunch":
                            monLunchDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        default:
                            monDinnerDishes.getDishesInDayOfWeek().add(plan);
                            break;
                    }
                } else if (currentDate.with(DayOfWeek.TUESDAY).getDayOfWeek()
                        .compareTo(dayOfPlannedMeal.getDayOfWeek()) == 0) {
                    switch (plan.getTypeOfMeal()) {
                        case "breakfast":
                            tueBreakfastDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "break":
                            tueBreakDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "lunch":
                            tueLunchDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        default:
                            tueDinnerDishes.getDishesInDayOfWeek().add(plan);
                            break;
                    }
                } else if (currentDate.with(DayOfWeek.WEDNESDAY).getDayOfWeek()
                        .compareTo(dayOfPlannedMeal.getDayOfWeek()) == 0) {
                    switch (plan.getTypeOfMeal()) {
                        case "breakfast":
                            wedBreakfastDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "break":
                            wedBreakDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "lunch":
                            wedLunchDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        default:
                            wedDinnerDishes.getDishesInDayOfWeek().add(plan);
                            break;
                    }
                } else if (currentDate.with(DayOfWeek.THURSDAY).getDayOfWeek()
                        .compareTo(dayOfPlannedMeal.getDayOfWeek()) == 0) {
                    switch (plan.getTypeOfMeal()) {
                        case "breakfast":
                            thuBreakfastDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "break":
                            thuBreakDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "lunch":
                            thuLunchDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        default:
                            thuDinnerDishes.getDishesInDayOfWeek().add(plan);
                            break;
                    }
                } else if (currentDate.with(DayOfWeek.FRIDAY).getDayOfWeek()
                        .compareTo(dayOfPlannedMeal.getDayOfWeek()) == 0) {
                    switch (plan.getTypeOfMeal()) {
                        case "breakfast":
                            friBreakfastDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "break":
                            friBreakDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "lunch":
                            friLunchDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        default:
                            friDinnerDishes.getDishesInDayOfWeek().add(plan);
                            break;
                    }
                } else if (currentDate.with(DayOfWeek.SATURDAY).getDayOfWeek()
                        .compareTo(dayOfPlannedMeal.getDayOfWeek()) == 0) {
                    switch (plan.getTypeOfMeal()) {
                        case "breakfast":
                            satBreakfastDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "break":
                            satBreakDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "lunch":
                            satLunchDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        default:
                            satDinnerDishes.getDishesInDayOfWeek().add(plan);
                            break;
                    }
                } else {
                    switch (plan.getTypeOfMeal()) {
                        case "breakfast":
                            sunBreakfastDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "break":
                            sunBreakDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        case "lunch":
                            sunLunchDishes.getDishesInDayOfWeek().add(plan);
                            break;
                        default:
                            sunDinnerDishes.getDishesInDayOfWeek().add(plan);
                            break;
                    }
                }
            }
        }

        monBreakfastAdapter.updateWeeklyMealDishesList(monBreakfastDishes.getDishesInDayOfWeek());
        monBreakAdapter.updateWeeklyMealDishesList(monBreakDishes.getDishesInDayOfWeek());
        monLunchAdapter.updateWeeklyMealDishesList(monLunchDishes.getDishesInDayOfWeek());
        monDinnerAdapter.updateWeeklyMealDishesList(monDinnerDishes.getDishesInDayOfWeek());
        if (monBreakfastDishes.getDishesInDayOfWeek().size() > 0) {
            binding.monMealTypeBreakfastEmptyTextView.setVisibility(View.GONE);
        }
        if (monBreakDishes.getDishesInDayOfWeek().size() > 0) {
            binding.monMealTypeBreakEmptyTextView.setVisibility(View.GONE);
        }
        if (monLunchDishes.getDishesInDayOfWeek().size() > 0) {
            binding.monMealTypeLunchEmptyTextView.setVisibility(View.GONE);
        }
        if (monDinnerDishes.getDishesInDayOfWeek().size() > 0) {
            binding.monMealTypeDinnerEmptyTextView.setVisibility(View.GONE);
        }

        tueBreakfastAdapter.updateWeeklyMealDishesList(tueBreakfastDishes.getDishesInDayOfWeek());
        tueBreakAdapter.updateWeeklyMealDishesList(tueBreakDishes.getDishesInDayOfWeek());
        tueLunchAdapter.updateWeeklyMealDishesList(tueLunchDishes.getDishesInDayOfWeek());
        tueDinnerAdapter.updateWeeklyMealDishesList(tueDinnerDishes.getDishesInDayOfWeek());
        if (tueBreakfastDishes.getDishesInDayOfWeek().size() > 0) {
            binding.tueMealTypeBreakfastEmptyTextView.setVisibility(View.GONE);
        }
        if (tueBreakDishes.getDishesInDayOfWeek().size() > 0) {
            binding.tueMealTypeBreakEmptyTextView.setVisibility(View.GONE);
        }
        if (tueLunchDishes.getDishesInDayOfWeek().size() > 0) {
            binding.tueMealTypeLunchEmptyTextView.setVisibility(View.GONE);
        }
        if (tueDinnerDishes.getDishesInDayOfWeek().size() > 0) {
            binding.tueMealTypeDinnerEmptyTextView.setVisibility(View.GONE);
        }

        wedBreakfastAdapter.updateWeeklyMealDishesList(wedBreakfastDishes.getDishesInDayOfWeek());
        wedBreakAdapter.updateWeeklyMealDishesList(wedBreakDishes.getDishesInDayOfWeek());
        wedLunchAdapter.updateWeeklyMealDishesList(wedLunchDishes.getDishesInDayOfWeek());
        wedDinnerAdapter.updateWeeklyMealDishesList(wedDinnerDishes.getDishesInDayOfWeek());
        if (wedBreakfastDishes.getDishesInDayOfWeek().size() > 0) {
            binding.wedMealTypeBreakfastEmptyTextView.setVisibility(View.GONE);
        }
        if (wedBreakDishes.getDishesInDayOfWeek().size() > 0) {
            binding.wedMealTypeBreakEmptyTextView.setVisibility(View.GONE);
        }
        if (wedLunchDishes.getDishesInDayOfWeek().size() > 0) {
            binding.wedMealTypeLunchEmptyTextView.setVisibility(View.GONE);
        }
        if (wedDinnerDishes.getDishesInDayOfWeek().size() > 0) {
            binding.wedMealTypeDinnerEmptyTextView.setVisibility(View.GONE);
        }

        thuBreakfastAdapter.updateWeeklyMealDishesList(thuBreakfastDishes.getDishesInDayOfWeek());
        thuBreakAdapter.updateWeeklyMealDishesList(thuBreakDishes.getDishesInDayOfWeek());
        thuLunchAdapter.updateWeeklyMealDishesList(thuLunchDishes.getDishesInDayOfWeek());
        thuDinnerAdapter.updateWeeklyMealDishesList(thuDinnerDishes.getDishesInDayOfWeek());
        if (thuBreakfastDishes.getDishesInDayOfWeek().size() > 0) {
            binding.thuMealTypeBreakfastEmptyTextView.setVisibility(View.GONE);
        }
        if (thuBreakDishes.getDishesInDayOfWeek().size() > 0) {
            binding.thuMealTypeBreakEmptyTextView.setVisibility(View.GONE);
        }
        if (thuLunchDishes.getDishesInDayOfWeek().size() > 0) {
            binding.thuMealTypeLunchEmptyTextView.setVisibility(View.GONE);
        }
        if (thuDinnerDishes.getDishesInDayOfWeek().size() > 0) {
            binding.thuMealTypeDinnerEmptyTextView.setVisibility(View.GONE);
        }

        friBreakfastAdapter.updateWeeklyMealDishesList(friBreakfastDishes.getDishesInDayOfWeek());
        friBreakAdapter.updateWeeklyMealDishesList(friBreakDishes.getDishesInDayOfWeek());
        friLunchAdapter.updateWeeklyMealDishesList(friLunchDishes.getDishesInDayOfWeek());
        friDinnerAdapter.updateWeeklyMealDishesList(friDinnerDishes.getDishesInDayOfWeek());
        if (friBreakfastDishes.getDishesInDayOfWeek().size() > 0) {
            binding.friMealTypeBreakfastEmptyTextView.setVisibility(View.GONE);
        }
        if (friBreakDishes.getDishesInDayOfWeek().size() > 0) {
            binding.friMealTypeBreakEmptyTextView.setVisibility(View.GONE);
        }
        if (friLunchDishes.getDishesInDayOfWeek().size() > 0) {
            binding.friMealTypeLunchEmptyTextView.setVisibility(View.GONE);
        }
        if (friDinnerDishes.getDishesInDayOfWeek().size() > 0) {
            binding.friMealTypeDinnerEmptyTextView.setVisibility(View.GONE);
        }

        satBreakfastAdapter.updateWeeklyMealDishesList(satBreakfastDishes.getDishesInDayOfWeek());
        satBreakAdapter.updateWeeklyMealDishesList(satBreakDishes.getDishesInDayOfWeek());
        satLunchAdapter.updateWeeklyMealDishesList(satLunchDishes.getDishesInDayOfWeek());
        satDinnerAdapter.updateWeeklyMealDishesList(satDinnerDishes.getDishesInDayOfWeek());
        if (satBreakfastDishes.getDishesInDayOfWeek().size() > 0) {
            binding.satMealTypeBreakfastEmptyTextView.setVisibility(View.GONE);
        }
        if (satBreakDishes.getDishesInDayOfWeek().size() > 0) {
            binding.satMealTypeBreakEmptyTextView.setVisibility(View.GONE);
        }
        if (satLunchDishes.getDishesInDayOfWeek().size() > 0) {
            binding.satMealTypeLunchEmptyTextView.setVisibility(View.GONE);
        }
        if (satDinnerDishes.getDishesInDayOfWeek().size() > 0) {
            binding.satMealTypeDinnerEmptyTextView.setVisibility(View.GONE);
        }

        sunBreakfastAdapter.updateWeeklyMealDishesList(sunBreakfastDishes.getDishesInDayOfWeek());
        sunBreakAdapter.updateWeeklyMealDishesList(sunBreakDishes.getDishesInDayOfWeek());
        sunLunchAdapter.updateWeeklyMealDishesList(sunLunchDishes.getDishesInDayOfWeek());
        sunDinnerAdapter.updateWeeklyMealDishesList(sunDinnerDishes.getDishesInDayOfWeek());
        if (sunBreakfastDishes.getDishesInDayOfWeek().size() > 0) {
            binding.sunMealTypeBreakfastEmptyTextView.setVisibility(View.GONE);
        }
        if (sunBreakDishes.getDishesInDayOfWeek().size() > 0) {
            binding.sunMealTypeBreakEmptyTextView.setVisibility(View.GONE);
        }
        if (sunLunchDishes.getDishesInDayOfWeek().size() > 0) {
            binding.sunMealTypeLunchEmptyTextView.setVisibility(View.GONE);
        }
        if (sunDinnerDishes.getDishesInDayOfWeek().size() > 0) {
            binding.sunMealTypeDinnerEmptyTextView.setVisibility(View.GONE);
        }
    }
}
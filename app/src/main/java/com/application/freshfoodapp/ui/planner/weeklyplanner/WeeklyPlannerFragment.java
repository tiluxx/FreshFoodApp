package com.application.freshfoodapp.ui.planner.weeklyplanner;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.application.freshfoodapp.R;
import com.application.freshfoodapp.adapter.DishAdapter;
import com.application.freshfoodapp.api.APIService;
import com.application.freshfoodapp.databinding.FragmentSearchDishBinding;
import com.application.freshfoodapp.databinding.FragmentWeeklyPlannerBinding;
import com.application.freshfoodapp.databinding.WeeklyMealListItemBinding;
import com.application.freshfoodapp.model.ItemOfMeal;
import com.application.freshfoodapp.model.PlanForMeal;
import com.application.freshfoodapp.model.SearchRecipes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeeklyPlannerFragment extends Fragment {

    private WeeklyPlannerViewModel mViewModel;
    private FragmentWeeklyPlannerBinding binding;
    private static FirebaseUser curUser;
    FirebaseFirestore db;
    private List<PlanForMeal> plans;
    public static String planWeek = "";
    private static final String APP_ID = "fa0eb92f";
    private static final String APP_KEY = "f49ace5bca4a053d1b877b4d369a673a";
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
        return binding.getRoot();    }

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

        navigateToPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDate date = LocalDate.parse(currentWeek.getText(), formatter);
                LocalDate previousWeekStartDate = date.minusWeeks(1);
                updateWeekDates(previousWeekStartDate);
            }
        });

        navigateToNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDate date = LocalDate.parse(currentWeek.getText(), formatter);
                LocalDate nextWeekStartDate = date.plusWeeks(1);
                updateWeekDates(nextWeekStartDate);
            }
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
        DishAdapter adapterMonday = new DishAdapter();
        RecyclerView recyclerView = binding.plannerMondayRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapterMonday);

        DishAdapter adapterTuesday = new DishAdapter();
        RecyclerView recyclerView2 = binding.plannerTuesdayRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapterTuesday);

        DishAdapter adapterWednesday = new DishAdapter();
        RecyclerView recyclerView3 = binding.plannerWednesdayRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapterWednesday);

        DishAdapter adapterThursday = new DishAdapter();
        RecyclerView recyclerView4 = binding.plannerThursdayRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapterThursday);

        DishAdapter adapterFriday = new DishAdapter();
        RecyclerView recyclerView5 = binding.plannerFridayRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapterFriday);

        DishAdapter adapterSaturday = new DishAdapter();
        RecyclerView recyclerView6 = binding.plannerSaturdayRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapterSaturday);

        DishAdapter adapterSunday = new DishAdapter();
        RecyclerView recyclerView7 = binding.plannerSundayRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapterSunday);

        for(PlanForMeal plan : plans) {

        }
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
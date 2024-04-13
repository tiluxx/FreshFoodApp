package com.application.freshfoodapp.ui.planner.adddish;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.databinding.ActivityAddingProductBinding;
import com.application.freshfoodapp.databinding.AddDishItemCardBinding;
import com.application.freshfoodapp.model.PlanForMeal;
import com.application.freshfoodapp.model.Restriction;
import com.application.freshfoodapp.ui.planner.searchdishes.SearchDishFragment;
import com.application.freshfoodapp.ui.recipes.recipesdetail.IngredientFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
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
import java.util.Collections;
import java.util.Date;

public class AddDishActivity extends AppCompatActivity {
    private AddDishItemCardBinding binding;
    private long expiryDatePicked = -1;
    private static FirebaseUser curUser;
    TextView numberOfServings;
    Button cancelAddingBtn, confirmAddingBtn, expiryDatePickerBtn, addBtn, removeBtn;
    MaterialButton breakfastBtn, breakBtn, lunchBtn, dinnerBtn;
    MaterialButtonToggleGroup toggleGroupButton;
    FirebaseFirestore db;
    MaterialDatePicker<Long> datePicker;
    public AddDishActivity() {
        curUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddDishItemCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        cancelAddingBtn = binding.cancelAddingBtn;
        confirmAddingBtn = binding.confirmAddingBtn;
        breakfastBtn = (MaterialButton) binding.breakfastBtn;
        breakBtn = (MaterialButton) binding.breakBtn;
        lunchBtn = (MaterialButton) binding.lunchBtn;
        dinnerBtn = (MaterialButton) binding.dinnerBtn;
        expiryDatePickerBtn = binding.dateOfMealBtn;
        numberOfServings = binding.numOfServingsTextView;
        toggleGroupButton = binding.toggleButton;
        addBtn = binding.addForMeal3;
        removeBtn = binding.addForMeal2;

        String type_of_meal = SearchDishFragment.type_of_meal;
        String date_of_plan = SearchDishFragment.date_of_plan;

        toggleGroupButton.setSingleSelection(true);
        toggleGroupButton.setSelectionRequired(true);

        if(type_of_meal != null) {
            if(type_of_meal.equals("breakfast")) {
                breakfastBtn.setChecked(true);
                breakBtn.setEnabled(false);
                dinnerBtn.setEnabled(false);
                lunchBtn.setEnabled(false);
            } else if(type_of_meal.equals("break")) {
                breakBtn.setChecked(true);
                breakfastBtn.setEnabled(false);
                dinnerBtn.setEnabled(false);
                lunchBtn.setEnabled(false);
            } else if(type_of_meal.equals("lunch")) {
                lunchBtn.setChecked(true);
                breakfastBtn.setEnabled(false);
                breakBtn.setEnabled(false);
                dinnerBtn.setEnabled(false);
            } else if(type_of_meal.equals("dinner")) {
                dinnerBtn.setChecked(true);
                breakfastBtn.setEnabled(false);
                breakBtn.setEnabled(false);
                lunchBtn.setEnabled(false);
            }
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(numberOfServings.getText().toString());
                if(num > 0) {
                    num += 1;
                    numberOfServings.setText(String.valueOf(num));
                }
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(numberOfServings.getText().toString());
                if(num > 1) {
                    num -= 1;
                    numberOfServings.setText(String.valueOf(num));
                }
            }
        });

        if(date_of_plan != null && !date_of_plan.isEmpty()) {
            Date initialDate = parseFormattedDate(date_of_plan);
            datePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Set expiry date")
                            .setSelection(initialDate.getTime())
                            .build();

            expiryDatePickerBtn.setText(date_of_plan);
        } else {
            CalendarConstraints.Builder constraintsBuilder =
                    new CalendarConstraints.Builder()
                            .setValidator(DateValidatorPointForward.now());

            datePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Set expiry date")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .setCalendarConstraints(constraintsBuilder.build())
                            .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                expiryDatePicked = selection;
                expiryDatePickerBtn.setText(datePicker.getHeaderText());
            });

            expiryDatePickerBtn.setOnClickListener(v -> datePicker.show(getSupportFragmentManager(), "DATE_PICKER"));
        }

        confirmAddingBtn.setOnClickListener(v -> {
            if(expiryDatePickerBtn.getText().toString() == null) {
                Toast.makeText(this, "Please set a planning date", Toast.LENGTH_SHORT).show();
            } else if(!breakBtn.isChecked() && !breakfastBtn.isChecked() &&
                    !lunchBtn.isChecked() && !dinnerBtn.isChecked()) {
                Toast.makeText(this, "Please choose a type of meal", Toast.LENGTH_SHORT).show();
            } else if(Integer.parseInt(numberOfServings.getText().toString()) <= 0) {
                Toast.makeText(this, "Please add number of servings", Toast.LENGTH_SHORT).show();
            } else {
                Date initialDate = parseFormattedDate(expiryDatePickerBtn.getText().toString());
                String typeOfMeal = "";
                if(breakfastBtn.isChecked()) {
                    typeOfMeal = "breakfast";
                } else if(breakBtn.isChecked()) {
                    typeOfMeal = "break";
                } else if(lunchBtn.isChecked()) {
                    typeOfMeal = "lunch";
                } else if(dinnerBtn.isChecked()) {
                    typeOfMeal = "dinner";
                }
                PlanForMeal planForMeal = new PlanForMeal();
                planForMeal.setDateOfPlan(convertDateToLong(initialDate));
                planForMeal.setTypeOfMeal(typeOfMeal);
                planForMeal.setNumOfServings(numberOfServings.getText().toString());
                planForMeal.setDishUri(IngredientFragment.uri.toString());
                planForMeal.setOwnerId(curUser.getUid());
                db.collection("plannings").add(planForMeal);
                Toast.makeText(this, "Adding successfully", Toast.LENGTH_SHORT).show();
            }
        });

        cancelAddingBtn.setOnClickListener(v -> {
            finish();
        });
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
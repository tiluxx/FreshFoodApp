package com.application.freshfoodapp.ui.productdetail;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.databinding.FragmentProductDetailBinding;
import com.application.freshfoodapp.model.MetaNutrition;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.ui.updatingproduct.UpdateProductActivity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ProductDetailFragment extends Fragment {
    public static final String ARG_PRODUCT_DETAIL = "product_detail";
    public static final String REQ_PRODUCT_ID = "REQ_PRODUCT_ID";

    public static final int RC_UPDATE_PRODUCT = 2002;

    private FragmentProductDetailBinding binding;
    private ProductDetailViewModel mViewModel;

    Button editProductBtn;
    TextView
            productNameTextView,
            productCaloriesTextView,
            brandContentTextView,
            originContentTextView,
            barcodeContentTextView,
            expiryDateTextView,
            ingredientContentTextView,
            fatNutritionContentTextView,
            ironNutritionContentTextView,
            fiberNutritionContentTextView,
            sugarsNutritionContentTextView,
            calciumNutritionContentTextView;
    Product curProduct;
    Bundle args;

    public static ProductDetailFragment newInstance() {
        return new ProductDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        args = getArguments();
        if (args != null) {
            if (args.getString(ARG_PRODUCT_DETAIL) != null) {
                mViewModel.fetchProductsByProductId(args.getString(ARG_PRODUCT_DETAIL));
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.getProductByProductId().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                curProduct = product;
                prepareData();

                expiryDateTextView = binding.expiryDateTextView;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, uuuu");
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(curProduct.getExpiryDate());
                LocalDate expiryDate = convertToLocalDateViaInstant(calendar.getTime());
                expiryDateTextView.setText(expiryDate.format(formatter));
                long duration = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);

                if ((duration <= 3 && expiryDate.isAfter(LocalDate.now())) ||
                        (expiryDate.isBefore(LocalDate.now()))) {
                    int color = ContextCompat.getColor(binding.getRoot().getContext(), R.color.md_theme_light_error);
                    expiryDateTextView.setTextColor(color);
                }

                editProductBtn = binding.editProductBtn;
                editProductBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(binding.getRoot().getContext(), UpdateProductActivity.class);
                        intent.putExtra(REQ_PRODUCT_ID, curProduct.getProductId());
                        startActivityForResult(intent, RC_UPDATE_PRODUCT);
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_UPDATE_PRODUCT) {
            if (resultCode == RESULT_OK) {
                boolean isSuccess = data.getBooleanExtra(UpdateProductActivity.RES_UPDATING_PRODUCT, false);
                if (isSuccess) {
                    MainActivity.getNavController().navigate(R.id.nav_kitchen);
                    Toast.makeText(binding.getRoot().getContext(), "Update product successfully", Toast.LENGTH_SHORT).show();
                }
            } else {
                MainActivity.getNavController().navigate(R.id.nav_kitchen);
                Toast.makeText(binding.getRoot().getContext(), "Cannot update product information", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void prepareData() {
        productNameTextView = binding.productNameTextView;
        brandContentTextView = binding.brandContentTextView;
        originContentTextView = binding.originContentTextView;
        barcodeContentTextView = binding.barcodeContentTextView;
        ingredientContentTextView = binding.ingredientContentTextView;
        productCaloriesTextView = binding.energyNutritionContentTextView;
        fatNutritionContentTextView = binding.fatNutritionContentTextView;
        ironNutritionContentTextView = binding.ironNutritionContentTextView;
        fiberNutritionContentTextView = binding.fiberNutritionContentTextView;
        sugarsNutritionContentTextView = binding.sugarsNutritionContentTextView;
        calciumNutritionContentTextView = binding.calciumNutritionContentTextView;

        productNameTextView.setText(curProduct.getTitle());
        brandContentTextView.setText(curProduct.getBrand());
        originContentTextView.setText(curProduct.getMetadata().countries);
        barcodeContentTextView.setText(curProduct.getBarcode());
        ingredientContentTextView.setText(curProduct.getMetadata().ingredients);

        MetaNutrition productNutrition = curProduct.getMetaNutrition();
        String calories = productNutrition.energy + productNutrition.energyUnit;
        String fat = productNutrition.fat + productNutrition.fatUnit;
        String iron = productNutrition.iron + productNutrition.ironUnit;
        String fiber = productNutrition.fiber + productNutrition.fiberUnit;
        String sugars = productNutrition.sugars + productNutrition.sugarsUnit;
        String calcium = productNutrition.calcium + productNutrition.calciumUnit;

        productCaloriesTextView.setText(calories);
        fatNutritionContentTextView.setText(fat);
        ironNutritionContentTextView.setText(iron);
        fiberNutritionContentTextView.setText(fiber);
        sugarsNutritionContentTextView.setText(sugars);
        calciumNutritionContentTextView.setText(calcium);
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
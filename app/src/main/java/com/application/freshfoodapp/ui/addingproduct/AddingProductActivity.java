package com.application.freshfoodapp.ui.addingproduct;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.api.UPCBarcodeAPIClient;
import com.application.freshfoodapp.api.UPCBarcodeAPIInterface;
import com.application.freshfoodapp.databinding.ActivityAddingProductBinding;
import com.application.freshfoodapp.model.Product;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddingProductActivity extends AppCompatActivity {
    public static final String RES_PRODUCT_LOOKUP = "PRODUCT_LOOKUP";
    private Product mProduct;
    private ActivityAddingProductBinding binding;
    private long expiryDatePicked = -1;
    Button cancelAddingBtn, confirmAddingBtn, expiryDatePickerBtn;
    ChipGroup chipCategoryGroup, chipKitchenGroup;
    Chip chip_1, chip_2, chip_3, chip_4, chip_5, chip_kitchen_1, chip_kitchen_2, chip_kitchen_3;
    EditText expiryDateInput;
    ShapeableImageView productImageView;
    Toolbar toolbar;
    TextInputEditText
            productTitleTextInput,
            brandTextInput,
            barcodeTextInput;
    TextInputLayout
            productTitleTextInputLayout,
            brandTextInputLayout,
            barcodeTextInputLayout;
    UPCBarcodeAPIInterface apiInterface;
    FirebaseFirestore db;
    MaterialDatePicker<Long> datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddingProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        toolbar = binding.addingProductTopAppBar;

        cancelAddingBtn = binding.contentAddingProduct.cancelAddingBtn;
        confirmAddingBtn = binding.contentAddingProduct.confirmAddingBtn;

        chipCategoryGroup = binding.contentAddingProduct.viewAddingProduct.chipCategoryGroup;
        chipKitchenGroup = binding.contentAddingProduct.viewAddingProduct.chipKitchenGroup;
        chip_1 = binding.contentAddingProduct.viewAddingProduct.chip1;
        chip_2 = binding.contentAddingProduct.viewAddingProduct.chip2;
        chip_3 = binding.contentAddingProduct.viewAddingProduct.chip3;
        chip_4 = binding.contentAddingProduct.viewAddingProduct.chip4;
        chip_5 = binding.contentAddingProduct.viewAddingProduct.chip5;
        chip_kitchen_1 = binding.contentAddingProduct.viewAddingProduct.chipKitchen1;
        chip_kitchen_2 = binding.contentAddingProduct.viewAddingProduct.chipKitchen2;
        chip_kitchen_3 = binding.contentAddingProduct.viewAddingProduct.chipKitchen3;

        productImageView = binding.contentAddingProduct.viewAddingProduct.productImageView;
        productTitleTextInput = binding.contentAddingProduct.viewAddingProduct.productTitleTextInput;
        brandTextInput = binding.contentAddingProduct.viewAddingProduct.brandTextInput;
        barcodeTextInput = binding.contentAddingProduct.viewAddingProduct.barcodeTextInput;

        expiryDatePickerBtn = binding.contentAddingProduct.viewAddingProduct.expiryDatePickerBtn;
        datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Set expiry date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            expiryDatePicked = selection;
            mProduct.setExpiryDate(selection);
            expiryDatePickerBtn.setText(datePicker.getHeaderText());
        });

        Intent intent = getIntent();
        String retrievedBarcode = intent.getStringExtra(MainActivity.REQ_BARCODE_STATE);
        if (retrievedBarcode != null) {
            apiInterface = UPCBarcodeAPIClient.getClient().create(UPCBarcodeAPIInterface.class);
            Call<Product> call = apiInterface.getProductInfoWithBarcode(retrievedBarcode);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                    mProduct = response.body();
                    if (mProduct != null) {
                        if (mProduct.isSuccess()) {
                            toolbar.setSubtitle("1 match found");
                        }
                        productTitleTextInput.setText(mProduct.getTitle());
                        brandTextInput.setText(mProduct.getBrand());
                        barcodeTextInput.setText(mProduct.getBarcode());
                        mProduct.setOwnerId(MainActivity.getCurUser().getUid());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Product> call, Throwable t) {
                    Log.i("ERROR", t.toString());
                    call.cancel();
                }
            });
        }

        expiryDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });
        cancelAddingBtn.setOnClickListener(v -> sendResultActivity(false));

        confirmAddingBtn.setOnClickListener(v -> {
            if (validateAllInput()) {
                db.collection("products").add(mProduct);
                sendResultActivity(true);
            }
        });
    }

    private void sendResultActivity(boolean isSuccess) {
        if (isSuccess) {
            Intent resIntent = new Intent();
            resIntent.putExtra(RES_PRODUCT_LOOKUP, mProduct.isSuccess());
            setResult(RESULT_OK, resIntent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private boolean validateAllInput() {
        boolean isAllFullFilled = true;
        if (productTitleTextInput.getText().toString().isEmpty()) {
            productTitleTextInputLayout.setErrorEnabled(true);
            productTitleTextInputLayout.setError("Please enter a title");
            isAllFullFilled = false;
        }

        if (brandTextInput.getText().toString().isEmpty()) {
            brandTextInputLayout.setErrorEnabled(true);
            brandTextInputLayout.setError("Please enter a brand");
            isAllFullFilled = false;
        }

        if (expiryDatePicked == -1) {
            isAllFullFilled = false;
            Toast.makeText(this, "Please pick an expiry date", Toast.LENGTH_SHORT).show();
        }

        return isAllFullFilled;
    }
}
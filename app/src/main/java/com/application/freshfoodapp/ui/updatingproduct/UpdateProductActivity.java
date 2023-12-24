package com.application.freshfoodapp.ui.updatingproduct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.api.UPCBarcodeAPIInterface;
import com.application.freshfoodapp.databinding.ActivityAddingProductBinding;
import com.application.freshfoodapp.databinding.ActivityUpdatingProductBinding;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.ui.productdetail.ProductDetailFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UpdateProductActivity extends AppCompatActivity {
    public static final String RES_UPDATING_PRODUCT = "UPDATING_PRODUCT";
    private Product mProduct;
    private ActivityUpdatingProductBinding binding;
    private long expiryDatePicked = -1;
    private String selectedPantry = "";
    private List<String> categorizes;
    private List<Integer> checkedChipIds;

    Button cancelAddingBtn, confirmAddingBtn, expiryDatePickerBtn;
    ChipGroup chipCategoryGroup, chipKitchenGroup;
    Chip chip1, chip2, chip3, chip4, chip5, chipKitchen1, chipKitchen2, chipKitchen3;
    ShapeableImageView productImageView;
    Toolbar toolbar;
    TextInputEditText
            productTitleTextInput,
            brandTextInput,
            barcodeTextInput;
    TextInputLayout
            productTitleTextInputLayout,
            brandTextInputLayout;
    UPCBarcodeAPIInterface apiInterface;
    FirebaseFirestore db;
    MaterialDatePicker<Long> datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdatingProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        toolbar = binding.updatingProductTopAppBar;

        cancelAddingBtn = binding.contentUpdatingProduct.cancelUpdatingBtn;
        confirmAddingBtn = binding.contentUpdatingProduct.confirmUpdatingBtn;

        chipCategoryGroup = binding.contentUpdatingProduct.viewUpdatingProduct.chipCategoryGroup;
        chip1 = binding.contentUpdatingProduct.viewUpdatingProduct.chip1;
        chip2 = binding.contentUpdatingProduct.viewUpdatingProduct.chip2;
        chip3 = binding.contentUpdatingProduct.viewUpdatingProduct.chip3;
        chip4 = binding.contentUpdatingProduct.viewUpdatingProduct.chip4;
        chip5 = binding.contentUpdatingProduct.viewUpdatingProduct.chip5;
        chipKitchenGroup = binding.contentUpdatingProduct.viewUpdatingProduct.chipKitchenGroup;
        chipKitchen1 = binding.contentUpdatingProduct.viewUpdatingProduct.chipKitchen1;
        chipKitchen2 = binding.contentUpdatingProduct.viewUpdatingProduct.chipKitchen2;
        chipKitchen3 = binding.contentUpdatingProduct.viewUpdatingProduct.chipKitchen3;

        productImageView = binding.contentUpdatingProduct.viewUpdatingProduct.productImageView;
        productTitleTextInput = binding.contentUpdatingProduct.viewUpdatingProduct.productTitleTextInput;
        brandTextInput = binding.contentUpdatingProduct.viewUpdatingProduct.brandTextInput;
        barcodeTextInput = binding.contentUpdatingProduct.viewUpdatingProduct.barcodeTextInput;
        expiryDatePickerBtn = binding.contentUpdatingProduct.viewUpdatingProduct.expiryDatePickerBtn;

        CalendarConstraints.Builder constraintsBuilder =
                new CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now());

        datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Set expiry date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build();

        Intent intent = getIntent();
        String productId = intent.getStringExtra(ProductDetailFragment.REQ_PRODUCT_ID);
        if (productId != null) {
            db.collection("products")
                    .document(productId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Product product = documentSnapshot.toObject(Product.class);
                        if (product != null) {
                            product.setProductId(productId);
                        }
                        mProduct = product;

                        productTitleTextInput.setText(mProduct.getTitle());
                        brandTextInput.setText(mProduct.getBrand());
                        barcodeTextInput.setText(mProduct.getBarcode());

                        List<String> categories = mProduct.getProductCategorizes();
                        for (String category : categories) {
                            if (chip1.getText().toString().equals(category)) {
                                chip1.setChecked(true);
                            }
                            if (chip2.getText().toString().equals(category)) {
                                chip2.setChecked(true);
                            }
                            if (chip3.getText().toString().equals(category)) {
                                chip3.setChecked(true);
                            }
                            if (chip4.getText().toString().equals(category)) {
                                chip4.setChecked(true);
                            }
                            if (chip5.getText().toString().equals(category)) {
                                chip5.setChecked(true);
                            }
                        }

                        String pantryType = mProduct.getPantry();
                        if (chipKitchen1.getText().toString().equals(pantryType)) {
                            chipKitchen1.setChecked(true);
                            selectedPantry = chipKitchen1.getText().toString();
                        } else if (chipKitchen2.getText().toString().equals(pantryType)) {
                            chipKitchen2.setChecked(true);
                            selectedPantry = chipKitchen2.getText().toString();
                        } else {
                            chipKitchen3.setChecked(true);
                            selectedPantry = chipKitchen3.getText().toString();
                        }
                    });
        }

        datePicker.addOnPositiveButtonClickListener(selection -> {
            expiryDatePicked = selection;
            mProduct.setExpiryDate(selection);
            expiryDatePickerBtn.setText(datePicker.getHeaderText());
        });

        toolbar.setNavigationOnClickListener(v -> sendResultActivity(false));

        chipKitchenGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            for (Integer checkedId: checkedIds) {
                Chip chip = group.findViewById(checkedId);
                selectedPantry = chip.getText().toString();
            }
        });

        expiryDatePickerBtn.setOnClickListener(v -> datePicker.show(getSupportFragmentManager(), "DATE_PICKER"));

        cancelAddingBtn.setOnClickListener(v -> sendResultActivity(false));

        confirmAddingBtn.setOnClickListener(v -> {
            if (validateAllInput()) {
                categorizes = new ArrayList<>();
                checkedChipIds = chipCategoryGroup.getCheckedChipIds();
                Chip selectedChip;
                for (Integer id : checkedChipIds) {
                    selectedChip = chipCategoryGroup.findViewById(id);
                    categorizes.add(selectedChip.getText().toString());
                }
                mProduct.setTitle(productTitleTextInput.getText().toString());
                mProduct.setBrand(brandTextInput.getText().toString());
                mProduct.setBarcode(barcodeTextInput.getText().toString());
                mProduct.setProductCategorizes(categorizes);
                mProduct.setPantry(selectedPantry);
                mProduct.setKitchenId(MainActivity.getCurKitchenId());

                db.collection("products")
                        .document(mProduct.getProductId())
                        .update(
                                "barcode", mProduct.getBarcode(),
                                "brand", mProduct.getBrand(),
                                "expiryDate", mProduct.getExpiryDate(),
                                "title", mProduct.getTitle(),
                                "pantry", mProduct.getPantry(),
                                "productCategorizes", mProduct.getProductCategorizes()
                        )
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                sendResultActivity(true);
                            } else {
                                sendResultActivity(false);
                            }
                        });
            }
        });
    }

    private void sendResultActivity(boolean isSuccess) {
        if (isSuccess) {
            Intent resIntent = new Intent();
            resIntent.putExtra(RES_UPDATING_PRODUCT, true);
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

        if (selectedPantry.isEmpty()) {
            isAllFullFilled = false;
            Toast.makeText(this, "Please select a storage space", Toast.LENGTH_SHORT).show();
        }

        if (chipCategoryGroup.getCheckedChipIds().size() < 1) {
            isAllFullFilled = false;
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
        }

        return isAllFullFilled;
    }
}
package com.application.freshfoodapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.application.freshfoodapp.ui.auth.AuthActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.application.freshfoodapp.databinding.ActivityMainBinding;
import com.application.freshfoodapp.model.Kitchen;
import com.application.freshfoodapp.ui.addingproduct.AddingProductActivity;
import com.application.freshfoodapp.ui.auth.AuthActivity;
import com.application.freshfoodapp.ui.barcodescanning.BarcodeScannerActivity;
import com.application.freshfoodapp.ui.sendinginvitation.SendingInvitationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    // Global scope
    public static final int RC_SCAN_BARCODE = 9001;
    public static final int RC_ADD_PRODUCT = 2001;
    public static final int RC_SHARING_KITCHEN = 5002;
    public static final String REQ_BARCODE_STATE = "BARCODE_COMMON_STATE";
    public static final String REQ_OWNER_ID_STATE = "OWNER_ID_STATE";

    // Other scopes
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private boolean isFABOpen = false;

    // Common states among fragments
    private String barcodeScanned;

    private static FirebaseUser curUser;
    private static NavController navController;
    private static String curKitchenId;
    private FirebaseFirestore db;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    FloatingActionButton fabScanner, fabGallery, fabManualInput, fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        fab = binding.appBarMain.fab;
        fabScanner = binding.appBarMain.fabScanner;
        fabGallery = binding.appBarMain.fabGallery;
        fabManualInput = binding.appBarMain.fabManualInput;
        fabScanner.hide();
        fabGallery.hide();
        fabManualInput.hide();

        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        getBarcodeValueFromImage(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        curUser = FirebaseAuth.getInstance().getCurrentUser();
        if (curUser != null) {
            db.collection("kitchens")
                    .whereEqualTo("ownerId", curUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    curKitchenId = document.getId();
                                }
                            } else {
                                createNewKitchen();
                            }
                        } else {
                            Snackbar.make(fab, "Cannot get your kitchen", Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    });
            // Action bar/Tool bar
            setSupportActionBar(binding.appBarMain.toolbar);
            if (fab != null) {
                fab.setOnClickListener(v -> {
                    if(!isFABOpen){
                        showFABMenu();
                    }else{
                        closeFABMenu();
                    }
                });
            }

            if (fabScanner != null) {
                fabScanner.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, BarcodeScannerActivity.class);
                    startActivityForResult(intent, RC_SCAN_BARCODE);
                });
            }

            if (fabGallery != null) {
                fabGallery.setOnClickListener(v -> {
                    pickMedia.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build());
                });
            }

            if (fabManualInput != null) {
                fabManualInput.setOnClickListener(v -> {
                    startActivityForResult(
                            new Intent(MainActivity.this, AddingProductActivity.class),
                            RC_ADD_PRODUCT);
                });
            }

            // Navigation view
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            assert navHostFragment != null;
            navController = navHostFragment.getNavController();

            NavigationView navigationView = binding.navView;
            if (navigationView != null) {
                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_kitchen, R.id.nav_sharing, R.id.nav_recipes, R.id.nav_planner, R.id.nav_profile)
                        .setOpenableLayout(binding.drawerLayout)
                        .build();
                NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
            }

            // Bottom navigation view
            BottomNavigationView bottomNavigationView = binding.appBarMain.contentMain.bottomNavView;
            if (bottomNavigationView != null) {
                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_kitchen, R.id.nav_sharing, R.id.nav_recipes, R.id.nav_planner, R.id.nav_profile)
                        .build();
                NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
                NavigationUI.setupWithNavController(bottomNavigationView, navController);
            }
        } else {
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MainActivity.RC_SCAN_BARCODE:
                if (resultCode == RESULT_OK) {
                    barcodeScanned = data.getStringExtra(BarcodeScannerActivity.RES_BARCODE);
                    retrieveAndAddProductActivity();
                } else {
                    Toast.makeText(this, "Cannot retrieve product information", Toast.LENGTH_SHORT).show();
                }
                break;
            case MainActivity.RC_ADD_PRODUCT:
                if (resultCode == RESULT_OK) {
                    boolean isSuccess = data.getBooleanExtra(AddingProductActivity.RES_PRODUCT_LOOKUP, false);
                    if (isSuccess) {
                        navController.navigate(R.id.nav_kitchen);
                        Toast.makeText(this, "Retrieve product successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Cannot retrieve product information", Toast.LENGTH_SHORT).show();
                }
                break;
            case RC_SHARING_KITCHEN:
                if (resultCode == RESULT_OK) {
                    String receivedEmail = data.getStringExtra(SendingInvitationActivity.RES_SHARING_KITCHEN);
                    String message = "Sent invitation to " + receivedEmail;
                    Snackbar.make(fab, message, Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Snackbar.make(fab, "Cannot share your kitchen", Snackbar.LENGTH_SHORT)
                            .show();
                }
        }
    }

    private void getBarcodeValueFromImage(Uri barcodeImageUri) {
        BarcodeScanner barcodeScanner = BarcodeScanning.getClient();
        InputImage image;
        try {
            image = InputImage.fromFilePath(MainActivity.this, barcodeImageUri);
            barcodeScanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        String rawValue = null;
                        for (Barcode barcode: barcodes) {
                            rawValue = barcode.getRawValue();
                        }
                        if (rawValue != null) {
                            barcodeScanned = rawValue;
                            retrieveAndAddProductActivity();
                        } else {
                            Toast.makeText(this, "Cannot retrieve product information", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Cannot retrieve product information", Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            Log.i("ERROR", e.toString());
        }
    }

    private void retrieveAndAddProductActivity() {
        Intent intent = new Intent(MainActivity.this, AddingProductActivity.class);
        intent.putExtra(REQ_BARCODE_STATE, barcodeScanned);
        startActivityForResult(intent, RC_ADD_PRODUCT);
    }

    private void showFABMenu(){
        isFABOpen=true;
        fabScanner.show();
        fabGallery.show();
        fabManualInput.show();

        fabManualInput.animate().translationY(-getResources().getDimension(R.dimen.standard_80));
        fabGallery.animate().translationY(-getResources().getDimension(R.dimen.standard_135));
        fabScanner.animate().translationY(-getResources().getDimension(R.dimen.standard_190));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fabManualInput.animate().translationY(0);
        fabGallery.animate().translationY(0);
        fabScanner.animate().translationY(0);
        fabManualInput.hide();
        fabGallery.hide();
        fabScanner.hide();
    }

    private void createNewKitchen() {
        db.collection("kitchens")
                .add(new Kitchen(
                        curUser.getUid(),
                        curUser.getDisplayName(),
                        curUser.getPhotoUrl().getPath()
                ))
                .addOnCompleteListener(addTask -> {
                    if (addTask.isSuccessful()) {
                        curKitchenId = addTask.getResult().getId();
                    } else {
                        Snackbar.make(fab, "Cannot get your kitchen", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    public static FirebaseUser getCurUser() {
        return curUser;
    }

    public static NavController getNavController() {
        return navController;
    }

    public static String getCurKitchenId() {
        return curKitchenId;
    }

    public static void setCurKitchenId(String curKitchenId) {
        MainActivity.curKitchenId = curKitchenId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        // Using findViewById because NavigationView exists in different layout files
        // between w600dp and w1240dp
        NavigationView navView = findViewById(R.id.nav_view);
        if (navView == null) {
            // The navigation drawer already has the items including the items in the overflow menu
            // We only inflate the overflow menu if the navigation drawer isn't visible
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share_kitchen) {
            startActivityForResult(
                    new Intent(MainActivity.this, SendingInvitationActivity.class),
                    RC_SHARING_KITCHEN);
            return true;
        }

        if (id == R.id.action_notification) {
            navController.navigate(R.id.nav_notification);
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
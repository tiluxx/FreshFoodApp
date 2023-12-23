package com.application.freshfoodapp.remindbroadcast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.model.Kitchen;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.model.ProductSubKitchen;
import com.application.freshfoodapp.model.SubKitchen;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderBroadcast extends BroadcastReceiver {
    public static final String CHANNEL_ID_OFF_NOTIFICATION = "notify_offline_channel";
    private FirebaseFirestore db;
    private static List<SubKitchen> subKitchens;
    private static String curKitchenId;
    private static String displayKitchenName;
    private static String ownerEmail;
    private static final String OWNER_EMAIL = "currentOwnerEmail";
    private static final String KITCHEN_NAME = "currentKitchenName";
    private static final String KITCHEN_ID = "currentKitchenId";

    public ReminderBroadcast() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ownerEmail = intent.getStringExtra(OWNER_EMAIL);
        displayKitchenName = intent.getStringExtra(KITCHEN_NAME);
        curKitchenId = intent.getStringExtra(KITCHEN_ID);
        subOwnerIds(context);
    }

    private void subOwnerIds(Context context) {
        subKitchens = new ArrayList<>();
        db.collection("kitchens")
            .whereArrayContains("subOwnerIds", ownerEmail)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Kitchen kitchen;
                    SubKitchen subKitchen = new SubKitchen();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        kitchen = document.toObject(Kitchen.class);
                        kitchen.setKitchenId(document.getId());
                        subKitchen.setSubOwnerId(kitchen.getKitchenId());
                        subKitchen.setSubOwnerDisplayName(kitchen.getOwnerDisplayName());
                        subKitchens.add(subKitchen);
                    }

                    // Notification food expiry date
                    getExpiryDate(context);
                }
            });
    }

    private void getExpiryDate(Context context) {
        if(subKitchens != null) {
            subKitchens.add(new SubKitchen(curKitchenId, displayKitchenName));
        } else {
            subKitchens = new ArrayList<>();
            subKitchens.add(new SubKitchen(curKitchenId, displayKitchenName));
        }

        List<String> subOwnerIds = new ArrayList<>();
        for (SubKitchen subKitchen : subKitchens) {
            subOwnerIds.add(subKitchen.getSubOwnerId());
        }

        // 4 days left to expire
        Date rateDate = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000);
        Date currentDate = new Date(System.currentTimeMillis());
        List<Product> products = new ArrayList<>();
        db.collection("products")
            .whereIn("kitchenId", subOwnerIds)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Product product;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        product = document.toObject(Product.class);
                        product.setProductId(document.getId());
                        Date expiryDate = new Date(product.getExpiryDate());
                        // 3 days 1 hour left to expire
                        Date reminderDate = new Date(expiryDate.getTime() - (2 * 23 * 60 * 60 * 1000));
                        if (reminderDate.after(currentDate) && reminderDate.before(rateDate)) {
                            products.add(product);
                        }
                    }

                    List<ProductSubKitchen> productSubKitchens = new ArrayList<>();
                    ProductSubKitchen productSubKitchen;
                    List<Product> itemList;
                    for (SubKitchen subKitchen : subKitchens) {
                        itemList = new ArrayList<>();
                        productSubKitchen = new ProductSubKitchen();
                        for (Product p : products) {
                            if (subKitchen.getSubOwnerId().equals(p.getKitchenId())) {
                                itemList.add(p);
                            }
                        }
                        productSubKitchen.setSubKitchens(subKitchen);
                        productSubKitchen.setProductList(itemList);
                        productSubKitchens.add(productSubKitchen);
                    }
                    showNotification(context, productSubKitchens);
                }
            });
    }

    @SuppressLint("MissingPermission")
    private void showNotification(Context context, List<ProductSubKitchen> productSubKitchens) {
        for(ProductSubKitchen productSubKitchen : productSubKitchens) {
            if(productSubKitchen.getProductList().size() > 0) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_OFF_NOTIFICATION)
                        .setSmallIcon(R.drawable.ic_notifications_24)
                        .setContentTitle("Food Expiry Reminder")
                        .setContentText("Check " + productSubKitchen.getSubKitchens()
                                .getSubOwnerDisplayName() + "'s Kitchen")
                        .setStyle(MainActivity.addProducts(productSubKitchen.getProductList())
                                .setBigContentTitle("Don't forget to check the expiry date"))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                notificationManager.notify(productSubKitchen.getSubKitchens().getSubOwnerId().hashCode(),
                            builder.build());
            }
        }
    }
}
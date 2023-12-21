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
import com.application.freshfoodapp.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderBroadcast extends BroadcastReceiver {
    public static final String CHANNEL_ID_OFF_NOTIFICATION = "notify_offline_channel";
    private FirebaseFirestore db;
    public ReminderBroadcast() {
        db = FirebaseFirestore.getInstance();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // 4 days left to expire
        Date rateDate = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000);
        Date currentDate = new Date(System.currentTimeMillis());
        List<Product> products = new ArrayList<>();
        db.collection("products")
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
                            if(reminderDate.after(currentDate) && reminderDate.before(rateDate)) {
                                products.add(product);
                            }
                        }
                        showNotification(context, intent, products);
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void showNotification(Context context, Intent intent, List<Product> products) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_OFF_NOTIFICATION)
                .setSmallIcon(R.drawable.ic_notifications_24)
                .setContentTitle("Food Expiry Reminder")
                .setContentText("Check the expiration dates of foods")
                .setStyle(MainActivity.addProducts(products).setBigContentTitle("Don't forget to check the expiry date"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        notificationManager.notify(200, builder.build());
    }
}

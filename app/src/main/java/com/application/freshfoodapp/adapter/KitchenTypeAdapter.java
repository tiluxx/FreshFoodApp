package com.application.freshfoodapp.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.databinding.ProductItemCardBinding;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.ui.productdetail.ProductDetailFragment;
import com.application.freshfoodapp.ui.recipes.recipesdetail.IngredientFragment;
import com.application.freshfoodapp.viewholder.KitchenViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class KitchenTypeAdapter extends RecyclerView.Adapter<KitchenViewHolder> {
    private List<Product> data = new ArrayList<>();

    @NonNull
    @Override
    public KitchenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemCardBinding binding = ProductItemCardBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new KitchenViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull KitchenViewHolder holder, int position) {
        int curPosition = holder.getAdapterPosition();
        Product curProduct = data.get(curPosition);
        holder.getProductNameTextView().setText(curProduct.getTitle());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, uuuu");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(curProduct.getExpiryDate());
        LocalDate expiryDate = convertToLocalDateViaInstant(calendar.getTime());
        holder.getExpiryDateTextView().setText(expiryDate.format(formatter));
        long duration = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);

        if ((duration <= 3 && expiryDate.isAfter(LocalDate.now())) ||
                (expiryDate.isBefore(LocalDate.now()))) {
            int color = ContextCompat.getColor(holder.getItemView().getContext(), R.color.md_theme_light_error);
            holder.getExpiryDateTextView().setTextColor(color);
            holder.getProductNameTextView().setTextColor(color);
        }

        holder.getDeleteProductBtn().setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("products")
                    .document(curProduct.getProductId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        data.remove(curProduct);
                        notifyItemRemoved(curPosition);
                        Toast.makeText(v.getContext(), "Delete product successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(v.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.w("ERROR", "Error deleting document", e);
                    });
        });

        holder.getItemView().setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString(ProductDetailFragment.ARG_PRODUCT_DETAIL, curProduct.getProductId());
            MainActivity.getNavController().navigate(R.id.nav_product_detail, args);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateProductList(List<Product> data) {
        this.data = data;
        notifyItemRangeChanged(0, data.size());
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}

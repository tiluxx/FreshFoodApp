package com.application.freshfoodapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.freshfoodapp.databinding.ProductItemCardBinding;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.viewholder.KitchenViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class KitchenAdapter extends RecyclerView.Adapter<KitchenViewHolder> {
    private List<Product> data;

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

        holder.getDeleteProductBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("products")
                        .document(curProduct.getProductId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                data.remove(curProduct);
                                notifyItemRemoved(curPosition);
                                Toast.makeText(v.getContext(), "Delete product successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                Log.w("ERROR", "Error deleting document", e);
                            }
                        });
            }
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
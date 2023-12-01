package com.application.freshfoodapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.databinding.ProductItemCardBinding;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.viewholder.KitchenViewHolder;

import java.time.format.DateTimeFormatter;

public class KitchenAdapter extends ListAdapter<Product, KitchenViewHolder> {
    public KitchenAdapter() {
        super(new DiffUtil.ItemCallback<Product>() {
            @Override
            public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                return false;
            }
        });
    }

    @NonNull
    @Override
    public KitchenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemCardBinding binding = ProductItemCardBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new KitchenViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull KitchenViewHolder holder, int position) {
        Product curProduct = getItem(position);
        holder.getProductNameTextView().setText(curProduct.getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        holder.getExpiryDateTextView().setText(curProduct.getExpiryDate().format(formatter));
    }
}



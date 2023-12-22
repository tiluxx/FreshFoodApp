package com.application.freshfoodapp.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.databinding.ProductItemCardBinding;
import com.google.android.material.imageview.ShapeableImageView;

public class KitchenViewHolder extends RecyclerView.ViewHolder {
    private Button deleteProductBtn;
    private ShapeableImageView productImageView;
    private TextView productNameTextView, expiryDateTextView, quantityTextView;
    private View itemView;

    public KitchenViewHolder(ProductItemCardBinding binding) {
        super(binding.getRoot());
        itemView = binding.getRoot();
        deleteProductBtn = binding.deleteProductBtn;
        productImageView = binding.productImageView;
        productNameTextView = binding.productNameTextView;
        expiryDateTextView = binding.expiryDateTextView;
        quantityTextView = binding.quantityTextView;
    }

    public View getItemView() {
        return itemView;
    }

    public Button getDeleteProductBtn() {
        return deleteProductBtn;
    }

    public ShapeableImageView getProductImageView() {
        return productImageView;
    }

    public TextView getProductNameTextView() {
        return productNameTextView;
    }

    public TextView getExpiryDateTextView() {
        return expiryDateTextView;
    }

    public TextView getQuantityTextView() {
        return quantityTextView;
    }
}

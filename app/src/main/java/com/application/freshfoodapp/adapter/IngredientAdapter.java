package com.application.freshfoodapp.adapter;

import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.freshfoodapp.databinding.IngredientDetailItemCardBinding;
import com.application.freshfoodapp.model.Ingredient;
import com.application.freshfoodapp.viewholder.IngredientViewHolder;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientViewHolder> {
    private List<Ingredient> data;

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IngredientDetailItemCardBinding binding = IngredientDetailItemCardBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = data.get(position);
        Float quantity = Float.valueOf(ingredient.getQuantity());
        Float weight = Float.valueOf(ingredient.getWeight());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if(ingredient.getMeasure() != null) {
            if(ingredient.getMeasure().toString().equals("<unit>")) {
                holder.getFood().setText(ingredient.getFood() + " (" + Float.valueOf(decimalFormat.format(quantity)) + " piece)");
            } else {
                holder.getFood().setText(ingredient.getFood() + " (" + Float.valueOf(decimalFormat.format(quantity)) + " "
                        + ingredient.getMeasure() + ")");
            }
        } else {
            holder.getFood().setText(ingredient.getFood() + " (" + Float.valueOf(decimalFormat.format(quantity)) + " "
                    + ingredient.getMeasure() + ")");
        }
        holder.getWeight().setText(Float.valueOf(decimalFormat.format(weight)) + "g");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void updateIngredientList(List<Ingredient> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
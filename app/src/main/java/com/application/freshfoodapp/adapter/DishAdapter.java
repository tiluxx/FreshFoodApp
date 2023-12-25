package com.application.freshfoodapp.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.databinding.DishItemCardBinding;
import com.application.freshfoodapp.databinding.RecipesItemVerticalCardBinding;
import com.application.freshfoodapp.model.ItemOfMeal;
import com.application.freshfoodapp.model.RootObjectModel;
import com.application.freshfoodapp.ui.recipes.recipesdetail.IngredientFragment;
import com.application.freshfoodapp.viewholder.DishesViewHolder;
import com.application.freshfoodapp.viewholder.RecipesVerticalViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishesViewHolder> {
    private List<ItemOfMeal> data;
    public DishAdapter() {
        this.data = new ArrayList<>();
    }
    @NonNull
    @Override
    public DishesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DishItemCardBinding binding = DishItemCardBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new DishesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DishesViewHolder holder, int position) {
        int curPosition = holder.getAdapterPosition();
        ItemOfMeal item = data.get(curPosition);
        Glide.with(holder.getDishImage().getContext()).load(item.getImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getDishImage());

        if(item.getLabelDish().length() > 20) {
            holder.getDishName().setText(item.getLabelDish().substring(0, 21) + "...");
        } else {
            holder.getDishName().setText(item.getLabelDish());
        }

        holder.getNumOfServings().setText(item.getNumOfServings());

        holder.getRemoveDish().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("plannings")
                    .whereEqualTo("dishUri", item.getUrlDish().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Iterate through the retrieved documents and delete each one
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    deleteDocument(db.collection("plannings").document(document.getId()));
                                }
                                data.remove(item);
                                notifyItemRemoved(curPosition);
                                Toast.makeText(v.getContext(), "Delete dish successfully", Toast.LENGTH_SHORT).show();

                                if (onItemRemovedListener != null) {
                                    onItemRemovedListener.onItemRemoved();
                                }
                            }
                        }
                    });
        }
        });

        holder.getItemView().setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString(IngredientFragment.ARG_INGREDIENTS_DETAIL, item.getUrlDish());
            MainActivity.getNavController().navigate(R.id.nav_ingredient, args);
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void updateDishesList(List<ItemOfMeal> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyItemRangeChanged(0, data.size());
    }

    private void deleteDocument(DocumentReference documentRef) {
        // Delete the document
        documentRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Document deleted successfully
                } else {
                    // Handle errors
                }
            }
        });
    }

    public interface OnItemRemovedListener {
        void onItemRemoved();
    }

    private OnItemRemovedListener onItemRemovedListener;

    public void setOnItemRemovedListener(OnItemRemovedListener listener) {
        this.onItemRemovedListener = listener;
    }
}
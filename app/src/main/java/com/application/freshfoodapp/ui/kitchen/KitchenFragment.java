package com.application.freshfoodapp.ui.kitchen;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.freshfoodapp.R;
import com.application.freshfoodapp.adapter.KitchenAdapter;
import com.application.freshfoodapp.databinding.FragmentKitchenBinding;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.viewholder.KitchenViewHolder;

public class KitchenFragment extends Fragment {

    private FragmentKitchenBinding binding;

    private KitchenViewModel mViewModel;

    public static KitchenFragment newInstance() {
        return new KitchenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(KitchenViewModel.class);
        binding = FragmentKitchenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.productRecyclerviewTransform;
        ListAdapter<Product, KitchenViewHolder> adapter = new KitchenAdapter();
        recyclerView.setAdapter(adapter);
        mViewModel.getProducts().observe(getViewLifecycleOwner(), adapter::submitList);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(KitchenViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
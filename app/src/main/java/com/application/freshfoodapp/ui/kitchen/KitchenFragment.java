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
    RecyclerView recyclerView;
    KitchenAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentKitchenBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(KitchenViewModel.class);

        adapter = new KitchenAdapter();
        mViewModel.getProducts().observe(getViewLifecycleOwner(), adapter::updateProductList);
        RecyclerView recyclerView = binding.productRecyclerviewTransform;
        int spanCount = 2;
        int spacing = 32;
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
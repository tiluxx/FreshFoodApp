package com.application.freshfoodapp.ui.sharing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.adapter.SharingAdapter;
import com.application.freshfoodapp.databinding.FragmentSharingBinding;

public class SharingFragment extends Fragment {

    private FragmentSharingBinding binding;
    private SharingViewModel mViewModel;
    SharingAdapter adapter;

    public static SharingFragment newInstance() {
        return new SharingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSharingBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(SharingViewModel.class);
        mViewModel.fetchSharingKitchensByUser();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SharingAdapter();
        mViewModel
                .getSharingKitchens()
                .observe(getViewLifecycleOwner(), adapter::updateSharingKitchenList);

        RecyclerView recyclerView = binding.sharingRecyclerView;
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
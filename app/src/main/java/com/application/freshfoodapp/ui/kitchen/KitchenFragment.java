package com.application.freshfoodapp.ui.kitchen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.application.freshfoodapp.adapter.KitchenPagerAdapter;
import com.application.freshfoodapp.databinding.FragmentKitchenBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class KitchenFragment extends Fragment {

    private FragmentKitchenBinding binding;
    private String subKitchenId = null;

    private KitchenViewModel mViewModel;
    TabLayout kitchenTabLayout;
    ViewPager2 kitchenPager;

    public static final String ARG_SUB_KITCHEN = "sub_kitchen";
    public static KitchenFragment newInstance() {
        return new KitchenFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subKitchenId = getArguments().getString(ARG_SUB_KITCHEN);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentKitchenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(KitchenViewModel.class);
        KitchenPagerAdapter kitchenPagerAdapter = new KitchenPagerAdapter(this, subKitchenId);
        kitchenTabLayout = binding.kitchenTabLayout;
        kitchenPager = binding.tabKitchenPager;
        kitchenPager.setAdapter(kitchenPagerAdapter);
        new TabLayoutMediator(kitchenTabLayout, kitchenPager,
                (tab, position) -> tab.setText(KitchenPagerAdapter.kitchenTypes[position])
        ).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
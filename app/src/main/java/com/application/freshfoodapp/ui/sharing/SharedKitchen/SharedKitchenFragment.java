package com.application.freshfoodapp.ui.sharing.SharedKitchen;

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
import com.application.freshfoodapp.adapter.SharedKitchenPagerAdapter;
import com.application.freshfoodapp.databinding.FragmentSharedKitchenBinding;
import com.application.freshfoodapp.ui.kitchen.KitchenViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SharedKitchenFragment extends Fragment {

    private FragmentSharedKitchenBinding binding;
    private SharedKitchenViewModel mViewModel;

    TabLayout kitchenTabLayout;
    ViewPager2 kitchenPager;
    String sharedKitchenId = null;

    public static String ARG_SHARED_KITCHEN = "shared_kitchen";
    public static SharedKitchenFragment newInstance() {
        return new SharedKitchenFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            sharedKitchenId = args.getString(SharedKitchenFragment.ARG_SHARED_KITCHEN);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSharedKitchenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SharedKitchenViewModel.class);
        SharedKitchenPagerAdapter kitchenPagerAdapter = new SharedKitchenPagerAdapter(this, sharedKitchenId);
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
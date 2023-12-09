package com.application.freshfoodapp.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.application.freshfoodapp.ui.kitchen.kitchentype.TabKitchenTypeFragment;
import com.application.freshfoodapp.ui.sharing.SharedKitchen.kitchentype.TabSharedKitchenTypeFragment;

public class SharedKitchenPagerAdapter extends FragmentStateAdapter {
    private String sharedKitchenId;
    public static final String[] kitchenTypes = {"Fridge", "Pantry", "Freezer"};

    public SharedKitchenPagerAdapter(@NonNull Fragment fragment, String sharedKitchenId) {
        super(fragment);
        this.sharedKitchenId = sharedKitchenId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new TabSharedKitchenTypeFragment();
        Bundle args = new Bundle();
        args.putString(TabSharedKitchenTypeFragment.ARG_OBJECT, kitchenTypes[position]);
        args.putString(TabSharedKitchenTypeFragment.ARG_SHARED_KITCHEN_ID, sharedKitchenId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return kitchenTypes.length;
    }
}

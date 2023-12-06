package com.application.freshfoodapp.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.application.freshfoodapp.ui.kitchen.KitchenFragment;
import com.application.freshfoodapp.ui.kitchen.kitchentype.TabKitchenTypeFragment;

public class KitchenPagerAdapter extends FragmentStateAdapter {
    private String subKitchenId = null;
    public static final String[] kitchenTypes = {"Fridge", "Pantry", "Freezer"};

    public KitchenPagerAdapter(@NonNull Fragment fragment, String subKitchenId) {
        super(fragment);
        if (subKitchenId != null) {
            this.subKitchenId = subKitchenId;
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new TabKitchenTypeFragment();
        Bundle args = new Bundle();
        args.putString(TabKitchenTypeFragment.ARG_OBJECT, kitchenTypes[position]);
        if (this.subKitchenId != null) {
            args.putString(KitchenFragment.ARG_SUB_KITCHEN, subKitchenId);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return kitchenTypes.length;
    }
}

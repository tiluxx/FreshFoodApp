package com.application.freshfoodapp.ui.kitchen.kitchentype;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.adapter.KitchenTypeAdapter;
import com.application.freshfoodapp.databinding.FragmentKitchenTypeBinding;
import com.application.freshfoodapp.ui.kitchen.sortingstrategy.ProductSorter;
import com.application.freshfoodapp.ui.kitchen.sortingstrategy.SortByExpiryDateStrategy;
import com.application.freshfoodapp.ui.kitchen.sortingstrategy.SortByNameStrategy;
import com.application.freshfoodapp.ui.kitchen.sortingstrategy.SortStrategy;
import com.application.freshfoodapp.utils.GridSpacingItemDecoration;
import com.google.android.material.tabs.TabLayout;

public class TabKitchenTypeFragment extends Fragment {

    public static final String ARG_OBJECT = "object";
    private FragmentKitchenTypeBinding binding;
    private TabKitchenTypeViewModel mViewModel;
    private ProductSorter productSorter;

    Button sortButton;
    KitchenTypeAdapter adapter;
    TabLayout tabLayout;
    Bundle args;

    public static TabKitchenTypeFragment newInstance() {
        return new TabKitchenTypeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentKitchenTypeBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(TabKitchenTypeViewModel.class);
        args = getArguments();
        mViewModel
                .fetchProductsByKitchenType(args.getString(ARG_OBJECT), MainActivity.getCurKitchenId());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new KitchenTypeAdapter();

        productSorter = new ProductSorter(new SortByExpiryDateStrategy());
        sortButton = binding.sortProductBtn;
        sortButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenuInflater().inflate(R.menu.product_sort_pop_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.expiryDateOptionItem) {
                    productSorter.setSortStrategy(new SortByExpiryDateStrategy());
                } else {
                    productSorter.setSortStrategy(new SortByNameStrategy());
                }
                mViewModel
                        .getProductsByKitchenType()
                        .observe(getViewLifecycleOwner(), data -> {
                            productSorter.sort(data);
                            adapter.updateProductList(data);
                        });

                return true;
            });
            popup.show();
        });

        mViewModel
                .getProductsByKitchenType()
                .observe(getViewLifecycleOwner(), data -> {
                    productSorter.sort(data);
                    adapter.updateProductList(data);
                });
        RecyclerView recyclerView = binding.productRecyclerviewTransform;
        int spanCount = 2;
        int spacing = 32;
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.application.freshfoodapp.ui.planner.weeklyplanner;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.freshfoodapp.R;

public class WeeklyPlannerFragment extends Fragment {

    private WeeklyPlannerViewModel mViewModel;

    public static WeeklyPlannerFragment newInstance() {
        return new WeeklyPlannerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weekly_planner, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WeeklyPlannerViewModel.class);
        // TODO: Use the ViewModel
    }

}
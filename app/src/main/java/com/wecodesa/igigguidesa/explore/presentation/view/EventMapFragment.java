package com.wecodesa.igigguidesa.explore.presentation.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wecodesa.igigguidesa.explore.presentation.viewmodel.ExploreViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventMapFragment extends Fragment {

    private ExploreViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // TODO: inflate fragment_event_map.xml here once it's created
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(ExploreViewModel.class);

        viewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            // TODO: update map markers via MapMarkerManager once that's implemented
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            // TODO: show/hide loading indicator
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            // TODO: show error message to user
        });
    }
}
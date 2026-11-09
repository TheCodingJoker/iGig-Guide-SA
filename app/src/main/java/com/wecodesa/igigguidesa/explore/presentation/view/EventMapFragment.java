package com.wecodesa.igigguidesa.explore.presentation.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.SupportMapFragment;
import com.wecodesa.igigguidesa.R;
import com.wecodesa.igigguidesa.explore.presentation.viewmodel.ExploreViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventMapFragment extends Fragment {

    private ExploreViewModel viewModel;
    private MapMarkerManager markerManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragments_event_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fixed: Use requireParentFragment() to share the ViewModel instance with ExploreFragment and EventListFragment
        viewModel = new ViewModelProvider(requireParentFragment()).get(ExploreViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                markerManager = new MapMarkerManager(googleMap);
                observeViewModel();
            });
        }
    }

    private void observeViewModel() {
        viewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            if (markerManager != null) {
                markerManager.updateMarkers(events);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            // TODO: show/hide loading indicator if added to fragments_event_map.xml
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            // TODO: show error message to user
        });
    }
}
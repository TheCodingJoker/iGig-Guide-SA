package com.wecodesa.igigguidesa.explore.presentation.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.wecodesa.igigguidesa.R;
import com.wecodesa.igigguidesa.explore.presentation.viewmodel.ExploreViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventListFragment extends Fragment {

    private ExploreViewModel viewModel;
    private EventListAdapter adapter;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Shared with the map: both fragments read the same ViewModel owned by ExploreFragment
        viewModel = new ViewModelProvider(requireParentFragment()).get(ExploreViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.event_recycler);
        emptyView = view.findViewById(R.id.empty_view);

        adapter = new EventListAdapter(event -> {
            Fragment parent = getParentFragment();
            if (parent instanceof EventNavigator) {
                ((EventNavigator) parent).openEventDetail(event.getId());
            }
        });
        recyclerView.setAdapter(adapter);

        viewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            adapter.submitList(events);
            boolean showEmpty = events != null && events.isEmpty();
            emptyView.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
        });
    }
}
package com.wecodesa.igigguidesa.explore.presentation.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wecodesa.igigguidesa.R;
import com.wecodesa.igigguidesa.explore.domain.model.EventType;
import com.wecodesa.igigguidesa.explore.presentation.viewmodel.ExploreViewModel;

import java.util.HashMap;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ExploreFragment extends Fragment implements EventNavigator {

    private ExploreViewModel viewModel;
    private TextInputEditText searchInput;
    private LinearProgressIndicator progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This fragment owns the ViewModel; the map and list fragments borrow it
        viewModel = new ViewModelProvider(this).get(ExploreViewModel.class);

        searchInput = view.findViewById(R.id.search_input);
        progress = view.findViewById(R.id.search_progress);

        setupSearch(view);
        setupChips(view);
        setupToggle(view);
        observeViewModel();

        if (savedInstanceState == null) {
            showChild(new EventMapFragment());
        }
    }

    private void setupSearch(View view) {
        TextInputLayout searchLayout = view.findViewById(R.id.search_layout);
        searchLayout.setEndIconOnClickListener(v -> performSearch());

        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String city = searchInput.getText() == null ? "" : searchInput.getText().toString().trim();
        if (city.isEmpty()) {
            return;
        }
        hideKeyboard();
        viewModel.searchEventsByArea(city);
    }

    private void setupChips(View view) {
        Map<Integer, EventType> chipTypes = new HashMap<>();
        chipTypes.put(R.id.chip_hotspots, EventType.HOTSPOTS);
        chipTypes.put(R.id.chip_clubs, EventType.CLUBS);
        chipTypes.put(R.id.chip_comedy, EventType.COMEDY);
        chipTypes.put(R.id.chip_restaurants, EventType.RESTAURANTS);
        chipTypes.put(R.id.chip_parks, EventType.PARKS);
        chipTypes.put(R.id.chip_churches, EventType.CHURCHES);

        ChipGroup chipGroup = view.findViewById(R.id.chip_group_types);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                viewModel.clearFilter();
            } else {
                viewModel.filterByType(chipTypes.get(checkedIds.get(0)));
            }
        });
    }

    private void setupToggle(View view) {
        MaterialButtonToggleGroup toggle = view.findViewById(R.id.view_toggle);
        toggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) {
                return;
            }
            Fragment current = getChildFragmentManager().findFragmentById(R.id.explore_content);
            if (checkedId == R.id.btn_list && !(current instanceof EventListFragment)) {
                showChild(new EventListFragment());
            } else if (checkedId == R.id.btn_map && !(current instanceof EventMapFragment)) {
                showChild(new EventMapFragment());
            }
        });
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading ->
                progress.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE));

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showChild(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.explore_content, fragment)
                .commit();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        }
    }

    @Override
    public void openEventDetail(String eventId) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .hide(this)
                .add(R.id.fragment_container, EventDetailFragment.newInstance(eventId))
                .addToBackStack(null)
                .commit();
    }
}
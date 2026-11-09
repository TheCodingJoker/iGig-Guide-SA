package com.wecodesa.igigguidesa.tickets.presentation.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wecodesa.igigguidesa.R;

public class TicketsFragment extends Fragment {

    public TicketsFragment() {
        super(R.layout.fragment_placeholder);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.placeholder_text)).setText(R.string.placeholder_tickets);
    }
}
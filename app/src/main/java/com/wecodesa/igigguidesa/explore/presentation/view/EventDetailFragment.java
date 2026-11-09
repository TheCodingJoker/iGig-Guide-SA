package com.wecodesa.igigguidesa.explore.presentation.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.wecodesa.igigguidesa.R;
import com.wecodesa.igigguidesa.explore.domain.model.Event;
import com.wecodesa.igigguidesa.explore.domain.model.TicketType;
import com.wecodesa.igigguidesa.explore.presentation.util.EventFormatter;
import com.wecodesa.igigguidesa.explore.presentation.util.TicketRedirectHandler;
import com.wecodesa.igigguidesa.explore.presentation.viewmodel.EventDetailViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventDetailFragment extends Fragment {

    private static final String ARG_EVENT_ID = "event_id";

    private EventDetailViewModel viewModel;

    private ShapeableImageView image;
    private TextView title;
    private TextView date;
    private TextView time;
    private TextView venueName;
    private TextView venueAddress;
    private TextView description;
    private LinearLayout ticketContainer;
    private MaterialButton buyButton;

    public static EventDetailFragment newInstance(String eventId) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(EventDetailViewModel.class);

        image = view.findViewById(R.id.detail_image);
        title = view.findViewById(R.id.detail_title);
        date = view.findViewById(R.id.detail_date);
        time = view.findViewById(R.id.detail_time);
        venueName = view.findViewById(R.id.detail_venue_name);
        venueAddress = view.findViewById(R.id.detail_venue_address);
        description = view.findViewById(R.id.detail_description);
        ticketContainer = view.findViewById(R.id.ticket_container);
        buyButton = view.findViewById(R.id.btn_buy);

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());

        viewModel.getEvent().observe(getViewLifecycleOwner(), this::bindEvent);

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        String eventId = requireArguments().getString(ARG_EVENT_ID);
        if (eventId != null) {
            viewModel.load(eventId);
        }
    }

    private void bindEvent(Event event) {
        if (event == null) {
            return;
        }

        title.setText(event.getTitle());
        date.setText(EventFormatter.detailDate(requireContext(), event.getStartDateTime()));
        time.setText(EventFormatter.timeRange(event.getStartDateTime(), event.getEndDateTime()));
        description.setText(event.getDescription());

        if (event.getVenue() != null) {
            venueName.setText(event.getVenue().getName());
            venueAddress.setText(event.getVenue().getAddress());
        }

        Glide.with(image)
                .load(event.getImageUrl())
                .placeholder(R.color.list_background)
                .centerCrop()
                .into(image);

        bindTicketTypes(event.getTicketTypes());

        boolean canBuy = TicketRedirectHandler.hasValidLink(event.getTicketPurchaseLink());
        buyButton.setVisibility(canBuy ? View.VISIBLE : View.GONE);
        buyButton.setOnClickListener(v ->
                TicketRedirectHandler.open(requireContext(), event.getTicketPurchaseLink()));
    }

    private void bindTicketTypes(List<TicketType> ticketTypes) {
        ticketContainer.removeAllViews();
        if (ticketTypes == null) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (TicketType type : ticketTypes) {
            View row = inflater.inflate(R.layout.item_ticket_type, ticketContainer, false);

            ((TextView) row.findViewById(R.id.ticket_name)).setText(type.getName());
            ((TextView) row.findViewById(R.id.ticket_price))
                    .setText(EventFormatter.tierPrice(requireContext(), type.getPrice()));

            TextView perks = row.findViewById(R.id.ticket_perks);
            if (type.getPerks() == null || type.getPerks().isEmpty()) {
                perks.setVisibility(View.GONE);
            } else {
                StringBuilder builder = new StringBuilder();
                for (String perk : type.getPerks()) {
                    if (builder.length() > 0) {
                        builder.append("\n");
                    }
                    builder.append("• ").append(perk);
                }
                perks.setText(builder.toString());
            }

            ticketContainer.addView(row);
        }
    }
}
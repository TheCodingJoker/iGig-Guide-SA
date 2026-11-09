package com.wecodesa.igigguidesa.explore.presentation.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.wecodesa.igigguidesa.R;
import com.wecodesa.igigguidesa.explore.domain.model.Event;
import com.wecodesa.igigguidesa.explore.presentation.util.EventFormatter;

import java.util.Objects;

public class EventListAdapter extends ListAdapter<Event, EventListAdapter.EventViewHolder> {

    public interface OnEventClickListener {
        void onEventClick(Event event);
    }

    private static final DiffUtil.ItemCallback<Event> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Event>() {
                @Override
                public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
                    return Objects.equals(oldItem.getId(), newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
                    return Objects.equals(oldItem.getTitle(), newItem.getTitle())
                            && Objects.equals(oldItem.getStartDateTime(), newItem.getStartDateTime())
                            && Objects.equals(oldItem.getImageUrl(), newItem.getImageUrl())
                            && Double.compare(oldItem.getLowestPrice(), newItem.getLowestPrice()) == 0;
                }
            };

    private final OnEventClickListener listener;

    public EventListAdapter(OnEventClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {

        private final ShapeableImageView image;
        private final TextView title;
        private final TextView city;
        private final TextView dateTime;
        private final TextView price;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.event_image);
            title = itemView.findViewById(R.id.event_title);
            city = itemView.findViewById(R.id.event_city);
            dateTime = itemView.findViewById(R.id.event_datetime);
            price = itemView.findViewById(R.id.event_price);
        }

        void bind(Event event, OnEventClickListener listener) {
            Context context = itemView.getContext();

            title.setText(event.getTitle());
            city.setText(event.getVenue() != null ? event.getVenue().getCity() : "");
            dateTime.setText(EventFormatter.listSchedule(context, event));

            String priceText = EventFormatter.priceLabel(context, event);
            if (priceText == null) {
                price.setVisibility(View.GONE);
            } else {
                price.setVisibility(View.VISIBLE);
                price.setText(priceText);
            }

            Glide.with(image)
                    .load(event.getImageUrl())
                    .placeholder(R.color.list_background)
                    .centerCrop()
                    .into(image);

            itemView.setOnClickListener(v -> listener.onEventClick(event));
        }
    }
}
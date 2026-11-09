package com.wecodesa.igigguidesa.explore.presentation.util;

import android.content.Context;

import com.wecodesa.igigguidesa.R;
import com.wecodesa.igigguidesa.explore.domain.model.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class EventFormatter {

    private EventFormatter() {
    }

    // "Tonight • 8pm - late" or "25 June • 7pm - 4am"
    public static String listSchedule(Context context, Event event) {
        Date start = event.getStartDateTime();
        if (start == null) {
            return context.getString(R.string.date_tbc);
        }
        return dayLabel(context, start) + " • " + timeRange(event.getStartDateTime(), event.getEndDateTime());
    }

    // "Tonight • 23 June" for near dates, plain "25 June" otherwise
    public static String detailDate(Context context, Date start) {
        if (start == null) {
            return context.getString(R.string.date_tbc);
        }
        String label = dayLabel(context, start);
        if (isRelative(start)) {
            return label + " • " + fullDate(start);
        }
        return label;
    }

    // "8pm - late" or "7pm - 4am"
    public static String timeRange(Date start, Date end) {
        if (start == null) {
            return "";
        }
        String from = formatTime(start);
        if (end == null) {
            return from + " - late";
        }
        return from + " - " + formatTime(end);
    }

    // "Free", "Tickets: from R130", or null when the event has no ticket tiers
    public static String priceLabel(Context context, Event event) {
        if (event.getTicketTypes() == null || event.getTicketTypes().isEmpty()) {
            return null;
        }
        if (event.isFree()) {
            return context.getString(R.string.price_free);
        }
        return context.getString(R.string.price_from, formatAmount(event.getLowestPrice()));
    }

    // "R130" for a single ticket tier pill
    public static String tierPrice(Context context, double price) {
        if (price == 0) {
            return context.getString(R.string.price_free);
        }
        return context.getString(R.string.price_amount, formatAmount(price));
    }

    private static String dayLabel(Context context, Date date) {
        Calendar target = Calendar.getInstance();
        target.setTime(date);

        Calendar today = Calendar.getInstance();
        if (isSameDay(target, today)) {
            return context.getString(target.get(Calendar.HOUR_OF_DAY) >= 17
                    ? R.string.day_tonight : R.string.day_today);
        }

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        if (isSameDay(target, tomorrow)) {
            return context.getString(R.string.day_tomorrow);
        }

        return fullDate(date);
    }

    private static boolean isRelative(Date date) {
        Calendar target = Calendar.getInstance();
        target.setTime(date);

        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);

        return isSameDay(target, today) || isSameDay(target, tomorrow);
    }

    private static boolean isSameDay(Calendar a, Calendar b) {
        return a.get(Calendar.YEAR) == b.get(Calendar.YEAR)
                && a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR);
    }

    private static String fullDate(Date date) {
        return new SimpleDateFormat("d MMMM", Locale.ENGLISH).format(date);
    }

    private static String formatTime(Date date) {
        String time = new SimpleDateFormat("h:mma", Locale.ENGLISH)
                .format(date)
                .toLowerCase(Locale.ENGLISH);
        return time.replace(":00", "");
    }

    private static String formatAmount(double amount) {
        if (amount == Math.rint(amount)) {
            return String.valueOf((long) amount);
        }
        return String.format(Locale.ENGLISH, "%.2f", amount);
    }
}
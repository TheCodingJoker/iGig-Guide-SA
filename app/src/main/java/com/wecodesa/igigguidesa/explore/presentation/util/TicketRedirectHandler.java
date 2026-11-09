package com.wecodesa.igigguidesa.explore.presentation.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.wecodesa.igigguidesa.R;

public final class TicketRedirectHandler {

    private TicketRedirectHandler() {
    }

    public static boolean hasValidLink(String link) {
        if (link == null) {
            return false;
        }
        String trimmed = link.trim().toLowerCase();
        return trimmed.startsWith("https://") || trimmed.startsWith("http://");
    }

    public static void open(Context context, String link) {
        if (!hasValidLink(link)) {
            Toast.makeText(context, R.string.ticket_link_unavailable, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link.trim())));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.ticket_link_error, Toast.LENGTH_LONG).show();
        }
    }
}
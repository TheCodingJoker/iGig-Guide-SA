
package com.wecodesa.igigguidesa.profile.presentation.view;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.wecodesa.igigguidesa.R;

public class AppNotificationHelper {

    public static final String PREFS_NAME = "profile_settings";
    public static final String KEY_NOTIFICATIONS_ENABLED =
            "notifications_enabled";

    private static final String CHANNEL_ID = "igig_app_notifications";
    private static final String CHANNEL_NAME = "iGig Guide Notifications";

    private AppNotificationHelper() {
        // Prevent object creation.
    }

    // Create the notification channel.
    public static void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channel.setDescription("Notifications about iGig Guide updates.");

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    // Check whether the user has enabled notifications.
    public static boolean areNotificationsEnabled(Context context) {

        SharedPreferences preferences =
                context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );

        boolean enabled = preferences.getBoolean(
                KEY_NOTIFICATIONS_ENABLED,
                true
        );

        if (!enabled) {
            return false;
        }

        // Android 13 and newer require permission.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            return ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED;
        }

        return NotificationManagerCompat.from(context)
                .areNotificationsEnabled();
    }

    // Display a notification only when notifications are enabled.
    public static void showNotification(
            Context context,
            String title,
            String message,
            int notificationId
    ) {

        if (!areNotificationsEnabled(context)) {
            return;
        }

        createNotificationChannel(context);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(message)
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        try {
            NotificationManagerCompat.from(context)
                    .notify(notificationId, builder.build());

        } catch (SecurityException e) {
            // Permission may have been revoked.
        }
    }
}

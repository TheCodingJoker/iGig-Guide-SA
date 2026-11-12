
package com.wecodesa.igigguidesa.profile.presentation.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.wecodesa.igigguidesa.R;

public class ProfileFragment extends Fragment {

    private static final String PREFS_NAME = "profile_settings";
    private static final String KEY_THEME = "selected_theme";
    private static final String KEY_PROFILE_IMAGE = "profile_image_uri";

    private View lightTheme, darkTheme, systemTheme;
    private SwitchCompat notificationSwitch;

    private LinearLayout logoutButton, deleteAccountButton;
    private ImageView editProfileImage;
    private ImageView profileImage;

    private SharedPreferences preferences;

    private ActivityResultLauncher<String[]> imagePickerLauncher;
    private ActivityResultLauncher<String> notificationPermissionLauncher;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open an image from the device gallery.
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null && getContext() != null) {
                        try {
                            requireContext().getContentResolver()
                                    .takePersistableUriPermission(
                                            uri,
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    );

                            profileImage.setImageURI(uri);

                            preferences.edit()
                                    .putString(KEY_PROFILE_IMAGE, uri.toString())
                                    .apply();

                        } catch (SecurityException e) {
                            Toast.makeText(
                                    requireContext(),
                                    "Unable to access the selected image.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                }
        );

        // Request notification permission on Android 13 and newer.
        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        saveNotificationPreference(true);
                        Toast.makeText(
                                requireContext(),
                                "Notifications enabled",
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        saveNotificationPreference(false);
                        notificationSwitch.setChecked(false);

                        Toast.makeText(
                                requireContext(),
                                "Notification permission was not granted.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.fragment_profile,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        preferences = requireContext().getSharedPreferences(
                PREFS_NAME,
                android.content.Context.MODE_PRIVATE
        );

        // Initialize views.
        notificationSwitch = view.findViewById(R.id.notificationSwitch);

        lightTheme = view.findViewById(R.id.lightTheme);
        darkTheme = view.findViewById(R.id.darkTheme);
        systemTheme = view.findViewById(R.id.systemTheme);

        logoutButton = view.findViewById(R.id.logoutButton);
        deleteAccountButton = view.findViewById(R.id.deleteAccountButton);

        editProfileImage = view.findViewById(R.id.editProfileImage);
        profileImage = view.findViewById(R.id.profileImage);

        // Prepare the notification channel.
        AppNotificationHelper.createNotificationChannel(requireContext());

        // Restore the saved profile picture.
        loadProfileImage();

        // Restore the saved theme.
        loadTheme();

        // Restore the notification preference.
        loadNotificationPreference();

        // Profile picture: open the gallery.
        editProfileImage.setOnClickListener(v ->
                imagePickerLauncher.launch(new String[]{"image/*"})
        );

        // Notifications.
        notificationSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> handleNotificationToggle(isChecked)
        );

        // Theme selection.
        lightTheme.setOnClickListener(v ->
                selectTheme(AppCompatDelegate.MODE_NIGHT_NO)
        );

        darkTheme.setOnClickListener(v ->
                selectTheme(AppCompatDelegate.MODE_NIGHT_YES)
        );

        systemTheme.setOnClickListener(v ->
                selectTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        );

        // Logout confirmation.
        logoutButton.setOnClickListener(v -> showLogoutConfirmation());

        // Delete account confirmation.
        deleteAccountButton.setOnClickListener(v -> showDeleteConfirmation());
    }

    // --------------------------------------------------
    // PROFILE PICTURE
    // --------------------------------------------------

    private void loadProfileImage() {
        String savedImage = preferences.getString(KEY_PROFILE_IMAGE, null);

        if (savedImage != null) {
            try {
                profileImage.setImageURI(Uri.parse(savedImage));
            } catch (Exception e) {
                preferences.edit()
                        .remove(KEY_PROFILE_IMAGE)
                        .apply();
            }
        }
    }

    // --------------------------------------------------
    // THEMES
    // --------------------------------------------------

    private void loadTheme() {
        int savedTheme = preferences.getInt(
                KEY_THEME,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        );

        updateThemeSelection(savedTheme);

        if (AppCompatDelegate.getDefaultNightMode() != savedTheme) {
            AppCompatDelegate.setDefaultNightMode(savedTheme);
        }
    }

    private void selectTheme(int mode) {

        // Save the user's selection.
        preferences.edit()
                .putInt(KEY_THEME, mode)
                .apply();

        // Update the selected theme indicator.
        updateThemeSelection(mode);

        // Apply the selected theme to the application.
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    private void updateThemeSelection(int mode) {

        // Reset all theme backgrounds.
        lightTheme.setBackgroundResource(R.drawable.theme_unselected);
        darkTheme.setBackgroundResource(R.drawable.theme_unselected);
        systemTheme.setBackgroundResource(R.drawable.theme_unselected);

        // Highlight the selected theme.
        switch (mode) {

            case AppCompatDelegate.MODE_NIGHT_NO:
                lightTheme.setBackgroundResource(R.drawable.theme_selected);
                break;

            case AppCompatDelegate.MODE_NIGHT_YES:
                darkTheme.setBackgroundResource(R.drawable.theme_selected);
                break;

            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                systemTheme.setBackgroundResource(R.drawable.theme_selected);
                break;
        }
    }

    // --------------------------------------------------
    // NOTIFICATIONS
    // --------------------------------------------------

    private void loadNotificationPreference() {
        boolean enabled = preferences.getBoolean(
                AppNotificationHelper.KEY_NOTIFICATIONS_ENABLED,
                true
        );

        // If Android permission is unavailable, do not show ON.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
                enabled = false;
            }
        }

        notificationSwitch.setChecked(enabled);
        saveNotificationPreference(enabled);
    }

    private void handleNotificationToggle(boolean isChecked) {

        if (!isChecked) {
            saveNotificationPreference(false);

            Toast.makeText(
                    requireContext(),
                    "Notifications disabled",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Android 13+ requires notification permission.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                notificationPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                );

                return;
            }
        }

        saveNotificationPreference(true);

        Toast.makeText(
                requireContext(),
                "Notifications enabled",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void saveNotificationPreference(boolean enabled) {
        preferences.edit()
                .putBoolean(
                        AppNotificationHelper.KEY_NOTIFICATIONS_ENABLED,
                        enabled
                )
                .apply();
    }

    // --------------------------------------------------
    // LOGOUT
    // --------------------------------------------------

    private void showLogoutConfirmation() {

        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Logout", (dialog, which) -> {

                    // Authentication has not been implemented yet.
                    Toast.makeText(
                            requireContext(),
                            "Logout will be available when authentication is implemented.",
                            Toast.LENGTH_LONG
                    ).show();

                })
                .show();
    }

    // --------------------------------------------------
    // DELETE ACCOUNT
    // --------------------------------------------------

    private void showDeleteConfirmation() {

        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Account")
                .setMessage(
                        "Are you sure you want to delete your account? "
                                + "This action cannot be undone."
                )
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Delete Account", (dialog, which) -> {

                    // Actual deletion requires authentication and backend support.
                    Toast.makeText(
                            requireContext(),
                            "Account deletion is not available yet.",
                            Toast.LENGTH_LONG
                    ).show();

                })
                .show();
    }
}

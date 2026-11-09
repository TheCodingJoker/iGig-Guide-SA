package com.wecodesa.igigguidesa;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wecodesa.igigguidesa.explore.presentation.view.ExploreFragment;
import com.wecodesa.igigguidesa.profile.presentation.view.ProfileFragment;
import com.wecodesa.igigguidesa.tickets.presentation.view.TicketsFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG_EXPLORE = "tab_explore";
    private static final String TAG_TICKETS = "tab_tickets";
    private static final String TAG_PROFILE = "tab_profile";

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            Fragment tickets = new TicketsFragment();
            Fragment profile = new ProfileFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, new ExploreFragment(), TAG_EXPLORE)
                    .add(R.id.fragment_container, tickets, TAG_TICKETS)
                    .hide(tickets)
                    .add(R.id.fragment_container, profile, TAG_PROFILE)
                    .hide(profile)
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            showTab(tagFor(item.getItemId()));
            return true;
        });

        // Tapping the current tab again closes the event detail screen, if one is open
        bottomNav.setOnItemReselectedListener(item ->
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                boolean detailOpen = getSupportFragmentManager().getBackStackEntryCount() > 0;
                if (!detailOpen && bottomNav.getSelectedItemId() != R.id.nav_explore) {
                    bottomNav.setSelectedItemId(R.id.nav_explore);
                } else {
                    // Let the system handle it: pops the detail screen, or exits the app
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                    setEnabled(true);
                }
            }
        });
    }

    private String tagFor(int menuItemId) {
        if (menuItemId == R.id.nav_tickets) {
            return TAG_TICKETS;
        }
        if (menuItemId == R.id.nav_profile) {
            return TAG_PROFILE;
        }
        return TAG_EXPLORE;
    }

    private void showTab(String targetTag) {
        FragmentManager fm = getSupportFragmentManager();

        // Close any open event detail screen before switching tabs
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction transaction = fm.beginTransaction().setReorderingAllowed(true);
        for (String tag : new String[]{TAG_EXPLORE, TAG_TICKETS, TAG_PROFILE}) {
            Fragment fragment = fm.findFragmentByTag(tag);
            if (fragment == null) {
                continue;
            }
            if (tag.equals(targetTag)) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }
}
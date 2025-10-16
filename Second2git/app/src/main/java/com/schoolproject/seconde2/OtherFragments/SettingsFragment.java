package com.schoolproject.seconde2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.schoolproject.seconde2.R;

public class SettingsFragment extends Fragment {

    private LinearLayout settingsContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the settings layout
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Setup exit button
        setupExitButton(view);

        settingsContainer = view.findViewById(R.id.settingsContainer);
        loadSettingsItems();

        return view;
    }

    private void setupExitButton(View view) {
        ImageView exitButton = view.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to main inbox
                if (getActivity() instanceof com.schoolproject.seconde2.activities.MainActivity) {
                    ((com.schoolproject.seconde2.activities.MainActivity) getActivity()).showMainInbox();
                }
            }
        });
    }

    private void loadSettingsItems() {
        // Account Settings section
        addSectionHeader("Account Settings");
        addSettingsItem("My profile", "Manage your personal information", R.drawable.ic_profile);

        addDivider();

        // App Settings section
        addSectionHeader("App Settings");
        addSettingsItem("Notifications", "Configure notification preferences", R.drawable.ic_notification);
        addSettingsItem("Theme", "Change app appearance", R.drawable.ic_theme);
        addSettingsItem("Languages", "Select your preferred language", R.drawable.ic_language);

        addDivider();

        // Login section
        addSectionHeader("Login");
        addSettingsItem("Log out", "Sign out of your account", R.drawable.ic_logout);
        addSettingsItem("Help & Support", "Get help and contact support", R.drawable.ic_support);
    }

    private void addSectionHeader(String title) {
        // Add a section header like "Account Settings"
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.item_settings_header, settingsContainer, false);
        TextView headerText = headerView.findViewById(R.id.settingsHeaderText);
        headerText.setText(title);
        settingsContainer.addView(headerView);
    }

    private void addSettingsItem(String title, String description, int iconRes) {
        // Add a settings item with title, description and icon
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_settings, settingsContainer, false);

        TextView titleText = itemView.findViewById(R.id.settingsItemTitle);
        TextView descText = itemView.findViewById(R.id.settingsItemDesc);
        ImageView icon = itemView.findViewById(R.id.settingsItemIcon);

        titleText.setText(title);
        descText.setText(description);
        icon.setImageResource(iconRes);

        // Set click listener for this settings item
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSettingsClick(title);
            }
        });

        settingsContainer.addView(itemView);
    }

    private void addDivider() {
        // Add a divider line between sections
        View dividerView = LayoutInflater.from(getContext()).inflate(R.layout.item_divider, settingsContainer, false);
        settingsContainer.addView(dividerView);
    }

    private void handleSettingsClick(String itemTitle) {
        // Handle clicks on different settings items
        switch (itemTitle) {
            case "My profile":
                // TODO: Open profile settings
                break;
            case "Notifications":
                // TODO: Open notification settings
                break;
            case "Theme":
                // TODO: Open theme settings
                break;
            case "Languages":
                // TODO: Open language settings
                break;
            case "Log out":
                // TODO: Handle logout
                break;
            case "Help & Support":
                // TODO: Open help and support
                break;
        }
    }
}
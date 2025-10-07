package com.schoolproject.seconde2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mainDrawer;
    private ImageView menuBtn; // Using ImageView instead of ImageButton - had some issues with ImageButton styling
    private LinearLayout composeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAllViews();
        wireUpButtons();
        setupEmailList(); // Set up email item clicks
    }

    // Initialize all the view references
    private void findAllViews() {
        mainDrawer = findViewById(R.id.drawer_layout);
        menuBtn = findViewById(R.id.rblhycly8y27); // TODO: Change this ID to something more readable
        composeBtn = findViewById(R.id.rp4smgichsuc); // Another cryptic ID - need to refactor these
    }

    private void wireUpButtons() {
        // Menu hamburger click - opens navigation drawer
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDrawer.openDrawer(findViewById(R.id.nav_custom));
            }
        });

        // Compose new email button
        composeBtn.setOnClickListener(v -> {
            Intent composeIntent = new Intent(MainActivity.this, ComposeEmailActivity.class);
            startActivity(composeIntent);
        });

        // Set up all the navigation drawer menu items
        handleNavigationMenuClicks();
    }

    // Handle clicks for navigation drawer items
    private void handleNavigationMenuClicks() {
        // Inbox - we're already here so just close drawer
        findViewById(R.id.nav_inbox).setOnClickListener(v -> {
            mainDrawer.closeDrawer(findViewById(R.id.nav_custom));
            // Could add some visual feedback here but inbox is default view anyway
        });

        // Drafts folder
        findViewById(R.id.nav_draft).setOnClickListener(v -> {
            mainDrawer.closeDrawer(findViewById(R.id.nav_custom));
            showToast("Drafts"); // Quick feedback for now
        });

        // Sent emails
        findViewById(R.id.nav_sent).setOnClickListener(v -> {
            mainDrawer.closeDrawer(findViewById(R.id.nav_custom));
            showToast("Sent");
        });

        // Trash/Deleted items
        findViewById(R.id.nav_trash).setOnClickListener(v -> {
            mainDrawer.closeDrawer(findViewById(R.id.nav_custom));
            showToast("Trash");
        });

        // Outlook integration - not implemented yet
        findViewById(R.id.nav_outlook).setOnClickListener(v -> {
            mainDrawer.closeDrawer(findViewById(R.id.nav_custom));
            showToast("Outlook");
        });

        // All mail view
        findViewById(R.id.nav_all_mail).setOnClickListener(v -> {
            mainDrawer.closeDrawer(findViewById(R.id.nav_custom));
            showToast("All Mail");
        });

        // Contracts - custom folder
        findViewById(R.id.nav_contracts).setOnClickListener(v -> {
            mainDrawer.closeDrawer(findViewById(R.id.nav_custom));
            showToast("Contracts");
        });

        // App settings
        findViewById(R.id.nav_settings).setOnClickListener(v -> {
            mainDrawer.closeDrawer(findViewById(R.id.nav_custom));
            showToast("Settings");
            // TODO: Create proper settings activity later
        });
    }

    // Set up click listeners for individual emails in the list
    private void setupEmailList() {
        // First email - Son Tran Giang about Moodle test
        findViewById(R.id.rel5em68e47i).setOnClickListener(v -> {
            openEmailDetails(
                    "Son Tran Giang (via USTH Moodle)",
                    "mobileApp25-26: Postpone of Moodle test",
                    "3:41 PM • Sep 30, 2024",
                    "you@example.com",
                    "Dear Students,\n\nThis is to inform you that the Moodle test for mobileApp25-26 has been postponed. The new date will be announced soon.\n\nPlease check the announcements forum for updates.\n\nBest regards,\nSon Tran Giang"
            );
        });

        // Second email - Minh Hoang Google Drive share
        findViewById(R.id.rommwk8vbiz).setOnClickListener(v -> {
            openEmailDetails(
                    "Minh Hoang (via Google Drive)",
                    "Share request for \"MOBILE\"",
                    "Sep 30, 2024 • 2:15 PM",
                    "you@example.com",
                    "I would like to share the MOBILE folder with you. Please accept the share request to access the materials.\n\nThe folder contains all the necessary resources for our mobile development project. You'll find the source code, documentation, and design assets.\n\nBest regards,\nMinh Hoang"
            );
        });

        // Third email - Milano missions complete
        findViewById(R.id.rqkb0gxkystq).setOnClickListener(v -> {
            openEmailDetails(
                    "Milano",
                    "Part 8 of 8: Missions Complete",
                    "Sep 30, 2024 • 10:30 AM",
                    "melancholia@example.com",
                    "Dear Melancholia,\n\nYou have completed the final mission. Congratulations on your achievement!\n\nAll missions are now complete. Thank you for your participation and dedication throughout this journey. Your performance has been exceptional.\n\nWe will be in touch soon regarding your next assignment.\n\nSincerely,\nMilano Team"
            );
        });
    }

    // Helper method to show toast messages
    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    // Open email detail view with all the email data
    private void openEmailDetails(String senderName, String emailSubject, String timestamp, String recipient, String emailContent) {
        Intent detailIntent = new Intent(MainActivity.this, EmailDetailActivity.class);

        // Pass all email data to detail activity
        detailIntent.putExtra("sender", senderName);
        detailIntent.putExtra("subject", emailSubject);
        detailIntent.putExtra("date", timestamp);
        detailIntent.putExtra("to", recipient);
        detailIntent.putExtra("body", emailContent);

        startActivity(detailIntent);
    }
}
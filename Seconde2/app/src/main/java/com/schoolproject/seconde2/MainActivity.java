package com.schoolproject.seconde2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    // Our main views
    private DrawerLayout drawerLayout;
    private ImageView menuButton;
    private LinearLayout composeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up all our views
        setupViews();

        // Set up button click listeners
        setupClickListeners();

        // Show inbox when app starts
        showInboxContent();
    }

    // Find all the views we need
    private void setupViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.rblhycly8y27);
        composeButton = findViewById(R.id.rp4smgichsuc);
    }

    // Set up all the button clicks
    private void setupClickListeners() {
        // Menu button opens the navigation drawer
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(findViewById(R.id.nav_custom));
            }
        });

        // Compose button opens compose email screen
        composeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent composeIntent = new Intent(MainActivity.this, ComposeEmailActivity.class);
                startActivity(composeIntent);
            }
        });

        // Set up navigation menu clicks
        setupNavigationClicks();

        // Set up email item clicks
        setupEmailItemClicks();
    }

    // Handle clicks on navigation menu items
    private void setupNavigationClicks() {
        // Inbox button
        findViewById(R.id.nav_inbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new InboxFragment());
                closeNavigationDrawer();
                Toast.makeText(MainActivity.this, "Showing Inbox", Toast.LENGTH_SHORT).show();
            }
        });

        // Drafts button
        findViewById(R.id.nav_draft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new DraftFragment());
                closeNavigationDrawer();
            }
        });

        // Sent button
        findViewById(R.id.nav_sent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new SentFragment());
                closeNavigationDrawer();
            }
        });

        // Trash button
        findViewById(R.id.nav_trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new TrashFragment());
                closeNavigationDrawer();
            }
        });

        // Outlook button
        findViewById(R.id.nav_outlook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new OutlookFragment());
                closeNavigationDrawer();
            }
        });

        // All Mail button
        findViewById(R.id.nav_all_mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new AllMailFragment());
                closeNavigationDrawer();
            }
        });

        // Contracts button
        findViewById(R.id.nav_contracts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new ContractsFragment());
                closeNavigationDrawer();
            }
        });

        // Settings button
        findViewById(R.id.nav_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new SettingsFragment());
                closeNavigationDrawer();
            }
        });
    }

    // Close the navigation drawer
    private void closeNavigationDrawer() {
        drawerLayout.closeDrawer(findViewById(R.id.nav_custom));
    }

    // Handle clicks on email list items
    private void setupEmailItemClicks() {
        // First email item
        findViewById(R.id.rel5em68e47i).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmailDetail(
                        "Son Tran Giang (via USTH Moodle)",
                        "mobileApp25-26: Postpone of Moodle test",
                        "3:41 PM • Sep 30, 2024",
                        "you@example.com",
                        "Dear Students,\n\nThis is to inform you that the Moodle test for mobileApp25-26 has been postponed. The new date will be announced soon.\n\nPlease check the announcements forum for updates.\n\nBest regards,\nSon Tran Giang"
                );
            }
        });

        // Second email item
        findViewById(R.id.rommwk8vbiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmailDetail(
                        "Minh Hoang (via Google Drive)",
                        "Share request for \"MOBILE\"",
                        "Sep 30, 2024 • 2:15 PM",
                        "you@example.com",
                        "I would like to share the MOBILE folder with you. Please accept the share request to access the materials.\n\nThe folder contains all the necessary resources for our mobile development project. You'll find the source code, documentation, and design assets.\n\nBest regards,\nMinh Hoang"
                );
            }
        });

        // Third email item
        findViewById(R.id.rqkb0gxkystq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmailDetail(
                        "Milano",
                        "Part 8 of 8: Missions Complete",
                        "Sep 30, 2024 • 10:30 AM",
                        "melancholia@example.com",
                        "Dear Melancholia,\n\nYou have completed the final mission. Congratulations on your achievement!\n\nAll missions are now complete. Thank you for your participation and dedication throughout this journey. Your performance has been exceptional.\n\nWe will be in touch soon regarding your next assignment.\n\nSincerely,\nMilano Team"
                );
            }
        });
    }

    // Show a fragment in the main content area
    private void showFragment(Fragment fragment) {
        try {
            // Hide the main inbox content
            View inboxContent = findViewById(R.id.main_inbox_content);
            if (inboxContent != null) {
                inboxContent.setVisibility(View.GONE);
            }

            // Show the fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();

            // Show which fragment we're showing
            String fragmentName = fragment.getClass().getSimpleName();
            Toast.makeText(this, "Showing: " + fragmentName, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error showing fragment", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Show the default inbox content (when app starts)
    private void showInboxContent() {
        // Show the main inbox layout
        View inboxContent = findViewById(R.id.main_inbox_content);
        if (inboxContent != null) {
            inboxContent.setVisibility(View.VISIBLE);
        }

        // Remove any fragment that might be showing
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
        }
    }

    // Open email detail screen
    private void openEmailDetail(String sender, String subject, String date, String to, String body) {
        Intent emailDetailIntent = new Intent(this, EmailDetailActivity.class);

        // Pass all the email data to the detail screen
        emailDetailIntent.putExtra("sender", sender);
        emailDetailIntent.putExtra("subject", subject);
        emailDetailIntent.putExtra("date", date);
        emailDetailIntent.putExtra("to", to);
        emailDetailIntent.putExtra("body", body);

        startActivity(emailDetailIntent);
    }

    // Show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // This method is called from EmailListFragment to open the navigation drawer
    public void openNavigationDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(findViewById(R.id.nav_custom));
        }
    }
}
package com.schoolproject.seconde2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigationDrawer();
        setupClickListeners();
    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);

        // Set up click listeners for custom navigation items
        int[] navIds = {
                R.id.nav_inbox, R.id.nav_draft, R.id.nav_sent, R.id.nav_trash,
                R.id.nav_outlook, R.id.nav_all_mail, R.id.nav_contracts, R.id.nav_settings
        };

        for (int id : navIds) {
            findViewById(id).setOnClickListener(v -> handleNavItemClick(v.getId()));
        }
    }

    private void handleNavItemClick(int viewId) {
        String itemName = "";

        if (viewId == R.id.nav_inbox) {
            itemName = "Inbox";
        } else if (viewId == R.id.nav_draft) {
            itemName = "Draft";
        } else if (viewId == R.id.nav_sent) {
            itemName = "Sent";
        } else if (viewId == R.id.nav_trash) {
            itemName = "Trash";
        } else if (viewId == R.id.nav_outlook) {
            itemName = "Outlook";
        } else if (viewId == R.id.nav_all_mail) {
            itemName = "All Mails";
        } else if (viewId == R.id.nav_contracts) {
            itemName = "Contracts";
        } else if (viewId == R.id.nav_settings) {
            itemName = "Settings";
        }

        Toast.makeText(this, itemName + " selected", Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setupClickListeners() {
        // Hamburger menu click listener
        View menuIcon = findViewById(R.id.rblhycly8y27);
        menuIcon.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // Profile icon click listener
        View profileIcon = findViewById(R.id.r7ftujq7tc27);
        profileIcon.setOnClickListener(v -> {
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
        });

        // Compose button click listener
        View composeButton = findViewById(R.id.rp4smgichsuc);
        composeButton.setOnClickListener(v -> {
            openComposeEmail();
        });

        // Email item click listeners
        View email1 = findViewById(R.id.rel5em68e47i);
        email1.setOnClickListener(v -> {
            Toast.makeText(this, "Opening email 1", Toast.LENGTH_SHORT).show();
        });

        View email2 = findViewById(R.id.rommwk8vbiz);
        email2.setOnClickListener(v -> {
            Toast.makeText(this, "Opening email 2", Toast.LENGTH_SHORT).show();
        });

        View email3 = findViewById(R.id.rqkb0gxkystq);
        email3.setOnClickListener(v -> {
            Toast.makeText(this, "Opening email 3", Toast.LENGTH_SHORT).show();
        });
    }

    private void openComposeEmail() {
        Intent intent = new Intent(this, ComposeEmailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
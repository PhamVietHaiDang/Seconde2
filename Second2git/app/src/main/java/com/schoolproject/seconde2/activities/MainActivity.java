package com.schoolproject.seconde2.activities;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.schoolproject.seconde2.R;
import com.schoolproject.seconde2.OnBoardingFragment1;
import com.schoolproject.seconde2.OnBoardingFragment2;
import com.schoolproject.seconde2.OnBoardingFragment3;
import com.schoolproject.seconde2.SignInFragment;
import com.schoolproject.seconde2.fragments.ContractsFragment;
import com.schoolproject.seconde2.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuButton;
    private LinearLayout composeButton;
    private View mainInboxContent;
    private View overlayContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if onboarding is completed
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean onboardingDone = prefs.getBoolean("onboarding_done", false);

        setupViews();
        setupClickListeners();

        // If onboarding not completed, show onboarding flow
        if (!onboardingDone) {
            showOnboardingFragment1();
        } else {
            showMainInbox();
        }
    }

    private void setupViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.rblhycly8y27);
        composeButton = findViewById(R.id.rp4smgichsuc);
        mainInboxContent = findViewById(R.id.main_inbox_content);
        overlayContainer = findViewById(R.id.overlay_container);
    }

    private void setupClickListeners() {
        menuButton.setOnClickListener(v ->
                drawerLayout.openDrawer(findViewById(R.id.nav_custom)));

        composeButton.setOnClickListener(v -> {
            Intent composeIntent = new Intent(MainActivity.this, ComposeEmailActivity.class);
            startActivity(composeIntent);
        });

        setupNavigationClicks();
        setupEmailItemClicks();
    }

    private void setupNavigationClicks() {
        findViewById(R.id.nav_inbox).setOnClickListener(v -> {
            showMainInbox();
            closeNavigationDrawer();
        });

        findViewById(R.id.nav_draft).setOnClickListener(v -> {
            showFragment(new com.schoolproject.seconde2.EmailFragments.DraftFragment());
            closeNavigationDrawer();
        });

        findViewById(R.id.nav_sent).setOnClickListener(v -> {
            showFragment(new com.schoolproject.seconde2.EmailFragments.SentFragment());
            closeNavigationDrawer();
        });

        findViewById(R.id.nav_trash).setOnClickListener(v -> {
            showFragment(new com.schoolproject.seconde2.EmailFragments.TrashFragment());
            closeNavigationDrawer();
        });

        findViewById(R.id.nav_outlook).setOnClickListener(v -> {
            showFragment(new com.schoolproject.seconde2.EmailFragments.OutlookFragment());
            closeNavigationDrawer();
        });

        findViewById(R.id.nav_all_mail).setOnClickListener(v -> {
            showFragment(new com.schoolproject.seconde2.EmailFragments.AllMailFragment());
            closeNavigationDrawer();
        });

        findViewById(R.id.nav_contracts).setOnClickListener(v -> {
            showFragment(new ContractsFragment());
            closeNavigationDrawer();
        });

        findViewById(R.id.nav_settings).setOnClickListener(v -> {
            showFragment(new SettingsFragment(), false); // Hide compose button for settings
            closeNavigationDrawer();
        });
    }

    private void setupEmailItemClicks() {
        findViewById(R.id.rel5em68e47i).setOnClickListener(v -> {
            openEmailDetail(
                    "Son Tran Giang (via USTH Moodle)",
                    "mobileApp25-26: Postpone of Moodle test",
                    "3:41 PM • Sep 30, 2024",
                    "you@example.com",
                    "Dear Students,\n\nThis is to inform you that the Moodle test for mobileApp25-26 has been postponed. The new date will be announced soon.\n\nPlease check the announcements forum for updates.\n\nBest regards,\nSon Tran Giang"
            );
        });

        findViewById(R.id.rommwk8vbiz).setOnClickListener(v -> {
            openEmailDetail(
                    "Minh Hoang (via Google Drive)",
                    "Share request for \"MOBILE\"",
                    "Sep 30, 2024 • 2:15 PM",
                    "you@example.com",
                    "I would like to share the MOBILE folder with you. Please accept the share request to access the materials.\n\nThe folder contains all the necessary resources for our mobile development project. You'll find the source code, documentation, and design assets.\n\nBest regards,\nMinh Hoang"
            );
        });

        findViewById(R.id.rqkb0gxkystq).setOnClickListener(v -> {
            openEmailDetail(
                    "Milano",
                    "Part 8 of 8: Missions Complete",
                    "Sep 30, 2024 • 10:30 AM",
                    "melancholia@example.com",
                    "Dear Melancholia,\n\nYou have completed the final mission. Congratulations on your achievement!\n\nAll missions are now complete. Thank you for your participation and dedication throughout this journey. Your performance has been exceptional.\n\nWe will be in touch soon regarding your next assignment.\n\nSincerely,\nMilano Team"
            );
        });
    }

    private void closeNavigationDrawer() {
        drawerLayout.closeDrawer(findViewById(R.id.nav_custom));
    }

    // Show onboarding flow (overlay on top of main content)
    public void showOnboardingFragment1() {
        mainInboxContent.setVisibility(View.GONE);
        overlayContainer.setVisibility(View.VISIBLE);
        composeButton.setVisibility(View.GONE); // Hide compose button

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.overlay_container, new OnBoardingFragment1());
        transaction.commit();
    }

    public void showOnboardingFragment2() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.overlay_container, new OnBoardingFragment2());
        transaction.commit();

        composeButton.setVisibility(View.GONE); // Hide compose button
    }

    public void showOnboardingFragment3() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.overlay_container, new OnBoardingFragment3());
        transaction.commit();

        composeButton.setVisibility(View.GONE); // Hide compose button
    }

    public void showSignInFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.overlay_container, new SignInFragment());
        transaction.commit();

        composeButton.setVisibility(View.GONE); // Hide compose button
    }

    // Show main inbox content (hide onboarding)
    public void showMainInbox() {
        mainInboxContent.setVisibility(View.VISIBLE);
        overlayContainer.setVisibility(View.GONE);
        composeButton.setVisibility(View.VISIBLE); // Show compose button

        // Mark onboarding as completed
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        prefs.edit().putBoolean("onboarding_done", true).apply();
    }

    // Show other fragments (replace main content) - Updated with showComposeButton parameter
    private void showFragment(Fragment fragment) {
        showFragment(fragment, true); // Default: show compose button
    }

    private void showFragment(Fragment fragment, boolean showComposeButton) {
        mainInboxContent.setVisibility(View.GONE);
        overlayContainer.setVisibility(View.VISIBLE);

        if (showComposeButton) {
            composeButton.setVisibility(View.VISIBLE); // Show compose button
        } else {
            composeButton.setVisibility(View.GONE); // Hide compose button
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.overlay_container, fragment);
        transaction.commit();
    }

    private void openEmailDetail(String sender, String subject, String date, String to, String body) {
        Intent emailDetailIntent = new Intent(this, EmailDetailActivity.class);
        emailDetailIntent.putExtra("sender", sender);
        emailDetailIntent.putExtra("subject", subject);
        emailDetailIntent.putExtra("date", date);
        emailDetailIntent.putExtra("to", to);
        emailDetailIntent.putExtra("body", body);
        startActivity(emailDetailIntent);
    }

    public void openNavigationDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(findViewById(R.id.nav_custom));
        }
    }
}
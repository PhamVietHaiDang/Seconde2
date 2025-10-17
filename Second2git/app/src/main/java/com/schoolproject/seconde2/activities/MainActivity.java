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
import androidx.lifecycle.ViewModelProvider;

import com.schoolproject.seconde2.R;
import com.schoolproject.seconde2.OnBoardingFragment1;
import com.schoolproject.seconde2.OnBoardingFragment2;
import com.schoolproject.seconde2.OnBoardingFragment3;
import com.schoolproject.seconde2.SignInFragment;
import com.schoolproject.seconde2.fragments.ContractsFragment;
import com.schoolproject.seconde2.fragments.SettingsFragment;
import com.schoolproject.seconde2.viewmodel.EmailViewModel;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuButton;
    private LinearLayout composeButton;
    private View mainInboxContent;
    private View overlayContainer;
    private EmailViewModel emailViewModel;

    private String userEmail;
    private String userPassword;

    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_ONBOARDING_DONE = "onboarding_done";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PASSWORD = "user_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ViewModel
        emailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);

        setupViews();
        setupClickListeners();
        loadUserCredentials();
        checkOnboardingStatus();

        // Observe emails
        observeEmails();
    }

    private void setupViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.rblhycly8y27);
        composeButton = findViewById(R.id.rp4smgichsuc);
        mainInboxContent = findViewById(R.id.main_inbox_content);
        overlayContainer = findViewById(R.id.overlay_container);
    }

    private void setupClickListeners() {
        menuButton.setOnClickListener(v -> openNavigationDrawer());
        composeButton.setOnClickListener(v -> openComposeEmail());

        setupNavigationClicks();
        setupEmailItemClicks();
    }

    private void loadUserCredentials() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userEmail = prefs.getString(KEY_USER_EMAIL, null);
        userPassword = prefs.getString(KEY_USER_PASSWORD, null);

        if (userEmail != null && userPassword != null) {
            emailViewModel.setCredentials(userEmail, userPassword);
        }
    }

    public void setUserCredentials(String email, String password) {
        this.userEmail = email;
        this.userPassword = password;

        // Store in SharedPreferences for persistence
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_USER_EMAIL, email)
                .putString(KEY_USER_PASSWORD, password)
                .apply();

        // Update ViewModel with credentials
        emailViewModel.setCredentials(email, password);

        // Refresh emails after signing in
        refreshAllEmails();
    }

    private void refreshAllEmails() {
        if (userEmail != null && userPassword != null) {
            emailViewModel.refreshEmails("inbox");
            emailViewModel.refreshEmails("sent");
            emailViewModel.refreshEmails("draft");
        }
    }

    private void observeEmails() {
        // Observe all emails
        emailViewModel.getAllEmails().observe(this, emails -> {
            if (emails != null && !emails.isEmpty()) {
                // Emails are available
                Toast.makeText(this, "Loaded " + emails.size() + " emails", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkOnboardingStatus() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean onboardingDone = prefs.getBoolean(KEY_ONBOARDING_DONE, false);

        if (!onboardingDone) {
            showOnboardingFragment1();
        } else {
            // Check if user is already signed in
            if (userEmail != null && userPassword != null) {
                showMainInbox();
                refreshAllEmails();
            } else {
                showSignInFragment();
            }
        }
    }

    private void setupNavigationClicks() {
        findViewById(R.id.nav_inbox).setOnClickListener(v -> {
            showMainInbox();
            closeNavigationDrawer();
            if (userEmail != null) {
                emailViewModel.refreshEmails("inbox");
            }
        });

        findViewById(R.id.nav_draft).setOnClickListener(v -> {
            showFragment(new com.schoolproject.seconde2.EmailFragments.DraftFragment());
            closeNavigationDrawer();
            if (userEmail != null) {
                emailViewModel.refreshEmails("draft");
            }
        });

        findViewById(R.id.nav_sent).setOnClickListener(v -> {
            showFragment(new com.schoolproject.seconde2.EmailFragments.SentFragment());
            closeNavigationDrawer();
            if (userEmail != null) {
                emailViewModel.refreshEmails("sent");
            }
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
            showFragment(new SettingsFragment(), false);
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

    private void openComposeEmail() {
        if (userEmail == null || userPassword == null) {
            Toast.makeText(this, "Please sign in first", Toast.LENGTH_SHORT).show();
            showSignInFragment();
            return;
        }

        Intent composeIntent = new Intent(this, ComposeEmailActivity.class);
        startActivity(composeIntent);
    }

    private void closeNavigationDrawer() {
        drawerLayout.closeDrawer(findViewById(R.id.nav_custom));
    }

    public void showOnboardingFragment1() {
        showOverlayFragment(new OnBoardingFragment1(), false);
    }

    public void showOnboardingFragment2() {
        showOverlayFragment(new OnBoardingFragment2(), false);
    }

    public void showOnboardingFragment3() {
        showOverlayFragment(new OnBoardingFragment3(), false);
    }

    public void showSignInFragment() {
        showOverlayFragment(new SignInFragment(), false);
    }

    public void showMainInbox() {
        mainInboxContent.setVisibility(View.VISIBLE);
        overlayContainer.setVisibility(View.GONE);
        composeButton.setVisibility(View.VISIBLE);

        markOnboardingCompleted();
    }

    private void showFragment(Fragment fragment) {
        showFragment(fragment, true);
    }

    private void showFragment(Fragment fragment, boolean showComposeButton) {
        showOverlayFragment(fragment, showComposeButton);
    }

    private void showOverlayFragment(Fragment fragment, boolean showComposeButton) {
        mainInboxContent.setVisibility(View.GONE);
        overlayContainer.setVisibility(View.VISIBLE);
        composeButton.setVisibility(showComposeButton ? View.VISIBLE : View.GONE);

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

    private void markOnboardingCompleted() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, true).apply();
    }

    public boolean isOnboardingCompleted() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(KEY_ONBOARDING_DONE, false);
    }

    public void resetOnboarding() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, false).apply();
    }

    public String getCurrentFragment() {
        if (mainInboxContent.getVisibility() == View.VISIBLE) {
            return "inbox";
        }
        return "unknown";
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean isUserSignedIn() {
        return userEmail != null && userPassword != null;
    }
}
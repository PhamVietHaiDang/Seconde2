package com.schoolproject.seconde2.activities;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private TextView navUserEmail;

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

        emailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);

        setupViews();
        setupNavigationHeader();
        setupClickListeners();
        loadUserCredentials();
        checkOnboardingStatus();
        observeEmails();
    }

    private void setupViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.rblhycly8y27);
        composeButton = findViewById(R.id.rp4smgichsuc);
        mainInboxContent = findViewById(R.id.main_inbox_content);
        overlayContainer = findViewById(R.id.overlay_container);
    }

    private void setupNavigationHeader() {
        View navDrawer = findViewById(R.id.nav_custom);
        if (navDrawer != null) {
            navUserEmail = navDrawer.findViewById(R.id.textView);
        }

        if (navUserEmail == null) {
            navUserEmail = findViewById(R.id.textView);
        }
        updateNavigationHeader();
    }

    private void updateNavigationHeader() {
        if (navUserEmail != null) {
            navUserEmail.setText(userEmail != null ? userEmail : "user@example.com");
        }
    }

    private void setupClickListeners() {
        menuButton.setOnClickListener(v -> openNavigationDrawer());
        composeButton.setOnClickListener(v -> openComposeEmail());
        setupNavigationClicks();
    }

    private void loadUserCredentials() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userEmail = prefs.getString(KEY_USER_EMAIL, null);
        userPassword = prefs.getString(KEY_USER_PASSWORD, null);

        if (userEmail != null && userPassword != null) {
            emailViewModel.setCredentials(userEmail, userPassword);
            updateNavigationHeader();
        }
    }

    public void setUserCredentials(String email, String password) {
        this.userEmail = email;
        this.userPassword = password;

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_USER_EMAIL, email)
                .putString(KEY_USER_PASSWORD, password)
                .apply();

        emailViewModel.setCredentials(email, password);
        updateNavigationHeader();

        refreshAllEmails();
        showInboxFragment();

        showToast("Signed in! Fetching your emails...");
    }

    private String extractNameFromEmail(String email) {
        if (email == null || email.isEmpty()) return "User";

        try {
            String namePart = email.substring(0, email.indexOf('@'));
            namePart = namePart.replaceAll("[^a-zA-Z]", " ").trim();

            if (!namePart.isEmpty()) {
                String[] words = namePart.split("\\s+");
                StringBuilder formattedName = new StringBuilder();
                for (String word : words) {
                    if (!word.isEmpty()) {
                        formattedName.append(Character.toUpperCase(word.charAt(0)))
                                .append(word.substring(1).toLowerCase())
                                .append(" ");
                    }
                }
                return formattedName.toString().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "User";
    }

    private void refreshAllEmails() {
        if (userEmail != null && userPassword != null) {
            emailViewModel.refreshEmails("inbox");
            emailViewModel.refreshEmails("sent");
            emailViewModel.refreshEmails("draft");
            emailViewModel.refreshEmails("trash");
            emailViewModel.refreshEmails("archive");
        }
    }

    private void observeEmails() {
        emailViewModel.getAllEmails().observe(this, emails -> {
            if (emails != null && !emails.isEmpty()) {
                showToast("Loaded " + emails.size() + " emails");
            }
        });
    }

    private void checkOnboardingStatus() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean onboardingDone = prefs.getBoolean(KEY_ONBOARDING_DONE, false);

        if (!onboardingDone) {
            showOnboardingFragment1();
        } else if (userEmail != null && userPassword != null) {
            showInboxFragment();
            refreshAllEmails();
        } else {
            showSignInFragment();
        }
    }

    private void setupNavigationClicks() {
        int[] navIds = {
                R.id.nav_inbox, R.id.nav_draft, R.id.nav_sent, R.id.nav_trash,
                R.id.nav_outlook, R.id.nav_all_mail, R.id.nav_contracts, R.id.nav_settings
        };

        Class<?>[] fragmentClasses = {
                com.schoolproject.seconde2.EmailFragments.InboxFragment.class,
                com.schoolproject.seconde2.EmailFragments.DraftFragment.class,
                com.schoolproject.seconde2.EmailFragments.SentFragment.class,
                com.schoolproject.seconde2.EmailFragments.TrashFragment.class,
                com.schoolproject.seconde2.EmailFragments.OutlookFragment.class,
                com.schoolproject.seconde2.EmailFragments.AllMailFragment.class,
                ContractsFragment.class,
                SettingsFragment.class
        };

        boolean[] showComposeButton = {true, true, true, true, true, true, true, false};

        for (int i = 0; i < navIds.length; i++) {
            final int index = i;
            findViewById(navIds[i]).setOnClickListener(v -> {
                try {
                    Fragment fragment = (Fragment) fragmentClasses[index].newInstance();
                    showFragment(fragment, showComposeButton[index]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                closeNavigationDrawer();
            });
        }
    }

    private void openComposeEmail() {
        if (userEmail == null || userPassword == null) {
            showToast("Please sign in first");
            showSignInFragment();
            return;
        }

        startActivity(new Intent(this, ComposeEmailActivity.class));
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

    public void showInboxFragment() {
        showFragment(new com.schoolproject.seconde2.EmailFragments.InboxFragment());
    }

    public void showMainInbox() {
        showInboxFragment();
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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.overlay_container, fragment)
                .commit();

        markOnboardingCompleted();
        updateNavigationHeader();
    }

    public void openNavigationDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(findViewById(R.id.nav_custom));
            updateNavigationHeader();
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
        return mainInboxContent.getVisibility() == View.VISIBLE ? "legacy_inbox" : "fragment_inbox";
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean isUserSignedIn() {
        return userEmail != null && userPassword != null;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
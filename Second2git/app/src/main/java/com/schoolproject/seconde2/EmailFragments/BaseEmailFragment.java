package com.schoolproject.seconde2.BaseEmailFragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.schoolproject.seconde2.R;
import com.schoolproject.seconde2.activities.ComposeEmailActivity;
import com.schoolproject.seconde2.activities.MainActivity;
import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.model.Email;
import com.schoolproject.seconde2.viewmodel.EmailViewModel;

import java.util.List;

public abstract class BaseEmailFragment extends EmailListFragment {

    protected abstract String getFolderName();
    protected abstract String getFragmentTag();

    @Override
    protected void loadEmailData() {
        setFolderTitle(getFolderName());
        Log.d(getFragmentTag(), "Loading " + getFolderName() + " data, signed in: " + emailViewModel.isUserSignedIn());

        if (emailViewModel.isUserSignedIn()) {
            // Show loading state immediately for better UX
            showNoDataScreen("Loading", "Fetching your " + getFolderName() + " emails...");

            Log.d(getFragmentTag(), "User is signed in, refreshing " + getFolderName() + " emails");
            emailViewModel.refreshEmails(getFolderName());

            // Start observing emails immediately
            observeEmails(getFolderName());
        } else {
            showNoDataScreen("Sign In Required", "Please sign in to view your " + getFolderName() + " emails");
        }
    }

    // SIMPLIFIED: Remove the complex callback approach
    protected void loadEmailDataWithProgress() {
        setFolderTitle(getFolderName());

        if (emailViewModel.isUserSignedIn()) {
            // Show loading state immediately
            showNoDataScreen("Loading", "Fetching your " + getFolderName() + " emails...");

            // Use the simpler refresh method
            emailViewModel.refreshEmails(getFolderName());

            // Start observing emails
            observeEmails(getFolderName());
        } else {
            showNoDataScreen("Sign In Required", "Please sign in to view your " + getFolderName() + " emails");
        }
    }

    @Override
    protected void observeEmails(String folder) {
        // Check if fragment is still alive before observing
        if (getViewLifecycleOwner().getLifecycle().getCurrentState().isAtLeast(androidx.lifecycle.Lifecycle.State.INITIALIZED)) {
            emailViewModel.getEmailsByFolder(folder).observe(getViewLifecycleOwner(), emails -> {
                Log.d(getFragmentTag(), getFolderName() + " emails observed: " + (emails != null ? emails.size() : "null"));

                setRefreshing(false);

                if (emails != null && !emails.isEmpty()) {
                    Log.d(getFragmentTag(), "Displaying " + emails.size() + " " + getFolderName() + " emails");
                    displayEmails(emails);
                } else {
                    Log.d(getFragmentTag(), "No " + getFolderName() + " emails found");
                    if (emailViewModel.isUserSignedIn()) {
                        showNoDataScreen("Empty " + getFolderName(), "No emails found in your " + getFolderName());
                    } else {
                        showNoDataScreen("Sign In Required", "Please sign in to view your " + getFolderName() + " emails");
                    }
                }
            });
        }
    }

    @Override
    protected void loadSampleEmails(String folder) {
        showNoDataScreen("Sign In Required", "Please sign in to view your " + getFolderName() + " emails");
    }

    @Override
    protected void openEmailDetails(Email email) {
        if (getActivity() == null) return;

        Intent intent = new Intent(getActivity(), EmailDetailActivity.class);
        intent.putExtra("sender", email.sender);
        intent.putExtra("subject", email.subject);
        intent.putExtra("date", email.date);
        intent.putExtra("to", emailViewModel.isUserSignedIn() ? emailViewModel.getUserEmail() : "you@example.com");
        intent.putExtra("body", email.body);
        intent.putExtra("html_content", email.htmlContent != null ? email.htmlContent : "");
        intent.putExtra("is_html", email.isHtml);
        intent.putExtra("folder", email.folder);
        intent.putExtra("email_id", email.id);

        startActivity(intent);
    }

    @Override
    protected String getCurrentFolder() {
        return getFolderName();
    }

    @Override
    protected void refreshEmails() {
        if (emailViewModel.isUserSignedIn()) {
            emailViewModel.refreshEmails(getFolderName());
            Toast.makeText(getContext(), "Refreshing " + getFolderName() + "...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please sign in to refresh " + getFolderName(), Toast.LENGTH_SHORT).show();
        }
    }
}
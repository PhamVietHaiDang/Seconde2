package com.schoolproject.seconde2.BaseEmailFragment;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.model.Email;

public abstract class BaseEmailFragment extends EmailListFragment {

    protected abstract String getFolderName();
    protected abstract String getFragmentTag();

    @Override
    protected void loadEmailData() {
        setFolderTitle(getFolderName());
        Log.d(getFragmentTag(), "Loading " + getFolderName() + " data, signed in: " + emailViewModel.isUserSignedIn());

        if (emailViewModel.isUserSignedIn()) {
            Log.d(getFragmentTag(), "User is signed in, refreshing " + getFolderName() + " emails");
            emailViewModel.refreshEmails(getFolderName());
            showNoDataScreen("Loading", "Fetching your " + getFolderName() + " emails...");
            observeEmails(getFolderName());
        } else {
            showNoDataScreen("Sign In Required", "Please sign in to view your " + getFolderName() + " emails");
        }
    }

    @Override
    protected void observeEmails(String folder) {
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

    @Override
    protected void loadSampleEmails(String folder) {
        showNoDataScreen("Sign In Required", "Please sign in to view your " + getFolderName() + " emails");
    }

    @Override
    protected void openEmailDetails(Email email) {
        if (getActivity() == null) return;

        // DEBUG: Log what we're sending
        System.out.println("DEBUG " + getFragmentTag() + " - Sending email data:");
        System.out.println("Sender: " + email.sender);
        System.out.println("Subject: " + email.subject);
        System.out.println("Date: " + email.date);
        System.out.println("Body: " + (email.body != null ? email.body.substring(0, Math.min(50, email.body.length())) + "..." : "null"));
        System.out.println("HTML Content: " + (email.htmlContent != null ? "Available, length: " + email.htmlContent.length() : "null"));
        System.out.println("Is HTML: " + email.isHtml);
        System.out.println("Folder: " + email.folder);
        System.out.println("ID: " + email.id);

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
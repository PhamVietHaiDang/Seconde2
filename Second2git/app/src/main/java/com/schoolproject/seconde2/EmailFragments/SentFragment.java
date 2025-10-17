package com.schoolproject.seconde2.EmailFragments;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;
import com.schoolproject.seconde2.model.Email;

import java.util.List;

public class SentFragment extends EmailListFragment {

    private static final String TAG = "SentFragment";

    @Override
    protected void loadEmailData() {
        setFolderTitle("Sent");
        Log.d(TAG, "Loading sent data, signed in: " + emailViewModel.isUserSignedIn());

        if (emailViewModel.isUserSignedIn()) {
            Log.d(TAG, "User is signed in, refreshing sent emails");
            emailViewModel.refreshEmails("sent");
            observeEmails("sent");
        } else {
            loadSampleEmails("sent");
        }
    }

    @Override
    protected void observeEmails(String folder) {
        emailViewModel.getEmailsByFolder(folder).observe(getViewLifecycleOwner(), emails -> {
            Log.d(TAG, "Sent emails observed: " + (emails != null ? emails.size() : "null"));

            if (emails != null && !emails.isEmpty()) {
                boolean hasRealEmails = hasRealEmails(emails);
                Log.d(TAG, "Displaying " + emails.size() + " sent emails, real emails: " + hasRealEmails);

                if (hasRealEmails) {
                    displayEmails(emails);
                    Toast.makeText(getContext(), "Showing your sent emails", Toast.LENGTH_SHORT).show();
                } else {
                    displayEmails(emails);
                }
            } else {
                Log.d(TAG, "No sent emails found");
                if (emailViewModel.isUserSignedIn()) {
                    showNoDataScreen("No Sent Emails",
                            "No sent emails found. Send an email to see it here.");
                } else {
                    loadSampleEmails(folder);
                }
            }
        });
    }

    private boolean hasRealEmails(List<Email> emails) {
        if (emails == null || emails.isEmpty()) return false;

        for (Email email : emails) {
            if (!email.sender.equals("System") && !email.subject.contains("No Sent Emails")) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void loadSampleEmails(String folder) {
        Log.d(TAG, "Loading sample sent emails");
        showNoDataScreen("Sign In Required", "Please sign in to view your sent emails");
    }

    @Override
    protected void openEmailDetails(Email email) {
        if (getActivity() == null) return;

        // DEBUG: Log what we're sending
        System.out.println("DEBUG SentFragment - Sending email data:");
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
        intent.putExtra("html_content", email.htmlContent != null ? email.htmlContent : ""); // ADD THIS LINE
        intent.putExtra("is_html", email.isHtml); // ADD THIS LINE
        intent.putExtra("folder", email.folder); // ADD THIS LINE
        intent.putExtra("email_id", email.id); // ADD THIS LINE

        startActivity(intent);
    }
}
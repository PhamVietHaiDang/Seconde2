package com.schoolproject.seconde2.EmailFragments;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;
import com.schoolproject.seconde2.model.Email; // ADD THIS IMPORT

public class OutlookFragment extends EmailListFragment {

    private static final String TAG = "OutlookFragment";

    @Override
    protected void loadEmailData() {
        setFolderTitle("Outlook");
        Log.d(TAG, "Loading outlook data, signed in: " + emailViewModel.isUserSignedIn());

        if (emailViewModel.isUserSignedIn()) {
            Log.d(TAG, "User is signed in, refreshing outlook emails");
            // For Outlook/Hotmail accounts, we need to use different server
            fetchOutlookEmails();
        } else {
            showOutlookNotConnected();
        }
    }

    private void fetchOutlookEmails() {
        // Try to fetch from Outlook IMAP server
        new Thread(() -> {
            try {
                // For Outlook accounts, we need to detect if user has Outlook email
                String userEmail = emailViewModel.getUserEmail();
                if (userEmail != null && (userEmail.contains("@outlook.com") || userEmail.contains("@hotmail.com"))) {
                    // User has Outlook account - fetch from Outlook server
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Fetching Outlook emails...", Toast.LENGTH_SHORT).show();
                        showOutlookConnectPrompt();
                    });
                } else {
                    // User has Gmail but viewing Outlook section
                    requireActivity().runOnUiThread(() -> {
                        showGmailUserMessage();
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching Outlook emails", e);
                requireActivity().runOnUiThread(() -> {
                    showOutlookNotConnected();
                });
            }
        }).start();
    }

    private void showOutlookConnectPrompt() {
        showNoDataScreen("Connect Outlook",
                "To view your Outlook emails:\n\n" +
                        "1. Sign out of your current account\n" +
                        "2. Sign in with your Outlook/Hotmail email\n" +
                        "3. Use your Outlook app password\n\n" +
                        "Currently signed in as: " + emailViewModel.getUserEmail());
    }

    private void showGmailUserMessage() {
        showNoDataScreen("Outlook Account Required",
                "You are currently signed in with Gmail.\n\n" +
                        "To view Outlook emails, please:\n\n" +
                        "• Sign out and sign in with your Outlook account\n" +
                        "• Or use the Gmail folders for your emails\n\n" +
                        "Current account: " + emailViewModel.getUserEmail());
    }

    private void showOutlookNotConnected() {
        showNoDataScreen("Outlook Not Connected",
                "Connect your Outlook account to view emails here\n\n" +
                        "Supported: Outlook, Hotmail, Live accounts");
    }

    @Override
    protected void observeEmails(String folder) {
        // Outlook doesn't use the standard email observation
        // We handle it differently since it's a separate email provider
    }

    @Override
    protected void loadSampleEmails(String folder) {
        // No sample emails for Outlook - always show connection prompt
        showOutlookNotConnected();
    }

    @Override
    protected void openEmailDetails(Email email) {
        if (getActivity() == null) return;

        // DEBUG: Log what we're sending
        System.out.println("DEBUG OutlookFragment - Sending email data:");
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

    @Override
    protected void refreshEmails() {
        if (emailViewModel.isUserSignedIn()) {
            fetchOutlookEmails();
            Toast.makeText(getContext(), "Refreshing Outlook...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please sign in to refresh Outlook", Toast.LENGTH_SHORT).show();
        }
    }

    // Backend integration methods
    private void displayOutlookEmails(java.util.List<Object> emails) {
        if (emails == null || emails.isEmpty()) {
            showOutlookNotConnected();
            return;
        }
        // Implementation for displaying Outlook emails would go here
    }
}
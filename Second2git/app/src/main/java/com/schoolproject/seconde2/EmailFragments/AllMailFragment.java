package com.schoolproject.seconde2.EmailFragments;

import android.content.Intent;
import android.view.View;

import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;
import com.schoolproject.seconde2.model.Email;

public class AllMailFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("All Mail");

        // Observe all emails from database
        observeEmails(null); // null will get all emails
    }

    @Override
    protected void observeEmails(String folder) {
        // Override to get all emails regardless of folder
        emailViewModel.getAllEmails().observe(getViewLifecycleOwner(), emails -> {
            if (emails != null && !emails.isEmpty()) {
                displayEmails(emails);
            } else {
                loadSampleEmails("all");
            }
        });
    }

    @Override
    protected void loadSampleEmails(String folder) {
        loadAllEmailCategories();
    }

    private void loadAllEmailCategories() {
        addInboxEmails();
        addSentEmails();
        addTrashEmails();
    }

    private void addInboxEmails() {
        addEmail("Mom", "How are you doing?",
                "Hi son, just checking in to see how you're doing with your studies...",
                "INBOX • 5:20 PM",
                "Hi son,\n\nJust checking in to see how you're doing with your studies. Are you eating well?\n\nLove,\nMom");
    }

    private void addSentEmails() {
        addEmail("Professor Smith", "Homework Assignment 5 Submission",
                "Dear Professor, please find my homework assignment attached...",
                "SENT • 8:15 AM",
                "Dear Professor,\n\nPlease find my homework assignment 5 attached to this email.\n\nI completed all the problems and included detailed explanations for problem 4.\n\nThank you!");
    }

    private void addTrashEmails() {
        addEmail("Super Discount Store", "50% OFF Everything Today Only!",
                "Limited time offer! Get 50% off all items in our store...",
                "TRASH • Today",
                "Limited time offer! Get 50% off all items in our store today only!\n\nUse code: SAVE50\n\nHurry, offer ends tonight at midnight!");
    }

    private void addEmail(String sender, String emailSubject, String previewText, String timestamp, String emailBody) {
        addEmailToList(sender, emailSubject, previewText, timestamp,
                view -> openEmailDetails(sender, emailSubject, formatDate(timestamp), "user@example.com", emailBody));
    }

    @Override
    protected void openEmailDetails(Email email) {
        if (getActivity() == null) return;

        Intent emailDetailIntent = new Intent(getActivity(), EmailDetailActivity.class);
        emailDetailIntent.putExtra("sender", email.sender);
        emailDetailIntent.putExtra("subject", email.subject);
        emailDetailIntent.putExtra("date", email.date);
        emailDetailIntent.putExtra("to", "user@example.com");
        emailDetailIntent.putExtra("body", email.body);

        startActivity(emailDetailIntent);
    }

    private void openEmailDetails(String senderName, String emailSubject, String emailDate, String recipient, String emailBody) {
        if (getActivity() == null) return;

        Intent emailDetailIntent = new Intent(getActivity(), EmailDetailActivity.class);
        emailDetailIntent.putExtra("sender", senderName);
        emailDetailIntent.putExtra("subject", emailSubject);
        emailDetailIntent.putExtra("date", emailDate);
        emailDetailIntent.putExtra("to", recipient);
        emailDetailIntent.putExtra("body", emailBody);

        startActivity(emailDetailIntent);
    }
}
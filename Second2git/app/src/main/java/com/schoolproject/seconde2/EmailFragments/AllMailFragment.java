package com.schoolproject.seconde2.EmailFragments;

import android.util.Log;
import android.widget.Toast;
import android.content.Intent;

import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;
import com.schoolproject.seconde2.model.Email;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllMailFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("All Mail");
        Log.d("AllMailFragment", "Loading all mail data, signed in: " + emailViewModel.isUserSignedIn());

        if (emailViewModel.isUserSignedIn()) {
            refreshAllFolders();
        } else {
            showNoDataScreen("Sign In Required", "Please sign in to view all your emails");
        }
    }

    private void refreshAllFolders() {
        emailViewModel.refreshEmails("inbox");
        emailViewModel.refreshEmails("sent");
        emailViewModel.refreshEmails("draft");
        emailViewModel.refreshEmails("archive");
        Toast.makeText(getContext(), "Fetching all your emails...", Toast.LENGTH_SHORT).show();
        observeAllEmails();
    }

    private void observeAllEmails() {
        emailViewModel.getAllEmails().observe(getViewLifecycleOwner(), allEmails -> {
            if (allEmails != null && !allEmails.isEmpty()) {
                List<Email> filteredEmails = filterOutTrashAndDuplicates(allEmails);
                if (!filteredEmails.isEmpty()) {
                    displayEmails(filteredEmails);
                } else {
                    showNoDataScreen("No Emails Found", "No emails found in your account (excluding trash)");
                }
            } else {
                showNoDataScreen("No Emails", "No emails found in your account");
            }
        });
    }

    private List<Email> filterOutTrashAndDuplicates(List<Email> allEmails) {
        List<Email> filtered = new ArrayList<>();
        Set<String> seenEmails = new HashSet<>();

        for (Email email : allEmails) {
            if ("trash".equalsIgnoreCase(email.folder)) continue;

            String emailKey = email.sender + "|" + email.subject + "|" + email.date;
            if (!seenEmails.contains(emailKey)) {
                filtered.add(email);
                seenEmails.add(emailKey);
            }
        }
        return filtered;
    }

    @Override
    protected void observeEmails(String folder) {
        // Not used
    }

    @Override
    protected void loadSampleEmails(String folder) {
        showNoDataScreen("Sign In Required", "Please sign in to view all your emails");
    }

    @Override
    protected void openEmailDetails(Email email) {
        if (getActivity() == null) return;

        Intent intent = createEmailDetailIntent(email);
        startActivity(intent);
    }

    @Override
    protected void refreshEmails() {
        if (emailViewModel.isUserSignedIn()) {
            refreshAllFolders();
        } else {
            Toast.makeText(getContext(), "Please sign in to refresh emails", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String getCurrentFolder() {
        return "all";
    }
}
package com.schoolproject.seconde2.EmailFragments;

import android.content.Intent;
import android.view.View;

import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;
import com.schoolproject.seconde2.model.Email;

public class DraftFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Drafts");

        // Observe draft emails from database
        observeEmails("draft");
    }

    @Override
    protected void loadSampleEmails(String folder) {
        // Show empty state if no drafts
        showNoDraftsScreen();
    }

    private void showNoDraftsScreen() {
        showNoDataScreen("No drafts", "You don't have any saved drafts");
    }

    @Override
    protected void openEmailDetails(Email email) {
        if (getActivity() == null) return;

        Intent intent = new Intent(getActivity(), EmailDetailActivity.class);
        intent.putExtra("sender", email.sender);
        intent.putExtra("subject", email.subject);
        intent.putExtra("date", email.date);
        intent.putExtra("to", "user@example.com");
        intent.putExtra("body", email.body);

        startActivity(intent);
    }
}
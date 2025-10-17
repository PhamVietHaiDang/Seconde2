package com.schoolproject.seconde2.EmailFragments;

import android.content.Intent;

import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;

public class OutlookFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Outlook");
        showOutlookNotConnected();
    }

    private void showOutlookNotConnected() {
        showNoDataScreen("No Outlook emails", "Connect your Outlook account to see emails here");
    }

    private void openEmail(String from, String subject, String date, String to, String body) {
        if (getActivity() == null) return;

        Intent intent = new Intent(getActivity(), EmailDetailActivity.class);
        intent.putExtra("sender", from);
        intent.putExtra("subject", subject);
        intent.putExtra("date", date);
        intent.putExtra("to", to);
        intent.putExtra("body", body);

        startActivity(intent);
    }

    // Backend integration methods
    private void fetchOutlookEmails() {
    }
    private void displayOutlookEmails(java.util.List<Object> emails) {
        if (emails == null || emails.isEmpty()) {
            showOutlookNotConnected();
            return;
        }

    }
}
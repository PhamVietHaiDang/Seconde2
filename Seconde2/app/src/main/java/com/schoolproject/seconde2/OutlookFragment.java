package com.schoolproject.seconde2;

import android.content.Intent;
import android.view.View;
import androidx.fragment.app.Fragment;

public class OutlookFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Outlook");

        // Show message that Outlook emails aren't connected yet
        showNoDataScreen("No Outlook emails", "Connect your Outlook account to see emails here");
    }

    private void openEmail(String from, String subject, String date, String to, String body) {
        // Open the email detail screen
        Intent intent = new Intent(getActivity(), EmailDetailActivity.class);

        // Pass all the email data to the detail screen
        intent.putExtra("sender", from);
        intent.putExtra("subject", subject);
        intent.putExtra("date", date);
        intent.putExtra("to", to);
        intent.putExtra("body", body);

        startActivity(intent);
    }
}
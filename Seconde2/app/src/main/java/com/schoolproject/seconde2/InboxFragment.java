package com.schoolproject.seconde2;

import android.content.Intent;
import android.view.View;
import androidx.fragment.app.Fragment;

public class InboxFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Inbox");

        // Example inbox emails
        addEmailToList(
                "USTH Administration",
                "Welcome to New Semester",
                "Dear students, welcome back to the new academic year...",
                "9:00 AM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmail("USTH Administration",
                                "Welcome to New Semester",
                                "Today • 9:00 AM",
                                "all-students@usth.edu.vn",
                                "Dear students,\n\nWelcome back to the new academic year!");
                    }
                }
        );

        addEmailToList(
                "Project Team",
                "Meeting About Mobile App Project",
                "Hi team, let's meet tomorrow to discuss the progress...",
                "2:30 PM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmail("Project Team",
                                "Meeting About Mobile App Project",
                                "Yesterday • 2:30 PM",
                                "team@project.com",
                                "Hi team,\n\nLet's meet tomorrow at 10 AM!");
                    }
                }
        );
    }

    private void openEmail(String from, String subject, String date, String to, String body) {
        Intent intent = new Intent(getActivity(), EmailDetailActivity.class);
        intent.putExtra("sender", from);
        intent.putExtra("subject", subject);
        intent.putExtra("date", date);
        intent.putExtra("to", to);
        intent.putExtra("body", body);
        startActivity(intent);
    }
}
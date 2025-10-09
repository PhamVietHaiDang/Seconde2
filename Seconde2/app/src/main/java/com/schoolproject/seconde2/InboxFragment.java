package com.schoolproject.seconde2;

import android.content.Intent;
import android.view.View;
import androidx.fragment.app.Fragment;

public class InboxFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Inbox");

        // Check if we have any inbox emails to show
        boolean hasInboxEmails = true; // Change this to false to see empty inbox

        if (hasInboxEmails) {
            loadInboxEmails();
        } else {
            // Show empty inbox message
            showNoDataScreen("Inbox is empty", "No new messages");
        }
    }

    private void loadInboxEmails() {
        // Add all the inbox emails to the list

        // University welcome email
        addEmailToList(
                "USTH Administration",
                "Welcome to New Semester",
                "Dear students, welcome back to the new academic year...",
                "9:00 AM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("USTH Administration",
                                "Welcome to New Semester",
                                "Today • 9:00 AM",
                                "all-students@usth.edu.vn",
                                "Dear students,\n\nWelcome back to the new academic year!");
                    }
                }
        );

        // Project team meeting email
        addEmailToList(
                "Project Team",
                "Meeting About Mobile App Project",
                "Hi team, let's meet tomorrow to discuss the progress...",
                "2:30 PM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("Project Team",
                                "Meeting About Mobile App Project",
                                "Yesterday • 2:30 PM",
                                "team@project.com",
                                "Hi team,\n\nLet's meet tomorrow at 10 AM!");
                    }
                }
        );

        // Library reminder email
        addEmailToList(
                "University Library",
                "Book Due Date Reminder",
                "Reminder: Your borrowed books are due next week...",
                "11:45 AM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("University Library",
                                "Book Due Date Reminder",
                                "Today • 11:45 AM",
                                "student@university.edu",
                                "Dear Student,\n\nThis is a reminder that your borrowed books are due for return next week.\n\nPlease return them by the due date to avoid late fees.\n\nThank you!");
                    }
                }
        );
    }

    private void openEmailDetails(String from, String subject, String date, String to, String body) {
        // Open the email detail screen with the selected email
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
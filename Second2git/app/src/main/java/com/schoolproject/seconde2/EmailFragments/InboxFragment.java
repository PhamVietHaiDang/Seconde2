package com.schoolproject.seconde2.EmailFragments;

import android.content.Intent;
import android.view.View;

import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;
import com.schoolproject.seconde2.model.Email;

public class InboxFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Inbox");

        // Observe emails from database
        observeEmails("inbox");

        // Optionally fetch from server (uncomment when ready)
        // emailViewModel.refreshEmails("imap.gmail.com", "user@gmail.com", "password", "inbox");
    }

    @Override
    protected void loadSampleEmails(String folder) {
        // Show sample data if no emails in database
        addInboxEmail("USTH Administration",
                "Welcome to New Semester",
                "Dear students, welcome back to the new academic year...",
                "9:00 AM",
                "all-students@usth.edu.vn",
                "Dear students,\n\nWelcome back to the new academic year!");

        addInboxEmail("Project Team",
                "Meeting About Mobile App Project",
                "Hi team, let's meet tomorrow to discuss the progress...",
                "2:30 PM",
                "team@project.com",
                "Hi team,\n\nLet's meet tomorrow at 10 AM!");

        addInboxEmail("University Library",
                "Book Due Date Reminder",
                "Reminder: Your borrowed books are due next week...",
                "11:45 AM",
                "student@university.edu",
                "Dear Student,\n\nThis is a reminder that your borrowed books are due for return next week.\n\nPlease return them by the due date to avoid late fees.\n\nThank you!");
    }

    private void addInboxEmail(String from, String subject, String preview, String time, String to, String body) {
        addEmailToList(from, subject, preview, time,
                v -> openEmailDetails(from, subject, formatDate(time), to, body));
    }

    @Override
    protected void openEmailDetails(Email email) {
        if (getActivity() == null) return;

        Intent intent = new Intent(getActivity(), EmailDetailActivity.class);
        intent.putExtra("sender", email.sender);
        intent.putExtra("subject", email.subject);
        intent.putExtra("date", email.date);
        intent.putExtra("to", "user@example.com"); // You might want to store recipient in Email model
        intent.putExtra("body", email.body);

        startActivity(intent);
    }

    // Overload for sample data
    private void openEmailDetails(String from, String subject, String date, String to, String body) {
        if (getActivity() == null) return;

        Intent intent = new Intent(getActivity(), EmailDetailActivity.class);
        intent.putExtra("sender", from);
        intent.putExtra("subject", subject);
        intent.putExtra("date", date);
        intent.putExtra("to", to);
        intent.putExtra("body", body);

        startActivity(intent);
    }
}
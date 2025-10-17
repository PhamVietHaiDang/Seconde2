package com.schoolproject.seconde2.EmailFragments;

import android.content.Intent;
import android.view.View;

import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;
import com.schoolproject.seconde2.model.Email;

public class SentFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Sent");

        // Observe sent emails from database
        observeEmails("sent");
    }

    @Override
    protected void loadSampleEmails(String folder) {
        // Show sample data if no sent emails in database
        addSentEmail("Professor Smith",
                "Homework Assignment 5 Submission",
                "Dear Professor, please find my homework assignment attached...",
                "SENT • 8:15 AM",
                "prof.smith@university.edu",
                "Dear Professor,\n\nPlease find my homework assignment 5 attached to this email.\n\nI completed all the problems and included detailed explanations for problem 4.\n\nThank you!");

        addSentEmail("Project Team",
                "Updated Design Mockups",
                "Hi team, I've updated the design mockups based on our discussion...",
                "SENT • 3:45 PM",
                "team@project.com",
                "Hi team,\n\nI've updated the design mockups based on our discussion yesterday. The changes include:\n\n- Improved navigation flow\n- Updated color scheme\n- Better button placements\n\nPlease review and let me know your feedback.");

        addSentEmail("IT Support",
                "Software Installation Request",
                "Hello, I need help installing the development software...",
                "SENT • 11:20 AM",
                "itsupport@company.com",
                "Hello IT Support,\n\nI need assistance installing the following development software on my machine:\n\n- Android Studio\n- Git\n- Node.js\n\nPlease let me know when you can help with the installation.\n\nThank you!");
    }

    private void addSentEmail(String from, String subject, String preview, String date, String to, String body) {
        addEmailToList(from, subject, preview, date,
                v -> openEmailDetails(from, subject, date, to, body));
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
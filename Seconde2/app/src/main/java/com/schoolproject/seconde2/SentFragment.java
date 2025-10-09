package com.schoolproject.seconde2;

import android.content.Intent;
import android.view.View;
import androidx.fragment.app.Fragment;

public class SentFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Sent");

        // Check if we have any sent emails to show
        boolean hasSentEmails = true; // Change this to false to see empty sent folder

        if (hasSentEmails) {
            loadSentEmails();
        } else {
            // Show empty sent folder message
            showNoDataScreen("No sent emails", "You haven't sent any emails yet");
        }
    }

    private void loadSentEmails() {
        // Add all the sent emails to the list

        // Homework submission email
        addEmailToList(
                "Professor Smith",
                "Homework Assignment 5 Submission",
                "Dear Professor, please find my homework assignment attached...",
                "SENT • 8:15 AM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("Professor Smith",
                                "Homework Assignment 5 Submission",
                                "SENT • 8:15 AM",
                                "prof.smith@university.edu",
                                "Dear Professor,\n\nPlease find my homework assignment 5 attached to this email.\n\nI completed all the problems and included detailed explanations for problem 4.\n\nThank you!");
                    }
                }
        );

        // Project team email
        addEmailToList(
                "Project Team",
                "Updated Design Mockups",
                "Hi team, I've updated the design mockups based on our discussion...",
                "SENT • 3:45 PM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("Project Team",
                                "Updated Design Mockups",
                                "SENT • 3:45 PM",
                                "team@project.com",
                                "Hi team,\n\nI've updated the design mockups based on our discussion yesterday. The changes include:\n\n- Improved navigation flow\n- Updated color scheme\n- Better button placements\n\nPlease review and let me know your feedback.");
                    }
                }
        );

        // IT support email
        addEmailToList(
                "IT Support",
                "Software Installation Request",
                "Hello, I need help installing the development software...",
                "SENT • 11:20 AM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("IT Support",
                                "Software Installation Request",
                                "SENT • 11:20 AM",
                                "itsupport@company.com",
                                "Hello IT Support,\n\nI need assistance installing the following development software on my machine:\n\n- Android Studio\n- Git\n- Node.js\n\nPlease let me know when you can help with the installation.\n\nThank you!");
                    }
                }
        );
    }

    private void openEmailDetails(String from, String subject, String date, String to, String body) {
        // Open the email detail screen with the sent email
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
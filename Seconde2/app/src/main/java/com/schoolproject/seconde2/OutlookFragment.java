package com.schoolproject.seconde2;

import android.content.Intent;
import android.view.View;
import androidx.fragment.app.Fragment;

public class OutlookFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Outlook");

        // Example Outlook emails
        addEmailToList(
                "Microsoft Outlook Team",
                "Welcome to Outlook Integration",
                "We're excited to have you connect your Outlook account to our app...",
                "Today • 10:30 AM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmail("Microsoft Outlook Team",
                                "Welcome to Outlook Integration",
                                "Today • 10:30 AM",
                                "user@outlook.com",
                                "Dear User,\n\nWe're excited to have you connect your Outlook account to our email app. You can now access all your Outlook emails directly from this application.\n\nBest regards,\nMicrosoft Outlook Team");
                    }
                }
        );

        addEmailToList(
                "Outlook Calendar",
                "Meeting Reminder: Project Review",
                "Reminder: You have a project review meeting scheduled for tomorrow at 2:00 PM...",
                "Yesterday • 3:15 PM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmail("Outlook Calendar",
                                "Meeting Reminder: Project Review",
                                "Yesterday • 3:15 PM",
                                "user@outlook.com",
                                "Meeting Reminder\n\nSubject: Project Review\nDate: Tomorrow at 2:00 PM\nLocation: Conference Room A\n\nPlease come prepared with your project updates.");
                    }
                }
        );

        addEmailToList(
                "Outlook Support",
                "Your Account Security Update",
                "We've recently updated our security features to better protect your account...",
                "Oct 2 • 11:20 AM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmail("Outlook Support",
                                "Your Account Security Update",
                                "Oct 2 • 11:20 AM",
                                "user@outlook.com",
                                "Dear User,\n\nWe've recently updated our security features to better protect your Outlook account. These updates include enhanced encryption and new login verification methods.\n\nThank you for using Outlook!\n\nSincerely,\nOutlook Support Team");
                    }
                }
        );

        addEmailToList(
                "Team Collaboration",
                "Shared Document: Project Proposal",
                "I've shared a document with you in our shared workspace. Please review...",
                "Oct 1 • 4:45 PM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmail("Team Collaboration",
                                "Shared Document: Project Proposal",
                                "Oct 1 • 4:45 PM",
                                "user@outlook.com",
                                "Hi Team,\n\nI've shared the project proposal document in our shared workspace. Please review it and add your comments by Friday.\n\nThe document contains our project timeline, budget, and resource requirements.\n\nThanks!");
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
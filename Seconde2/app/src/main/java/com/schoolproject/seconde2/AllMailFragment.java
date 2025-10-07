package com.schoolproject.seconde2;

import android.content.Intent;
import android.view.View;
import androidx.fragment.app.Fragment;

public class AllMailFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("All Mail");

        // Load all types of emails
        loadAllEmailCategories();
    }

    private void loadAllEmailCategories() {
        addInboxEmails();
        addDraftEmails();
        addSentEmails();
        addTrashEmails();
        addOutlookEmails();
        addMixedEmails();
    }

    private void addInboxEmails() {
        // Personal emails from family and friends
        addEmail("Mom", "How are you doing?",
                "Hi son, just checking in to see how you're doing with your studies...",
                "INBOX • 5:20 PM",
                "Hi son,\n\nJust checking in to see how you're doing with your studies. Are you eating well?\n\nLove,\nMom");

        // School-related emails
        addEmail("USTH Administration", "Welcome to New Semester",
                "Dear students, welcome back to the new academic year...",
                "INBOX • 9:00 AM",
                "Dear students,\n\nWelcome back to the new academic year! We hope you had a great break.\n\nPlease check your schedules and course materials on Moodle.\n\nBest regards,\nUSTH Administration");

        // Project team emails
        addEmail("Project Team", "Meeting About Mobile App Project",
                "Hi team, let's meet tomorrow to discuss the progress...",
                "INBOX • 2:30 PM",
                "Hi team,\n\nLet's meet tomorrow at 10 AM in room 301 to discuss our mobile app progress.\n\nPlease bring your updates.\n\nThanks!");
    }

    private void addDraftEmails() {
        // School-related draft
        addEmail("Professor Johnson", "Question About Homework Assignment",
                "Dear Professor, I have a question about problem 3 in the homework...",
                "DRAFT • Just now",
                "Dear Professor,\n\nI have a question about problem 3 in the homework assignment. I'm not sure how to approach the second part...");

        // Personal draft
        addEmail("Friends Group", "Birthday Party Invitation",
                "Hi friends, I'm having a birthday party this Saturday at my place...",
                "DRAFT • 2 hours ago",
                "Hi friends,\n\nI'm having a birthday party this Saturday at my place! The party starts at 7 PM.\n\nPlease let me know if you can make it!\n\nBest,\nMe");
    }

    private void addSentEmails() {
        // School sent emails
        addEmail("Professor Smith", "Homework Assignment 5 Submission",
                "Dear Professor, please find my homework assignment attached...",
                "SENT • 8:15 AM",
                "Dear Professor,\n\nPlease find my homework assignment 5 attached to this email.\n\nI completed all the problems and included detailed explanations for problem 4.\n\nThank you!");

        // Project sent emails
        addEmail("Project Team", "Updated Design Mockups",
                "Hi team, I've updated the design mockups based on our discussion...",
                "SENT • 3:45 PM",
                "Hi team,\n\nI've updated the design mockups based on our discussion yesterday. The changes include improved navigation layout and better color contrast.\n\nPlease review and let me know your thoughts!");
    }

    private void addTrashEmails() {
        // Spam emails
        addEmail("Super Discount Store", "50% OFF Everything Today Only!",
                "Limited time offer! Get 50% off all items in our store...",
                "TRASH • Today",
                "Limited time offer! Get 50% off all items in our store today only!\n\nUse code: SAVE50\n\nHurry, offer ends tonight at midnight!");

        // Newsletter emails
        addEmail("Tech News Daily", "Weekly Tech News Roundup",
                "This week in tech: New smartphone releases, AI breakthroughs...",
                "TRASH • Yesterday",
                "This week in technology:\n\n- New smartphone models released\n- AI breakthrough in natural language processing\n- Major software security update\n\nRead more on our website!");
    }

    private void addOutlookEmails() {
        // System emails from Outlook
        addEmail("Microsoft Outlook Team", "Welcome to Outlook Integration",
                "We're excited to have you connect your Outlook account to our app...",
                "OUTLOOK • 10:30 AM",
                "Dear User,\n\nWe're excited to have you connect your Outlook account to our email app. You can now access all your Outlook emails directly from this application.\n\nBest regards,\nMicrosoft Outlook Team");

        // Calendar notifications
        addEmail("Outlook Calendar", "Meeting Reminder: Project Review",
                "Reminder: You have a project review meeting scheduled for tomorrow...",
                "OUTLOOK • 3:15 PM",
                "Meeting Reminder\n\nSubject: Project Review\nDate: Tomorrow at 2:00 PM\nLocation: Conference Room A\nDuration: 1 hour\n\nPlease come prepared with your project updates.");
    }

    private void addMixedEmails() {
        // School notifications
        addEmail("School Library", "Book Return Reminder",
                "This is a reminder that your borrowed books are due next week...",
                "INBOX • 11:45 AM",
                "Dear Student,\n\nThis is a reminder that your borrowed books are due for return next week.\n\nPlease return them by the due date to avoid late fees.\n\nThank you!");

        // Work in progress
        addEmail("Project Manager", "Weekly Project Status Report",
                "Here is my status report for this week. I completed the login screen...",
                "DRAFT • Yesterday",
                "Weekly Status Report\n\nCompleted this week:\n- Login screen implementation\n- Email composition UI\n- Navigation drawer setup\n\nPlanned for next week:\n- Email sending functionality\n- Attachment handling");
    }

    // Helper method to add an email to the list
    private void addEmail(String sender, String emailSubject, String previewText, String timestamp, String emailBody) {
        addEmailToList(sender, emailSubject, previewText, timestamp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmailDetails(sender, emailSubject, formatDate(timestamp), "user@example.com", emailBody);
            }
        });
    }

    // Helper method to format the date properly
    private String formatDate(String timeString) {
        if (timeString.contains("Today")) {
            return "Today, 2024";
        } else if (timeString.contains("Yesterday")) {
            return "Yesterday, 2024";
        } else {
            return timeString + ", 2024";
        }
    }

    // Method to open email detail screen
    private void openEmailDetails(String senderName, String emailSubject, String emailDate, String recipient, String emailBody) {
        Intent emailDetailIntent = new Intent(getActivity(), EmailDetailActivity.class);

        // Put all the email data into the intent
        emailDetailIntent.putExtra("sender", senderName);
        emailDetailIntent.putExtra("subject", emailSubject);
        emailDetailIntent.putExtra("date", emailDate);
        emailDetailIntent.putExtra("to", recipient);
        emailDetailIntent.putExtra("body", emailBody);

        // Start the email detail activity
        startActivity(emailDetailIntent);
    }
}
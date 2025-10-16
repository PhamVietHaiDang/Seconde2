package com.schoolproject.seconde2.EmailFragments;

import android.content.Intent;
import android.view.View;

import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;

public class AllMailFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("All Mail");
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
        addEmail("Mom", "How are you doing?",
                "Hi son, just checking in to see how you're doing with your studies...",
                "INBOX • 5:20 PM",
                "Hi son,\n\nJust checking in to see how you're doing with your studies. Are you eating well?\n\nLove,\nMom");
    }

    private void addDraftEmails() {
        addEmail("Professor Johnson", "Question About Homework Assignment",
                "Dear Professor, I have a question about problem 3 in the homework...",
                "DRAFT • Just now",
                "Dear Professor,\n\nI have a question about problem 3 in the homework assignment. I'm not sure how to approach the second part...");
    }

    private void addSentEmails() {
        addEmail("Professor Smith", "Homework Assignment 5 Submission",
                "Dear Professor, please find my homework assignment attached...",
                "SENT • 8:15 AM",
                "Dear Professor,\n\nPlease find my homework assignment 5 attached to this email.\n\nI completed all the problems and included detailed explanations for problem 4.\n\nThank you!");
    }

    private void addTrashEmails() {
        addEmail("Super Discount Store", "50% OFF Everything Today Only!",
                "Limited time offer! Get 50% off all items in our store...",
                "TRASH • Today",
                "Limited time offer! Get 50% off all items in our store today only!\n\nUse code: SAVE50\n\nHurry, offer ends tonight at midnight!");
    }

    private void addOutlookEmails() {
        addEmail("Microsoft Outlook Team", "Welcome to Outlook Integration",
                "We're excited to have you connect your Outlook account to our app...",
                "OUTLOOK • 10:30 AM",
                "Dear User,\n\nWe're excited to have you connect your Outlook account to our email app. You can now access all your Outlook emails directly from this application.\n\nBest regards,\nMicrosoft Outlook Team");
    }

    private void addMixedEmails() {
        // Some inbox emails
        addEmail("School Library", "Book Return Reminder",
                "This is a reminder that your borrowed books are due next week...",
                "INBOX • 11:45 AM",
                "Dear Student,\n\nThis is a reminder that your borrowed books are due for return next week.\n\nPlease return them by the due date to avoid late fees.\n\nThank you!");

        // Some sent emails
        addEmail("Project Manager", "Weekly Status Report",
                "Here is your weekly project status report and updates...",
                "SENT • 4:30 PM",
                "Hi Team,\n\nHere is the weekly status report:\n\n- Completed: User authentication module\n- In Progress: Email composition UI\n- Next: Integration testing\n\nBest regards,\nProject Manager");

        // More inbox emails
        addEmail("University Registrar", "Course Registration Confirmation",
                "Your course registration for next semester has been confirmed...",
                "INBOX • 9:15 AM",
                "Dear Student,\n\nYour course registration for the upcoming semester has been confirmed.\n\nRegistered Courses:\n- Mobile Application Development\n- Database Systems\n- Software Engineering\n\nThank you!");
    }

    // Helper method to add emails to the list
    private void addEmail(String sender, String emailSubject, String previewText, String timestamp, String emailBody) {
        addEmailToList(sender, emailSubject, previewText, timestamp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the email details when clicked
                openEmailDetails(sender, emailSubject, formatDate(timestamp), "user@example.com", emailBody);
            }
        });
    }

    // Make the date look nicer for the detail screen
    private String formatDate(String timeString) {
        if (timeString.contains("Today")) {
            return "Today, 2024";
        } else if (timeString.contains("Yesterday")) {
            return "Yesterday, 2024";
        } else {
            return timeString + ", 2024";
        }
    }

    // Open the email detail screen with all the email info
    private void openEmailDetails(String senderName, String emailSubject, String emailDate, String recipient, String emailBody) {
        // Check if we have a valid activity
        if (getActivity() == null) {
            return;
        }

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
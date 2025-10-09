package com.schoolproject.seconde2.EmailFragments;

import android.content.Intent;
import android.view.View;

import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;

public class DraftFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Drafts");

        // Check if we have any draft emails to show
        boolean hasDrafts = false; // Change this to true to see draft emails

        if (hasDrafts) {
            loadDraftEmails();
        } else {
            // Show empty state when no drafts
            showNoDataScreen("No drafts", "You don't have any saved drafts");
        }
    }

    private void loadDraftEmails() {
        // Add all the draft emails to the list
        addEmailToList(
                "Professor Johnson",
                "Question About Homework Assignment",
                "Dear Professor, I have a question about problem 3 in the homework...",
                "DRAFT • Just now",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("Professor Johnson",
                                "Question About Homework Assignment",
                                "DRAFT • Just now",
                                "prof.johnson@university.edu",
                                "Dear Professor,\n\nI have a question about problem 3 in the homework assignment. I'm not sure how to approach the second part...");
                    }
                }
        );

        addEmailToList(
                "Team Members",
                "Project Meeting Agenda",
                "Hi team, here's the agenda for our meeting tomorrow...",
                "DRAFT • Yesterday",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("Team Members",
                                "Project Meeting Agenda",
                                "DRAFT • Yesterday",
                                "team@project.com",
                                "Hi team,\n\nHere's the agenda for our meeting tomorrow:\n\n1. Project progress review\n2. Next sprint planning\n3. Technical challenges\n4. Q&A");
                    }
                }
        );

        addEmailToList(
                "HR Department",
                "Vacation Request - Next Month",
                "Dear HR, I would like to request vacation days for next month...",
                "DRAFT • Last week",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("HR Department",
                                "Vacation Request - Next Month",
                                "DRAFT • Last week",
                                "hr@company.com",
                                "Dear HR Department,\n\nI would like to request vacation days from [start date] to [end date].\n\nThank you for your consideration.\n\nBest regards,");
                    }
                }
        );
    }

    private void openEmailDetails(String from, String subject, String date, String to, String body) {
        // Open the email detail screen with the draft content
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
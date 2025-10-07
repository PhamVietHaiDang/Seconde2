package com.schoolproject.seconde2;

import android.view.View;
import androidx.fragment.app.Fragment;

public class SentFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Sent");

        addEmailToList(
                "me@example.com",
                "Homework Assignment 5 Submission",
                "Dear Professor, please find my homework assignment attached...",
                "8:15 AM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // View sent email
                    }
                }
        );

        addEmailToList(
                "me@example.com",
                "Updated Design Mockups",
                "Hi team, I've updated the design mockups based on our discussion...",
                "3:45 PM",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // View sent email
                    }
                }
        );
    }
}
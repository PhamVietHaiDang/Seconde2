package com.schoolproject.seconde2;

import android.view.View;
import androidx.fragment.app.Fragment;

public class DraftFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Drafts");

        addEmailToList(
                "me@example.com",
                "Question About Homework Assignment",
                "Dear Professor, I have a question about problem 3...",
                "Draft",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Open draft for editing
                    }
                }
        );

        addEmailToList(
                "me@example.com",
                "Birthday Party Invitation",
                "Hi friends, I'm having a birthday party this Saturday...",
                "Draft",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Open draft for editing
                    }
                }
        );
    }
}
package com.schoolproject.seconde2;

import android.view.View;
import androidx.fragment.app.Fragment;

public class TrashFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Trash");

        addEmailToList(
                "Super Discount Store",
                "50% OFF Everything Today Only!",
                "Limited time offer! Get 50% off all items in our store...",
                "Deleted",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // View deleted email
                    }
                }
        );

        addEmailToList(
                "Tech News Daily",
                "Weekly Tech News Roundup",
                "This week in tech: New smartphone releases, AI breakthroughs...",
                "Deleted",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // View deleted email
                    }
                }
        );
    }
}
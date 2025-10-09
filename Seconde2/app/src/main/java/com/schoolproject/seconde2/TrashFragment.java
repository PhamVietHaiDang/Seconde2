package com.schoolproject.seconde2;

import android.content.Intent;
import android.view.View;
import androidx.fragment.app.Fragment;

public class TrashFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Trash");

        // Check if we have any trash emails to show
        boolean hasTrashEmails = true; // Change this to false to see empty trash

        if (hasTrashEmails) {
            loadTrashEmails();
        } else {
            // Show empty trash message
            showNoDataScreen("Trash is empty", "No emails in the trash folder");
        }
    }

    private void loadTrashEmails() {
        // Add all the trash emails to the list

        // Spam promotion email
        addEmailToList(
                "Super Discount Store",
                "50% OFF Everything Today Only!",
                "Limited time offer! Get 50% off all items in our store...",
                "TRASH • Today",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("Super Discount Store",
                                "50% OFF Everything Today Only!",
                                "TRASH • Today",
                                "promotions@discountstore.com",
                                "Limited time offer! Get 50% off all items in our store today only!\n\nUse code: SAVE50\n\nHurry, offer ends tonight at midnight!");
                    }
                }
        );

        // Newsletter email
        addEmailToList(
                "Tech News Daily",
                "Weekly Tech News Roundup",
                "This week in tech: New smartphone releases, AI breakthroughs...",
                "TRASH • Yesterday",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("Tech News Daily",
                                "Weekly Tech News Roundup",
                                "TRASH • Yesterday",
                                "news@techdaily.com",
                                "This week in tech:\n\n- New smartphone releases from major brands\n- AI breakthroughs in machine learning\n- Latest updates in software development\n- Industry trends and analysis");
                    }
                }
        );

        // Social media notification
        addEmailToList(
                "Social Media Platform",
                "Your Weekly Activity Summary",
                "Here's what you missed on your social media accounts...",
                "TRASH • 3 days ago",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEmailDetails("Social Media Platform",
                                "Your Weekly Activity Summary",
                                "TRASH • 3 days ago",
                                "noreply@socialmedia.com",
                                "Here's your weekly activity summary:\n\n- 15 new followers\n- 32 likes on your posts\n- 5 new comments\n- 3 friend requests\n\nStay connected with your community!");
                    }
                }
        );
    }

    private void openEmailDetails(String from, String subject, String date, String to, String body) {
        // Open the email detail screen with the trash email
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
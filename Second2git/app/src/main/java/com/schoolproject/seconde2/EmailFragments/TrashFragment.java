package com.schoolproject.seconde2.EmailFragments;

import android.content.Intent;
import android.view.View;

import com.schoolproject.seconde2.activities.EmailDetailActivity;
import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;
import com.schoolproject.seconde2.model.Email;

public class TrashFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Trash");

        // Observe trash emails from database
        observeEmails("trash");
    }

    @Override
    protected void loadSampleEmails(String folder) {
        addTrashEmail("Super Discount Store",
                "50% OFF Everything Today Only!",
                "Limited time offer! Get 50% off all items in our store...",
                "TRASH • Today",
                "promotions@discountstore.com",
                "Limited time offer! Get 50% off all items in our store today only!\n\nUse code: SAVE50\n\nHurry, offer ends tonight at midnight!");

        addTrashEmail("Tech News Daily",
                "Weekly Tech News Roundup",
                "This week in tech: New smartphone releases, AI breakthroughs...",
                "TRASH • Yesterday",
                "news@techdaily.com",
                "This week in tech:\n\n- New smartphone releases from major brands\n- AI breakthroughs in machine learning\n- Latest updates in software development\n- Industry trends and analysis");
    }

    private void addTrashEmail(String from, String subject, String preview, String date, String to, String body) {
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
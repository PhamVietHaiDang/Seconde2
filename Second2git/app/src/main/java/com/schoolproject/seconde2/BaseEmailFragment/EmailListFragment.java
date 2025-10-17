package com.schoolproject.seconde2.BaseEmailFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.schoolproject.seconde2.R;
import com.schoolproject.seconde2.activities.ComposeEmailActivity;
import com.schoolproject.seconde2.activities.MainActivity;
import com.schoolproject.seconde2.model.Email;
import com.schoolproject.seconde2.viewmodel.EmailViewModel;

import java.util.List;

public class EmailListFragment extends Fragment {

    protected LinearLayout emailListContainer;
    protected ImageView menuButton;
    protected TextView folderTitle;
    protected LinearLayout composeButton;
    protected EmailViewModel emailViewModel;

    public EmailListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, android.os.Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email_list, container, false);

        // Initialize ViewModel
        emailViewModel = new ViewModelProvider(requireActivity()).get(EmailViewModel.class);

        emailListContainer = view.findViewById(R.id.emailListContainer);
        menuButton = view.findViewById(R.id.btnMenu);
        folderTitle = view.findViewById(R.id.folderTitle);
        composeButton = view.findViewById(R.id.composeButton);

        menuButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openNavigationDrawer();
            }
        });

        composeButton.setOnClickListener(v -> {
            android.content.Intent composeIntent = new android.content.Intent(getActivity(), ComposeEmailActivity.class);
            startActivity(composeIntent);
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadEmailData();
    }

    protected void loadEmailData() {
        // To be implemented by child fragments
    }

    protected void observeEmails(String folder) {
        emailViewModel.getEmailsByFolder(folder).observe(getViewLifecycleOwner(), emails -> {
            if (emails != null && !emails.isEmpty()) {
                displayEmails(emails);
            } else {
                // Load sample data or show empty state
                loadSampleEmails(folder);
            }
        });
    }

    protected void displayEmails(List<Email> emails) {
        emailListContainer.removeAllViews();

        for (Email email : emails) {
            addEmailToList(email.sender, email.subject, getEmailPreview(email.body),
                    formatDate(email.date), v -> openEmailDetails(email));
        }
    }

    protected void loadSampleEmails(String folder) {
        // Fallback to sample data if no emails in database
        // This will be overridden by child fragments
    }

    protected void addEmailToList(String from, String subject, String preview, String time, View.OnClickListener click) {
        if (emailListContainer == null) return;

        View emailView = LayoutInflater.from(getContext()).inflate(R.layout.item_email, emailListContainer, false);

        TextView fromText = emailView.findViewById(R.id.txtSender);
        TextView subjectText = emailView.findViewById(R.id.txtSubject);
        TextView timeText = emailView.findViewById(R.id.txtDate);

        fromText.setText(from);

        String fullSubject = subject;
        if (preview != null && !preview.isEmpty()) {
            fullSubject = subject + " - " + preview;
        }
        subjectText.setText(fullSubject);

        timeText.setText(time);

        if (click != null) {
            emailView.setOnClickListener(click);
        }

        emailListContainer.addView(emailView);
    }

    protected String getEmailPreview(String body) {
        if (body == null || body.isEmpty()) return "";
        return body.length() > 50 ? body.substring(0, 50) + "..." : body;
    }

    protected String formatDate(String date) {
        if (date == null) return "Unknown";
        // Simple date formatting - you can enhance this
        return date.contains("2024") ? date.substring(0, Math.min(date.length(), 16)) : date;
    }

    protected void setFolderTitle(String title) {
        if (folderTitle != null) {
            folderTitle.setText(title);
        }
    }

    protected void showNoDataScreen(String title, String message) {
        if (getView() == null || emailListContainer == null) return;

        emailListContainer.removeAllViews();

        emailListContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        View noDataView = getLayoutInflater().inflate(R.layout.layout_no_data, emailListContainer, false);

        TextView titleText = noDataView.findViewById(R.id.noDataTitle);
        TextView messageText = noDataView.findViewById(R.id.noDataMessage);

        titleText.setText(title);
        messageText.setText(message);

        noDataView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        emailListContainer.addView(noDataView);
    }

    protected void showNoDataScreen() {
        showNoDataScreen("No data found", "There's nothing to display here");
    }

    // Method to open email details - to be implemented by child fragments
    protected void openEmailDetails(Email email) {
        // To be implemented by child fragments
    }
}
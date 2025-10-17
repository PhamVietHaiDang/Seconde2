package com.schoolproject.seconde2.BaseEmailFragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    protected SwipeRefreshLayout swipeRefreshLayout;

    // Change from private to protected so child classes can access
    protected Handler uiHandler = new Handler(Looper.getMainLooper());
    protected boolean isRefreshing = false;

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
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        menuButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openNavigationDrawer();
            }
        });

        composeButton.setOnClickListener(v -> {
            android.content.Intent composeIntent = new android.content.Intent(getActivity(), ComposeEmailActivity.class);
            startActivity(composeIntent);
        });

        // Setup swipe refresh listener
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(() -> {
                refreshEmails();
            });

            // Set the refresh colors
            swipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
            );
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadEmailData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up handlers to prevent memory leaks
        uiHandler.removeCallbacksAndMessages(null);
    }

    protected void loadEmailData() {
        // To be implemented by child fragments
    }

    protected void refreshEmails() {
        if (isRefreshing) {
            return; // Prevent multiple simultaneous refreshes
        }

        if (emailViewModel.isUserSignedIn()) {
            isRefreshing = true;
            setRefreshing(true);

            String folder = getCurrentFolder();
            emailViewModel.refreshEmails(folder);
            showToast("Refreshing emails...");

            // Auto-stop refresh after timeout
            uiHandler.postDelayed(() -> {
                if (isRefreshing) {
                    setRefreshing(false);
                    isRefreshing = false;
                    showToast("Refresh completed");
                }
            }, 15000);
        } else {
            showToast("Please sign in to refresh emails");
            setRefreshing(false);
        }
    }

    // Safe method to show Toast
    protected void showToast(String message) {
        if (getContext() != null) {
            try {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Ignore toast errors
            }
        }
    }

    // Helper method to safely set refreshing state
    protected void setRefreshing(final boolean refreshing) {
        if (getActivity() != null && swipeRefreshLayout != null) {
            getActivity().runOnUiThread(() -> {
                if (swipeRefreshLayout != null) {
                    try {
                        swipeRefreshLayout.setRefreshing(refreshing);
                    } catch (Exception e) {
                        // Ignore refresh state errors
                    }
                }
            });
        }
    }

    // Helper method to get current folder
    protected String getCurrentFolder() {
        // Default to inbox, override in child fragments if different
        return "inbox";
    }

    protected void observeEmails(String folder) {
        emailViewModel.getEmailsByFolder(folder).observe(getViewLifecycleOwner(), emails -> {
            isRefreshing = false;
            setRefreshing(false);
            uiHandler.removeCallbacksAndMessages(null); // Clear timeout

            if (emails != null && !emails.isEmpty()) {
                displayEmailsOptimized(emails);
            } else {
                loadSampleEmails(folder);
            }
        });
    }

    // Optimized email display
    protected void displayEmailsOptimized(List<Email> emails) {
        if (emailListContainer == null) return;

        emailListContainer.removeAllViews();

        // Use ViewHolder pattern and limit initial rendering
        int maxInitialDisplay = 20;
        List<Email> emailsToDisplay = emails.size() > maxInitialDisplay ?
                emails.subList(0, maxInitialDisplay) : emails;

        for (Email email : emailsToDisplay) {
            addEmailToListOptimized(email);
        }

        // Load remaining emails in background if needed
        if (emails.size() > maxInitialDisplay) {
            loadRemainingEmailsAsync(emails.subList(maxInitialDisplay, emails.size()));
        }
    }

    protected void displayEmails(List<Email> emails) {
        displayEmailsOptimized(emails);
    }

    private void addEmailToListOptimized(Email email) {
        if (emailListContainer == null || getContext() == null) return;

        View emailView = LayoutInflater.from(getContext()).inflate(R.layout.item_email, emailListContainer, false);

        TextView fromText = emailView.findViewById(R.id.txtSender);
        TextView subjectText = emailView.findViewById(R.id.txtSubject);
        TextView timeText = emailView.findViewById(R.id.txtDate);

        fromText.setText(email.sender);

        // Optimized subject display
        String preview = getEmailPreviewOptimized(email.body);
        String displayText = preview.isEmpty() ? email.subject : email.subject + " - " + preview;
        subjectText.setText(displayText);

        timeText.setText(email.date);

        emailView.setOnClickListener(v -> openEmailDetails(email));
        emailListContainer.addView(emailView);
    }

    protected void addEmailToList(String from, String subject, String preview, String time, View.OnClickListener click) {
        // Legacy method - redirect to optimized version
        Email tempEmail = new Email();
        tempEmail.sender = from;
        tempEmail.subject = subject;
        tempEmail.body = preview;
        tempEmail.date = time;
        addEmailToListOptimized(tempEmail);
    }

    protected String getEmailPreviewOptimized(String body) {
        if (body == null || body.isEmpty()) return "";
        // Only create preview if body is not the default placeholder
        return "Email content".equals(body) ? "" :
                (body.length() > 50 ? body.substring(0, 50) + "..." : body);
    }

    protected String getEmailPreview(String body) {
        return getEmailPreviewOptimized(body);
    }

    private void loadRemainingEmailsAsync(List<Email> remainingEmails) {
        new Thread(() -> {
            for (Email email : remainingEmails) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (emailListContainer != null) {
                            addEmailToListOptimized(email);
                        }
                    });
                }
                // Small delay to prevent UI freezing
                try { Thread.sleep(50); } catch (InterruptedException e) { }
            }
        }).start();
    }

    protected void loadSampleEmails(String folder) {
        // Fallback to sample data if no emails in database
        // This will be overridden by child fragments

        // Ensure refresh stops when showing sample/no data
        setRefreshing(false);
    }

    protected String formatDate(String date) {
        if (date == null) return "Unknown";

        // If date already has year format, return as is
        if (date.contains("2024") || date.contains("2023") || date.contains("2025")) {
            return date;
        }

        // For dates without year, try to parse and add year
        try {
            // Simple enhancement - you can make this more sophisticated
            if (date.matches(".*\\d{1,2}:\\d{2}.*")) {
                // Time format, assume today
                return date + " • Today";
            } else if (date.equals("Yesterday")) {
                return date;
            } else if (date.matches("[A-Za-z]{3} \\d{1,2}")) {
                // Month day format, add current year
                return date + ", 2024";
            }
        } catch (Exception e) {
            // If parsing fails, return original date
        }

        return date;
    }

    protected void setFolderTitle(String title) {
        if (folderTitle != null) {
            folderTitle.setText(title);
        }
    }

    protected void showNoDataScreen(String title, String message) {
        if (getView() == null || emailListContainer == null || getContext() == null) return;

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

        // Stop refresh if showing no data
        setRefreshing(false);
    }

    protected void showNoDataScreen() {
        showNoDataScreen("No data found", "There's nothing to display here");
    }

    // Method to open email details - to be implemented by child fragments
    protected void openEmailDetails(Email email) {
        // To be implemented by child fragments

    }

    // ========== ADDED HELPER METHOD ==========
    protected android.content.Intent createEmailDetailIntent(Email email) {
        android.content.Intent intent = new android.content.Intent(getActivity(),
                com.schoolproject.seconde2.activities.EmailDetailActivity.class);
        intent.putExtra("sender", email.sender);
        intent.putExtra("subject", email.subject);
        intent.putExtra("date", email.date);
        intent.putExtra("to", emailViewModel.isUserSignedIn() ? emailViewModel.getUserEmail() : "you@example.com");
        intent.putExtra("body", email.body);
        intent.putExtra("html_content", email.htmlContent != null ? email.htmlContent : "");
        intent.putExtra("is_html", email.isHtml);
        intent.putExtra("folder", email.folder);
        intent.putExtra("email_id", email.id);
        return intent;
    }
}
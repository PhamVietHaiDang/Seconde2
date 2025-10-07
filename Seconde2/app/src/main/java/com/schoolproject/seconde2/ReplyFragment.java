package com.schoolproject.seconde2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class ReplyFragment extends Fragment {

    // UI Elements
    private EditText editFrom, editTo, editSubject, editMessage;
    private ImageButton btnBack, btnSend;
    private TextView toolbarTitle;
    private TextView txtOriginalSender, txtOriginalSubject, txtOriginalDate, txtOriginalBody;
    private ImageView iconType;

    // Original email data
    private String originalSender;
    private String originalSubject;
    private String originalDate;
    private String originalBody;

    public ReplyFragment() {
        // Required empty public constructor for fragments
    }

    // Factory method to create fragment with email data
    public static ReplyFragment newInstance(String sender, String subject, String date, String body) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        args.putString("sender", sender);
        args.putString("subject", subject);
        args.putString("date", date);
        args.putString("body", body);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reply_forward, container, false);

        // Initialize all views
        initViews(rootView);

        // Setup button click handlers
        setupClickListeners();

        // Load the original email content
        loadOriginalEmail();

        // Configure UI for reply mode
        setupReplyMode();

        return rootView;
    }

    private void initViews(View view) {
        // Get references to all UI components
        editFrom = view.findViewById(R.id.editFrom);
        editTo = view.findViewById(R.id.editTo);
        editSubject = view.findViewById(R.id.editSubject);
        editMessage = view.findViewById(R.id.editMessage);

        btnBack = view.findViewById(R.id.btnBack);
        btnSend = view.findViewById(R.id.btnSend);

        toolbarTitle = view.findViewById(R.id.toolbarTitle);

        // Original email display components
        txtOriginalSender = view.findViewById(R.id.txtOriginalSender);
        txtOriginalSubject = view.findViewById(R.id.txtOriginalSubject);
        txtOriginalDate = view.findViewById(R.id.txtOriginalDate);
        txtOriginalBody = view.findViewById(R.id.txtOriginalBody);

        iconType = view.findViewById(R.id.iconType);

        // Set the reply icon
        iconType.setImageResource(R.drawable.ic_reply);
    }

    private void setupClickListeners() {
        // Handle back button press
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        // Handle send button press
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReply();
            }
        });
    }

    private void loadOriginalEmail() {
        // Extract original email data from arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            originalSender = arguments.getString("sender");
            originalSubject = arguments.getString("subject");
            originalDate = arguments.getString("date");
            originalBody = arguments.getString("body");

            // Display the original email information
            if (txtOriginalSender != null) {
                txtOriginalSender.setText(originalSender);
            }
            if (txtOriginalSubject != null) {
                txtOriginalSubject.setText(originalSubject);
            }
            if (txtOriginalDate != null) {
                txtOriginalDate.setText(originalDate);
            }
            if (txtOriginalBody != null) {
                txtOriginalBody.setText(originalBody);
            }
        }
    }

    private void setupReplyMode() {
        // Set toolbar title
        if (toolbarTitle != null) {
            toolbarTitle.setText("Reply");
        }

        // Auto-fill recipient field
        if (originalSender != null && !originalSender.isEmpty()) {
            String replyToEmail = extractEmailFromSender(originalSender);
            editTo.setText(replyToEmail);
        }

        // Auto-fill subject with "Re: " prefix
        if (originalSubject != null && !originalSubject.isEmpty()) {
            String replySubject = "Re: " + originalSubject;
            editSubject.setText(replySubject);
        }

        // Clear message field and set hint text
        editMessage.setText(""); // Start with empty compose area
        editMessage.setHint("Type your reply here...");
    }

    private void sendReply() {
        // Get input values from form fields
        String fromEmail = editFrom.getText().toString().trim();
        String toEmail = editTo.getText().toString().trim();
        String subjectText = editSubject.getText().toString().trim();
        String messageText = editMessage.getText().toString().trim();

        // Basic field validation
        if (fromEmail.isEmpty() || toEmail.isEmpty() || subjectText.isEmpty() || messageText.isEmpty()) {
            // TODO: Show proper error message to user
            return;
        }

        // Send the reply email - only send user's reply text, not the original quoted message
        // Note: Using hardcoded password for now, should implement proper OAuth later
        EmailConfig config = new EmailConfig(fromEmail, "your-app-password");

        EmailSender.sendEmail(config, toEmail, subjectText, messageText, new EmailSender.EmailSendListener() {
            @Override
            public void onSuccess() {
                // Handle successful email sending
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: Show success toast message
                            // Navigate back to previous screen
                            getActivity().onBackPressed();
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Handle email sending error
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: Show error dialog with details
                        }
                    });
                }
            }
        });
    }

    // Helper method to extract email address from sender string
    private String extractEmailFromSender(String senderString) {
        if (senderString == null || senderString.isEmpty()) {
            return "";
        }

        // Check if sender is in format "Name <email@domain.com>"
        if (senderString.contains("<") && senderString.contains(">")) {
            int startIndex = senderString.indexOf("<") + 1;
            int endIndex = senderString.indexOf(">");
            if (startIndex > 0 && endIndex > startIndex) {
                return senderString.substring(startIndex, endIndex);
            }
        }

        // If no brackets found, assume the whole string is the email
        return senderString;
    }
}
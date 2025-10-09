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

public class ForwardFragment extends Fragment {

    // All the input fields and buttons
    private EditText fromField, toField, subjectField, messageField;
    private ImageButton backButton, sendButton;
    private TextView toolbarTitle;
    private TextView originalSenderText, originalSubjectText, originalDateText, originalBodyText;
    private ImageView iconType;

    // Store the original email data
    private String originalSender;
    private String originalSubject;
    private String originalDate;
    private String originalBody;

    public ForwardFragment() {
        // Empty constructor needed for fragments
    }

    // Create a new forward fragment with email data
    public static ForwardFragment newInstance(String sender, String subject, String date, String body) {
        ForwardFragment fragment = new ForwardFragment();
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
        View view = inflater.inflate(R.layout.fragment_reply_forward, container, false);

        // Set up all the views
        setupViews(view);

        // Set up button click listeners
        setupClickListeners();

        // Load the original email we're forwarding
        loadOriginalEmail();

        // Set up everything for forwarding mode
        setupForwardMode();

        return view;
    }

    private void setupViews(View view) {
        // Find all the input fields
        fromField = view.findViewById(R.id.editFrom);
        toField = view.findViewById(R.id.editTo);
        subjectField = view.findViewById(R.id.editSubject);
        messageField = view.findViewById(R.id.editMessage);

        // Find all the buttons
        backButton = view.findViewById(R.id.btnBack);
        sendButton = view.findViewById(R.id.btnSend);

        // Find the toolbar title
        toolbarTitle = view.findViewById(R.id.toolbarTitle);

        // Find the views that show the original email
        originalSenderText = view.findViewById(R.id.txtOriginalSender);
        originalSubjectText = view.findViewById(R.id.txtOriginalSubject);
        originalDateText = view.findViewById(R.id.txtOriginalDate);
        originalBodyText = view.findViewById(R.id.txtOriginalBody);

        // Find the icon and set it to forward icon
        iconType = view.findViewById(R.id.iconType);
        iconType.setImageResource(R.drawable.ic_forward);
    }

    private void setupClickListeners() {
        // Back button goes back to previous screen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        // Send button sends the forwarded email
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendForwardedEmail();
            }
        });
    }

    private void loadOriginalEmail() {
        // Get the email data that was passed to us
        Bundle arguments = getArguments();
        if (arguments != null) {
            originalSender = arguments.getString("sender");
            originalSubject = arguments.getString("subject");
            originalDate = arguments.getString("date");
            originalBody = arguments.getString("body");

            // Show the original email information
            if (originalSenderText != null) {
                originalSenderText.setText(originalSender);
            }
            if (originalSubjectText != null) {
                originalSubjectText.setText(originalSubject);
            }
            if (originalDateText != null) {
                originalDateText.setText(originalDate);
            }
            if (originalBodyText != null) {
                originalBodyText.setText(originalBody);
            }
        }
    }

    private void setupForwardMode() {
        // Set the toolbar title to "Forward"
        if (toolbarTitle != null) {
            toolbarTitle.setText("Forward");
        }

        // Pre-fill the subject with "Fwd: " prefix
        if (originalSubject != null && !originalSubject.isEmpty()) {
            String forwardSubject = "Fwd: " + originalSubject;
            subjectField.setText(forwardSubject);
        }

        // Create the forwarded message with original email content
        if (originalBody != null && !originalBody.isEmpty()) {
            StringBuilder forwardedMessage = new StringBuilder();
            forwardedMessage.append("\n\n");
            forwardedMessage.append("---------- Forwarded message ---------\n");
            forwardedMessage.append("From: ").append(originalSender != null ? originalSender : "Unknown").append("\n");
            forwardedMessage.append("Date: ").append(originalDate != null ? originalDate : "Unknown").append("\n");
            forwardedMessage.append("Subject: ").append(originalSubject != null ? originalSubject : "No subject").append("\n");
            forwardedMessage.append("\n");
            forwardedMessage.append(originalBody);

            messageField.setText(forwardedMessage.toString());
        }
    }

    private void sendForwardedEmail() {
        // Get all the input values
        String fromEmail = fromField.getText().toString().trim();
        String toEmail = toField.getText().toString().trim();
        String subjectText = subjectField.getText().toString().trim();
        String messageText = messageField.getText().toString().trim();

        // Check if all required fields are filled
        if (fromEmail.isEmpty() || toEmail.isEmpty() || subjectText.isEmpty() || messageText.isEmpty()) {
            // TODO: Show error message to user
            return;
        }

        // Send the email using our EmailSender class
        EmailConfig config = new EmailConfig(fromEmail, "your-app-password");

        EmailSender.sendEmail(config, toEmail, subjectText, messageText, new EmailSender.EmailSendListener() {
            @Override
            public void onSuccess() {
                // Email sent successfully
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: Show success message
                            // Go back to previous screen
                            getActivity().onBackPressed();
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                // There was an error sending the email
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: Show error message to user
                        }
                    });
                }
            }
        });
    }
}
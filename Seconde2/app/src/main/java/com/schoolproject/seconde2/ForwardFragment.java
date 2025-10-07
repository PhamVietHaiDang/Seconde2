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

    // UI components
    private EditText editFrom, editTo, editSubject, editMessage;
    private ImageButton btnBack, btnSend;
    private TextView toolbarTitle;
    private TextView txtOriginalSender, txtOriginalSubject, txtOriginalDate, txtOriginalBody;
    private ImageView iconType;

    // Original email data - maybe I should use a model class for this later
    private String originalSender;
    private String originalSubject;
    private String originalDate;
    private String originalBody;

    public ForwardFragment() {
        // Required empty public constructor
    }

    // Factory method to create new instance with email data
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

        // Initialize all the views
        initViews(view);

        // Set up click listeners
        setupClickListeners();

        // Load the original email content
        loadOriginalEmail();

        // Configure for forward mode
        setupForwardMode();

        return view;
    }

    private void initViews(View view) {
        // Find all the views by ID
        editFrom = view.findViewById(R.id.editFrom);
        editTo = view.findViewById(R.id.editTo);
        editSubject = view.findViewById(R.id.editSubject);
        editMessage = view.findViewById(R.id.editMessage);

        btnBack = view.findViewById(R.id.btnBack);
        btnSend = view.findViewById(R.id.btnSend);

        toolbarTitle = view.findViewById(R.id.toolbarTitle);

        // Original email display views
        txtOriginalSender = view.findViewById(R.id.txtOriginalSender);
        txtOriginalSubject = view.findViewById(R.id.txtOriginalSubject);
        txtOriginalDate = view.findViewById(R.id.txtOriginalDate);
        txtOriginalBody = view.findViewById(R.id.txtOriginalBody);

        iconType = view.findViewById(R.id.iconType);

        // Set the forward icon
        iconType.setImageResource(R.drawable.ic_forward);
    }

    private void setupClickListeners() {
        // Back button handling
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        // Send button handling
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendForward();
            }
        });
    }

    private void loadOriginalEmail() {
        // Get the arguments passed to this fragment
        Bundle arguments = getArguments();
        if (arguments != null) {
            originalSender = arguments.getString("sender");
            originalSubject = arguments.getString("subject");
            originalDate = arguments.getString("date");
            originalBody = arguments.getString("body");

            // Display original email info
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

    private void setupForwardMode() {
        // Set the toolbar title
        if (toolbarTitle != null) {
            toolbarTitle.setText("Forward");
        }

        // Pre-fill the subject with "Fwd: " prefix
        if (originalSubject != null && !originalSubject.isEmpty()) {
            String forwardSubject = "Fwd: " + originalSubject;
            editSubject.setText(forwardSubject);
        }

        // Create the forwarded message body with original email content
        if (originalBody != null && !originalBody.isEmpty()) {
            StringBuilder forwardedMessage = new StringBuilder();
            forwardedMessage.append("\n\n");
            forwardedMessage.append("---------- Forwarded message ---------\n");
            forwardedMessage.append("From: ").append(originalSender != null ? originalSender : "Unknown").append("\n");
            forwardedMessage.append("Date: ").append(originalDate != null ? originalDate : "Unknown").append("\n");
            forwardedMessage.append("Subject: ").append(originalSubject != null ? originalSubject : "No subject").append("\n");
            forwardedMessage.append("\n");
            forwardedMessage.append(originalBody);

            editMessage.setText(forwardedMessage.toString());
        }
    }

    private void sendForward() {
        // Get the input values
        String fromEmail = editFrom.getText().toString().trim();
        String toEmail = editTo.getText().toString().trim();
        String subjectText = editSubject.getText().toString().trim();
        String messageText = editMessage.getText().toString().trim();

        // Basic validation - TODO: maybe add better validation later
        if (fromEmail.isEmpty() || toEmail.isEmpty() || subjectText.isEmpty() || messageText.isEmpty()) {
            // Show error message - need to implement toast or dialog
            return;
        }

        // TODO: Implement actual email sending logic
        // For now just using the EmailSender class
        EmailConfig config = new EmailConfig(fromEmail, "your-app-password"); // Note: should use proper auth

        EmailSender.sendEmail(config, toEmail, subjectText, messageText, new EmailSender.EmailSendListener() {
            @Override
            public void onSuccess() {
                // Handle success on UI thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Show success message - TODO: implement toast
                            // Go back to previous screen
                            getActivity().onBackPressed();
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error on UI thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Show error message - TODO: implement error dialog
                        }
                    });
                }
            }
        });
    }
}
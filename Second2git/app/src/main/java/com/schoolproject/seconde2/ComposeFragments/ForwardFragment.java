package com.schoolproject.seconde2.ComposeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.schoolproject.seconde2.EmailConfig;
import com.schoolproject.seconde2.ServiceFragment.EmailSender;
import com.schoolproject.seconde2.R;

public class ForwardFragment extends Fragment {

    private EditText fromField, toField, subjectField, messageField;
    private ImageButton backButton, sendButton;
    private TextView toolbarTitle;
    private TextView originalSenderText, originalSubjectText, originalDateText, originalBodyText;
    private ImageView iconType;

    private String originalSender;
    private String originalSubject;
    private String originalDate;
    private String originalBody;

    private static final String EMAIL_APP_PASSWORD = "your-app-password";
    private static final String FORWARD_PREFIX = "Fwd: ";

    public ForwardFragment() {
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_forward, container, false);

        setupViews(view);
        setupClickListeners();
        loadOriginalEmail();
        setupForwardMode();

        return view;
    }

    private void setupViews(View view) {
        fromField = view.findViewById(R.id.editFrom);
        toField = view.findViewById(R.id.editTo);
        subjectField = view.findViewById(R.id.editSubject);
        messageField = view.findViewById(R.id.editMessage);

        backButton = view.findViewById(R.id.btnBack);
        sendButton = view.findViewById(R.id.btnSend);

        toolbarTitle = view.findViewById(R.id.toolbarTitle);

        originalSenderText = view.findViewById(R.id.txtOriginalSender);
        originalSubjectText = view.findViewById(R.id.txtOriginalSubject);
        originalDateText = view.findViewById(R.id.txtOriginalDate);
        originalBodyText = view.findViewById(R.id.txtOriginalBody);

        iconType = view.findViewById(R.id.iconType);
        iconType.setImageResource(R.drawable.ic_forward);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> goBack());
        sendButton.setOnClickListener(v -> sendForwardedEmail());
    }

    private void loadOriginalEmail() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            originalSender = arguments.getString("sender");
            originalSubject = arguments.getString("subject");
            originalDate = arguments.getString("date");
            originalBody = arguments.getString("body");

            updateOriginalEmailDisplay();
        }
    }

    private void updateOriginalEmailDisplay() {
        if (originalSenderText != null) originalSenderText.setText(originalSender);
        if (originalSubjectText != null) originalSubjectText.setText(originalSubject);
        if (originalDateText != null) originalDateText.setText(originalDate);
        if (originalBodyText != null) originalBodyText.setText(originalBody);
    }

    private void setupForwardMode() {
        if (toolbarTitle != null) {
            toolbarTitle.setText("Forward");
        }

        setupForwardSubject();
        setupForwardMessage();
    }

    private void setupForwardSubject() {
        if (originalSubject != null && !originalSubject.isEmpty()) {
            String forwardSubject = FORWARD_PREFIX + originalSubject;
            subjectField.setText(forwardSubject);
        }
    }

    private void setupForwardMessage() {
        if (originalBody != null && !originalBody.isEmpty()) {
            String forwardedMessage = buildForwardedMessage();
            messageField.setText(forwardedMessage);
        }
    }

    private String buildForwardedMessage() {
        StringBuilder message = new StringBuilder();
        message.append("\n\n");
        message.append("---------- Forwarded message ---------\n");
        message.append("From: ").append(originalSender != null ? originalSender : "Unknown").append("\n");
        message.append("Date: ").append(originalDate != null ? originalDate : "Unknown").append("\n");
        message.append("Subject: ").append(originalSubject != null ? originalSubject : "No subject").append("\n");
        message.append("\n");
        message.append(originalBody);

        return message.toString();
    }

    private void sendForwardedEmail() {
        String fromEmail = fromField.getText().toString().trim();
        String toEmail = toField.getText().toString().trim();
        String subjectText = subjectField.getText().toString().trim();
        String messageText = messageField.getText().toString().trim();

        if (!validateEmailFields(fromEmail, toEmail, subjectText, messageText)) {
            return;
        }

        sendEmailToServer(fromEmail, toEmail, subjectText, messageText);
    }

    private boolean validateEmailFields(String from, String to, String subject, String message) {
        if (from.isEmpty() || to.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            showToast("Please fill all required fields");
            return false;
        }
        return true;
    }

    private void sendEmailToServer(String from, String to, String subject, String message) {
        EmailConfig config = new EmailConfig(from, EMAIL_APP_PASSWORD);

        EmailSender.sendEmail(config, to, subject, message, new EmailSender.EmailSendListener() {
            @Override
            public void onSuccess() {
                handleSendSuccess();
            }

            @Override
            public void onError(String errorMessage) {
                handleSendError(errorMessage);
            }
        });
    }

    private void handleSendSuccess() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                showToast("Email forwarded successfully");
                goBack();
            });
        }
    }

    private void handleSendError(String errorMessage) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                showToast("Failed to send: " + errorMessage);
            });
        }
    }

    private void goBack() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
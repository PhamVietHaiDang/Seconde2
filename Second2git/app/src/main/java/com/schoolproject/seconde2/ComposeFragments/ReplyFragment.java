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

public class ReplyFragment extends Fragment {

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
    private static final String REPLY_PREFIX = "Re: ";

    public ReplyFragment() {
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_forward, container, false);

        setupViews(view);
        setupClickListeners();
        loadOriginalEmail();
        setupReplyMode();

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
        iconType.setImageResource(R.drawable.ic_reply);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> goBack());
        sendButton.setOnClickListener(v -> sendReplyEmail());
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

    private void setupReplyMode() {
        if (toolbarTitle != null) {
            toolbarTitle.setText("Reply");
        }

        setupReplyToField();
        setupReplySubject();
        setupReplyMessage();
    }

    private void setupReplyToField() {
        if (originalSender != null && !originalSender.isEmpty()) {
            String replyToEmail = extractEmailFromSender(originalSender);
            toField.setText(replyToEmail);
        }
    }

    private void setupReplySubject() {
        if (originalSubject != null && !originalSubject.isEmpty()) {
            String replySubject = REPLY_PREFIX + originalSubject;
            subjectField.setText(replySubject);
        }
    }

    private void setupReplyMessage() {
        messageField.setText("");
        messageField.setHint("Type your reply here...");
    }

    private void sendReplyEmail() {
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
                showToast("Reply sent successfully");
                goBack();
            });
        }
    }

    private void handleSendError(String errorMessage) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                showToast("Failed to send reply: " + errorMessage);
            });
        }
    }

    private String extractEmailFromSender(String senderString) {
        if (senderString == null || senderString.isEmpty()) {
            return "";
        }

        if (senderString.contains("<") && senderString.contains(">")) {
            int startIndex = senderString.indexOf("<") + 1;
            int endIndex = senderString.indexOf(">");
            if (startIndex > 0 && endIndex > startIndex) {
                return senderString.substring(startIndex, endIndex);
            }
        }

        return senderString;
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
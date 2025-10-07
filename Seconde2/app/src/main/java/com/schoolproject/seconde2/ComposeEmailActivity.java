package com.schoolproject.seconde2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ComposeEmailActivity extends AppCompatActivity {

    // UI elements for email fields
    private EditText fromField, toField, ccField, bccField, subjectField, messageField;

    // Buttons for actions
    private Button sendButton;
    private ImageButton exitButton, attachButton, sendIconButton, settingsButton, expandToButton;

    // Loading indicator
    private ProgressBar loadingBar;

    // Track if CC/BCC fields are visible
    private boolean areCcBccVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_email);

        setupUIElements();
        setupButtonListeners();
    }

    private void setupUIElements() {
        // Find all the email input fields
        fromField = findViewById(R.id.editFrom);
        toField = findViewById(R.id.editTo);
        ccField = findViewById(R.id.editCc);
        bccField = findViewById(R.id.editBcc);
        subjectField = findViewById(R.id.editSubject);
        messageField = findViewById(R.id.editMessage);

        // Find all the buttons
        sendButton = findViewById(R.id.btnSend);
        exitButton = findViewById(R.id.btnExit);
        attachButton = findViewById(R.id.btnAttachment);
        sendIconButton = findViewById(R.id.btnSendIcon);
        settingsButton = findViewById(R.id.btnSettings);
        expandToButton = findViewById(R.id.btnExpandTo);

        // Find the progress bar
        loadingBar = findViewById(R.id.progressBar);

        // Hide CC and BCC fields by default
        ccField.setVisibility(View.GONE);
        bccField.setVisibility(View.GONE);
    }

    private void setupButtonListeners() {
        // Set up click listeners for all buttons
        sendButton.setOnClickListener(v -> handleSendEmailAction());
        sendIconButton.setOnClickListener(v -> handleSendEmailAction());
        exitButton.setOnClickListener(v -> closeComposeScreen());
        attachButton.setOnClickListener(v -> handleAddAttachment());
        settingsButton.setOnClickListener(v -> showSettingsMenu());
        expandToButton.setOnClickListener(v -> toggleCcBccFields());
    }

    private void toggleCcBccFields() {
        // Switch between showing and hiding CC/BCC fields
        areCcBccVisible = !areCcBccVisible;

        if (areCcBccVisible) {
            // Show CC and BCC fields
            ccField.setVisibility(View.VISIBLE);
            bccField.setVisibility(View.VISIBLE);
            expandToButton.setImageResource(R.drawable.ic_arrow_drop_up);
        } else {
            // Hide CC and BCC fields
            ccField.setVisibility(View.GONE);
            bccField.setVisibility(View.GONE);
            expandToButton.setImageResource(R.drawable.ic_arrow_drop_down);
        }
    }

    private void handleAddAttachment() {
        showMessage("Attachment feature will be added in the next update!");
    }

    private void showSettingsMenu() {
        // Create and show the popup menu
        PopupMenu settingsMenu = new PopupMenu(this, settingsButton);
        settingsMenu.getMenuInflater().inflate(R.menu.compose_settings_menu, settingsMenu.getMenu());

        settingsMenu.setOnMenuItemClickListener(item -> {
            return handleSettingsMenuClick(item);
        });

        settingsMenu.show();
    }

    private boolean handleSettingsMenuClick(MenuItem menuItem) {
        // Handle which menu item was clicked
        int itemId = menuItem.getItemId();

        if (itemId == R.id.menu_save_draft) {
            saveEmailAsDraft();
            return true;
        } else if (itemId == R.id.menu_discard) {
            discardEmail();
            return true;
        } else if (itemId == R.id.menu_settings) {
            openSettingsScreen();
            return true;
        }

        return false;
    }

    private void saveEmailAsDraft() {
        // Check if there's any content to save
        if (!hasAnyEmailContent()) {
            showMessage("No content to save as draft");
            return;
        }

        showMessage("Email saved as draft successfully");
        // Here we would normally save to database or file
    }

    private void discardEmail() {
        // Only show confirmation if there's content
        if (hasAnyEmailContent()) {
            showDiscardConfirmation();
        } else {
            closeComposeScreen();
        }
    }

    private void showDiscardConfirmation() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Discard Email")
                .setMessage("Are you sure you want to discard this email? Your changes will be lost.")
                .setPositiveButton("Discard", (dialog, which) -> {
                    closeComposeScreen();
                })
                .setNegativeButton("Keep Editing", null)
                .show();
    }

    private void openSettingsScreen() {
        showMessage("App settings will be available soon!");
    }

    private void handleSendEmailAction() {
        // Get all the email data from fields
        EmailData currentEmail = collectEmailData();

        // Validate the email before sending
        if (!isEmailValid(currentEmail)) {
            showMessage("Please fill in all required fields: From, To, Subject, and Message");
            return;
        }

        // Send the email
        sendEmailToServer(currentEmail);
    }

    private EmailData collectEmailData() {
        // Collect all the data from input fields
        String fromAddress = fromField.getText().toString().trim();
        String toAddress = toField.getText().toString().trim();
        String ccAddress = ccField.getText().toString().trim();
        String bccAddress = bccField.getText().toString().trim();
        String emailSubject = subjectField.getText().toString().trim();
        String emailBody = messageField.getText().toString().trim();

        return new EmailData(fromAddress, toAddress, ccAddress, bccAddress, emailSubject, emailBody);
    }

    private boolean isEmailValid(EmailData email) {
        // Check if required fields are filled
        boolean hasFrom = !email.fromAddress.isEmpty();
        boolean hasTo = !email.toAddress.isEmpty();
        boolean hasSubject = !email.subject.isEmpty();
        boolean hasBody = !email.body.isEmpty();

        return hasFrom && hasTo && hasSubject && hasBody;
    }

    private boolean hasAnyEmailContent() {
        // Check if any field has content
        EmailData email = collectEmailData();

        boolean hasFrom = !email.fromAddress.isEmpty();
        boolean hasTo = !email.toAddress.isEmpty();
        boolean hasCc = !email.ccAddress.isEmpty();
        boolean hasBcc = !email.bccAddress.isEmpty();
        boolean hasSubject = !email.subject.isEmpty();
        boolean hasBody = !email.body.isEmpty();

        return hasFrom || hasTo || hasCc || hasBcc || hasSubject || hasBody;
    }

    private void sendEmailToServer(EmailData email) {
        // Show loading indicator
        showLoading(true);

        // Set up email configuration
        EmailConfig emailConfig = new EmailConfig(email.fromAddress, "your-app-password-here");

        // Send the email
        EmailSender.sendEmail(emailConfig, email.toAddress, email.subject, email.body, new EmailSender.EmailSendListener() {
            @Override
            public void onSuccess() {
                // Email sent successfully
                runOnUiThread(() -> {
                    showLoading(false);
                    showMessage("Email sent successfully!");
                    closeComposeScreen();
                });
            }

            @Override
            public void onError(String error) {
                // There was an error sending
                runOnUiThread(() -> {
                    showLoading(false);
                    showMessage("Failed to send email: " + error);
                });
            }
        });
    }

    private void showLoading(boolean show) {
        // Show or hide loading indicator
        if (show) {
            loadingBar.setVisibility(View.VISIBLE);
        } else {
            loadingBar.setVisibility(View.GONE);
        }

        // Enable or disable buttons during loading
        boolean buttonsEnabled = !show;
        sendButton.setEnabled(buttonsEnabled);
        sendIconButton.setEnabled(buttonsEnabled);
        exitButton.setEnabled(buttonsEnabled);
        attachButton.setEnabled(buttonsEnabled);
        settingsButton.setEnabled(buttonsEnabled);
        expandToButton.setEnabled(buttonsEnabled);
    }

    private void closeComposeScreen() {
        finish();
    }

    private void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    // Helper class to store email information
    private static class EmailData {
        final String fromAddress;
        final String toAddress;
        final String ccAddress;
        final String bccAddress;
        final String subject;
        final String body;

        EmailData(String from, String to, String cc, String bcc, String subject, String body) {
            this.fromAddress = from;
            this.toAddress = to;
            this.ccAddress = cc;
            this.bccAddress = bcc;
            this.subject = subject;
            this.body = body;
        }
    }
}
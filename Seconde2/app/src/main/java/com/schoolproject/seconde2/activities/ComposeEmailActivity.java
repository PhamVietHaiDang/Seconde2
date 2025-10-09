package com.schoolproject.seconde2.activities;

import android.content.Intent;
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

import com.schoolproject.seconde2.EmailConfig;
import com.schoolproject.seconde2.ServiceFragment.EmailSender;
import com.schoolproject.seconde2.R;

public class ComposeEmailActivity extends AppCompatActivity {

    // All the input fields
    private EditText fromField, toField, ccField, bccField, subjectField, messageField;
    private Button sendButton;
    private ImageButton exitButton, attachButton, sendIconButton, settingsButton, expandToButton;
    private ProgressBar loadingBar;

    // Track if CC and BCC fields are showing
    private boolean areCcBccVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_email);

        // Set up all the views and buttons
        findViews();
        setupButtons();
        prefillFields();
    }

    private void findViews() {
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

        // Find the loading progress bar
        loadingBar = findViewById(R.id.progressBar);

        // Hide CC and BCC fields at start
        ccField.setVisibility(View.GONE);
        bccField.setVisibility(View.GONE);
    }

    private void setupButtons() {
        // Both send buttons do the same thing
        sendButton.setOnClickListener(v -> sendEmail());
        sendIconButton.setOnClickListener(v -> sendEmail());

        // Other buttons
        exitButton.setOnClickListener(v -> closeScreen());
        attachButton.setOnClickListener(v -> showToast("Attachment feature coming soon!"));
        settingsButton.setOnClickListener(v -> showSettings());
        expandToButton.setOnClickListener(v -> toggleCcBcc());
    }

    private void prefillFields() {
        // Pre-fill the from field with user's email
        fromField.setText("user@example.com");

        // Check if we got email addresses to pre-fill
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("prefilled_to")) {
            String emails = intent.getStringExtra("prefilled_to");
            toField.setText(emails);
        }
    }

    private void toggleCcBcc() {
        // Show or hide CC and BCC fields
        areCcBccVisible = !areCcBccVisible;

        if (areCcBccVisible) {
            ccField.setVisibility(View.VISIBLE);
            bccField.setVisibility(View.VISIBLE);
            expandToButton.setImageResource(R.drawable.ic_arrow_drop_up);
        } else {
            ccField.setVisibility(View.GONE);
            bccField.setVisibility(View.GONE);
            expandToButton.setImageResource(R.drawable.ic_arrow_drop_down);
        }
    }

    private void showSettings() {
        // Show the settings popup menu
        PopupMenu menu = new PopupMenu(this, settingsButton);
        menu.getMenuInflater().inflate(R.menu.compose_settings_menu, menu.getMenu());

        menu.setOnMenuItemClickListener(item -> {
            handleMenuClick(item);
            return true;
        });

        menu.show();
    }

    private void handleMenuClick(MenuItem item) {
        // Handle clicks from the settings menu
        int id = item.getItemId();

        if (id == R.id.menu_save_draft) {
            saveDraft();
        } else if (id == R.id.menu_discard) {
            discardEmail();
        } else if (id == R.id.menu_settings) {
            showToast("Settings coming soon!");
        }
    }

    private void saveDraft() {
        // Save the current email as a draft
        if (hasContent()) {
            showToast("Draft saved!");
        } else {
            showToast("No content to save");
        }
    }

    private void discardEmail() {
        // Discard the current email
        if (hasContent()) {
            showDiscardDialog();
        } else {
            closeScreen();
        }
    }

    private void showDiscardDialog() {
        // Ask user if they're sure they want to discard
        new android.app.AlertDialog.Builder(this)
                .setTitle("Discard Email")
                .setMessage("Are you sure? Your changes will be lost.")
                .setPositiveButton("Discard", (dialog, which) -> closeScreen())
                .setNegativeButton("Keep Editing", null)
                .show();
    }

    private void sendEmail() {
        // Get all the email content
        String from = fromField.getText().toString().trim();
        String to = toField.getText().toString().trim();
        String cc = ccField.getText().toString().trim();
        String bcc = bccField.getText().toString().trim();
        String subject = subjectField.getText().toString().trim();
        String body = messageField.getText().toString().trim();

        // Check if all required fields are filled
        if (from.isEmpty() || to.isEmpty() || subject.isEmpty() || body.isEmpty()) {
            showToast("Please fill all required fields");
            return;
        }

        // Check if email addresses look right
        if (!areEmailsValid(to)) {
            showToast("Please check the email addresses in 'To' field");
            return;
        }

        if (!cc.isEmpty() && !areEmailsValid(cc)) {
            showToast("Please check the email addresses in 'CC' field");
            return;
        }

        if (!bcc.isEmpty() && !areEmailsValid(bcc)) {
            showToast("Please check the email addresses in 'BCC' field");
            return;
        }

        // Show loading and send the email
        setLoading(true);
        sendEmailToServer(from, to, cc, bcc, subject, body);
    }

    private boolean areEmailsValid(String emails) {
        // Check if email addresses look correct
        if (emails.isEmpty()) return true;

        // Split by commas and check each email
        String[] emailArray = emails.split(",");
        for (String email : emailArray) {
            String trimmedEmail = email.trim();
            if (!isValidEmail(trimmedEmail)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        // Simple check if email looks like an email address
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendEmailToServer(String from, String to, String cc, String bcc, String subject, String body) {
        // Create email config and send the email
        EmailConfig config = new EmailConfig(from, "your-app-password");

        // Combine all recipients
        String allRecipients = to;
        if (!cc.isEmpty()) {
            allRecipients += "," + cc;
        }
        if (!bcc.isEmpty()) {
            allRecipients += "," + bcc;
        }

        // Count how many people we're sending to
        int recipientCount = countRecipients(to, cc, bcc);

        EmailSender.sendEmail(config, allRecipients, subject, body, new EmailSender.EmailSendListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    setLoading(false);
                    showToast("Email sent to " + recipientCount + " people!");
                    closeScreen();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showToast("Send failed: " + error);
                });
            }
        });
    }

    private int countRecipients(String to, String cc, String bcc) {
        // Count how many email addresses we have
        int count = 0;

        if (!to.isEmpty()) {
            count += to.split(",").length;
        }
        if (!cc.isEmpty()) {
            count += cc.split(",").length;
        }
        if (!bcc.isEmpty()) {
            count += bcc.split(",").length;
        }

        return count;
    }

    private boolean hasContent() {
        // Check if any field has content
        return !fromField.getText().toString().isEmpty() ||
                !toField.getText().toString().isEmpty() ||
                !ccField.getText().toString().isEmpty() ||
                !bccField.getText().toString().isEmpty() ||
                !subjectField.getText().toString().isEmpty() ||
                !messageField.getText().toString().isEmpty();
    }

    private void setLoading(boolean loading) {
        // Show or hide loading bar and enable/disable buttons
        loadingBar.setVisibility(loading ? View.VISIBLE : View.GONE);

        // Disable buttons while loading
        boolean enabled = !loading;
        sendButton.setEnabled(enabled);
        sendIconButton.setEnabled(enabled);
        exitButton.setEnabled(enabled);
        attachButton.setEnabled(enabled);
        settingsButton.setEnabled(enabled);
        expandToButton.setEnabled(enabled);
    }

    private void closeScreen() {
        // Close this screen and go back
        finish();
    }

    private void showToast(String message) {
        // Show a short toast message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
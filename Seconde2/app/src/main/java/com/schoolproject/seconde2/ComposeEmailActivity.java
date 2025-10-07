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
    // UI components - organized roughly by importance
    private EditText fromField, toField, ccField, bccField, subjectField, messageField;
    private Button sendButton;
    private ImageButton exitBtn, attachBtn, sendIconBtn, settingsBtn, expandToBtn;
    private ProgressBar loadingBar;

    // Track if CC/BCC fields are showing
    private boolean showingCcBcc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_email);

        // Set up all the views first
        setupViews();

        // Wire up the click listeners
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSendEmail();
            }
        });

        sendIconBtn.setOnClickListener(v -> handleSendEmail()); // mixed lambda and traditional styles
        exitBtn.setOnClickListener(v -> finish());

        attachBtn.setOnClickListener(v -> handleAttachment());
        settingsBtn.setOnClickListener(v -> openSettingsMenu());
        expandToBtn.setOnClickListener(v -> toggleCcBccVisibility());
    }

    private void setupViews() {
        // Find all views - doing this manually for each one
        fromField = findViewById(R.id.editFrom);
        toField = findViewById(R.id.editTo);
        ccField = findViewById(R.id.editCc);
        bccField = findViewById(R.id.editBcc);
        subjectField = findViewById(R.id.editSubject);
        messageField = findViewById(R.id.editMessage);

        sendButton = findViewById(R.id.btnSend);
        exitBtn = findViewById(R.id.btnExit);
        attachBtn = findViewById(R.id.btnAttachment);
        sendIconBtn = findViewById(R.id.btnSendIcon);
        settingsBtn = findViewById(R.id.btnSettings);
        expandToBtn = findViewById(R.id.btnExpandTo);

        loadingBar = findViewById(R.id.progressBar);
    }

    // Toggle CC and BCC field visibility - this was a pain to get right initially
    private void toggleCcBccVisibility() {
        if (showingCcBcc) {
            // Hide the extra fields
            ccField.setVisibility(View.GONE);
            bccField.setVisibility(View.GONE);
            expandToBtn.setImageResource(R.drawable.ic_arrow_drop_down);
            showingCcBcc = false;
        } else {
            // Show CC and BCC
            ccField.setVisibility(View.VISIBLE);
            bccField.setVisibility(View.VISIBLE);
            expandToBtn.setImageResource(R.drawable.ic_arrow_drop_up);
            showingCcBcc = true;
        }
    }

    private void handleAttachment() {
        // TODO: Need to implement file picker here later
        Toast.makeText(this, "Attachment feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    // Settings menu popup
    private void openSettingsMenu() {
        PopupMenu popup = new PopupMenu(this, settingsBtn);
        popup.getMenuInflater().inflate(R.menu.compose_settings_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_save_draft) {
                    saveDraft();
                    return true;
                } else if (itemId == R.id.menu_discard) {
                    discardCurrentEmail();
                    return true;
                } else if (itemId == R.id.menu_settings) {
                    openAppSettings();
                    return true;
                }
                return false;
            }
        });

        popup.show();
    }

    // Save current email as draft
    private void saveDraft() {
        String fromText = fromField.getText().toString().trim();
        String toText = toField.getText().toString().trim();
        String ccText = ccField.getText().toString().trim();
        String bccText = bccField.getText().toString().trim();
        String subjectText = subjectField.getText().toString().trim();
        String messageText = messageField.getText().toString().trim();

        // Check if there's actually any content to save
        boolean hasContent = !fromText.isEmpty() || !toText.isEmpty() ||
                !ccText.isEmpty() || !bccText.isEmpty() ||
                !subjectText.isEmpty() || !messageText.isEmpty();

        if (!hasContent) {
            Toast.makeText(this, "No content to save as draft", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Actually implement draft saving to database or something
        Toast.makeText(this, "Email saved as draft", Toast.LENGTH_SHORT).show();
    }

    private void discardCurrentEmail() {
        String fromText = fromField.getText().toString().trim();
        String toText = toField.getText().toString().trim();
        String ccText = ccField.getText().toString().trim();
        String bccText = bccField.getText().toString().trim();
        String subjectText = subjectField.getText().toString().trim();
        String messageText = messageField.getText().toString().trim();

        // Check if user has typed anything before showing confirmation
        boolean hasContent = !fromText.isEmpty() || !toText.isEmpty() ||
                !ccText.isEmpty() || !bccText.isEmpty() ||
                !subjectText.isEmpty() || !messageText.isEmpty();

        if (hasContent) {
            // Ask user to confirm before discarding
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Discard Email")
                    .setMessage("Are you sure you want to discard this email?")
                    .setPositiveButton("Discard", (dialog, which) -> finish())
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            // Nothing to discard, just close
            finish();
        }
    }

    private void openAppSettings() {
        // TODO: Create settings activity eventually
        Toast.makeText(this, "App Settings coming soon!", Toast.LENGTH_SHORT).show();
    }

    // Main send email function
    private void handleSendEmail() {
        String fromAddress = fromField.getText().toString().trim();
        String toAddress = toField.getText().toString().trim();
        String ccAddresses = ccField.getText().toString().trim();
        String bccAddresses = bccField.getText().toString().trim();
        String emailSubject = subjectField.getText().toString().trim();
        String emailBody = messageField.getText().toString().trim();

        // Basic validation - require the essential fields
        if (fromAddress.isEmpty() || toAddress.isEmpty() ||
                emailSubject.isEmpty() || emailBody.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Start sending process
        showProgress(true);

        // Initialize email config with sender credentials
        EmailConfig config = new EmailConfig(fromAddress, "your-app-password");

        // Send the email - Note: CC/BCC handling needs to be implemented in EmailSender
        EmailSender.sendEmail(config, toAddress, emailSubject, emailBody, new EmailSender.EmailSendListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                        Toast.makeText(ComposeEmailActivity.this, "Email sent successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity after sending
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                        Toast.makeText(ComposeEmailActivity.this,
                                "Failed to send email: " + errorMessage,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    // Show/hide loading progress and disable/enable buttons accordingly
    private void showProgress(boolean isLoading) {
        if (isLoading) {
            loadingBar.setVisibility(View.VISIBLE);
        } else {
            loadingBar.setVisibility(View.GONE);
        }

        // Disable all interactive elements while sending
        sendButton.setEnabled(!isLoading);
        sendIconBtn.setEnabled(!isLoading);
        exitBtn.setEnabled(!isLoading);
        attachBtn.setEnabled(!isLoading);
        settingsBtn.setEnabled(!isLoading);
        expandToBtn.setEnabled(!isLoading);
    }
}
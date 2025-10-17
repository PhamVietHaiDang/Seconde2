package com.schoolproject.seconde2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.schoolproject.seconde2.R;
import com.schoolproject.seconde2.viewmodel.EmailViewModel;

public class ComposeEmailActivity extends AppCompatActivity {

    private EditText fromField, toField, ccField, bccField, subjectField, messageField;
    private Button sendButton;
    private ImageButton exitButton, attachButton, sendIconButton, settingsButton, expandToButton;
    private ProgressBar loadingBar;

    private boolean areCcBccVisible = false;
    private EmailViewModel emailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_email);

        emailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);

        findViews();
        setupButtons();
        prefillFields();
    }

    private void findViews() {
        fromField = findViewById(R.id.editFrom);
        toField = findViewById(R.id.editTo);
        ccField = findViewById(R.id.editCc);
        bccField = findViewById(R.id.editBcc);
        subjectField = findViewById(R.id.editSubject);
        messageField = findViewById(R.id.editMessage);

        sendButton = findViewById(R.id.btnSend);
        exitButton = findViewById(R.id.btnExit);
        attachButton = findViewById(R.id.btnAttachment);
        sendIconButton = findViewById(R.id.btnSendIcon);
        settingsButton = findViewById(R.id.btnSettings);
        expandToButton = findViewById(R.id.btnExpandTo);
        loadingBar = findViewById(R.id.progressBar);

        ccField.setVisibility(View.GONE);
        bccField.setVisibility(View.GONE);
    }

    private void setupButtons() {
        sendButton.setOnClickListener(v -> sendEmail());
        sendIconButton.setOnClickListener(v -> sendEmail());
        exitButton.setOnClickListener(v -> finish());
        attachButton.setOnClickListener(v -> showToast("Attachment feature coming soon!"));
        settingsButton.setOnClickListener(v -> showSettings());
        expandToButton.setOnClickListener(v -> toggleCcBcc());
    }

    private void prefillFields() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", "");
        fromField.setText(userEmail.isEmpty() ? "you@example.com" : userEmail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("prefilled_to")) {
            toField.setText(intent.getStringExtra("prefilled_to"));
        }
    }

    private void toggleCcBcc() {
        areCcBccVisible = !areCcBccVisible;

        int visibility = areCcBccVisible ? View.VISIBLE : View.GONE;
        int icon = areCcBccVisible ? R.drawable.ic_arrow_drop_up : R.drawable.ic_arrow_drop_down;

        ccField.setVisibility(visibility);
        bccField.setVisibility(visibility);
        expandToButton.setImageResource(icon);
    }

    private void showSettings() {
        PopupMenu menu = new PopupMenu(this, settingsButton);
        menu.getMenuInflater().inflate(R.menu.compose_settings_menu, menu.getMenu());

        menu.setOnMenuItemClickListener(item -> {
            handleMenuClick(item);
            return true;
        });

        menu.show();
    }

    private void handleMenuClick(MenuItem item) {
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
        showToast(hasContent() ? "Draft saved!" : "No content to save");
    }

    private void discardEmail() {
        if (hasContent()) {
            showDiscardDialog();
        } else {
            finish();
        }
    }

    private void showDiscardDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Discard Email")
                .setMessage("Are you sure? Your changes will be lost.")
                .setPositiveButton("Discard", (dialog, which) -> finish())
                .setNegativeButton("Keep Editing", null)
                .show();
    }

    private void sendEmail() {
        String from = fromField.getText().toString().trim();
        String to = toField.getText().toString().trim();
        String cc = ccField.getText().toString().trim();
        String bcc = bccField.getText().toString().trim();
        String subject = subjectField.getText().toString().trim();
        String body = messageField.getText().toString().trim();

        // Validate required fields
        if (from.isEmpty() || to.isEmpty() || subject.isEmpty() || body.isEmpty()) {
            showToast("Please fill all required fields");
            return;
        }

        // Validate email formats
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

        // Check user authentication
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", "");
        String userPassword = prefs.getString("user_password", "");

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            showToast("Please sign in first");
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        setLoading(true);
        sendEmailToServer(from, to, cc, bcc, subject, body);
    }

    private boolean areEmailsValid(String emails) {
        if (emails.isEmpty()) return true;

        String[] emailArray = emails.split(",");
        for (String email : emailArray) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                return false;
            }
        }
        return true;
    }

    private void sendEmailToServer(String from, String to, String cc, String bcc, String subject, String body) {
        // Build recipient list
        StringBuilder allRecipients = new StringBuilder(to);
        if (!cc.isEmpty()) allRecipients.append(",").append(cc);
        if (!bcc.isEmpty()) allRecipients.append(",").append(bcc);

        emailViewModel.sendEmail(allRecipients.toString(), subject, body);

        // Simulate email sending
        new Handler().postDelayed(() -> {
            setLoading(false);

            // Count recipients for success message
            int count = 0;
            if (!to.isEmpty()) count += to.split(",").length;
            if (!cc.isEmpty()) count += cc.split(",").length;
            if (!bcc.isEmpty()) count += bcc.split(",").length;

            showToast("Email sent to " + count + " people!");
            finish();
        }, 2000);
    }

    private boolean hasContent() {
        return !fromField.getText().toString().isEmpty() ||
                !toField.getText().toString().isEmpty() ||
                !ccField.getText().toString().isEmpty() ||
                !bccField.getText().toString().isEmpty() ||
                !subjectField.getText().toString().isEmpty() ||
                !messageField.getText().toString().isEmpty();
    }

    private void setLoading(boolean loading) {
        loadingBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        boolean enabled = !loading;

        sendButton.setEnabled(enabled);
        sendIconButton.setEnabled(enabled);
        exitButton.setEnabled(enabled);
        attachButton.setEnabled(enabled);
        settingsButton.setEnabled(enabled);
        expandToButton.setEnabled(enabled);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
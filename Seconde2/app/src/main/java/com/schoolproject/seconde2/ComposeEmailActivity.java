package com.schoolproject.seconde2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ComposeEmailActivity extends AppCompatActivity {
    private EditText editTo, editSubject, editMessage;
    private Button btnSend;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_email);

        initViews();

        btnSend.setOnClickListener(v -> sendEmail());
    }

    private void initViews() {
        editTo = findViewById(R.id.editTo);
        editSubject = findViewById(R.id.editSubject);
        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);
        progressBar = findViewById(R.id.progressBar);
    }

    private void sendEmail() {
        String to = editTo.getText().toString().trim();
        String subject = editSubject.getText().toString().trim();
        String message = editMessage.getText().toString().trim();

        if (to.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        setInProgress(true);

        // Use the EmailSender class
        EmailConfig emailConfig = new EmailConfig("your-email@gmail.com", "your-app-password");

        EmailSender.sendEmail(emailConfig, to, subject, message, new EmailSender.EmailSendListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    setInProgress(false);
                    Toast.makeText(ComposeEmailActivity.this, "Email sent successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    setInProgress(false);
                    Toast.makeText(ComposeEmailActivity.this, "Failed to send email: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void setInProgress(boolean inProgress) {
        progressBar.setVisibility(inProgress ? View.VISIBLE : View.GONE);
        btnSend.setEnabled(!inProgress);
    }
}
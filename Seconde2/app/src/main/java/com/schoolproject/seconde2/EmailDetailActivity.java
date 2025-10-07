package com.schoolproject.seconde2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class EmailDetailActivity extends AppCompatActivity {

    // Text views for displaying email information
    private TextView senderTextView, subjectTextView, dateTextView, recipientTextView, bodyTextView;

    // Buttons for navigation and actions
    private ImageButton backButton;
    private LinearLayout replyButton, forwardButton;

    // Variables to store email data
    private String emailSender, emailSubject, emailDate, emailRecipient, emailBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);

        // Set up the activity
        initializeViews();
        setupButtonListeners();
        loadEmailDataFromIntent();
    }

    // Find all the views in the layout
    private void initializeViews() {
        // Text views for email details
        senderTextView = findViewById(R.id.txtSender);
        subjectTextView = findViewById(R.id.txtSubject);
        dateTextView = findViewById(R.id.txtDate);
        recipientTextView = findViewById(R.id.txtTo);
        bodyTextView = findViewById(R.id.txtBody);

        // Action buttons
        backButton = findViewById(R.id.btnBack);
        replyButton = findViewById(R.id.btnReply);
        forwardButton = findViewById(R.id.btnForward);
    }

    // Set up click listeners for all buttons
    private void setupButtonListeners() {
        // Back button - returns to previous screen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToEmailList();
            }
        });

        // Reply button - opens reply fragment
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReplyFragment();
            }
        });

        // Forward button - opens forward fragment
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForwardFragment();
            }
        });

    }

    // Get email data from the intent that started this activity
    private void loadEmailDataFromIntent() {
        Intent incomingIntent = getIntent();

        if (incomingIntent != null) {
            // Extract all the email data from intent extras
            emailSender = incomingIntent.getStringExtra("sender");
            emailSubject = incomingIntent.getStringExtra("subject");
            emailDate = incomingIntent.getStringExtra("date");
            emailRecipient = incomingIntent.getStringExtra("to");
            emailBody = incomingIntent.getStringExtra("body");

            // Display the data in the text views
            displayEmailData();
        }
    }

    // Display the email data in the text views
    private void displayEmailData() {
        // Set sender name
        if (emailSender != null) {
            senderTextView.setText(emailSender);
        } else {
            senderTextView.setText("Unknown Sender");
        }

        // Set email subject
        if (emailSubject != null) {
            subjectTextView.setText(emailSubject);
        } else {
            subjectTextView.setText("No Subject");
        }

        // Set date
        if (emailDate != null) {
            dateTextView.setText(emailDate);
        } else {
            dateTextView.setText("Date not available");
        }

        // Set recipient (if available)
        if (emailRecipient != null) {
            recipientTextView.setText(emailRecipient);
        } else {
            recipientTextView.setText("user@example.com"); // Default recipient
        }

        // Set email body
        if (emailBody != null) {
            bodyTextView.setText(emailBody);
        } else {
            bodyTextView.setText("No content available");
        }
    }

    // Open the reply fragment with current email data
    private void openReplyFragment() {
        Fragment replyFragment = ReplyFragment.newInstance(emailSender, emailSubject, emailDate, emailBody);
        displayFragment(replyFragment);
    }

    // Open the forward fragment with current email data
    private void openForwardFragment() {
        Fragment forwardFragment = ForwardFragment.newInstance(emailSender, emailSubject, emailDate, emailBody);
        displayFragment(forwardFragment);
    }

    // Helper method to display any fragment
    private void displayFragment(Fragment fragment) {
        // Get the fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Start a fragment transaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace whatever is in the fragment container with new fragment
        transaction.replace(android.R.id.content, fragment);

        // Add to back stack so user can go back
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    // Close this activity and return to email list
    private void goBackToEmailList() {
        finish();
    }

}
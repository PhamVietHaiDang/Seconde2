package com.schoolproject.seconde2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ContractDetailActivity extends AppCompatActivity {

    // UI elements
    private ImageButton backButton;
    private ImageView contactAvatar;
    private TextView phoneNumberText, emailAddressText, contactNameText, companyNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);

        setupViews();
        loadContactInformation();
        setupEventListeners();
    }

    private void setupViews() {
        // Find all the views we need
        backButton = findViewById(R.id.btnBack);
        contactAvatar = findViewById(R.id.imgAvatar);
        phoneNumberText = findViewById(R.id.txtPhone);
        emailAddressText = findViewById(R.id.txtEmail);

    }

    private void loadContactInformation() {
        // Set up sample contact data for demonstration
        setupSampleContactData();

        // Check if we received any data from the previous screen
        checkForIntentData();
    }

    private void setupSampleContactData() {
        // For now, we're using sample data
        // In a real app, this would come from a database or API

        phoneNumberText.setText("+1 (555) 123-4567");
        emailAddressText.setText("contact@company.com");

        // Set sample name and company if those fields exist
        if (contactNameText != null) {
            contactNameText.setText("John Smith");
        }

        if (companyNameText != null) {
            companyNameText.setText("Tech Solutions Inc.");
        }

        // You could also set a sample avatar here
        // contactAvatar.setImageResource(R.drawable.default_avatar);
    }

    private void checkForIntentData() {
        // Check if we got any data from the previous screen
        /*
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String companyName = extras.getString("company_name");
            String contactName = extras.getString("contact_name");
            String phone = extras.getString("phone_number");
            String email = extras.getString("email_address");

            // Use the real data if available
            if (companyName != null) {
                companyNameText.setText(companyName);
            }
            if (contactName != null) {
                contactNameText.setText(contactName);
            }
            if (phone != null) {
                phoneNumberText.setText(phone);
            }
            if (email != null) {
                emailAddressText.setText(email);
            }
        }
        */
    }

    private void setupEventListeners() {
        // Back button to return to previous screen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToPreviousScreen();
            }
        });

        // Phone number - make it clickable to call
        phoneNumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePhoneNumberClick();
            }
        });

        // Email address - make it clickable to send email
        emailAddressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleEmailAddressClick();
            }
        });

        // Avatar image - could be made clickable for larger view
        contactAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("View contact photo");
                // TODO: Open full screen avatar view later
            }
        });
    }

    private void goBackToPreviousScreen() {
        // Close this activity and go back to the previous one
        finish();
    }

    private void handlePhoneNumberClick() {
        String phoneNumber = phoneNumberText.getText().toString();
        showToast("Calling: " + phoneNumber);

        // TODO: Implement actual phone call functionality later
        /*
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
        */
    }

    private void handleEmailAddressClick() {
        String emailAddress = emailAddressText.getText().toString();
        showToast("Sending email to: " + emailAddress);

        // TODO: Implement actual email functionality later
        /*
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + emailAddress));
        startActivity(emailIntent);
        */
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

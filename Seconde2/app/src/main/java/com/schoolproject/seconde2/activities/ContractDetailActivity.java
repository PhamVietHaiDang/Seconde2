package com.schoolproject.seconde2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.schoolproject.seconde2.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ContractDetailActivity extends AppCompatActivity {

    // IDs for our menu items
    private static final int MENU_ADD_DETAILS = 1;
    private static final int MENU_FAVORITE = 2;
    private static final int MENU_COPY_DETAILS = 3;
    private static final int MENU_COMPOSE_EMAIL = 4;

    private ImageButton backButton, menuButton;
    private TextView phoneNumberText, emailAddressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);

        // Set up all our views and data
        setupViews();
        loadContactData();
        setupClickListeners();
    }

    private void setupViews() {
        // Find all the buttons and text views
        backButton = findViewById(R.id.btnBack);
        menuButton = findViewById(R.id.btnContractMenu);
        phoneNumberText = findViewById(R.id.txtPhone);
        emailAddressText = findViewById(R.id.txtEmail);
    }

    private void loadContactData() {
        // Set some sample data for testing
        setupSampleData();

        // Check if we got real data from previous screen
        checkForPassedData();
    }

    private void setupSampleData() {
        phoneNumberText.setText("+1 (555) 123-4567");
        emailAddressText.setText("contact@company.com");
    }

    private void checkForPassedData() {
        // See if the previous screen sent us any contact data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String phone = extras.getString("phone_number");
            String email = extras.getString("email_address");

            // Use the real data if we have it
            if (phone != null) {
                phoneNumberText.setText(phone);
            }
            if (email != null) {
                emailAddressText.setText(email);
            }
        }
    }

    private void setupClickListeners() {
        // Back button goes to previous screen
        backButton.setOnClickListener(view -> finish());

        // Click phone number to call
        phoneNumberText.setOnClickListener(view -> handlePhoneClick());

        // Click email to send email
        emailAddressText.setOnClickListener(view -> handleEmailClick());

        // Menu button shows options
        menuButton.setOnClickListener(view -> showContactMenu(view));
    }

    private void showContactMenu(View view) {
        // Create the popup menu
        PopupMenu popupMenu = new PopupMenu(this, view);
        Menu menu = popupMenu.getMenu();

        // Add all the menu options with icons
        menu.add(0, MENU_ADD_DETAILS, 0, "  Add details").setIcon(R.drawable.ic_add);
        menu.add(0, MENU_FAVORITE, 1, "  Mark as favorite").setIcon(R.drawable.ic_favorite);
        menu.add(0, MENU_COPY_DETAILS, 2, "  Copy details").setIcon(R.drawable.ic_copy);
        menu.add(0, MENU_COMPOSE_EMAIL, 3, "  Compose email").setIcon(R.drawable.ic_compose);

        // Make sure icons show up
        forceMenuIconsToShow(popupMenu);

        // Handle menu item clicks
        setupMenuClickListeners(popupMenu);

        popupMenu.show();
    }

    private void forceMenuIconsToShow(PopupMenu popupMenu) {
        // This is a hack to make sure menu icons show up
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object menuPopupHelper = field.get(popupMenu);
            Class<?> classPopupHelper = menuPopupHelper.getClass();
            Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
            setForceShowIcon.invoke(menuPopupHelper, true);
        } catch (Exception e) {
            // If it fails, the menu will still work but might not show icons
        }
    }

    private void setupMenuClickListeners(PopupMenu popupMenu) {
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == MENU_ADD_DETAILS) {
                addContactDetails();
            } else if (itemId == MENU_FAVORITE) {
                markAsFavorite();
            } else if (itemId == MENU_COPY_DETAILS) {
                copyContactDetails();
            } else if (itemId == MENU_COMPOSE_EMAIL) {
                composeEmail();
            }
            return true;
        });
    }

    private void addContactDetails() {
        Toast.makeText(this, "Add contact details", Toast.LENGTH_SHORT).show();
        // TODO: Add logic for adding more contact info
    }

    private void markAsFavorite() {
        Toast.makeText(this, "Contact marked as favorite", Toast.LENGTH_SHORT).show();
        // TODO: Add favorite functionality
    }

    private void copyContactDetails() {
        // Get the contact info as text
        String contactInfo = "Phone: " + phoneNumberText.getText() + "\nEmail: " + emailAddressText.getText();

        // Copy to clipboard
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Contact details", contactInfo);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Contact details copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private void composeEmail() {
        String email = emailAddressText.getText().toString();

        // Open the compose email screen
        Intent composeIntent = new Intent(this, ComposeEmailActivity.class);

        // Pre-fill the email address
        composeIntent.putExtra("prefilled_to", email);

        startActivity(composeIntent);
    }

    private void handlePhoneClick() {
        String phoneNumber = phoneNumberText.getText().toString();
        Toast.makeText(this, "Calling: " + phoneNumber, Toast.LENGTH_SHORT).show();
        // TODO: Add actual phone call functionality
    }

    private void handleEmailClick() {
        String emailAddress = emailAddressText.getText().toString();
        Toast.makeText(this, "Sending email to: " + emailAddress, Toast.LENGTH_SHORT).show();
        // TODO: Add actual email functionality
    }
}
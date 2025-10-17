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

    // Menu item IDs
    private static final int MENU_ADD_DETAILS = 1;
    private static final int MENU_FAVORITE = 2;
    private static final int MENU_COPY_DETAILS = 3;
    private static final int MENU_COMPOSE_EMAIL = 4;

    // Intent extra keys
    private static final String EXTRA_PHONE_NUMBER = "phone_number";
    private static final String EXTRA_EMAIL_ADDRESS = "email_address";
    private static final String EXTRA_PREFILLED_TO = "prefilled_to";

    // UI components
    private ImageButton backButton, menuButton;
    private TextView phoneNumberText, emailAddressText;

    // Contact data
    private String contactPhone, contactEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);

        setupViews();
        loadContactData();
        setupClickListeners();
    }

    private void setupViews() {
        backButton = findViewById(R.id.btnBack);
        menuButton = findViewById(R.id.btnContractMenu);
        phoneNumberText = findViewById(R.id.txtPhone);
        emailAddressText = findViewById(R.id.txtEmail);
    }

    private void loadContactData() {
        // Try to get real data first
        if (!loadDataFromIntent()) {
            // Fall back to sample data
            setupSampleData();
        }
    }

    private boolean loadDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            contactPhone = extras.getString(EXTRA_PHONE_NUMBER);
            contactEmail = extras.getString(EXTRA_EMAIL_ADDRESS);

            // Update UI with real data
            if (contactPhone != null) {
                phoneNumberText.setText(contactPhone);
            }
            if (contactEmail != null) {
                emailAddressText.setText(contactEmail);
            }

            return contactPhone != null || contactEmail != null;
        }
        return false;
    }

    private void setupSampleData() {
        contactPhone = "+1 (555) 123-4567";
        contactEmail = "contact@company.com";

        phoneNumberText.setText(contactPhone);
        emailAddressText.setText(contactEmail);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(view -> finish());
        phoneNumberText.setOnClickListener(view -> handlePhoneClick());
        emailAddressText.setOnClickListener(view -> handleEmailClick());
        menuButton.setOnClickListener(view -> showContactMenu(view));
    }

    private void showContactMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        Menu menu = popupMenu.getMenu();

        // Add menu items
        menu.add(0, MENU_ADD_DETAILS, 0, "Add details").setIcon(R.drawable.ic_add);
        menu.add(0, MENU_FAVORITE, 1, "Mark as favorite").setIcon(R.drawable.ic_favorite);
        menu.add(0, MENU_COPY_DETAILS, 2, "Copy details").setIcon(R.drawable.ic_copy);
        menu.add(0, MENU_COMPOSE_EMAIL, 3, "Compose email").setIcon(R.drawable.ic_compose);

        forceMenuIconsToShow(popupMenu);
        setupMenuClickListeners(popupMenu);

        popupMenu.show();
    }

    private void forceMenuIconsToShow(PopupMenu popupMenu) {
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object menuPopupHelper = field.get(popupMenu);
            Class<?> classPopupHelper = menuPopupHelper.getClass();
            Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
            setForceShowIcon.invoke(menuPopupHelper, true);
        } catch (Exception e) {
            // Menu will work without icons if reflection fails
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
        // TODO: Implement add details logic
    }

    private void markAsFavorite() {
        Toast.makeText(this, "Contact marked as favorite", Toast.LENGTH_SHORT).show();
        // TODO: Implement favorite functionality
    }

    private void copyContactDetails() {
        String contactInfo = "Phone: " + phoneNumberText.getText() + "\nEmail: " + emailAddressText.getText();

        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Contact details", contactInfo);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Contact details copied", Toast.LENGTH_SHORT).show();
    }

    private void composeEmail() {
        String email = emailAddressText.getText().toString();

        Intent composeIntent = new Intent(this, ComposeEmailActivity.class);
        composeIntent.putExtra(EXTRA_PREFILLED_TO, email);
        startActivity(composeIntent);
    }

    private void handlePhoneClick() {
        String phoneNumber = phoneNumberText.getText().toString();
        Toast.makeText(this, "Calling: " + phoneNumber, Toast.LENGTH_SHORT).show();
        // TODO: Implement phone call functionality
    }

    private void handleEmailClick() {
        String emailAddress = emailAddressText.getText().toString();
        Toast.makeText(this, "Sending email to: " + emailAddress, Toast.LENGTH_SHORT).show();
        // TODO: Implement email functionality
    }

    // Helper methods for backend integration
    public String getContactPhone() {
        return contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactData(String phone, String email) {
        this.contactPhone = phone;
        this.contactEmail = email;
        updateContactDisplay();
    }

    private void updateContactDisplay() {
        if (contactPhone != null) {
            phoneNumberText.setText(contactPhone);
        }
        if (contactEmail != null) {
            emailAddressText.setText(contactEmail);
        }
    }
}
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

    private static final int MENU_ADD_DETAILS = 1;
    private static final int MENU_FAVORITE = 2;
    private static final int MENU_COPY_DETAILS = 3;
    private static final int MENU_COMPOSE_EMAIL = 4;

    private static final String EXTRA_PHONE_NUMBER = "phone_number";
    private static final String EXTRA_EMAIL_ADDRESS = "email_address";
    private static final String EXTRA_PREFILLED_TO = "prefilled_to";

    private ImageButton backButton, menuButton;
    private TextView phoneNumberText, emailAddressText;

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
        if (!loadDataFromIntent()) {
            setupSampleData();
        }
    }

    private boolean loadDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) return false;

        contactPhone = extras.getString(EXTRA_PHONE_NUMBER);
        contactEmail = extras.getString(EXTRA_EMAIL_ADDRESS);

        if (contactPhone != null) phoneNumberText.setText(contactPhone);
        if (contactEmail != null) emailAddressText.setText(contactEmail);

        return contactPhone != null || contactEmail != null;
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
            // Menu icons may not show if reflection fails
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
        showToast("Add contact details");
    }

    private void markAsFavorite() {
        showToast("Contact marked as favorite");
    }

    private void copyContactDetails() {
        String contactInfo = "Phone: " + phoneNumberText.getText() + "\nEmail: " + emailAddressText.getText();

        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Contact details", contactInfo);
        clipboard.setPrimaryClip(clip);

        showToast("Contact details copied");
    }

    private void composeEmail() {
        String email = emailAddressText.getText().toString();

        Intent composeIntent = new Intent(this, ComposeEmailActivity.class);
        composeIntent.putExtra(EXTRA_PREFILLED_TO, email);
        startActivity(composeIntent);
    }

    private void handlePhoneClick() {
        showToast("Calling: " + phoneNumberText.getText());
    }

    private void handleEmailClick() {
        showToast("Sending email to: " + emailAddressText.getText());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

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
        if (contactPhone != null) phoneNumberText.setText(contactPhone);
        if (contactEmail != null) emailAddressText.setText(contactEmail);
    }
}
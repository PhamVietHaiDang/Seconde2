package com.schoolproject.seconde2.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.schoolproject.seconde2.ComposeFragments.ForwardFragment;
import com.schoolproject.seconde2.R;
import com.schoolproject.seconde2.ComposeFragments.ReplyFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EmailDetailActivity extends AppCompatActivity {

    // Default values for missing email data
    private static final String DEFAULT_SENDER = "Unknown Sender";
    private static final String DEFAULT_SUBJECT = "No Subject";
    private static final String DEFAULT_DATE = "Date not available";
    private static final String DEFAULT_RECIPIENT = "user@example.com";
    private static final String DEFAULT_BODY = "No content available";

    // Content menu item IDs
    private static final int MENU_CONTENT_REPLY = 1;
    private static final int MENU_CONTENT_FORWARD = 2;
    private static final int MENU_CONTENT_ARCHIVE = 3;
    private static final int MENU_CONTENT_SPAM = 4;

    // Intent extra keys
    private static final String EXTRA_SENDER = "sender";
    private static final String EXTRA_SUBJECT = "subject";
    private static final String EXTRA_DATE = "date";
    private static final String EXTRA_TO = "to";
    private static final String EXTRA_BODY = "body";

    // UI components
    private TextView senderTextView, subjectTextView, dateTextView, recipientTextView, bodyTextView;
    private ImageButton backButton, replyButton, shareButton, menuButton, contentMenuButton;

    // Email data
    private String emailSender, emailSubject, emailDate, emailRecipient, emailBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);

        setupViews();
        setupClickListeners();
        loadEmailData();
    }

    private void setupViews() {
        // Text views
        senderTextView = findViewById(R.id.txtSender);
        subjectTextView = findViewById(R.id.txtSubject);
        dateTextView = findViewById(R.id.txtDate);
        recipientTextView = findViewById(R.id.txtTo);
        bodyTextView = findViewById(R.id.txtBody);

        // Buttons
        backButton = findViewById(R.id.btnBack);
        replyButton = findViewById(R.id.btnToolbarReply);
        shareButton = findViewById(R.id.btnShare);
        menuButton = findViewById(R.id.btnMenu);
        contentMenuButton = findViewById(R.id.btnContentMenu);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        replyButton.setOnClickListener(v -> openReplyScreen());
        shareButton.setOnClickListener(v -> showToast("Share email"));
        menuButton.setOnClickListener(v -> showOptionsMenu(v));
        contentMenuButton.setOnClickListener(v -> showContentOptionsMenu(v));
    }

    private void showOptionsMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.email_detail_menu, popupMenu.getMenu());

        customizeMenuItems(popupMenu.getMenu());
        forceMenuIconsToShow(popupMenu);
        setupMenuClickListeners(popupMenu);

        popupMenu.show();
    }

    private void showContentOptionsMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        Menu menu = popupMenu.getMenu();

        // Add menu items
        menu.add(0, MENU_CONTENT_REPLY, 0, "  Reply").setIcon(R.drawable.ic_reply);
        menu.add(0, MENU_CONTENT_FORWARD, 1, "  Forward").setIcon(R.drawable.ic_forward);
        menu.add(0, MENU_CONTENT_ARCHIVE, 2, "  Archive").setIcon(R.drawable.ic_archive);

        // Make spam item red
        MenuItem spamItem = menu.add(0, MENU_CONTENT_SPAM, 3, "  Report spam");
        spamItem.setIcon(R.drawable.ic_warning);
        SpannableString redTitle = new SpannableString("  Report spam");
        redTitle.setSpan(new ForegroundColorSpan(Color.RED), 0, redTitle.length(), 0);
        spamItem.setTitle(redTitle);

        forceMenuIconsToShow(popupMenu);
        setupContentMenuClickListeners(popupMenu);

        popupMenu.show();
    }

    private void customizeMenuItems(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            String spacedTitle = "  " + item.getTitle();

            if (i == 3) {
                SpannableString redTitle = new SpannableString(spacedTitle);
                redTitle.setSpan(new ForegroundColorSpan(Color.RED), 0, redTitle.length(), 0);
                item.setTitle(redTitle);
            } else {
                item.setTitle(spacedTitle);
            }
        }
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
            // Menu works without icons if this fails
        }
    }

    private void setupMenuClickListeners(PopupMenu popupMenu) {
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_archive) {
                archiveEmail();
            } else if (itemId == R.id.menu_trash) {
                moveToTrash();
            } else if (itemId == R.id.menu_unread) {
                markAsUnread();
            } else if (itemId == R.id.menu_spam) {
                reportAsSpam();
            }
            return true;
        });
    }

    private void setupContentMenuClickListeners(PopupMenu popupMenu) {
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == MENU_CONTENT_REPLY) {
                openReplyScreen();
            } else if (itemId == MENU_CONTENT_FORWARD) {
                openForwardScreen();
            } else if (itemId == MENU_CONTENT_ARCHIVE) {
                archiveEmail();
            } else if (itemId == MENU_CONTENT_SPAM) {
                reportAsSpam();
            }
            return true;
        });
    }

    private void loadEmailData() {
        Intent intent = getIntent();
        if (intent == null) return;

        // Store email data for backend access
        emailSender = getStringExtra(intent, EXTRA_SENDER, DEFAULT_SENDER);
        emailSubject = getStringExtra(intent, EXTRA_SUBJECT, DEFAULT_SUBJECT);
        emailDate = getStringExtra(intent, EXTRA_DATE, DEFAULT_DATE);
        emailRecipient = getStringExtra(intent, EXTRA_TO, DEFAULT_RECIPIENT);
        emailBody = getStringExtra(intent, EXTRA_BODY, DEFAULT_BODY);

        // Update UI
        updateEmailDisplay();
    }

    private void updateEmailDisplay() {
        senderTextView.setText(emailSender);
        subjectTextView.setText(emailSubject);
        dateTextView.setText(emailDate);
        recipientTextView.setText(emailRecipient);
        bodyTextView.setText(emailBody);
    }

    private String getStringExtra(Intent intent, String key, String defaultValue) {
        String value = intent.getStringExtra(key);
        return value != null ? value : defaultValue;
    }

    private void openReplyScreen() {
        Fragment replyFragment = ReplyFragment.newInstance(
                emailSender,
                emailSubject,
                emailDate,
                emailBody
        );
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, replyFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openForwardScreen() {
        Fragment forwardFragment = ForwardFragment.newInstance(
                emailSender,
                emailSubject,
                emailDate,
                emailBody
        );
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, forwardFragment)
                .addToBackStack(null)
                .commit();
    }

    // Backend integration methods
    private void archiveEmail() {
        showToast("Email archived");
        // TODO: Call backend API to archive email
    }

    private void moveToTrash() {
        showToast("Email moved to trash");
        // TODO: Call backend API to move to trash
    }

    private void markAsUnread() {
        showToast("Email marked as unread");
        // TODO: Call backend API to mark as unread
    }

    private void reportAsSpam() {
        showToast("Email reported as spam");
        // TODO: Call backend API to report spam
    }

    // Getters for backend data access
    public String getEmailSender() {
        return emailSender;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public String getEmailDate() {
        return emailDate;
    }

    public String getEmailRecipient() {
        return emailRecipient;
    }

    public String getEmailBody() {
        return emailBody;
    }

    // Method to update email data from backend
    public void setEmailData(String sender, String subject, String date, String recipient, String body) {
        this.emailSender = sender;
        this.emailSubject = subject;
        this.emailDate = date;
        this.emailRecipient = recipient;
        this.emailBody = body;
        updateEmailDisplay();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
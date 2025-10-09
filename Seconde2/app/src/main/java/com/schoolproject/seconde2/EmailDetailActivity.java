package com.schoolproject.seconde2;

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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EmailDetailActivity extends AppCompatActivity {

    // Default values in case we don't get any email data
    private static final String DEFAULT_SENDER = "Unknown Sender";
    private static final String DEFAULT_SUBJECT = "No Subject";
    private static final String DEFAULT_DATE = "Date not available";
    private static final String DEFAULT_RECIPIENT = "user@example.com";
    private static final String DEFAULT_BODY = "No content available";

    // IDs for the content menu items
    private static final int MENU_CONTENT_REPLY = 1;
    private static final int MENU_CONTENT_FORWARD = 2;
    private static final int MENU_CONTENT_ARCHIVE = 3;
    private static final int MENU_CONTENT_SPAM = 4;

    private TextView senderTextView, subjectTextView, dateTextView, recipientTextView, bodyTextView;
    private ImageButton backButton, replyButton, shareButton, menuButton, contentMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);

        // Set up all the views and load the email data
        setupViews();
        setupClickListeners();
        loadEmailData();
    }

    private void setupViews() {
        // Find all the text views for displaying email info
        senderTextView = findViewById(R.id.txtSender);
        subjectTextView = findViewById(R.id.txtSubject);
        dateTextView = findViewById(R.id.txtDate);
        recipientTextView = findViewById(R.id.txtTo);
        bodyTextView = findViewById(R.id.txtBody);

        // Find all the buttons
        backButton = findViewById(R.id.btnBack);
        replyButton = findViewById(R.id.btnToolbarReply);
        shareButton = findViewById(R.id.btnShare);
        menuButton = findViewById(R.id.btnMenu);
        contentMenuButton = findViewById(R.id.btnContentMenu);
    }

    private void setupClickListeners() {
        // Back button closes this screen
        backButton.setOnClickListener(v -> finish());

        // Reply button opens reply screen
        replyButton.setOnClickListener(v -> openReplyScreen());

        // Share button (not implemented yet)
        shareButton.setOnClickListener(v -> showToast("Share email"));

        // Menu buttons show options
        menuButton.setOnClickListener(v -> showOptionsMenu(v));
        contentMenuButton.setOnClickListener(v -> showContentOptionsMenu(v));
    }

    private void showOptionsMenu(View view) {
        // Show the main options menu
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.email_detail_menu, popupMenu.getMenu());

        // Make the menu items look nice
        customizeMenuItems(popupMenu.getMenu());
        forceMenuIconsToShow(popupMenu);
        setupMenuClickListeners(popupMenu);

        popupMenu.show();
    }

    private void showContentOptionsMenu(View view) {
        // Show the content options menu (the one in the email body area)
        PopupMenu popupMenu = new PopupMenu(this, view);
        Menu menu = popupMenu.getMenu();

        // Add menu items with icons
        menu.add(0, MENU_CONTENT_REPLY, 0, "  Reply").setIcon(R.drawable.ic_reply);
        menu.add(0, MENU_CONTENT_FORWARD, 1, "  Forward").setIcon(R.drawable.ic_forward);
        menu.add(0, MENU_CONTENT_ARCHIVE, 2, "  Archive").setIcon(R.drawable.ic_archive);

        // Make the spam item red
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
        // Add spacing and color to menu items
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            String spacedTitle = "  " + item.getTitle();

            // Make the spam item red
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
        // This makes sure the menu icons actually show up
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

            if (itemId == R.id.menu_archive) {
                showToast("Email archived");
            } else if (itemId == R.id.menu_trash) {
                showToast("Email moved to trash");
            } else if (itemId == R.id.menu_unread) {
                showToast("Email marked as unread");
            } else if (itemId == R.id.menu_spam) {
                showToast("Email reported as spam");
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
                showToast("Email archived");
            } else if (itemId == MENU_CONTENT_SPAM) {
                showToast("Email reported as spam");
            }
            return true;
        });
    }

    private void loadEmailData() {
        // Get the email data that was passed to us
        Intent intent = getIntent();
        if (intent == null) return;

        // Set all the email information to the text views
        senderTextView.setText(getStringExtra(intent, "sender", DEFAULT_SENDER));
        subjectTextView.setText(getStringExtra(intent, "subject", DEFAULT_SUBJECT));
        dateTextView.setText(getStringExtra(intent, "date", DEFAULT_DATE));
        recipientTextView.setText(getStringExtra(intent, "to", DEFAULT_RECIPIENT));
        bodyTextView.setText(getStringExtra(intent, "body", DEFAULT_BODY));
    }

    private String getStringExtra(Intent intent, String key, String defaultValue) {
        // Helper method to get string from intent with a default value
        String value = intent.getStringExtra(key);
        return value != null ? value : defaultValue;
    }

    private void openReplyScreen() {
        // Open the reply screen with the current email data
        Fragment replyFragment = ReplyFragment.newInstance(
                getIntent().getStringExtra("sender"),
                getIntent().getStringExtra("subject"),
                getIntent().getStringExtra("date"),
                getIntent().getStringExtra("body")
        );
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, replyFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openForwardScreen() {
        // Open the forward screen with the current email data
        Fragment forwardFragment = ForwardFragment.newInstance(
                getIntent().getStringExtra("sender"),
                getIntent().getStringExtra("subject"),
                getIntent().getStringExtra("date"),
                getIntent().getStringExtra("body")
        );
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, forwardFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
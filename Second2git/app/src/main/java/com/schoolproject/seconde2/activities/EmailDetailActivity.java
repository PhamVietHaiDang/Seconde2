package com.schoolproject.seconde2.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.schoolproject.seconde2.ComposeFragments.ForwardFragment;
import com.schoolproject.seconde2.R;
import com.schoolproject.seconde2.ComposeFragments.ReplyFragment;
import com.schoolproject.seconde2.model.Email;
import com.schoolproject.seconde2.service.EmailService;
import com.schoolproject.seconde2.viewmodel.EmailViewModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EmailDetailActivity extends AppCompatActivity {

    private static final String DEFAULT_SENDER = "Unknown Sender";
    private static final String DEFAULT_SUBJECT = "No Subject";
    private static final String DEFAULT_DATE = "Date not available";
    private static final String DEFAULT_RECIPIENT = "user@example.com";
    private static final String DEFAULT_BODY = "No content available";

    private static final int MENU_CONTENT_REPLY = 1;
    private static final int MENU_CONTENT_FORWARD = 2;
    private static final int MENU_CONTENT_ARCHIVE = 3;
    private static final int MENU_CONTENT_SPAM = 4;

    private static final String EXTRA_SENDER = "sender";
    private static final String EXTRA_SUBJECT = "subject";
    private static final String EXTRA_DATE = "date";
    private static final String EXTRA_TO = "to";
    private static final String EXTRA_BODY = "body";
    private static final String EXTRA_FOLDER = "folder";
    private static final String EXTRA_EMAIL_ID = "email_id";
    private static final String EXTRA_IS_HTML = "is_html";
    private static final String EXTRA_HTML_CONTENT = "html_content";

    private TextView senderTextView, subjectTextView, dateTextView, recipientTextView, bodyTextView;
    private WebView webViewBody;
    private ImageButton backButton, replyButton, shareButton, menuButton, contentMenuButton;

    private String emailSender, emailSubject, emailDate, emailRecipient, emailBody;
    private String emailFolder, emailHtmlContent;
    private int emailId;
    private boolean isHtmlEmail = false;

    private EmailViewModel emailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);

        emailViewModel = new androidx.lifecycle.ViewModelProvider(this).get(EmailViewModel.class);

        setupViews();
        setupClickListeners();
        loadEmailData();
    }

    private void setupViews() {
        senderTextView = findViewById(R.id.txtSender);
        subjectTextView = findViewById(R.id.txtSubject);
        dateTextView = findViewById(R.id.txtDate);
        recipientTextView = findViewById(R.id.txtTo);
        bodyTextView = findViewById(R.id.txtBody);
        webViewBody = findViewById(R.id.webViewBody);

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

        menu.add(0, MENU_CONTENT_REPLY, 0, "  Reply").setIcon(R.drawable.ic_reply);
        menu.add(0, MENU_CONTENT_FORWARD, 1, "  Forward").setIcon(R.drawable.ic_forward);
        menu.add(0, MENU_CONTENT_ARCHIVE, 2, "  Archive").setIcon(R.drawable.ic_archive);

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

        emailSender = getStringExtra(intent, EXTRA_SENDER, DEFAULT_SENDER);
        emailSubject = getStringExtra(intent, EXTRA_SUBJECT, DEFAULT_SUBJECT);
        emailDate = getStringExtra(intent, EXTRA_DATE, DEFAULT_DATE);
        emailRecipient = getStringExtra(intent, EXTRA_TO, DEFAULT_RECIPIENT);
        emailBody = getStringExtra(intent, EXTRA_BODY, DEFAULT_BODY);
        emailFolder = getStringExtra(intent, EXTRA_FOLDER, "inbox");
        emailId = intent.getIntExtra(EXTRA_EMAIL_ID, -1);
        emailHtmlContent = intent.getStringExtra(EXTRA_HTML_CONTENT);
        isHtmlEmail = intent.getBooleanExtra(EXTRA_IS_HTML, false);

        // Debug logging
        System.out.println("DEBUG EmailDetailActivity - Email data:");
        System.out.println("Sender: " + emailSender);
        System.out.println("Subject: " + emailSubject);
        System.out.println("Is HTML: " + isHtmlEmail);
        System.out.println("Body length: " + (emailBody != null ? emailBody.length() : 0));
        System.out.println("HTML length: " + (emailHtmlContent != null ? emailHtmlContent.length() : 0));

        updateEmailDisplay();
    }

    private void updateEmailDisplay() {
        subjectTextView.setText(emailSubject);
        senderTextView.setText(emailSender);
        dateTextView.setText(emailDate);
        recipientTextView.setText("to: " + emailRecipient);

        if (isHtmlEmail && emailHtmlContent != null && !emailHtmlContent.isEmpty()) {
            displayHtmlContent(emailHtmlContent);
            bodyTextView.setVisibility(View.GONE);
            webViewBody.setVisibility(View.VISIBLE);
        } else {
            bodyTextView.setText(emailBody != null && !emailBody.isEmpty() ? emailBody : "No email content available.");
            bodyTextView.setVisibility(View.VISIBLE);
            webViewBody.setVisibility(View.GONE);
        }
    }

    private void displayHtmlContent(String htmlContent) {
        WebSettings webSettings = webViewBody.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadsImagesAutomatically(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webViewBody.setBackgroundColor(Color.TRANSPARENT);
        webViewBody.loadDataWithBaseURL("https://email.example.com/", createEmailHtmlWrapper(htmlContent), "text/html", "UTF-8", null);

        webViewBody.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                injectResponsiveCSS(view);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                System.out.println("WebView error: " + description);
            }
        });
    }

    private String createEmailHtmlWrapper(String originalHtml) {
        if (originalHtml.toLowerCase().contains("<html") && originalHtml.toLowerCase().contains("<body")) {
            if (!originalHtml.toLowerCase().contains("viewport")) {
                originalHtml = originalHtml.replaceFirst("(?i)<head>", "<head>\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            }
            return originalHtml;
        } else {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=yes\">\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <style>\n" +
                    "        body { margin: 0; padding: 16px; background: #ffffff; -webkit-text-size-adjust: 100%; }\n" +
                    "        img { max-width: 100%; height: auto; }\n" +
                    "        table { max-width: 100%; }\n" +
                    "        a { color: #2196F3; }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div id=\"email-content\">" + originalHtml + "</div>\n" +
                    "</body>\n" +
                    "</html>";
        }
    }

    private void injectResponsiveCSS(WebView webView) {
        String responsiveCSS = "javascript: (function() {" +
                "var style = document.createElement('style');" +
                "style.innerHTML = '" +
                "body { margin: 0 !important; padding: 16px !important; }" +
                "img { max-width: 100% !important; height: auto !important; }" +
                "table { max-width: 100% !important; }" +
                "* { -webkit-text-size-adjust: 100% !important; }" +
                "';" +
                "document.head.appendChild(style);" +
                "})()";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(responsiveCSS, null);
        }
    }

    private String getStringExtra(Intent intent, String key, String defaultValue) {
        String value = intent.getStringExtra(key);
        return value != null ? value : defaultValue;
    }

    private void openReplyScreen() {
        Fragment replyFragment = ReplyFragment.newInstance(emailSender, emailSubject, emailDate, emailBody);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, replyFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openForwardScreen() {
        Fragment forwardFragment = ForwardFragment.newInstance(emailSender, emailSubject, emailDate, emailBody);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, forwardFragment)
                .addToBackStack(null)
                .commit();
    }

    private void archiveEmail() {
        showToast("Email archived");
    }

    private void moveToTrash() {
        showToast("Email moved to trash");
    }

    private void markAsUnread() {
        showToast("Email marked as unread");
    }

    private void reportAsSpam() {
        showToast("Email reported as spam");
    }

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
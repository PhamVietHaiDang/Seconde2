package com.schoolproject.seconde2.service;

import com.schoolproject.seconde2.model.Email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailService {

    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static final int MAX_EMAILS = 30;

    private static final ThreadLocal<SimpleDateFormat> dateFormatCache = ThreadLocal.withInitial(() ->
            new SimpleDateFormat("MMM d, yyyy 'at' h:mm a", Locale.getDefault()));

    public void fetchEmails(String host, String user, String pass, String folderName, EmailFetchCallback callback) {
        executor.execute(() -> {
            List<Email> emails = new ArrayList<>();
            Store store = null;
            Folder folder = null;

            try {
                Properties props = createImapProperties();
                Session session = Session.getInstance(props);
                store = session.getStore("imaps");

                long startTime = System.currentTimeMillis();
                store.connect(host, user, pass);

                String imapFolderName = getImapFolderName(folderName);
                folder = store.getFolder(imapFolderName);

                if (!folder.exists()) {
                    String fallbackFolder = getFallbackFolderName(folderName);
                    folder = store.getFolder(fallbackFolder);
                    if (!folder.exists()) {
                        callback.onComplete(emails);
                        return;
                    }
                }

                folder.open(Folder.READ_ONLY);
                int messageCount = folder.getMessageCount();

                if (messageCount > 0) {
                    int start = Math.max(1, messageCount - MAX_EMAILS + 1);
                    int end = messageCount;
                    Message[] messages = folder.getMessages(start, end);

                    processMessagesBatch(messages, folderName, emails);
                }

                long endTime = System.currentTimeMillis();
                System.out.println("Email fetch completed in " + (endTime - startTime) + "ms");

                callback.onComplete(emails);

            } catch (Exception e) {
                System.out.println("Error fetching emails: " + e.getMessage());
                e.printStackTrace();
                callback.onError(e);
            } finally {
                closeResources(folder, store);
            }
        });
    }

    private void processMessagesBatch(Message[] messages, String folderName, List<Email> emails) {
        for (int i = 0; i < messages.length; i++) {
            try {
                System.out.println("Processing message " + (i + 1) + " of " + messages.length);
                Email email = convertToEmail(messages[i], folderName);
                if (email != null) {
                    emails.add(email);
                    System.out.println("✓ Loaded email: " + email.subject);
                    System.out.println("  Is HTML: " + email.isHtml);
                    System.out.println("  Body preview: " + (email.body != null ? email.body.substring(0, Math.min(100, email.body.length())) : "null"));
                }
            } catch (Exception e) {
                System.out.println("✗ Error processing email: " + e.getMessage());
                e.printStackTrace();
            }
        }

        Collections.sort(emails, (e1, e2) -> Long.compare(e2.timestamp, e1.timestamp));
    }

    private Properties createImapProperties() {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", "imap.gmail.com");
        props.put("mail.imaps.port", "993");
        props.put("mail.imaps.ssl.enable", "true");
        props.put("mail.imaps.timeout", "20000");
        props.put("mail.imaps.connectiontimeout", "20000");
        props.put("mail.imaps.fetchsize", "1048576");
        props.put("mail.imaps.partialfetch", "false");
        return props;
    }

    private String getImapFolderName(String folderName) {
        switch (folderName.toLowerCase()) {
            case "inbox": return "INBOX";
            case "sent": return "[Gmail]/Sent Mail";
            case "draft": return "[Gmail]/Drafts";
            case "trash": return "[Gmail]/Trash";
            case "archive": return "[Gmail]/All Mail";
            default: return folderName;
        }
    }

    private String getFallbackFolderName(String folderName) {
        switch (folderName.toLowerCase()) {
            case "inbox": return "INBOX";
            case "sent": return "Sent";
            case "draft": return "Drafts";
            case "trash": return "Trash";
            case "archive": return "Archive";
            default: return folderName;
        }
    }

    private Email convertToEmail(Message message, String folder) throws Exception {
        Email email = new Email();

        // Extract sender with display name
        email.sender = extractSenderWithDisplayName(message);
        email.subject = message.getSubject() != null ? message.getSubject() : "(No Subject)";

        // Date handling
        Date actualDate = message.getSentDate() != null ? message.getSentDate() : message.getReceivedDate();
        if (actualDate != null) {
            email.timestamp = actualDate.getTime();
            email.date = formatDateSimpleAndCorrect(actualDate);
        } else {
            email.timestamp = System.currentTimeMillis();
            email.date = "Unknown date";
        }

        // Extract email content
        EmailContent content = extractContentFromMessage(message);
        email.body = content.plainText;
        email.htmlContent = content.html;
        email.isHtml = content.isHtml;

        email.folder = folder;

        return email;
    }

    private String extractSenderWithDisplayName(Message message) throws Exception {
        if (message.getFrom() == null || message.getFrom().length == 0) {
            return "Unknown Sender";
        }

        Address[] fromAddresses = message.getFrom();
        if (fromAddresses[0] instanceof InternetAddress) {
            InternetAddress internetAddress = (InternetAddress) fromAddresses[0];
            String personal = internetAddress.getPersonal();
            String address = internetAddress.getAddress();

            if (personal != null && !personal.trim().isEmpty()) {
                return personal + " <" + address + ">";
            } else {
                return address;
            }
        } else {
            String sender = fromAddresses[0].toString();
            return sender;
        }
    }

    private String formatDateSimpleAndCorrect(Date date) {
        try {
            if (date == null) return "Unknown date";
            return dateFormatCache.get().format(date);
        } catch (Exception e) {
            return "Unknown date";
        }
    }

    // Helper class to store both HTML and plain text content
    private static class EmailContent {
        String html;
        String plainText;
        boolean isHtml;

        EmailContent(String html, String plainText, boolean isHtml) {
            this.html = html;
            this.plainText = plainText;
            this.isHtml = isHtml;
        }
    }

    // Extract content from message - returns both HTML and plain text
    private EmailContent extractContentFromMessage(Message message) {
        try {
            Object content = message.getContent();
            System.out.println("Content object type: " + content.getClass().getSimpleName());

            if (content instanceof String) {
                String text = (String) content;
                System.out.println("Found plain text email, length: " + text.length());
                // Check if it's actually HTML disguised as plain text
                if (isLikelyHtmlContent(text)) {
                    System.out.println("Text content appears to be HTML, using as HTML email");
                    String cleanText = convertHtmlToCleanText(text);
                    return new EmailContent(text, cleanText, true);
                } else {
                    String cleanText = cleanTextContent(text);
                    return new EmailContent("", cleanText, false);
                }
            } else if (content instanceof Multipart) {
                Multipart multipart = (Multipart) content;
                System.out.println("Multipart with " + multipart.getCount() + " parts");
                return extractContentFromMultipart(multipart);
            }
        } catch (Exception e) {
            System.out.println("Error extracting content from message: " + e.getMessage());
            e.printStackTrace();
        }

        String errorMsg = "Unable to load email content.";
        return new EmailContent("", errorMsg, false);
    }

    private EmailContent extractContentFromMultipart(Multipart multipart) throws Exception {
        String htmlContent = "";
        String plainContent = "";

        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            String contentType = bodyPart.getContentType();
            System.out.println("Part " + i + " content type: " + contentType);

            if (bodyPart.isMimeType("text/plain") && plainContent.isEmpty()) {
                String content = getBodyPartContent(bodyPart);
                plainContent = cleanTextContent(content);
                System.out.println("Found plain text part, length: " + plainContent.length());
            } else if (bodyPart.isMimeType("text/html") && htmlContent.isEmpty()) {
                String content = getBodyPartContent(bodyPart);
                htmlContent = content;
                System.out.println("Found HTML part, length: " + htmlContent.length());
                System.out.println("HTML preview: " + content.substring(0, Math.min(300, content.length())));
            } else if (bodyPart.getContent() instanceof Multipart) {
                // Recursive call for nested multipart
                System.out.println("Found nested multipart in part " + i);
                EmailContent nestedContent = extractContentFromMultipart((Multipart) bodyPart.getContent());
                if (htmlContent.isEmpty() && !nestedContent.html.isEmpty()) {
                    htmlContent = nestedContent.html;
                }
                if (plainContent.isEmpty() && !nestedContent.plainText.isEmpty()) {
                    plainContent = nestedContent.plainText;
                }
            }
        }

        // SIMPLIFIED DECISION: If HTML content exists and is reasonable, use it
        if (!htmlContent.isEmpty() && htmlContent.length() > 50) {
            System.out.println("Using HTML content for email display");
            String cleanPlainText = !plainContent.isEmpty() ? plainContent : convertHtmlToCleanText(htmlContent);
            return new EmailContent(htmlContent, cleanPlainText, true);
        } else if (!plainContent.isEmpty()) {
            System.out.println("Using plain text content");
            return new EmailContent("", plainContent, false);
        } else {
            String noContent = "No readable content found in this email.";
            return new EmailContent("", noContent, false);
        }
    }

    private String getBodyPartContent(BodyPart bodyPart) {
        try {
            Object content = bodyPart.getContent();
            if (content instanceof String) {
                return (String) content;
            }
        } catch (Exception e) {
            System.out.println("Error getting body part content: " + e.getMessage());
        }
        return "";
    }

    // Check if content is likely HTML (even if not properly declared)
    private boolean isLikelyHtmlContent(String content) {
        if (content == null || content.length() < 50) return false;

        String trimmed = content.trim();
        // Check for common HTML patterns in emails
        boolean hasHtmlTags = trimmed.contains("<div") || trimmed.contains("<p") || trimmed.contains("<br") ||
                trimmed.contains("<table") || trimmed.contains("<span") || trimmed.contains("<font") ||
                trimmed.contains("<img") || trimmed.contains("<a href") || trimmed.contains("<style");
        boolean hasHtmlStructure = trimmed.contains("<html") || trimmed.contains("<!DOCTYPE") || trimmed.contains("<body");
        boolean hasHtmlEntities = trimmed.contains("&nbsp;") || trimmed.contains("&amp;") || trimmed.contains("&lt;") ||
                trimmed.contains("&quot;") || trimmed.contains("&#");

        return hasHtmlTags || hasHtmlStructure || hasHtmlEntities;
    }

    // Clean text content by removing HTML entities and extra whitespace
    private String cleanTextContent(String text) {
        if (text == null || text.isEmpty()) return "";

        String cleaned = text
                // Replace common HTML entities
                .replaceAll("&nbsp;", " ")
                .replaceAll("&amp;", "&")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"")
                .replaceAll("&#39;", "'")
                .replaceAll("&rsquo;", "'")
                .replaceAll("&lsquo;", "'")
                .replaceAll("&rdquo;", "\"")
                .replaceAll("&ldquo;", "\"")
                .replaceAll("&ndash;", "-")
                .replaceAll("&mdash;", "-")
                .replaceAll("&copy;", "(c)")
                .replaceAll("&reg;", "(r)")
                .replaceAll("&trade;", "(tm)")
                // Clean up whitespace
                .replaceAll("\\s+", " ")
                .trim();

        // Remove excessive blank lines
        cleaned = cleaned.replaceAll("\\n\\s*\\n", "\n");

        return cleaned;
    }

    // Convert HTML to clean plain text
    private String convertHtmlToCleanText(String html) {
        if (html == null || html.isEmpty()) return "";

        // First, clean HTML entities
        String text = cleanTextContent(html);

        // Then remove any remaining HTML tags and structure
        text = text
                .replaceAll("\\<br\\s*/?\\>", "\n")
                .replaceAll("\\<p\\s*[^>]*\\>", "\n")
                .replaceAll("\\</p\\>", "\n")
                .replaceAll("\\<div\\s*[^>]*\\>", "\n")
                .replaceAll("\\</div\\>", "\n")
                .replaceAll("\\<li\\s*[^>]*\\>", "\n• ")
                .replaceAll("\\</li\\>", "\n")
                .replaceAll("\\<ul\\s*[^>]*\\>", "\n")
                .replaceAll("\\</ul\\>", "\n")
                .replaceAll("\\<ol\\s*[^>]*\\>", "\n")
                .replaceAll("\\</ol\\>", "\n")
                .replaceAll("\\<h[1-6]\\s*[^>]*\\>", "\n\n")
                .replaceAll("\\</h[1-6]\\>", "\n\n")
                .replaceAll("\\<[^>]*>", "")
                .replaceAll("\\s+", " ")
                .trim();

        // Remove excessive blank lines
        text = text.replaceAll("\\n\\s*\\n", "\n");

        return text;
    }

    private void closeResources(Folder folder, Store store) {
        try {
            if (folder != null && folder.isOpen()) {
                folder.close(false);
            }
        } catch (Exception e) {
            System.out.println("Error closing folder: " + e.getMessage());
        }

        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (Exception e) {
            System.out.println("Error closing store: " + e.getMessage());
        }
    }

    public interface EmailFetchCallback {
        void onComplete(List<Email> emails);
        void onError(Exception e);
    }

    public void sendEmail(String host, String user, String pass, String to, String subject, String body) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.timeout", "15000");
        props.put("mail.smtp.connectiontimeout", "15000");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        message.setSentDate(new Date());

        Transport.send(message);
    }

    public boolean testConnection(String host, String user, String pass) {
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            props.put("mail.imaps.host", host);
            props.put("mail.imaps.port", "993");
            props.put("mail.imaps.ssl.enable", "true");
            props.put("mail.imaps.timeout", "10000");
            props.put("mail.imaps.connectiontimeout", "10000");

            Session session = Session.getInstance(props);
            Store store = session.getStore("imaps");
            store.connect(host, user, pass);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            int count = inbox.getMessageCount();
            inbox.close(false);
            store.close();

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
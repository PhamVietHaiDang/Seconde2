package com.schoolproject.seconde2.service;

import com.schoolproject.seconde2.model.Email;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

    public List<Email> fetchEmails(String host, String user, String pass, String folder) throws Exception {
        List<Email> list = new ArrayList<>();

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imaps");
        store.connect(host, user, pass);

        Folder emailFolder = store.getFolder(folder);
        emailFolder.open(Folder.READ_ONLY);
        Message[] messages = emailFolder.getMessages();

        for (Message msg : messages) {
            Email email = new Email();
            email.sender = msg.getFrom()[0].toString();
            email.subject = msg.getSubject() != null ? msg.getSubject() : "(No Subject)";
            email.body = msg.getContent().toString();
            email.date = msg.getReceivedDate() != null ? msg.getReceivedDate().toString() : "Unknown";
            email.folder = folder;
            list.add(email);
        }

        emailFolder.close(false);
        store.close();
        return list;
    }

    public void sendEmail(String host, String user, String pass,
                          String to, String subject, String body) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

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

        Transport.send(message);
    }
}
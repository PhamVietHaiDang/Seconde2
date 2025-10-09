package com.schoolproject.seconde2.ServiceFragment;

import android.os.AsyncTask;

import com.schoolproject.seconde2.EmailConfig;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    // Interface to know when email sending is done
    public interface EmailSendListener {
        void onSuccess();
        void onError(String error);
    }

    // Main method to send an email to multiple people
    public static void sendEmail(EmailConfig config, String toEmails, String emailSubject,
                                 String emailMessage, EmailSendListener callback) {

        // Use AsyncTask to send email in background so app doesn't freeze
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    // Set up the email server settings
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", EmailConfig.SMTP_SERVER);
                    props.put("mail.smtp.port", EmailConfig.SMTP_PORT);

                    // Create a session with login credentials
                    Session emailSession = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    // Use the email and password from config
                                    return new PasswordAuthentication(config.getEmail(), config.getPassword());
                                }
                            });

                    // Create the email message
                    Message message = new MimeMessage(emailSession);

                    // Set who the email is from
                    message.setFrom(new InternetAddress(config.getEmail()));

                    // Set who to send the email to (can be multiple people)
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmails));

                    // Set the email subject
                    message.setSubject(emailSubject);

                    // Set the email message body
                    message.setText(emailMessage);

                    // Actually send the email
                    Transport.send(message);

                    // If we get here, everything worked
                    return null;

                } catch (Exception e) {
                    // If something went wrong, return the error message
                    e.printStackTrace();
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                // This runs on the main thread after sending is done
                if (result != null) {
                    // There was an error sending the email
                    callback.onError(result);
                } else {
                    // Email was sent successfully
                    callback.onSuccess();
                }
            }
        }.execute(); // Start the background task
    }
}
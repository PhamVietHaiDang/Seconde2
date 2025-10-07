package com.schoolproject.seconde2;

import android.os.AsyncTask;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    // interface for callbacks
    public interface EmailSendListener {
        void onSuccess();
        void onError(String error);
    }

    // main method to send email
    public static void sendEmail(EmailConfig config, String toEmail, String emailSubject,
                                 String emailMessage, EmailSendListener callback) {

        // use asynctask to do network stuff in background
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    // setup properties for email
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", EmailConfig.SMTP_SERVER);
                    props.put("mail.smtp.port", EmailConfig.SMTP_PORT);

                    // create session with login info
                    Session emailSession = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    // use the email and password from config
                                    return new PasswordAuthentication(config.getEmail(), config.getPassword());
                                }
                            });

                    // create the actual email message
                    Message message = new MimeMessage(emailSession);

                    // set who its from
                    message.setFrom(new InternetAddress(config.getEmail()));

                    // set who to send to
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

                    // set subject
                    message.setSubject(emailSubject);

                    // set message body
                    message.setText(emailMessage);

                    // actually send the email
                    Transport.send(message);

                    // if we get here, it worked!
                    return null;

                } catch (Exception e) {
                    // if something went wrong, return the error
                    e.printStackTrace();
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                // this runs on main thread after doInBackground finishes
                if (result != null) {
                    // means there was an error
                    callback.onError(result);
                } else {
                    // means it worked!
                    callback.onSuccess();
                }
            }
        }.execute(); // don't forget to call execute!
    }
}
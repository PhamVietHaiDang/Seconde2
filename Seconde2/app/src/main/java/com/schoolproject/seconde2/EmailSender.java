package com.schoolproject.seconde2;

import android.os.AsyncTask;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    public interface EmailSendListener {
        void onSuccess();
        void onError(String error);
    }

    public static void sendEmail(EmailConfig config, String to, String subject,
                                 String message, EmailSendListener listener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", EmailConfig.SMTP_AUTH);
                    props.put("mail.smtp.starttls.enable", EmailConfig.SMTP_STARTTLS);
                    props.put("mail.smtp.host", EmailConfig.SMTP_SERVER);
                    props.put("mail.smtp.port", EmailConfig.SMTP_PORT);

                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(config.getEmail(), config.getPassword());
                                }
                            });

                    Message mimeMessage = new MimeMessage(session);
                    mimeMessage.setFrom(new InternetAddress(config.getEmail()));
                    mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                    mimeMessage.setSubject(subject);
                    mimeMessage.setText(message);

                    Transport.send(mimeMessage);
                    return null;

                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String error) {
                if (error != null) {
                    listener.onError(error);
                } else {
                    listener.onSuccess();
                }
            }
        }.execute();
    }
}
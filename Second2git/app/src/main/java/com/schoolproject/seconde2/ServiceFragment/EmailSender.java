package com.schoolproject.seconde2.ServiceFragment;

import android.os.AsyncTask;

import com.schoolproject.seconde2.EmailConfig;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    public interface EmailSendListener {
        void onSuccess();
        void onError(String error);
    }

    public static void sendEmail(EmailConfig config, String toEmails, String emailSubject,
                                 String emailMessage, EmailSendListener callback) {

        new EmailSendTask(config, toEmails, emailSubject, emailMessage, callback).execute();
    }

    private static class EmailSendTask extends AsyncTask<Void, Void, String> {
        private final EmailConfig config;
        private final String toEmails;
        private final String emailSubject;
        private final String emailMessage;
        private final EmailSendListener callback;

        EmailSendTask(EmailConfig config, String toEmails, String emailSubject,
                      String emailMessage, EmailSendListener callback) {
            this.config = config;
            this.toEmails = toEmails;
            this.emailSubject = emailSubject;
            this.emailMessage = emailMessage;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Properties props = createEmailProperties();
                Session emailSession = createEmailSession(props);
                Message message = createEmailMessage(emailSession);
                Transport.send(message);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        private Properties createEmailProperties() {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", EmailConfig.SMTP_SERVER);
            props.put("mail.smtp.port", EmailConfig.SMTP_PORT);
            return props;
        }

        private Session createEmailSession(Properties props) {
            return Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(config.getEmail(), config.getPassword());
                        }
                    });
        }

        private Message createEmailMessage(Session session) throws Exception {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getEmail()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmails));
            message.setSubject(emailSubject);
            message.setText(emailMessage);
            return message;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                callback.onError(result);
            } else {
                callback.onSuccess();
            }
        }
    }
}
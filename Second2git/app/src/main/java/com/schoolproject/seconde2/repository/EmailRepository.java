package com.schoolproject.seconde2.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.schoolproject.seconde2.data.AppDatabase;
import com.schoolproject.seconde2.data.EmailDao;
import com.schoolproject.seconde2.model.Email;
import com.schoolproject.seconde2.service.EmailService;
import java.util.*;
import java.util.concurrent.*;

public class EmailRepository {
    private EmailDao emailDao;
    private LiveData<List<Email>> allEmails;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public EmailRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        emailDao = db.emailDao();
        allEmails = emailDao.getAllEmails();
    }

    public LiveData<List<Email>> getAllEmails() {
        return allEmails;
    }

    public LiveData<List<Email>> getEmailsByFolder(String folder) {
        return emailDao.getEmailsByFolder(folder);
    }

    public void fetchAndSaveEmails(String host, String user, String pass, String folder) {
        executor.execute(() -> {
            try {
                EmailService service = new EmailService();
                List<Email> emails = service.fetchEmails(host, user, pass, folder);
                for (Email e : emails) {
                    emailDao.insert(e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendEmail(String host, String user, String pass,
                          String to, String subject, String body) {
        executor.execute(() -> {
            try {
                new EmailService().sendEmail(host, user, pass, to, subject, body);

                // Save sent email to local database
                Email sentEmail = new Email();
                sentEmail.sender = user;
                sentEmail.subject = subject;
                sentEmail.body = body;
                sentEmail.date = new Date().toString();
                sentEmail.folder = "sent";
                emailDao.insert(sentEmail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
package com.schoolproject.seconde2.repository;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.schoolproject.seconde2.data.AppDatabase;
import com.schoolproject.seconde2.data.EmailDao;
import com.schoolproject.seconde2.model.Email;
import com.schoolproject.seconde2.service.EmailService;
import java.util.*;
import java.util.concurrent.*;

public class EmailRepository {
    private EmailDao emailDao;
    private LiveData<List<Email>> allEmails;
    private ExecutorService executor = Executors.newFixedThreadPool(2);
    private static final String TAG = "EmailRepository";

    // Cache implementation for faster loading
    private Map<String, List<Email>> emailCache = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutes cache
    private Map<String, Long> cacheTimestamps = new ConcurrentHashMap<>();

    public EmailRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        emailDao = db.emailDao();
        allEmails = emailDao.getAllEmails();
    }

    public LiveData<List<Email>> getAllEmails() {
        return allEmails;
    }

    public LiveData<List<Email>> getEmailsByFolder(String folder) {
        // Check cache first for faster response
        if (emailCache.containsKey(folder) &&
                System.currentTimeMillis() - cacheTimestamps.getOrDefault(folder, 0L) < CACHE_DURATION) {
            Log.d(TAG, "Returning cached emails for: " + folder);
            List<Email> cached = new ArrayList<>(emailCache.get(folder));
            MutableLiveData<List<Email>> liveData = new MutableLiveData<>();
            liveData.setValue(cached);
            return liveData;
        }

        Log.d(TAG, "No cache found for: " + folder + ", querying database");
        return emailDao.getEmailsByFolder(folder);
    }

    public void fetchAndSaveEmails(String host, String user, String pass, String folder) {
        executor.execute(() -> {
            try {
                Log.d(TAG, "Starting email fetch for: " + user + ", folder: " + folder);
                EmailService service = new EmailService();

                service.fetchEmails(host, user, pass, folder, new EmailService.EmailFetchCallback() {
                    @Override
                    public void onComplete(List<Email> emails) {
                        if (emails != null && !emails.isEmpty()) {
                            saveEmailsToDatabase(emails, folder);
                        } else {
                            Log.d(TAG, "No emails fetched for folder: " + folder);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Error fetching emails: " + e.getMessage());
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Error in fetchAndSaveEmails: " + e.getMessage());
            }
        });
    }

    private void saveEmailsToDatabase(List<Email> emails, String folder) {
        executor.execute(() -> {
            try {
                // Clear old emails for this folder
                emailDao.deleteByFolder(folder);

                // Update cache with new emails
                emailCache.put(folder, new ArrayList<>(emails));
                cacheTimestamps.put(folder, System.currentTimeMillis());

                // Insert in smaller batches for better performance
                int batchSize = 5;
                for (int i = 0; i < emails.size(); i += batchSize) {
                    int end = Math.min(i + batchSize, emails.size());
                    List<Email> batch = emails.subList(i, end);
                    for (Email email : batch) {
                        emailDao.insert(email);
                    }
                    // Small delay to prevent overwhelming the database
                    try { Thread.sleep(10); } catch (InterruptedException e) { }
                }

                Log.d(TAG, "Saved " + emails.size() + " emails to database and cache for folder: " + folder);
            } catch (Exception e) {
                Log.e(TAG, "Error saving emails: " + e.getMessage());
            }
        });
    }

    public void sendEmail(String host, String user, String pass,
                          String to, String subject, String body) {
        executor.execute(() -> {
            try {
                Log.d(TAG, "Sending email to: " + to);
                new EmailService().sendEmail(host, user, pass, to, subject, body);

                Email sentEmail = new Email();
                sentEmail.sender = user;
                sentEmail.subject = subject;
                sentEmail.body = body;
                sentEmail.date = new Date().toString();
                sentEmail.folder = "sent";
                sentEmail.timestamp = System.currentTimeMillis();
                emailDao.insert(sentEmail);

                Log.d(TAG, "Email sent successfully to: " + to);
            } catch (Exception e) {
                Log.e(TAG, "Error sending email: " + e.getMessage());
            }
        });
    }

    // Clear cache when user signs out or when needed
    public void clearCache() {
        emailCache.clear();
        cacheTimestamps.clear();
        Log.d(TAG, "Email cache cleared");
    }
}
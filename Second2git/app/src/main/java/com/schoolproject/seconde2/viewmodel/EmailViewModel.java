package com.schoolproject.seconde2.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.schoolproject.seconde2.model.Email;
import com.schoolproject.seconde2.repository.EmailRepository;
import java.util.ArrayList;
import java.util.List;

public class EmailViewModel extends AndroidViewModel {
    private EmailRepository repository;
    private String userEmail;
    private String userPassword;

    private MutableLiveData<List<Email>> allEmails = new MutableLiveData<>();
    private MutableLiveData<List<Email>> inboxEmails = new MutableLiveData<>();
    private MutableLiveData<List<Email>> sentEmails = new MutableLiveData<>();
    private MutableLiveData<List<Email>> draftEmails = new MutableLiveData<>();

    private static final String TAG = "EmailViewModel";

    public EmailViewModel(Application application) {
        super(application);
        repository = new EmailRepository(application);

        // Initialize with empty lists - NO SAMPLE DATA
        allEmails.setValue(new ArrayList<>());
        inboxEmails.setValue(new ArrayList<>());
        sentEmails.setValue(new ArrayList<>());
        draftEmails.setValue(new ArrayList<>());
    }

    public void setCredentials(String email, String password) {
        this.userEmail = email;
        this.userPassword = password;
        Log.d(TAG, "Credentials set for: " + email);
    }

    public LiveData<List<Email>> getAllEmails() {
        if (userEmail != null && userPassword != null) {
            Log.d(TAG, "Getting real emails from database");
            return repository.getAllEmails();
        } else {
            Log.d(TAG, "No emails - user not signed in");
            return allEmails; // Returns empty list
        }
    }

    public LiveData<List<Email>> getEmailsByFolder(String folder) {
        Log.d(TAG, "Getting emails for folder: " + folder);
        if (userEmail != null && userPassword != null) {
            return repository.getEmailsByFolder(folder);
        } else {
            // Return empty lists when not signed in - NO SAMPLE DATA
            switch (folder) {
                case "inbox":
                    return inboxEmails;
                case "sent":
                    return sentEmails;
                case "draft":
                    return draftEmails;
                default:
                    return allEmails;
            }
        }
    }

    public void refreshEmails(String folder) {
        Log.d(TAG, "Refreshing emails for folder: " + folder);
        if (userEmail != null && userPassword != null) {
            Log.d(TAG, "Calling repository to fetch emails for: " + folder);
            repository.fetchAndSaveEmails("imap.gmail.com", userEmail, userPassword, folder);
            Toast.makeText(getApplication(), "Fetching " + folder + " emails...", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "User not signed in - cannot refresh emails");
            Toast.makeText(getApplication(), "Please sign in first", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendEmail(String to, String subject, String body) {
        if (userEmail != null && userPassword != null) {
            repository.sendEmail("smtp.gmail.com", userEmail, userPassword, to, subject, body);
            Toast.makeText(getApplication(), "Email sent!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), "Please sign in first", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isUserSignedIn() {
        return userEmail != null && userPassword != null;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
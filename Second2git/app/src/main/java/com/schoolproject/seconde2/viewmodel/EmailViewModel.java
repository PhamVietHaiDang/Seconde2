package com.schoolproject.seconde2.viewmodel;

import android.app.Application;
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

    public EmailViewModel(Application application) {
        super(application);
        repository = new EmailRepository(application);

        // Initialize with empty lists
        allEmails.setValue(new ArrayList<>());
        inboxEmails.setValue(new ArrayList<>());
        sentEmails.setValue(new ArrayList<>());
        draftEmails.setValue(new ArrayList<>());
    }

    public void setCredentials(String email, String password) {
        this.userEmail = email;
        this.userPassword = password;
    }

    public LiveData<List<Email>> getAllEmails() {
        if (userEmail != null && userPassword != null) {
            return repository.getAllEmails();
        } else {
            return allEmails;
        }
    }

    public LiveData<List<Email>> getEmailsByFolder(String folder) {
        if (userEmail != null && userPassword != null) {
            return repository.getEmailsByFolder(folder);
        } else {
            // Return appropriate LiveData based on folder
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
        if (userEmail != null && userPassword != null) {
            // Use real email service
            repository.fetchAndSaveEmails("imap.gmail.com", userEmail, userPassword, folder);
        } else {
            // Fallback to sample data if not signed in
            loadSampleEmails(folder);
        }
    }

    public void sendEmail(String to, String subject, String body) {
        if (userEmail != null && userPassword != null) {
            repository.sendEmail("smtp.gmail.com", userEmail, userPassword, to, subject, body);
        } else {
            // If not signed in, just show success message but don't actually send
            // You might want to handle this differently
        }
    }

    private void loadSampleEmails(String folder) {
        // Sample data fallback
        List<Email> sampleEmails = new ArrayList<>();

        switch (folder) {
            case "inbox":
                sampleEmails.add(new Email("USTH Administration", "Welcome to New Semester",
                        "Dear students, welcome back to the new academic year...", "9:00 AM", "inbox"));
                sampleEmails.add(new Email("Project Team", "Meeting About Mobile App Project",
                        "Hi team, let's meet tomorrow to discuss the progress...", "2:30 PM", "inbox"));
                inboxEmails.setValue(sampleEmails);
                break;

            case "sent":
                sampleEmails.add(new Email("Professor Smith", "Homework Assignment 5 Submission",
                        "Dear Professor, please find my homework assignment attached...", "8:15 AM", "sent"));
                sampleEmails.add(new Email("Project Team", "Updated Design Mockups",
                        "Hi team, I've updated the design mockups based on our discussion...", "3:45 PM", "sent"));
                sentEmails.setValue(sampleEmails);
                break;

            case "draft":
                // Empty drafts by default
                draftEmails.setValue(new ArrayList<>());
                break;
        }

        // Update all emails
        List<Email> currentAll = allEmails.getValue();
        if (currentAll != null) {
            currentAll.addAll(sampleEmails);
            allEmails.setValue(currentAll);
        }
    }

    public boolean isUserSignedIn() {
        return userEmail != null && userPassword != null;
    }
}
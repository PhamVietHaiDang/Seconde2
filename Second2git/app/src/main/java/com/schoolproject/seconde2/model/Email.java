package com.schoolproject.seconde2.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "emails")
public class Email {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "sender")
    public String sender;

    @ColumnInfo(name = "subject")
    public String subject;

    @ColumnInfo(name = "body")
    public String body;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "timestamp")
    public long timestamp = System.currentTimeMillis();

    @ColumnInfo(name = "folder")
    public String folder = "inbox";

    @ColumnInfo(name = "is_read")
    public boolean isRead = false;

    // Add these new fields for HTML support
    @ColumnInfo(name = "html_content")
    public String htmlContent;

    @ColumnInfo(name = "is_html")
    public boolean isHtml = false;

    public Email() {}

    // Main constructor
    public Email(String sender, String subject, String body, String date, String folder) {
        this.sender = sender;
        this.subject = subject;
        this.body = body;
        this.date = date;
        this.folder = folder;
    }

    // Constructor with ID
    public Email(int id, String sender, String subject, String body, String date, String folder, boolean isRead) {
        this(sender, subject, body, date, folder);
        this.id = id;
        this.isRead = isRead;
    }

    // Constructor with timestamp
    public Email(String sender, String subject, String body, String date, long timestamp, String folder) {
        this(sender, subject, body, date, folder);
        this.timestamp = timestamp;
    }
}
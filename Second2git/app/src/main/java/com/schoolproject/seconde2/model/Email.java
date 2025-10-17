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

    @ColumnInfo(name = "folder")
    public String folder = "inbox"; // inbox, sent, draft, trash

    @ColumnInfo(name = "is_read")
    public boolean isRead = false;

    // Default constructor (required by Room)
    public Email() {
    }

    // Constructor for creating sample emails (without id)
    public Email(String sender, String subject, String body, String date, String folder) {
        this.sender = sender;
        this.subject = subject;
        this.body = body;
        this.date = date;
        this.folder = folder;
        this.isRead = false; // default value
    }

    // Optional: Constructor with all fields including id
    public Email(int id, String sender, String subject, String body, String date, String folder, boolean isRead) {
        this.id = id;
        this.sender = sender;
        this.subject = subject;
        this.body = body;
        this.date = date;
        this.folder = folder;
        this.isRead = isRead;
    }
}
package com.schoolproject.seconde2;

public class EmailConfig {
    public static final String SMTP_SERVER = "smtp.gmail.com";
    public static final String SMTP_PORT = "587";
    public static final String SMTP_AUTH = "true";
    public static final String SMTP_STARTTLS = "true";

    private String email;
    private String password;

    public EmailConfig(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
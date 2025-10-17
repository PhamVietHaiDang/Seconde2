package com.schoolproject.seconde2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.schoolproject.seconde2.R;
import com.schoolproject.seconde2.activities.MainActivity;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

public class SignInFragment extends Fragment {

    private EditText emailEditText, passwordEditText;
    private ImageView passwordToggle;
    private ProgressBar progressBar;
    private TextView signInButton;
    private boolean isPasswordVisible = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        setupViews(view);
        setupSignInButton();
        setupPasswordToggle();
        setupForgotPassword(view);
        return view;
    }

    private void setupViews(View view) {
        emailEditText = view.findViewById(R.id.etEmail);
        passwordEditText = view.findViewById(R.id.etPassword);
        passwordToggle = view.findViewById(R.id.ivTogglePassword);
        progressBar = view.findViewById(R.id.progressBar);
        signInButton = view.findViewById(R.id.btnSignIn);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setupSignInButton() {
        signInButton.setOnClickListener(v -> handleSignIn());
    }

    private void setupPasswordToggle() {
        passwordToggle.setOnClickListener(v -> togglePasswordVisibility());
    }

    private void setupForgotPassword(View view) {
        view.findViewById(R.id.tvForgot).setOnClickListener(v -> handleForgotPassword());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordToggle.setImageResource(R.drawable.ic_eye_off);
        } else {
            passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordToggle.setImageResource(R.drawable.ic_eye_on);
        }
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    private void handleSignIn() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(getContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        authenticateUser(email, password);
    }

    private void authenticateUser(String email, String password) {
        showLoading(true);

        new Thread(() -> {
            boolean isAuthenticated = false;
            String errorMessage = "Login failed. Please check your credentials.";

            try {
                // Test email credentials by trying to connect to email server
                isAuthenticated = testEmailCredentials(email, password);

                if (!isAuthenticated) {
                    errorMessage = "Cannot verify email credentials. Please check:\n\n• Email and password\n• For Gmail: Use App Password\n• Internet connection";
                }

            } catch (Exception e) {
                errorMessage = "Connection error: " + getReadableErrorMessage(e);
            }

            final boolean finalAuth = isAuthenticated;
            final String finalError = errorMessage;

            requireActivity().runOnUiThread(() -> {
                showLoading(false);
                if (finalAuth) {
                    saveCredentialsAndProceed(email, password);
                } else {
                    Toast.makeText(getContext(), finalError, Toast.LENGTH_LONG).show();
                }
            });
        }).start();
    }

    private boolean testEmailCredentials(String email, String password) {
        // Try different email providers
        if (testGmailSmtp(email, password)) {
            return true;
        }

        if (testOutlookSmtp(email, password)) {
            return true;
        }

        if (testYahooSmtp(email, password)) {
            return true;
        }

        // Try generic SMTP as fallback
        return testGenericSmtp(email, password);
    }

    private boolean testGmailSmtp(String email, String password) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.connectiontimeout", "10000");

            Session session = Session.getInstance(props);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", email, password);
            boolean isConnected = transport.isConnected();
            transport.close();
            return isConnected;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean testOutlookSmtp(String email, String password) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp-mail.outlook.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.connectiontimeout", "10000");

            Session session = Session.getInstance(props);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp-mail.outlook.com", email, password);
            boolean isConnected = transport.isConnected();
            transport.close();
            return isConnected;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean testYahooSmtp(String email, String password) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.mail.yahoo.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.connectiontimeout", "10000");

            Session session = Session.getInstance(props);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.mail.yahoo.com", email, password);
            boolean isConnected = transport.isConnected();
            transport.close();
            return isConnected;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean testGenericSmtp(String email, String password) {
        try {
            // Extract domain from email
            String domain = email.substring(email.indexOf('@') + 1).toLowerCase();
            String smtpHost = "smtp." + domain;

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.connectiontimeout", "10000");

            Session session = Session.getInstance(props);
            Transport transport = session.getTransport("smtp");
            transport.connect(smtpHost, email, password);
            boolean isConnected = transport.isConnected();
            transport.close();
            return isConnected;
        } catch (Exception e) {
            return false;
        }
    }

    private String getReadableErrorMessage(Exception e) {
        String message = e.getMessage();
        if (message == null) return "Unknown error occurred";

        if (message.contains("535") || message.contains("535-5.7.8")) {
            return "Invalid email or password. For Gmail, you need to use an App Password.";
        } else if (message.contains("534") || message.contains("Application-specific password required")) {
            return "Gmail requires an App Password. Please generate one in your Google Account settings.";
        } else if (message.contains("550") || message.contains("User unknown")) {
            return "Email address not found or doesn't exist.";
        } else if (message.contains("connection timeout") || message.contains("Connection timed out")) {
            return "Connection timeout. Please check your internet connection.";
        } else if (message.contains("No route to host") || message.contains("Network is unreachable")) {
            return "Network error. Please check your internet connection.";
        } else {
            return message;
        }
    }

    private void saveCredentialsAndProceed(String email, String password) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setUserCredentials(email, password);
            ((MainActivity) getActivity()).showMainInbox();
            Toast.makeText(getContext(), "Welcome to your email app!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading(boolean loading) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            }

            emailEditText.setEnabled(!loading);
            passwordEditText.setEnabled(!loading);
            passwordToggle.setEnabled(!loading);
            signInButton.setEnabled(!loading);

            if (loading) {
                signInButton.setText("Signing in...");
                signInButton.setAlpha(0.7f);
            } else {
                signInButton.setText("Sign in");
                signInButton.setAlpha(1.0f);
            }
        });
    }

    private void handleForgotPassword() {
        Toast.makeText(getContext(), "Please reset your password through your email provider's website", Toast.LENGTH_LONG).show();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
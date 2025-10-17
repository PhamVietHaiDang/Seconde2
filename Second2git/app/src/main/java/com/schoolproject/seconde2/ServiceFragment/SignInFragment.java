package com.schoolproject.seconde2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.schoolproject.seconde2.R;
import com.schoolproject.seconde2.activities.MainActivity;

public class SignInFragment extends Fragment {

    private EditText emailEditText, passwordEditText;
    private ImageView passwordToggle;
    private boolean isPasswordVisible = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        setupViews(view);
        setupSignInButton(view);
        setupPasswordToggle(view);
        setupForgotPassword(view);
        return view;
    }

    private void setupViews(View view) {
        emailEditText = view.findViewById(R.id.etEmail);
        passwordEditText = view.findViewById(R.id.etPassword);
        passwordToggle = view.findViewById(R.id.ivTogglePassword);
    }

    private void setupSignInButton(View view) {
        view.findViewById(R.id.btnSignIn).setOnClickListener(v -> handleSignIn());
    }

    private void setupPasswordToggle(View view) {
        passwordToggle.setOnClickListener(v -> togglePasswordVisibility());
    }

    private void setupForgotPassword(View view) {
        view.findViewById(R.id.tvForgot).setOnClickListener(v -> handleForgotPassword());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordToggle.setImageResource(R.drawable.ic_eye_off);
        } else {
            // Show password
            passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordToggle.setImageResource(R.drawable.ic_eye_on);
        }
        isPasswordVisible = !isPasswordVisible;

        // Move cursor to end
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

        // Store credentials and proceed to main inbox
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setUserCredentials(email, password);
            ((MainActivity) getActivity()).showMainInbox();
            Toast.makeText(getContext(), "Signed in successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleForgotPassword() {
        Toast.makeText(getContext(), "Forgot password feature coming soon!", Toast.LENGTH_SHORT).show();
        // TODO: Implement forgot password flow
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
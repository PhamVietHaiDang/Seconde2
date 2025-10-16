package com.schoolproject.seconde2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.schoolproject.seconde2.R;
import com.schoolproject.seconde2.activities.MainActivity;

public class SplashFragment extends Fragment {

    private static final String TAG = "SplashFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        // Auto-navigate after 2 seconds
        view.postDelayed(() -> {
            navigateToOnboarding();
        }, 2000);

        // Click listener as fallback
        View root = view.findViewById(R.id.splashRoot);
        if (root == null) {
            root = view;
        }

        root.setOnClickListener(v -> {
            navigateToOnboarding();
        });

        return view;
    }

    private void navigateToOnboarding() {
        try {
            // Use the same method as other fragments
            ((com.schoolproject.seconde2.activities.MainActivity) requireActivity()).showOnboardingFragment1();
        } catch (Exception navErr) {
            Log.w(TAG, "Navigation failed, falling back to Main", navErr);
            Intent i = new Intent(requireContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            requireActivity().finish();
        }
    }
}
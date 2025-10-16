package com.schoolproject.seconde2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.schoolproject.seconde2.R;

public class OnBoardingFragment2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onboarding_2, container, false);
        v.findViewById(R.id.btnContinue).setOnClickListener(view -> {
            ((com.schoolproject.seconde2.activities.MainActivity) requireActivity()).showOnboardingFragment3();
        });
        return v;
    }
}
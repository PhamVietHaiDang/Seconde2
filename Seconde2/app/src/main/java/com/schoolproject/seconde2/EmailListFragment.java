package com.schoolproject.seconde2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class EmailListFragment extends Fragment {

    protected LinearLayout emailListContainer;
    protected ImageView btnMenu;
    protected TextView folderTitle;
    protected LinearLayout composeButton;

    public EmailListFragment() {
        // empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email_list, container, false);

        // find views
        emailListContainer = view.findViewById(R.id.emailListContainer);
        btnMenu = view.findViewById(R.id.btnMenu);
        folderTitle = view.findViewById(R.id.folderTitle);
        composeButton = view.findViewById(R.id.composeButton);

        // set up menu button
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open navigation drawer
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).openNavigationDrawer();
                }
            }
        });

        // set up compose button
        composeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent composeIntent = new Intent(getActivity(), ComposeEmailActivity.class);
                startActivity(composeIntent);
            }
        });

        // load emails
        loadEmailData();

        return view;
    }

    // this method will be different in each child class
    protected void loadEmailData() {
        // child classes will fill this
    }

    // helper method to add an email to the list
    protected void addEmailToList(String from, String subject, String preview, String time, View.OnClickListener click) {
        View emailView = LayoutInflater.from(getContext()).inflate(R.layout.item_email, emailListContainer, false);

        TextView fromText = emailView.findViewById(R.id.txtSender);
        TextView subjectText = emailView.findViewById(R.id.txtSubject);
        TextView timeText = emailView.findViewById(R.id.txtDate);

        fromText.setText(from);

        // Combine subject and preview since we don't have separate preview field
        String fullSubject = subject;
        if (preview != null && !preview.isEmpty()) {
            fullSubject = subject + " - " + preview;
        }
        subjectText.setText(fullSubject);

        timeText.setText(time);

        if (click != null) {
            emailView.setOnClickListener(click);
        }

        emailListContainer.addView(emailView);
    }

    // set the folder title
    protected void setFolderTitle(String title) {
        if (folderTitle != null) {
            folderTitle.setText(title);
        }
    }
}
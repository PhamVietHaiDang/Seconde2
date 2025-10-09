package com.schoolproject.seconde2.BaseEmailFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.schoolproject.seconde2.R;
import com.schoolproject.seconde2.activities.ComposeEmailActivity;
import com.schoolproject.seconde2.activities.MainActivity;

public class EmailListFragment extends Fragment {

    protected LinearLayout emailListContainer;
    protected ImageView menuButton;
    protected TextView folderTitle;
    protected LinearLayout composeButton;

    public EmailListFragment() {
        // Empty constructor needed for fragments
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, android.os.Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email_list, container, false);

        // Find all the views we need
        emailListContainer = view.findViewById(R.id.emailListContainer);
        menuButton = view.findViewById(R.id.btnMenu);
        folderTitle = view.findViewById(R.id.folderTitle);
        composeButton = view.findViewById(R.id.composeButton);

        // Set up the menu button to open navigation drawer
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).openNavigationDrawer();
                }
            }
        });

        // Set up the compose button to start compose activity
        composeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent composeIntent = new android.content.Intent(getActivity(), ComposeEmailActivity.class);
                startActivity(composeIntent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Load the email data now that the view is ready
        loadEmailData();
    }

    // This method will be different in each child fragment
    protected void loadEmailData() {
        // Child classes will override this to load their specific emails
    }

    // Helper method to add an email item to the list
    protected void addEmailToList(String from, String subject, String preview, String time, View.OnClickListener click) {
        if (emailListContainer == null) return;

        // Create a new email item view
        View emailView = LayoutInflater.from(getContext()).inflate(R.layout.item_email, emailListContainer, false);

        // Find the text views in the email item
        TextView fromText = emailView.findViewById(R.id.txtSender);
        TextView subjectText = emailView.findViewById(R.id.txtSubject);
        TextView timeText = emailView.findViewById(R.id.txtDate);

        // Set the email data
        fromText.setText(from);

        // Combine subject and preview since we don't have separate preview field
        String fullSubject = subject;
        if (preview != null && !preview.isEmpty()) {
            fullSubject = subject + " - " + preview;
        }
        subjectText.setText(fullSubject);

        timeText.setText(time);

        // Set click listener if provided
        if (click != null) {
            emailView.setOnClickListener(click);
        }

        // Add the email view to the list
        emailListContainer.addView(emailView);
    }

    // Set the title at the top of the screen
    protected void setFolderTitle(String title) {
        if (folderTitle != null) {
            folderTitle.setText(title);
        }
    }

    // Show a message when there are no emails to display
    protected void showNoDataScreen(String title, String message) {
        if (getView() == null || emailListContainer == null) {
            return;
        }

        // Clear any existing emails first
        emailListContainer.removeAllViews();

        // Make the container fill the screen
        emailListContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        // Create the no data view
        View noDataView = getLayoutInflater().inflate(R.layout.layout_no_data, emailListContainer, false);

        // Set the title and message
        TextView titleText = noDataView.findViewById(R.id.noDataTitle);
        TextView messageText = noDataView.findViewById(R.id.noDataMessage);

        if (title != null) {
            titleText.setText(title);
        }
        if (message != null) {
            messageText.setText(message);
        }

        // Make the no data view fill the container
        noDataView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        // Add the no data view to the container
        emailListContainer.addView(noDataView);
    }

    // Show no data screen with default messages
    protected void showNoDataScreen() {
        showNoDataScreen("No data found", "There's nothing to display here");
    }
}
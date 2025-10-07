package com.schoolproject.seconde2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class EmailDetailActivity extends AppCompatActivity {

    // variables for text views
    TextView txtSender, txtSubject, txtDate, txtTo, txtBody;

    // buttons
    ImageButton btnBack;
    LinearLayout btnReply, btnForward;

    // to store email stuff
    String sender, subject, date, body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);

        // call methods to set things up
        setupViews();
        setupClicks();
        getDataFromIntent();
    }

    // method to find all the views by id
    void setupViews() {
        txtSender = findViewById(R.id.txtSender);
        txtSubject = findViewById(R.id.txtSubject);
        txtDate = findViewById(R.id.txtDate);
        txtTo = findViewById(R.id.txtTo);
        txtBody = findViewById(R.id.txtBody);

        btnBack = findViewById(R.id.btnBack);
        btnReply = findViewById(R.id.btnReply);
        btnForward = findViewById(R.id.btnForward);
    }

    // handle button clicks
    void setupClicks() {
        // back button just closes this activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // reply button
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open reply fragment
                openReply();
            }
        });

        // forward button
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open forward fragment
                openForward();
            }
        });
    }

    // get the email data that was sent to this activity
    void getDataFromIntent() {
        Intent i = getIntent();
        if (i != null) {
            // get all the strings from intent
            sender = i.getStringExtra("sender");
            subject = i.getStringExtra("subject");
            date = i.getStringExtra("date");
            body = i.getStringExtra("body");

            // put the data in text views
            if (sender != null) {
                txtSender.setText(sender);
            }
            if (subject != null) {
                txtSubject.setText(subject);
            }
            if (date != null) {
                txtDate.setText(date);
            }
            if (body != null) {
                txtBody.setText(body);
            }
        }
    }

    // method for reply
    void openReply() {
        // create reply fragment with the email data
        Fragment replyFrag = ReplyFragment.newInstance(sender, subject, date, body);
        showMyFragment(replyFrag);
    }

    // method for forward
    void openForward() {
        // create forward fragment with the email data
        Fragment forwardFrag = ForwardFragment.newInstance(sender, subject, date, body);
        showMyFragment(forwardFrag);
    }

    // helper method to show a fragment
    void showMyFragment(Fragment frag) {
        // get fragment manager and start transaction
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // replace current fragment and add to back stack
        ft.replace(android.R.id.content, frag);
        ft.addToBackStack(null);
        ft.commit();
    }
}
package com.schoolproject.seconde2.fragments;

import android.content.Intent;
import android.view.View;

import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;
import com.schoolproject.seconde2.activities.ContractDetailActivity;

public class ContractsFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Contracts");

        // Load all the company contracts we have
        loadAllContracts();
    }

    private void loadAllContracts() {
        // Add contracts from different companies we work with
        addMicrosoftContract();
        addGoogleContract();
        addAmazonContract();
        addSlackContract();
        addAdobeContract();

        // We can add more contracts here when we get new ones
    }

    private void addMicrosoftContract() {
        addEmailToList(
                "Microsoft Corporation",
                "Software License Agreement",
                "Office 365 Business Premium - 50 users",
                "Active • Renews: Dec 2024",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openContractDetails("Microsoft Corporation");
                    }
                }
        );
    }

    private void addGoogleContract() {
        addEmailToList(
                "Google Cloud Platform",
                "Cloud Services Agreement",
                "GCP Enterprise Support - Production",
                "Active • Renews: Jan 2025",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openContractDetails("Google Cloud Platform");
                    }
                }
        );
    }

    private void addAmazonContract() {
        addEmailToList(
                "Amazon Web Services",
                "Hosting Service Contract",
                "AWS Business Support Plan",
                "Active • Renews: Nov 2024",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openContractDetails("Amazon Web Services");
                    }
                }
        );
    }

    private void addSlackContract() {
        addEmailToList(
                "Slack Technologies",
                "Collaboration Subscription",
                "Slack Enterprise Grid - 100 seats",
                "Active • Renews: Mar 2025",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openContractDetails("Slack Technologies");
                    }
                }
        );
    }

    private void addAdobeContract() {
        addEmailToList(
                "Adobe Inc.",
                "Creative Cloud License",
                "Adobe Creative Cloud - Design Team",
                "Active • Renews: Feb 2025",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openContractDetails("Adobe Inc.");
                    }
                }
        );
    }

    private void openContractDetails(String companyName) {
        // Open the contract detail screen for this company
        Intent contractIntent = new Intent(getActivity(), ContractDetailActivity.class);

        // Tell the detail screen which company we're looking at
        contractIntent.putExtra("company_name", companyName);

        // Start the detail activity
        startActivity(contractIntent);
    }
}
package com.schoolproject.seconde2;

import android.content.Intent;
import android.view.View;
import androidx.fragment.app.Fragment;

public class ContractsFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Contracts");

        // Load all our company contracts
        loadAllContracts();
    }

    private void loadAllContracts() {
        // Add contracts from different companies
        addMicrosoftContract();
        addGoogleContract();
        addAmazonContract();
        addSlackContract();
        addAdobeContract();

        // We could add more contracts here later
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
        // Create an intent to open the contract detail screen
        Intent contractIntent = new Intent(getActivity(), ContractDetailActivity.class);

        // Pass the company name to the detail screen
        contractIntent.putExtra("company_name", companyName);

        startActivity(contractIntent);
    }

}
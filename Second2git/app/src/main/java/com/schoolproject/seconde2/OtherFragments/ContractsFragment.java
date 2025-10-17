package com.schoolproject.seconde2.fragments;

import android.content.Intent;
import android.view.View;

import com.schoolproject.seconde2.BaseEmailFragment.EmailListFragment;
import com.schoolproject.seconde2.activities.ContractDetailActivity;

public class ContractsFragment extends EmailListFragment {

    @Override
    protected void loadEmailData() {
        setFolderTitle("Contracts");
        loadAllContracts();
    }

    private void loadAllContracts() {
        addMicrosoftContract();
        addGoogleContract();
        addAmazonContract();
        addSlackContract();
        addAdobeContract();
    }

    private void addMicrosoftContract() {
        addContract("Microsoft Corporation",
                "Software License Agreement",
                "Office 365 Business Premium - 50 users",
                "Active • Renews: Dec 2024");
    }

    private void addGoogleContract() {
        addContract("Google Cloud Platform",
                "Cloud Services Agreement",
                "GCP Enterprise Support - Production",
                "Active • Renews: Jan 2025");
    }

    private void addAmazonContract() {
        addContract("Amazon Web Services",
                "Hosting Service Contract",
                "AWS Business Support Plan",
                "Active • Renews: Nov 2024");
    }

    private void addSlackContract() {
        addContract("Slack Technologies",
                "Collaboration Subscription",
                "Slack Enterprise Grid - 100 seats",
                "Active • Renews: Mar 2025");
    }

    private void addAdobeContract() {
        addContract("Adobe Inc.",
                "Creative Cloud License",
                "Adobe Creative Cloud - Design Team",
                "Active • Renews: Feb 2025");
    }

    private void addContract(String company, String title, String description, String status) {
        addEmailToList(company, title, description, status,
                v -> openContractDetails(company));
    }

    private void openContractDetails(String companyName) {
        if (getActivity() == null) return;

        Intent contractIntent = new Intent(getActivity(), ContractDetailActivity.class);
        contractIntent.putExtra("company_name", companyName);
        startActivity(contractIntent);
    }

    // Backend integration methods
    private void fetchContractsFromBackend() {

    }

    private void displayContracts(java.util.List<Object> contracts) {
        if (contracts == null || contracts.isEmpty()) {
            showNoDataScreen("No contracts", "No active contracts found");
            return;
        }

    }
}
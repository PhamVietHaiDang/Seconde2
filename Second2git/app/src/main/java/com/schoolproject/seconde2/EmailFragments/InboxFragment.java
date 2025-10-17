package com.schoolproject.seconde2.EmailFragments;

import com.schoolproject.seconde2.BaseEmailFragment.BaseEmailFragment;

public class InboxFragment extends BaseEmailFragment {
    @Override
    protected String getFolderName() {
        return "inbox";
    }

    @Override
    protected String getFragmentTag() {
        return "InboxFragment";
    }
}
package com.schoolproject.seconde2.EmailFragments;

import com.schoolproject.seconde2.BaseEmailFragment.BaseEmailFragment;

public class DraftFragment extends BaseEmailFragment {
    @Override
    protected String getFolderName() {
        return "draft";
    }

    @Override
    protected String getFragmentTag() {
        return "DraftFragment";
    }
}
package com.schoolproject.seconde2.EmailFragments;

import com.schoolproject.seconde2.BaseEmailFragment.BaseEmailFragment;

public class TrashFragment extends BaseEmailFragment {
    @Override
    protected String getFolderName() {
        return "trash";
    }

    @Override
    protected String getFragmentTag() {
        return "TrashFragment";
    }
}
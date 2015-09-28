package ru.kadei.diaryworkouts.fragments;

import android.support.v7.app.ActionBar;

import ru.kadei.diaryworkouts.R;

public class MainFragment extends CustomFragment {

    @Override
    protected void configToolbar(ActionBar bar) {
        bar.setTitle(R.string.app_name);
    }
}

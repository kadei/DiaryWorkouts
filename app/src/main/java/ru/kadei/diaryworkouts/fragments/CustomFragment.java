package ru.kadei.diaryworkouts.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;

import ru.kadei.diaryworkouts.activities.MainActivity;

public class CustomFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getClass().getName();
        Log.d("TEST", name);

        final MainActivity a = (MainActivity) getActivity();
        configToolbar(a.getSupportActionBar());
    }

    protected void configToolbar(ActionBar bar) {
    }

    public void save(Bundle bundle) {
    }

    public void restore(Bundle bundle) {
    }

    public void prepareForClose(Preparer preparer) {
        preparer.iReady();
    }
}

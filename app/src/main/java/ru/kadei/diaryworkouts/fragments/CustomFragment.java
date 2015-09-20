package ru.kadei.diaryworkouts.fragments;

import android.app.Fragment;

public class CustomFragment extends Fragment {
    protected int mId = -1;

    public void prepareForClose(Notifier notifier) {
        notifier.iReadyToClose();
    }
}

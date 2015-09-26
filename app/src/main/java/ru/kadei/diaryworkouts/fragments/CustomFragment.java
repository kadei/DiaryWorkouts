package ru.kadei.diaryworkouts.fragments;

import android.app.Fragment;

public class CustomFragment extends Fragment {

    public void prepareForClose(Notifier notifier) {
        notifier.iReadyToClose();
    }
}

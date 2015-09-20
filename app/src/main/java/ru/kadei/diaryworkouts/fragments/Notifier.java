package ru.kadei.diaryworkouts.fragments;

import static ru.kadei.diaryworkouts.fragments.FragmentSwitcher.NO_FRAGMENT;

/**
 * Created by kadei on 20.09.15.
 */
public abstract class Notifier {

    private final int idNextFragment;

    protected Notifier(int idNextFragment) {
        this.idNextFragment = idNextFragment;
    }

    public int getIdNextFragment() {
        return idNextFragment;
    }

    public abstract void iReadyToClose();
}

package ru.kadei.diaryworkouts.fragments;

/**
 * Created by kadei on 20.09.15.
 */
public abstract class Notifier {

    private final Class<? extends CustomFragment> nextFragment;

    protected Notifier(Class<? extends CustomFragment> nextFragment) {
        this.nextFragment = nextFragment;
    }

    public Class<? extends CustomFragment> getNextFragment() {
        return nextFragment;
    }

    public abstract void iReadyToClose();
}

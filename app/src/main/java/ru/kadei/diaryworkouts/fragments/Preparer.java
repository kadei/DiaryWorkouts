package ru.kadei.diaryworkouts.fragments;

/**
 * Created by kadei on 20.09.15.
 */
public abstract class Preparer {

    private final Class<? extends CustomFragment> nextFragment;

    protected Preparer(Class<? extends CustomFragment> nextFragment) {
        this.nextFragment = nextFragment;
    }

    public Class<? extends CustomFragment> getNextFragment() {
        return nextFragment;
    }

    public abstract void iReady();
}

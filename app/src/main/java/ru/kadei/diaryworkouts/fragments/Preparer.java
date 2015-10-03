package ru.kadei.diaryworkouts.fragments;

/**
 * Created by kadei on 20.09.15.
 */
public abstract class Preparer {

    private final int nextFragment;

    protected Preparer(int nextFragment) {
        this.nextFragment = nextFragment;
    }

    public int getNextFragment() {
        return nextFragment;
    }

    public abstract void iReady();
}

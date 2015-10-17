package ru.kadei.diaryworkouts.fragments;

/**
 * Created by kadei on 20.09.15.
 */
public abstract class Inspector {

    private final int nextFragment;

    protected Inspector(int nextFragment) {
        this.nextFragment = nextFragment;
    }

    public int getNextFragment() {
        return nextFragment;
    }

    public abstract void iReady();
}

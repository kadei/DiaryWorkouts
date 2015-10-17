package ru.kadei.diaryworkouts.threads;

/**
 * Created by kadei on 17.10.15.
 */
public abstract class Task {

    private Throwable throwable = null;

    public abstract void execute();
    public abstract void successfully();
    public abstract void fail();

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}

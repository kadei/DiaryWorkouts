package ru.kadei.diaryworkouts.threads;

/**
 * Created by kadei on 04.09.15.
 */
public class TaskException extends Exception {

    private Throwable originalException;

    public TaskException(String message) {
        super(message);
    }

    public TaskException(Throwable e) {
        originalException = e;
    }

    public Throwable getOriginalException() {
        return originalException;
    }
}

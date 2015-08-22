package ru.kadei.simpleandroidorm.main;

/**
 * Created by kadei on 20.08.15.
 */
public class DatabaseException extends RuntimeException {

    public enum ErrorCode {OK, NOT_ANNOTATED}

    private ErrorCode errorCode;
    private String messageAboutError;

    public DatabaseException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }
}

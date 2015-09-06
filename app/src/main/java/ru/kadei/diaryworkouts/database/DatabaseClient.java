package ru.kadei.diaryworkouts.database;

/**
 * Created by kadei on 05.09.15.
 */
public interface DatabaseClient {
    void loaded(ObjectBuilder builder);
    void saved(Record record);
    void fail(Throwable throwable);
}

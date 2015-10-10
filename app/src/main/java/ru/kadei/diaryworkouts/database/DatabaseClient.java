package ru.kadei.diaryworkouts.database;

/**
 * Created by kadei on 05.09.15.
 */
public interface DatabaseClient {
    void loaded(DatabaseReader reader);
    void saved(Record record);
    void executed(DatabaseExecutor executor);
    void fail(Throwable throwable);
}

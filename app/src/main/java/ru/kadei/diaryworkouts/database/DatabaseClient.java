package ru.kadei.diaryworkouts.database;

/**
 * Created by kadei on 05.09.15.
 */
public interface DatabaseClient {
    void loaded(DatabaseReader reader);
    void saved(DatabaseWriter writer);
    void executed(DatabaseExecutor executor);
    void fail(Throwable throwable);
}

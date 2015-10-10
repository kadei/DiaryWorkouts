package ru.kadei.diaryworkouts.util.stubs;

import ru.kadei.diaryworkouts.database.DatabaseClient;
import ru.kadei.diaryworkouts.database.DatabaseExecutor;
import ru.kadei.diaryworkouts.database.DatabaseReader;
import ru.kadei.diaryworkouts.database.Record;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;

/**
 * Created by kadei on 06.09.15.
 */
public class StubDatabaseClient implements DatabaseClient {

    protected final WorkoutManagerClient client;

    public StubDatabaseClient(WorkoutManagerClient client) {
        this.client = client;
    }

    @Override
    public void loaded(DatabaseReader reader) {

    }

    @Override
    public void saved(Record record) {

    }

    @Override
    public void executed(DatabaseExecutor executor) {

    }

    @Override
    public void fail(Throwable throwable) {
        client.fail(throwable);
    }
}

package ru.kadei.diaryworkouts.managers.bridges;

import ru.kadei.diaryworkouts.database.DatabaseClient;
import ru.kadei.diaryworkouts.database.Record;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;

/**
 * Created by kadei on 06.09.15.
 */
public abstract class BridgeLoad implements DatabaseClient {

    protected final WorkoutManagerClient client;

    public BridgeLoad(WorkoutManagerClient client) {
        this.client = client;
    }

    @Override
    public void saved(Record record) {

    }

    @Override
    public void fail(Throwable throwable) {

    }
}

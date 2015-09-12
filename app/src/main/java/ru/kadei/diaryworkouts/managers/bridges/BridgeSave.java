package ru.kadei.diaryworkouts.managers.bridges;

import ru.kadei.diaryworkouts.database.DatabaseReader;
import ru.kadei.diaryworkouts.database.DatabaseClient;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;

/**
 * Created by kadei on 06.09.15.
 */
public abstract class BridgeSave implements DatabaseClient {

    protected final WorkoutManagerClient client;

    public BridgeSave(WorkoutManagerClient client) {
        this.client = client;
    }

    @Override
    public void loaded(DatabaseReader reader) {
    }

    @Override
    public void fail(Throwable throwable) {
        client.fail(throwable);
    }
}

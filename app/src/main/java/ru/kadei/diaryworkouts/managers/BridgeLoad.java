package ru.kadei.diaryworkouts.managers;

import ru.kadei.diaryworkouts.database.DatabaseClient;
import ru.kadei.diaryworkouts.models.db.Cortege;

/**
 * Created by kadei on 06.09.15.
 */
public abstract class BridgeLoad implements DatabaseClient {

    protected final WorkoutManagerClient client;

    public BridgeLoad(WorkoutManagerClient client) {
        this.client = client;
    }

    @Override
    public void saved(Cortege cortege) {

    }

    @Override
    public void fail(Throwable throwable) {

    }
}

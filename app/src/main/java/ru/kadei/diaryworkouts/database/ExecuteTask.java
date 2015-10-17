package ru.kadei.diaryworkouts.database;

import ru.kadei.diaryworkouts.threads.Task;

/**
 * Created by kadei on 17.10.15.
 */
public abstract class ExecuteTask extends Task {

    private final DatabaseExecutor executor;
    private final DatabaseClient client;

    protected ExecuteTask(DatabaseExecutor executor, DatabaseClient client) {
        this.executor = executor;
        this.client = client;
    }

    public DatabaseExecutor getExecutor() {
        return executor;
    }

    public DatabaseClient getClient() {
        return client;
    }
}

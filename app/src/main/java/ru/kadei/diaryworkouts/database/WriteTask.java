package ru.kadei.diaryworkouts.database;

import ru.kadei.diaryworkouts.threads.Task;

/**
 * Created by kadei on 17.10.15.
 */
public abstract class WriteTask extends Task {

    private final DatabaseWriter writer;
    private final DatabaseClient client;

    protected WriteTask(DatabaseWriter writer, DatabaseClient client) {
        this.writer = writer;
        this.client = client;
    }

    public DatabaseWriter getWriter() {
        return writer;
    }

    public DatabaseClient getClient() {
        return client;
    }
}

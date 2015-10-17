package ru.kadei.diaryworkouts.database;

import android.support.annotation.NonNull;

import ru.kadei.diaryworkouts.threads.Task;

/**
 * Created by kadei on 17.10.15.
 */
public abstract class ReadTask extends Task {

    private final DatabaseReader reader;
    private final DatabaseClient client;

    protected ReadTask(@NonNull DatabaseReader reader, @NonNull DatabaseClient client) {
        this.reader = reader;
        this.client = client;
    }

    public DatabaseReader getReader() {
        return reader;
    }

    public DatabaseClient getClient() {
        return client;
    }
}

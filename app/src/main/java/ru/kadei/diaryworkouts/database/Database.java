package ru.kadei.diaryworkouts.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayDeque;
import java.util.Queue;

import ru.kadei.diaryworkouts.threads.BackgroundLogic;
import ru.kadei.diaryworkouts.threads.Task;

/**
 * Created by kadei on 04.09.15.
 */
public class Database {

    public static final int TRUE = 0xff;
    public static final int FALSE = 0x0;

    private final SQLiteOpenHelper dbHelper;
    private final BackgroundLogic bgLogic;
    private final Queue<DatabaseClient> clients;

    private final Task taskLoadFromDatabase;
    private final Task taskSaveInDatabase;

    public Database(SQLiteOpenHelper dbHelper, BackgroundLogic bgLogic) {
        this.dbHelper = dbHelper;
        this.bgLogic = bgLogic;
        clients = new ArrayDeque<>();

        taskLoadFromDatabase = new Task();
        taskSaveInDatabase = new Task();
        initialTasks();
    }

    private void initialTasks() {
        taskLoadFromDatabase
                .setClient(this)
                .setExecutedMethod("executeLoad", String.class, DatabaseReader.class)
                .setCompleteMethod("completeLoad")
                .setFailMethod("fail");

        taskSaveInDatabase
                .setClient(this)
                .setExecutedMethod("executeSave", Record.class, DatabaseWriter.class)
                .setCompleteMethod("completeSave")
                .setFailMethod("fail");
    }

    public void load(String query, DatabaseReader reader, DatabaseClient client) {
        clients.offer(client);
        taskLoadFromDatabase.setParameters(query, reader);
        bgLogic.execute(taskLoadFromDatabase);
    }

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    private DatabaseReader executeLoad(String query, DatabaseReader reader) {
        SQLiteDatabase db = getDB();
        try {
            reader.setDb(db);
            reader.readObjects(query);
            reader.forgetReferenceDB();
        } finally {
            db.close();
        }
        return reader;
    }

    private void completeLoad(DatabaseReader reader) {
        clients.poll().loaded(reader);
    }

    private void fail(Throwable throwable) {
        clients.poll().fail(throwable);
    }

    public void save(Record record, DatabaseWriter writer, DatabaseClient client) {
        clients.offer(client);
        taskSaveInDatabase.setParameters(record, writer);
        bgLogic.execute(taskSaveInDatabase);
    }

    private Record executeSave(Record record, DatabaseWriter writer) {
        final SQLiteDatabase db = getDB();
        try {
            db.beginTransaction();

            writer.setDB(db);
            writer.writeObject(record);
            writer.forgetReferenceDB();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return record;
    }

    private void completeSave(Record record) {
        clients.poll().saved(record);
    }

    private SQLiteDatabase getDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db == null)
            throw new RuntimeException("Error connection database");
        return db;
    }
}

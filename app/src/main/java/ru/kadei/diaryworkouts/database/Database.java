package ru.kadei.diaryworkouts.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
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
    private final Task taskExecute;

    public Database(SQLiteOpenHelper dbHelper, BackgroundLogic bgLogic) {
        this.dbHelper = dbHelper;
        this.bgLogic = bgLogic;
        clients = new ArrayDeque<>();

        taskLoadFromDatabase = new Task();
        taskSaveInDatabase = new Task();
        taskExecute = new Task();
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

        taskExecute
                .setClient(this)
                .setExecutedMethod("executeExecutor", DatabaseExecutor.class)
                .setCompleteMethod("completeExecute")
                .setFailMethod("fail");
    }

    public void stop() {
        if (!bgLogic.isThisThread())
            bgLogic.stop();

        clients.clear();
    }

    public void load(String query, DatabaseReader reader, DatabaseClient client) {
        clients.offer(client);
        bgLogic.execute(taskLoadFromDatabase, query, reader);
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
        if (!clients.isEmpty())
            clients.poll().loaded(reader);
    }

    private void fail(Throwable throwable) {
        if (!clients.isEmpty())
            clients.poll().fail(throwable);
    }

    public void save(Record record, DatabaseWriter writer, DatabaseClient client) {
        clients.offer(client);
        bgLogic.execute(taskSaveInDatabase, record, writer);
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
        if (!clients.isEmpty())
            clients.poll().saved(record);
    }

    private SQLiteDatabase getDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null)
            throw new RuntimeException("Error connection database");
        return db;
    }

    public void executeTask(DatabaseExecutor executor, DatabaseClient client) {
        clients.offer(client);
        bgLogic.execute(taskExecute, executor);
    }

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    private DatabaseExecutor executeExecutor(DatabaseExecutor executor) {
        final SQLiteDatabase db = getDB();
        try {
            executor.setDB(db);
            executor.execute();
            executor.forgetReferenceDB();
        } finally {
            db.close();
        }
        return executor;
    }

    private void completeExecute(DatabaseExecutor executor) {
        if (!clients.isEmpty())
            clients.poll().executed(executor);
    }

    public void removeDB() {
        SQLiteDatabase db = getDB();
        File dbFile = new File(db.getPath());
        db.close();

        if(dbFile.exists()) {
            if (!dbFile.delete())
                throw new RuntimeException("Error delete database file: " + dbFile.getPath());

            String journalName = dbFile.getName().replace(".db", ".db-journal");
            File journalFile = new File(journalName);
            if(journalFile.exists())
                if(!journalFile.delete())
                    throw new RuntimeException("Error delete journal file: " + journalFile.getPath());
        }
    }
}

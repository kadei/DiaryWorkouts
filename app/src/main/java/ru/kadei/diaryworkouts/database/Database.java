package ru.kadei.diaryworkouts.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

import ru.kadei.diaryworkouts.threads.BackgroundLogic;

/**
 * Created by kadei on 04.09.15.
 */
public class Database {

    public static final int TRUE = 0xff;
    public static final int FALSE = 0x0;

    private final SQLiteOpenHelper dbHelper;
    private final BackgroundLogic bgLogic;

    public Database(SQLiteOpenHelper dbHelper, BackgroundLogic bgLogic) {
        this.dbHelper = dbHelper;
        this.bgLogic = bgLogic;
    }

    public void stop() {
        bgLogic.stop();
    }

    public void load(DatabaseReader reader, DatabaseClient client) {
//        Log.i("TEST", reader.getQuery());
//        Log.i("TEST", "load client = " + client);
        bgLogic.execute(new ReadTask(reader, client) {

            @SuppressWarnings("TryFinallyCanBeTryWithResources")
            @Override
            public void execute() {
                final DatabaseReader r = getReader();
                final SQLiteDatabase db = getDB();

                try {
                    r.setDb(db);
                    r.readObjects();
                    r.forgetReferenceDB();
                } finally {
                    db.close();
                }
            }

            @Override
            public void successfully() {
                getClient().loaded(getReader());
            }

            @Override
            public void fail() {
                getClient().fail(getThrowable());
            }
        });
    }

    public void save(DatabaseWriter writer, DatabaseClient client) {
        bgLogic.execute(new WriteTask(writer, client) {
            @Override
            public void execute() {
                final DatabaseWriter w = getWriter();
                final SQLiteDatabase db = getDB();

                try {
                    db.beginTransaction();

                    w.setDB(db);
                    w.writeObject();
                    w.forgetReferenceDB();

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }
            }

            @Override
            public void successfully() {
                getClient().saved(getWriter());
            }

            @Override
            public void fail() {
                getClient().fail(getThrowable());
            }
        });
    }

    public void executeTask(DatabaseExecutor executor, DatabaseClient client) {
        bgLogic.execute(new ExecuteTask(executor, client) {
            @SuppressWarnings("TryFinallyCanBeTryWithResources")
            @Override
            public void execute() {
                final DatabaseExecutor e = getExecutor();
                final SQLiteDatabase db = getDB();

                try {
                    e.setDB(db);
                    e.execute();
                    e.forgetReferenceDB();
                } finally {
                    db.close();
                }
            }

            @Override
            public void successfully() {
                getClient().executed(getExecutor());
            }

            @Override
            public void fail() {
                getClient().fail(getThrowable());
            }
        });
    }

    private SQLiteDatabase getDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null)
            throw new RuntimeException("Error connection database");
        return db;
    }

    public void removeDB() {
        SQLiteDatabase db = getDB();
        File dbFile = new File(db.getPath());
        db.close();

        if (dbFile.exists()) {
            if (!dbFile.delete())
                throw new RuntimeException("Error delete database file: " + dbFile.getPath());

            String journalName = dbFile.getName().replace(".db", ".db-journal");
            File journalFile = new File(journalName);
            if (journalFile.exists())
                if (!journalFile.delete())
                    throw new RuntimeException("Error delete journal file: " + journalFile.getPath());
        }
    }
}

package ru.kadei.diaryworkouts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import ru.kadei.diaryworkouts.threads.BackgroundLogic;
import ru.kadei.diaryworkouts.threads.Task;

/**
 * Created by kadei on 04.09.15.
 */
public class Database {

    private final SQLiteOpenHelper dbHelper;
    private final BackgroundLogic bgLogic;
    private final Queue<DatabaseClient> clients;

    private final Task taskLoadFromDatabase;
    private final Task taskSaveInDatabase;

    public Database(SQLiteOpenHelper dbHelper, Context context, BackgroundLogic bgLogic) {
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
                .setExecutedMethod("executeLoad", String.class, ObjectBuilder.class)
                .setCompleteMethod("completeLoad")
                .setFailMethod("fail");

        taskSaveInDatabase
                .setClient(this)
                .setExecutedMethod("executeSave", Record.class, CortegeBuilder.class)
                .setCompleteMethod("completeSave")
                .setFailMethod("fail");
    }

    public void load(String query, ObjectBuilder builder, DatabaseClient client) {
        clients.offer(client);
        taskLoadFromDatabase.setParameters(query, builder);
        bgLogic.execute(taskLoadFromDatabase);
    }

    private ObjectBuilder executeLoad(String query, ObjectBuilder builder) {
        SQLiteDatabase db = getDB();
        try {
            builder.setDb(db);
            builder.buildObjects(query);
            builder.forgetReferenceDB();
        } finally {
            db.close();
        }
        return builder;
    }

    private void completeLoad(ObjectBuilder builder) {
        clients.poll().loaded(builder);
    }

    private void fail(Throwable throwable) {
        clients.poll().fail(throwable);
    }

    public void save(Record record, CortegeBuilder builder, DatabaseClient client) {
        clients.offer(client);
        taskSaveInDatabase.setParameters(record, builder);
        bgLogic.execute(taskSaveInDatabase);
    }

    private Record executeSave(Record record, CortegeBuilder builder) {
        builder.buildCortegeFor(record);
        Cortege cortege = builder.getCortege();

        final long id = record.id;
        if(!existsRecordsInTable(cortege.nameTable, id))
            record.id = insertCortege(cortege);
        else
            updateCortege(cortege, id);

        return record;
    }

    private void completeSave(Record record) {
        clients.poll().saved(record);
    }

    private boolean existsRecordsInTable(String nameTable, long id) {
        SQLiteDatabase db = getDB();
        boolean exists = false;
        try {
            Cursor c = db.rawQuery("SELECT _id FROM " + nameTable + " WHERE _id = " + id, null);
            exists = c.moveToFirst();
            c.close();
        } finally {
            db.close();
        }
        return exists;
    }

    private long insertCortege(Cortege cortege) {
        final SQLiteDatabase db = getDB();
        long id;
        try {
            db.beginTransaction();

            id = insert(db, cortege.nameTable, cortege.values);
            insertRelations(db, cortege.relations);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return id;
    }

    private long insert(SQLiteDatabase db, String nameTable, ContentValues values) {
        long id = db.insert(nameTable, null, values);
        if(id == -1)
            throw new RuntimeException("Error insert in table ["+nameTable+"]");
        return id;
    }

    private void insertRelations(SQLiteDatabase db, ArrayList<Relation> relations) {
        for (Relation r : relations) {
            String nameTable = r.nameTable;
            for(ContentValues values : r.values)
                insert(db, nameTable, values);
        }
    }

    private void updateCortege(Cortege cortege, long id) {
        final SQLiteDatabase db = getDB();
        try {
            db.beginTransaction();

            update(db, cortege.nameTable, "_id = " + id, cortege.values);
            updateRelations(db, cortege.relations);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void update(SQLiteDatabase db, String nameTable, String where, ContentValues values) {
        int rows = db.update(nameTable, values, where, null);
        if(rows == 0)
            throw new RuntimeException("Error update int table ["+nameTable+"], where ["+where+"]");
    }

    private void updateRelations(SQLiteDatabase db, ArrayList<Relation> relations) {
        for (Relation r : relations) {
            String nameTable = r.nameTable;
            db.delete(nameTable, r.columnIdTarget +" = "+r.idTarget, null);
            for (ContentValues cv : r.values) {
                insert(db, nameTable, cv);
            }
        }
    }

    private SQLiteDatabase getDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db == null)
            throw new RuntimeException("Error connection database");
        return db;
    }
}

package ru.kadei.diaryworkouts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import ru.kadei.diaryworkouts.builder_models.DefaultBuilder;
import ru.kadei.diaryworkouts.models.db.Cortege;
import ru.kadei.diaryworkouts.models.db.Relation;
import ru.kadei.diaryworkouts.threads.BackgroundLogic;
import ru.kadei.diaryworkouts.threads.Task;

/**
 * Created by kadei on 04.09.15.
 */
public class Database {

    private final DBHelper dbHelper;
    private final BackgroundLogic bgLogic;
    private final Queue<DatabaseClient> clients;

    private final Task taskLoadFromDatabase;
    private final Task taskSaveInDatabase;

    public Database(Context context, BackgroundLogic bgLogic) {
        dbHelper = new DBHelper(context);
        this.bgLogic = bgLogic;
        clients = new ArrayDeque<>();

        taskLoadFromDatabase = new Task();
        taskSaveInDatabase = new Task();
        initialTasks();
    }

    private void initialTasks() {
        taskLoadFromDatabase
                .setClient(this)
                .setExecutedMethod("executeLoad", String.class, DefaultBuilder.class)
                .setCompleteMethod("completeLoad")
                .setFailMethod("fail");

        taskSaveInDatabase
                .setClient(this)
                .setExecutedMethod("executeSave", Cortege.class)
                .setCompleteMethod("completeSave")
                .setFailMethod("fail");
    }

    public void load(String query, DefaultBuilder builder, DatabaseClient client) {
        clients.offer(client);
        taskLoadFromDatabase.setParameters(query, builder);
        bgLogic.execute(taskLoadFromDatabase);
    }

    private DefaultBuilder executeLoad(String query, DefaultBuilder builder) {
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

    private void completeLoad(DefaultBuilder builder) {
        clients.poll().loaded(builder);
    }

    private void fail(Throwable throwable) {
        clients.poll().fail(throwable);
    }

    public void save(Cortege cortege, DatabaseClient client) {
        clients.offer(client);
        taskSaveInDatabase.setParameters(cortege);
        bgLogic.execute(taskSaveInDatabase);
    }

    private Cortege executeSave(Cortege cortege) {
        long id = cortege.id;
        String table = cortege.nameTable;
        if(!existsRecordsInTable(table, id))
            insertCortege(cortege);
        else
            updateCortege(cortege);

        return cortege;
    }

    private void completeSave(Cortege cortege) {
        clients.poll().saved(cortege);
    }

    private boolean existsRecordsInTable(String nameTable, long id) {
        SQLiteDatabase db = getDB();
        boolean exists = false;
        try {
            Cursor c = db.rawQuery("SELECT _id FORM " + nameTable + " WHERE _id = " + id, null);
            exists = c.moveToFirst();
            c.close();
        } finally {
            db.close();
        }
        return exists;
    }

    private void insertCortege(Cortege cortege) {
        final SQLiteDatabase db = getDB();
        try {
            db.beginTransaction();

            insert(db, cortege.nameTable, cortege.values);
            insertRelations(db, cortege.relations);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void insert(SQLiteDatabase db, String nameTable, ContentValues values) {
        long id = db.insert(nameTable, null, values);
        if(id == -1)
            throw new RuntimeException("Error insert in table ["+nameTable+"]");
    }

    private void insertRelations(SQLiteDatabase db, ArrayList<Relation> relations) {
        for (Relation r : relations) {
            String nameTable = r.nameTable;
            for(ContentValues values : r.values)
                insert(db, nameTable, values);
        }
    }

    private void updateCortege(Cortege cortege) {
        final SQLiteDatabase db = getDB();
        try {
            db.beginTransaction();

            update(db, cortege.nameTable, "_id = " + cortege.id, cortege.values);
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

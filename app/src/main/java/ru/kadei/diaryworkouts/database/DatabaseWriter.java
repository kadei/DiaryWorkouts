package ru.kadei.diaryworkouts.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by kadei on 03.09.15.
 */
public abstract class DatabaseWriter {

    protected SQLiteDatabase db;

    public void setDB(SQLiteDatabase db) {
        this.db = db;
    }

    public final void writeObjects(ArrayList<?> objects) {
        for (int i = 0, end = objects.size(); i < end; ++i)
            writeObject(objects.get(i));
    }

    public abstract void writeObject(Object object);

    public void forgetReferenceDB() {
        db = null;
    }

    protected final void oops(Object object) {
        throw new RuntimeException("Unexpected object [" + object + "]");
    }

    protected final long save(Cortege cortege, Record record) {
        if (existsRecordsInTable(cortege.nameTable, record.id))
            updateCortege(cortege, record.id);
        else
            record.id = insertCortege(cortege);
        return record.id;
    }

    private boolean existsRecordsInTable(String nameTable, long id) {
        Cursor c = db.rawQuery("SELECT _id FROM " + nameTable + " WHERE _id = " + id, null);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    private long insertCortege(Cortege cortege) {
        long id = insert(cortege.nameTable, cortege.values);
        insertRelations(cortege.relations);
        return id;
    }

    private long insert(String nameTable, ContentValues values) {
        long id = db.insert(nameTable, null, values);
        if (id == -1)
            throw new RuntimeException("Error insert in table [" + nameTable + "]");
        return id;
    }

    private void insertRelations(ArrayList<Relation> relations) {
        for (Relation r : relations) {
            String nameTable = r.nameTable;
            for (ContentValues values : r.values)
                insert(nameTable, values);
        }
    }

    private void updateCortege(Cortege cortege, long id) {
        update(cortege.nameTable, "_id = " + id, cortege.values);
        updateRelations(cortege.relations);
    }

    private void update(String nameTable, String where, ContentValues values) {
        int rows = db.update(nameTable, values, where, null);
        if (rows == 0)
            throw new RuntimeException("Error update int table [" + nameTable + "], where [" + where + "]");
    }

    private void updateRelations(ArrayList<Relation> relations) {
        final SQLiteDatabase db = this.db;
        for (Relation r : relations) {
            String nameTable = r.nameTable;
            db.delete(nameTable, r.columnIdTarget + " = " + r.idTarget, null);
            for (ContentValues cv : r.values) {
                insert(nameTable, cv);
            }
        }
    }
}

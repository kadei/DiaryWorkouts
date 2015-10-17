package ru.kadei.diaryworkouts.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static java.lang.String.valueOf;

/**
 * Created by kadei on 03.09.15.
 */
public abstract class DatabaseWriter extends SQLCreator {

    protected Record record;
    protected SQLiteDatabase db;

    public void setRecord(Record record) {
        this.record = record;
    }

    public Record getRecord() {
        final Record tmp = record;
        record = null;
        return tmp;
    }

    public void setDB(SQLiteDatabase db) {
        this.db = db;
    }

    public final void writeObjects(ArrayList<Record> objects) {
        for (int i = 0, end = objects.size(); i < end; ++i) {
            setRecord(objects.get(i));
            writeObject();
        }
    }

    public abstract void writeObject();

    public void forgetReferenceDB() {
        db = null;
    }

    protected final void oops(Object object) {
        throw new RuntimeException("Unexpected object [" + object + "]");
    }

    protected final long save(Cortege cortege, Record record) {
        if (existsRecordsInTable(cortege.nameTable, record.id)) {
            String query = query("_id = ").append(record.id).toString();
            update(cortege.nameTable, cortege.values, query);
        } else {
            record.id = insertInto(cortege.nameTable, cortege.values);
        }
        return record.id;
    }

    protected final boolean existsRecordsInTable(String nameTable, long id) {
        return existsColumnIn(nameTable, "_id", valueOf(id));
    }

    protected final boolean existsColumnIn(String table, String column, String value) {
        String query = query("SELECT ").append(column).append(" FROM ").append(table)
                .append(" WHERE ").append(column).append(" = ").append(value).toString();

        Cursor c = db.rawQuery(query, null);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    protected final long insertInto(String table, ContentValues values) {
        long id = db.insert(table, null, values);
        if (id == -1)
            throw new RuntimeException("Error insert in table [" + table + "]");
        return id;
    }

    protected final void update(String table, ContentValues values, String where) {
        int rows = db.update(table, values, where, null);
        if (rows == 0)
            throw new RuntimeException("Error update int table [" + table + "]");
    }
}

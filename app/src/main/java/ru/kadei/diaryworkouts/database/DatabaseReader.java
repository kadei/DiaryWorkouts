package ru.kadei.diaryworkouts.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by kadei on 01.09.15.
 */
public abstract class DatabaseReader extends SQLCreator {

    private String query;

    protected SQLiteDatabase db;
    protected ArrayList<?> objects;

    public DatabaseReader(StringBuilder sb) {
        super(sb);
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public final void readObjects(){
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            objects = buildFromCursor(c);
        }
        c.close();
    }

    public abstract ArrayList<?> buildFromCursor(Cursor c);

    public final ArrayList<?> getObjects() {
        query = null;

        if(objects == null)
            return new ArrayList<>();
        else {
            ArrayList<?> tmp = objects;
            objects = null;
            return tmp;
        }
    }

    public final void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public final void forgetReferenceDB() {
        db = null;
    }

    public String getQuery() {
        return query;
    }
}

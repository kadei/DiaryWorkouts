package ru.kadei.diaryworkouts.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by kadei on 01.09.15.
 */
public abstract class DatabaseReader extends SQLCreator {

    protected SQLiteDatabase db;
    protected ArrayList<?> objects;

    public final void readObjects(String query){
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            objects = buildFromCursor(c);
        }
        c.close();
    }

    public abstract ArrayList<?> buildFromCursor(Cursor c);

    public final ArrayList<?> getObjects() {
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
}

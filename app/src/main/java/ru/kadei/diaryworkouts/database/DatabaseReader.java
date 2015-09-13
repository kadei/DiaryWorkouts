package ru.kadei.diaryworkouts.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by kadei on 01.09.15.
 */
public abstract class DatabaseReader extends SQLCreator {

    protected SQLiteDatabase db;
    protected ArrayList<?> objects;

    public abstract void readObjects(String query);

    public ArrayList<?> getObjects() {
        ArrayList<?> tmp = objects;
        objects = null;
        return tmp;
    }

    public final void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public final void forgetReferenceDB() {
        db = null;
    }
}

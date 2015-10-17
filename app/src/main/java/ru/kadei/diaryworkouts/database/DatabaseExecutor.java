package ru.kadei.diaryworkouts.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kadei on 10.10.15.
 */
public abstract class DatabaseExecutor extends SQLCreator {

    protected Object object;
    protected SQLiteDatabase db;

    public DatabaseExecutor() {
        super(createStringBuilder());
    }

    public abstract void execute();

    public void setDB(SQLiteDatabase db) {
        this.db = db;
    }

    public void forgetReferenceDB() {
        db = null;
    }

    public final Object getResult() {
        Object tmp = object;
        object = null;
        return tmp;
    }
}

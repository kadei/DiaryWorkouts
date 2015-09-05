package ru.kadei.diaryworkouts.builder_models;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by kadei on 01.09.15.
 */
public abstract class DefaultBuilder {

    protected SQLiteDatabase db;
    protected ArrayList<?> objects;
    private final StringBuilder sb = new StringBuilder(256);

    public abstract void buildObjects(String query);

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

    protected final StringBuilder getClearStringBuilder() {
        sb.delete(0, sb.length());
        return sb;
    }
}

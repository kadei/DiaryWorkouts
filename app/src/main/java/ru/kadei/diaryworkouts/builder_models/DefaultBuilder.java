package ru.kadei.diaryworkouts.builder_models;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kadei on 01.09.15.
 */
public class DefaultBuilder {

    protected final SQLiteDatabase db;
    private final StringBuilder sb = new StringBuilder(256);

    public DefaultBuilder(SQLiteDatabase db) {
        this.db = db;
    }

    protected final StringBuilder getClearStringBuilder() {
        sb.delete(0, sb.length());
        return sb;
    }
}

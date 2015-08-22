package ru.kadei.diaryworkouts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kadei on 22.08.15.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE __entities (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "entity TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

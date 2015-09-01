package ru.kadei.diaryworkouts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kadei on 31.08.15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "data.db";
    public static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        buildSchema(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void buildSchema(SQLiteDatabase db) {

        // base tables
        db.execSQL("CREATE TABLE `descriptionProgram` (" +
                "`_id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`name` TEXT," +
                "`description` TEXT);");

        db.execSQL("CREATE TABLE `descriptionWorkout` (" +
                "`_id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`name` TEXT," +
                "`description` TEXT);");

        db.execSQL("CREATE TABLE `descriptionExercise` (" +
                "`_id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`name` TEXT," +
                "`description` TEXT," +
                "`type` INTEGER DEFAULT 0," +
                "`measureSpec` INTEGER DEFAULT 0," +
                "`muscleGroupSpec` INTEGER DEFAULT 0);");

        // auxiliary tables
        db.execSQL("CREATE TABLE `listDescriptionWorkout` (" +
                "`idProgram` INTEGER," +
                "`idWorkout` INTEGER," +
                "`orderInList` INTEGER)");

        db.execSQL("CREATE TABLE `listDescriptionExercise` (" +
                "`idWorkout` INTEGER," +
                "`idExercise` INTEGER," +
                "`orderInList` INTEGER)");

        db.execSQL("CREATE TABLE `listContentSuperset` (" +
                "`idSuperset` INTEGER," +
                "`idExercise` INTEGER," +
                "`orderInList` INTEGER)");
    }
}

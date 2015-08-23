package ru.kadei.diaryworkouts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.schema.Schema;

/**
 * Created by kadei on 22.08.15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TABLE_CREATED_SCHEMAS = "__entities";
    private static final String COLUMN_NAME = "__name";
    private static final String COLUMN_SCHEMA = "__schema";

    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                TABLE_CREATED_SCHEMAS +
                "(" +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SCHEMA + "  BLOB" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Schema> getCreatedSchemas(SQLiteDatabase db) {
        ArrayList<Schema> schemas = new ArrayList<>(4);
        Cursor cursor = db.rawQuery("SELECT "+ COLUMN_SCHEMA +" FROM "+TABLE_CREATED_SCHEMAS, null);
        if(cursor.moveToFirst())
            fillListSchemasFromCursor(schemas, cursor);

        return schemas;
    }

    private void fillListSchemasFromCursor(ArrayList<Schema> schemas, Cursor cursor) {
        try {
            int index = cursor.getColumnIndex(COLUMN_SCHEMA);
            schemas.add(deserializeSchema(cursor.getBlob(index)));
        } finally {
            cursor.close();
        }
    }

    private Schema deserializeSchema(byte[] source) {
        Schema schema;
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(source));
            schema = (Schema) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        return schema;
    }

    public void createSchema(SQLiteDatabase db, Schema schema) {
        db.beginTransaction();
        try {
            db.execSQL(schema.toString());

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, schema.nameEntity);
            cv.put(COLUMN_SCHEMA, serializeSchema(schema));
            db.insert(TABLE_CREATED_SCHEMAS, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
    }

    private byte[] serializeSchema(Schema schema) {
        byte[] result;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(1048576);
            ObjectOutputStream oos = new ObjectOutputStream(buffer);
            oos.writeObject(schema);
            oos.flush();
            result = buffer.toByteArray();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
}

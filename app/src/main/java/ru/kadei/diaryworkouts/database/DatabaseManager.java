package ru.kadei.diaryworkouts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.schema.Schema;
import ru.kadei.diaryworkouts.database.schema.SchemaBuilder;

import static ru.kadei.diaryworkouts.database.ParserOfEntities.newParser;
import static ru.kadei.diaryworkouts.database.utils.ArrayUtils.containsDuplicate;

/**
 * Created by kadei on 21.08.15.
 */
public class DatabaseManager {

    final Context context;
    final DBHelper dbHelper;
    final SchemaBuilder schemaBuilder;

    ArrayList<String> nameEntities;

    public DatabaseManager(@NonNull Context context, @NonNull String nameFileEntities, @NonNull String nameDatabase)
            throws IOException, XmlPullParserException, ClassNotFoundException {

        this.context = context;
        dbHelper = new DBHelper(context, nameDatabase, 1);
        schemaBuilder = new SchemaBuilder(this);

        loadAndCheckNameEntities(nameFileEntities);
        checkOrCreateSchemas();
    }

    private void loadAndCheckNameEntities(String nameFileEntities)
            throws XmlPullParserException, IOException {

        nameEntities = newParser(context).getNameEntitiesFrom(nameFileEntities);
        if(!nameEntities.isEmpty()) {
            int posDuplicate = containsDuplicate(nameEntities);
            if(posDuplicate != -1)
                throw new RuntimeException("File ["+ nameFileEntities +"]" +
                        " contains duplicates ["+nameEntities.get(posDuplicate)+"]");
        }
        else
            throw new RuntimeException("No registered entities, check file ["+ nameFileEntities +"]");
    }

    private void checkOrCreateSchemas() throws ClassNotFoundException {
        ArrayList<Schema> schemaEntities = getSchemaEntities();
        ArrayList<Schema> createdSchemas = getCreatedSchemas();
        if(!schemaEntities.equals(createdSchemas)) {

        }
    }

    private ArrayList<Schema> getSchemaEntities() throws ClassNotFoundException {
        final SchemaBuilder builder = schemaBuilder;
        ArrayList<Schema> schemas = new ArrayList<>(nameEntities.size());
        for(String nameEntity : nameEntities)
            schemas.add(builder.buildSchemaFor(Class.forName(nameEntity)));
        return schemas;
    }

    private ArrayList<Schema> getCreatedSchemas() {
        SQLiteDatabase db = getDatabase();
        ArrayList<Schema> schemas = dbHelper.getCreatedSchemas(db);
        db.close();
        return schemas;
    }

    private SQLiteDatabase getDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db == null)
            throw new RuntimeException("Error open connection to ["+dbHelper.getDatabaseName()+"]");
        return db;
    }

    public boolean isEntity(Class c) {
        return nameEntities.contains(c.getName());
    }

    public ArrayList<String> getNameEntities() {
        return nameEntities;
    }

    public SchemaBuilder getSchemaBuilder() {
        return schemaBuilder;
    }
}

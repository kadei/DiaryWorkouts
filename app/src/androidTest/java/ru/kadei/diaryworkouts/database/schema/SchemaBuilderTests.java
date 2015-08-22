package ru.kadei.diaryworkouts.database.schema;

import junit.framework.Assert;

import ru.kadei.diaryworkouts.ApplicationTest;
import ru.kadei.diaryworkouts.database.DatabaseManager;
import ru.kadei.diaryworkouts.models.entities.Entity1;
import ru.kadei.diaryworkouts.models.entities.EntityWithAnnotation;
import ru.kadei.diaryworkouts.models.entities.EntityWithIgnoreFields;

/**
 * Created by kadei on 22.08.15.
 */
public class SchemaBuilderTests extends ApplicationTest {

    DatabaseManager db;
    SchemaBuilder schemaBuilder;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        db = new DatabaseManager(getContext(), "");
        schemaBuilder = new SchemaBuilder(db);
    }

    public void testBuildSchema() throws Exception {
        Schema schema = schemaBuilder.buildSchemaFor(Entity1.class);
        Assert.assertEquals(schema.toString(), "CREATE TABLE Entity1 " +
                "(" +
                "aBoolean INTEGER, " +
                "aByte INTEGER, " +
                "aChar TEXT, " +
                "aDouble REAL, " +
                "aFloat REAL, " +
                "aLong INTEGER, " +
                "aShort INTEGER, " +
                "anInt INTEGER" +
                        ");");
    }

    public void testBuildSchemaWithAnnotation() throws Exception {
        Schema schema = schemaBuilder.buildSchemaFor(EntityWithAnnotation.class);
        Assert.assertEquals(schema.toString(), "CREATE TABLE table_for_entity (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "isExists INTEGER, " +
                "summa REAL);");

    }

    public void testBuildSchemaWithIgnoreFields() throws Exception {
        Schema schema = schemaBuilder.buildSchemaFor(EntityWithIgnoreFields.class);
        Assert.assertEquals(schema.toString(), "CREATE TABLE ignoreFields (" +
                "distance REAL, " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "xPosition REAL" +
                ");");
    }
}

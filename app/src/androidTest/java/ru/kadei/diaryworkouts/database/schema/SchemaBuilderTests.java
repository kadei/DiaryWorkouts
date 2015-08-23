package ru.kadei.diaryworkouts.database.schema;

import junit.framework.Assert;

import ru.kadei.diaryworkouts.ApplicationTest;
import ru.kadei.diaryworkouts.database.DatabaseManager;
import ru.kadei.diaryworkouts.models.entities.Entity1;
import ru.kadei.diaryworkouts.models.entities.EntityWithAnnotation;
import ru.kadei.diaryworkouts.models.entities.EntityWithDuplicateAnnotation;
import ru.kadei.diaryworkouts.models.entities.EntityWithIgnoreFields;
import ru.kadei.diaryworkouts.models.entities.EntityWithSerializable;

/**
 * Created by kadei on 22.08.15.
 */
public class SchemaBuilderTests extends ApplicationTest {

    SchemaBuilder builder;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DatabaseManager db = new DatabaseManager(getContext(),
                "entitiesWithOneEntity.xml",
                "schemaBuilderTests.db");
        builder = db.getSchemaBuilder();
    }

    public void testBuildSchema() throws Exception {
        Schema schema = builder.buildSchemaFor(Entity1.class);
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
        Schema schema = builder.buildSchemaFor(EntityWithAnnotation.class);
        Assert.assertEquals(schema.toString(), "CREATE TABLE table_for_entity (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "isExists INTEGER, " +
                "summa REAL);");

    }

    public void testBuildSchemaWithIgnoreFields() throws Exception {
        Schema schema = builder.buildSchemaFor(EntityWithIgnoreFields.class);
        Assert.assertEquals(schema.toString(), "CREATE TABLE ignoreFields (" +
                "distance REAL, " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "xPosition REAL" +
                ");");
    }

    public void testBuildSchemaWithDuplicateAnnotation() throws Exception {
        try {
            Schema schema = builder.buildSchemaFor(EntityWithDuplicateAnnotation.class);
            fail("Should throws exception about [duplication name column]");
        } catch (Exception e) {
            // successfully
        }
    }

    public void testBuildSchemaWithSerializable() throws Exception {
        Schema schema = builder.buildSchemaFor(EntityWithSerializable.class);
        Assert.assertEquals(schema.toString(), "CREATE TABLE EntityWithSerializable (" +
                "name TEXT, " +
                "serial BLOB, " +
                "x REAL, " +
                "y REAL);");
    }
}

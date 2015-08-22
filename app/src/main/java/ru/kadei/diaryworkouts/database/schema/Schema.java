package ru.kadei.diaryworkouts.database.schema;

/**
 * Created by kadei on 22.08.15.
 */
public class Schema {

    public String nameTable;
    public SchemaRecord schemaRecord;

    @Override
    public String toString() {
        return "CREATE TABLE " + nameTable + " (" + schemaRecord.toString() + ");";
    }
}

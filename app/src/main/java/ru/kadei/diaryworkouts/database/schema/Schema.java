package ru.kadei.diaryworkouts.database.schema;

import java.io.Serializable;

/**
 * Created by kadei on 22.08.15.
 */
public class Schema implements Serializable {

    public String nameEntity;
    public String nameTable;
    public SchemaCortege schemaCortege;

    @Override
    public String toString() {
        return "CREATE TABLE " + nameTable + " (" + schemaCortege.toString() + ");";
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof Schema && allElementsEquals((Schema) o);
    }

    private boolean allElementsEquals(Schema schema) {
        return nameEntity.equals(schema.nameEntity)
                && nameTable.equals(schema.nameTable)
                && schemaCortege.equals(schema.schemaCortege);
    }
}

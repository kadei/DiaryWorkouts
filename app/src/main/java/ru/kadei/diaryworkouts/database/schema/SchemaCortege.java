package ru.kadei.diaryworkouts.database.schema;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kadei on 22.08.15.
 */
public class SchemaCortege implements Serializable {

    public final ArrayList<String> nameFields;
    public final ArrayList<String> nameColumns;
    public final ArrayList<String> typeColumns;

    public SchemaCortege() {
        nameFields = new ArrayList<>(4);
        nameColumns = new ArrayList<>(4);
        typeColumns = new ArrayList<>(4);
    }

    public void addElement(String nameField, String nameColumn, String typeColumn) {
        checkDuplicateColumn(nameColumn);
        nameFields.add(nameField);
        nameColumns.add(nameColumn);
        typeColumns.add(typeColumn);
    }

    private void checkDuplicateColumn(String nameColumn) {
        if(nameColumns.contains(nameColumn))
            throw new RuntimeException("Duplicate name of column ["+nameColumn+"]");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        for(int i = 0, end = nameColumns.size(); i < end; ++i) {
            sb.append(nameColumns.get(i)).append(" ").append(typeColumns.get(i));
            if(i < end - 1)
                sb.append(", ");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof SchemaCortege && allElementsEquals((SchemaCortege)o);
    }

    private boolean allElementsEquals(SchemaCortege sc) {
        return nameFields.equals(sc.nameFields)
                && nameColumns.equals(sc.nameColumns)
                && typeColumns.equals(sc.typeColumns);
    }
}

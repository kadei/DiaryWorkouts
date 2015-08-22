package ru.kadei.diaryworkouts.database.schema;

import java.util.ArrayList;

/**
 * Created by kadei on 22.08.15.
 */
public class SchemaRecord {

    public final ArrayList<String> nameFields;
    public final ArrayList<String> nameColumns;
    public final ArrayList<String> typeColumns;

    public SchemaRecord() {
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
        final ArrayList<String> list = nameColumns;
        for(int i = 0, end = list.size(); i < end; ++i)
            if(nameColumn.equals(list.get(i)))
                throw new RuntimeException("Duplicate name of column [" + nameColumn + "]");
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
}

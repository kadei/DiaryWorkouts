package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.DatabaseWriter;
import ru.kadei.diaryworkouts.database.Record;

/**
 * Created by kadei on 13.09.15.
 */
public abstract class DescriptionWriter extends DatabaseWriter {

    /**
     * @param tableInfo [0]-nameTable, [1]-column id target, [2]-column id records, [3]-order in list
     */
    protected final void saveRelations(long idTarget, ArrayList<? extends Record> records, String[] tableInfo) {
        removeRelationsWith(tableInfo, idTarget);

        final ContentValues cv = new ContentValues(3);
        int count = 0;
        for (Record record : records) {
            cv.put(tableInfo[1], idTarget);
            cv.put(tableInfo[2], record.id);
            cv.put(tableInfo[3], count++);

            insertInto(tableInfo[0], cv);
        }
        cv.clear();
    }

    private void removeRelationsWith(String[] tableInfo, long id) {
        String query = query(tableInfo[1]).append(" = ").append(id).toString();
        db.delete(tableInfo[0], query, null);
    }
}

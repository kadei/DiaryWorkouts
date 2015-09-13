package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import ru.kadei.diaryworkouts.database.Cortege;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;

/**
 * Created by kadei on 03.09.15.
 */
public class ProgramWriter extends DescriptionWriter {

    @Override
    public void writeObject(Object object) {
        if(object instanceof DescriptionProgram)
            saveProgram((DescriptionProgram) object);
        else
            oops(object);
    }

    private void saveProgram(DescriptionProgram program) {
        final Cortege cortege = new Cortege();
        cortege.nameTable = "descriptionProgram";

        ContentValues cv = new ContentValues(2);
        cv.put("name", program.name);
        cv.put("description", program.description);
        cortege.values = cv;

        save(cortege, program);
        saveRelations(program.id, program.workouts, TABLE_INFO);
    }

    private static final String[] TABLE_INFO = new String[] {
            "listDescriptionWorkout", "idProgram", "idWorkout", "orderInList"
    };
}

package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.DatabaseWriter;
import ru.kadei.diaryworkouts.database.Cortege;
import ru.kadei.diaryworkouts.database.Relation;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 03.09.15.
 */
public class ProgramWriter extends DatabaseWriter {

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

        cortege.relations.add(buildRelationWorkoutsAnd(program));

        save(cortege, program);
    }

    private Relation buildRelationWorkoutsAnd(DescriptionProgram program) {
        final ArrayList<DescriptionWorkout> workouts = program.workouts;
        final int size = workouts.size();
        final ArrayList<ContentValues> values = new ArrayList<>(size);

        final long idPrg = program.id;
        for(int pos = 0; pos < size; ++pos) {
            long idWorkout = workouts.get(pos).id;

            ContentValues cv = new ContentValues(3);
            cv.put("idProgram", idPrg);
            cv.put("idWorkout", idWorkout);
            cv.put("orderInList", pos);

            values.add(cv);
        }

        final Relation relation = new Relation();
        relation.nameTable = "listDescriptionWorkout";
        relation.idTarget = idPrg;
        relation.columnIdTarget = "idProgram";
        relation.values = values;
        return relation;
    }
}

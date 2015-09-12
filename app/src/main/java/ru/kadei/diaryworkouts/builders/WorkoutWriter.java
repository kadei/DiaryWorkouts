package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.DatabaseWriter;
import ru.kadei.diaryworkouts.database.Cortege;
import ru.kadei.diaryworkouts.database.Relation;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 04.09.15.
 */
public class WorkoutWriter extends DatabaseWriter {
    @Override
    public void writeObject(Object object) {
        if(object instanceof DescriptionWorkout)
            saveWokout((DescriptionWorkout) object);
        else
            oops(object);
    }

    private void saveWokout(DescriptionWorkout workout) {
        final Cortege cortege = new Cortege();
        cortege.nameTable = "descriptionWorkout";

        ContentValues cv = new ContentValues(2);
        cv.put("name", workout.name);
        cv.put("description", workout.description);
        cortege.values = cv;

        cortege.relations.add(buildRelationExercisesAnd(workout));

        save(cortege, workout);
    }

    private Relation buildRelationExercisesAnd(DescriptionWorkout workout) {
        final ArrayList<DescriptionExercise> exercises = workout.exercises;
        final int size = exercises.size();
        final ArrayList<ContentValues> values = new ArrayList<>(size);

        final long idWorkout = workout.id;
        for (int pos = 0; pos < size; ++pos) {
            long idExercise = exercises.get(pos).id;

            ContentValues cv = new ContentValues(3);
            cv.put("idWorkout", idWorkout);
            cv.put("idExercise", idExercise);
            cv.put("orderInList", pos);

            values.add(cv);
        }

        final Relation relation = new Relation();
        relation.nameTable = "listDescriptionExercise";
        relation.idTarget = idWorkout;
        relation.columnIdTarget = "idWorkout";
        relation.values = values;
        return relation;
    }
}

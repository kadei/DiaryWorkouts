package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import ru.kadei.diaryworkouts.database.Cortege;
import ru.kadei.diaryworkouts.database.Record;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 04.09.15.
 */
public class WorkoutWriter extends DescriptionWriter {
    @Override
    public void writeObject(Record object) {
        if(object instanceof DescriptionWorkout)
            saveWorkout((DescriptionWorkout) object);
        else
            oops(object);
    }

    private void saveWorkout(DescriptionWorkout workout) {
        final Cortege cortege = new Cortege();
        cortege.nameTable = "descriptionWorkout";

        ContentValues cv = new ContentValues(2);
        cv.put("name", workout.name);
        cv.put("description", workout.description);
        cortege.values = cv;

        save(cortege, workout);
        saveRelations(workout.id, workout.exercises, TABLE_INFO);
    }

    private static final String[] TABLE_INFO = new String[] {
            "listDescriptionExercise", "idWorkout", "idExercise", "orderInList"
    };
}

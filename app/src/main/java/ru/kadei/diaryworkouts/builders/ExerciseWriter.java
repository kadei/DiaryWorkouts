package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import ru.kadei.diaryworkouts.database.Cortege;
import ru.kadei.diaryworkouts.database.DatabaseWriter;
import ru.kadei.diaryworkouts.database.Record;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionStandardExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionSupersetExercise;

/**
 * Created by kadei on 06.09.15.
 */
public class ExerciseWriter extends DescriptionWriter {

    @Override
    public void writeObject(Record object) {
        if (object instanceof DescriptionExercise)
            saveExercise((DescriptionExercise) object);
        else
            oops(object);
    }

    private void saveExercise(DescriptionExercise exercise) {
        final Cortege cortege = new Cortege();
        cortege.nameTable = "descriptionExercise";

        ContentValues cv = new ContentValues(5);
        cv.put("name", exercise.name);
        cv.put("description", exercise.description);
        cv.put("type", exercise.type);
        cv.put("measureSpec", exercise.getMeasureSpec());
        cv.put("muscleGroupSpec", exercise.getMuscleGroupSpec());
        cortege.values = cv;

        save(cortege, exercise);
        if (exercise.isSuperset()) {
            DescriptionSupersetExercise superset = (DescriptionSupersetExercise) exercise;
            saveRelations(superset.id, superset.exercises, TABLE_INFO);
        }
    }

    private static final String[] TABLE_INFO = new String[] {
            "listContentSuperset", "idSuperset", "idExercise", "orderInList"
    };
}

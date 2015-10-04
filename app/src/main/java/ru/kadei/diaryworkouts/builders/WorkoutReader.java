package ru.kadei.diaryworkouts.builders;

import android.database.Cursor;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 01.09.15.
 */
public class WorkoutReader extends DescriptionReader {

    private DescriptionReader exerciseReader;

    public WorkoutReader(BufferDescriptions bufferDescriptions,
                         ExerciseReader exerciseReader) {
        super(bufferDescriptions);
        this.exerciseReader = exerciseReader;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<?> buildFromCursor(Cursor c) {
        final DescriptionReader reader = exerciseReader;
        final BufferDescriptions buffer = bufferDescriptions;
        final ArrayList<DescriptionWorkout> list = new ArrayList<>(c.getCount());

        final int indexID = c.getColumnIndex("_id");
        final int indexName = c.getColumnIndex("name");
        final int indexDescription = c.getColumnIndex("description");

        reader.setDb(db);
        do {
            long id = c.getLong(indexID);
            DescriptionWorkout dw = buffer.getWorkout(id);
            if (dw == null) {
                dw = new DescriptionWorkout();
                dw.id = id;
                dw.name = c.getString(indexName);
                dw.description = c.getString(indexDescription);

                reader.readObjects(createQueryFor(dw.id));
                dw.exercises = (ArrayList<DescriptionExercise>) reader.getObjects();

                buffer.addWorkout(dw);
            }

            list.add(dw);
        } while (c.moveToNext());
        reader.forgetReferenceDB();

        return list;
    }

    private String createQueryFor(long id) {
        return query("SELECT descriptionExercise._id, descriptionExercise.name, descriptionExercise.description, " +
                "descriptionExercise.type, descriptionExercise.measureSpec, descriptionExercise.muscleGroupSpec " +
                "FROM descriptionExercise, listDescriptionExercise " +
                "WHERE listDescriptionExercise.idWorkout = ").append(id).append(
                " AND descriptionExercise._id = listDescriptionExercise.idExercise " +
                        "ORDER BY listDescriptionExercise.orderInList;").toString();
    }
}

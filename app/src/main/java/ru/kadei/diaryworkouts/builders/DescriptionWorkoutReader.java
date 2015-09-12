package ru.kadei.diaryworkouts.builders;

import android.database.Cursor;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.managers.BufferDescriptions;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 01.09.15.
 */
public class DescriptionWorkoutReader extends DescriptionReader {

    private DescriptionReader exerciseReader;

    public DescriptionWorkoutReader(BufferDescriptions bufferDescriptions,
                                    DescriptionExerciseReader exerciseReader) {
        super(bufferDescriptions);
        this.exerciseReader = exerciseReader;
    }

    @Override
    public void readObjects(String query) {
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            objects = buildList(c);
        }
        c.close();
    }

    @SuppressWarnings("unchecked")
    private ArrayList<DescriptionWorkout> buildList(Cursor c) {
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

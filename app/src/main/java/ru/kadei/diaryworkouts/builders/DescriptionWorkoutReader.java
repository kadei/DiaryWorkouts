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

    private DescriptionReader exerciseBuilder;

    public DescriptionWorkoutReader(BufferDescriptions bufferDescriptions,
                                    DescriptionReader exerciseBuilder) {
        super(bufferDescriptions);
        this.exerciseBuilder = exerciseBuilder;
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
        final DescriptionReader builder = exerciseBuilder;
        final BufferDescriptions buffer = bufferDescriptions;
        final ArrayList<DescriptionWorkout> list = new ArrayList<>(c.getCount());

        final int indexID = c.getColumnIndex("_id");
        final int indexName = c.getColumnIndex("name");
        final int indexDescription = c.getColumnIndex("description");

        builder.setDb(db);
        do {
            long id = c.getLong(indexID);
            DescriptionWorkout dw = buffer.getWorkout(id);
            if (dw == null) {
                dw = new DescriptionWorkout();
                dw.id = id;
                dw.name = c.getString(indexName);
                dw.description = c.getString(indexDescription);

                builder.readObjects(createQueryFor(dw.id));
                dw.exercises = (ArrayList<DescriptionExercise>) builder.getObjects();

                buffer.addWorkout(dw);
            }

            list.add(dw);
        } while (c.moveToNext());
        builder.forgetReferenceDB();

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

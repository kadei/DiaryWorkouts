package ru.kadei.diaryworkouts.builder_models;

import android.database.Cursor;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 01.09.15.
 */
public class DescriptionWorkoutBuilder extends DescriptionBuilder {

    private DescriptionBuilder exerciseBuilder;

    public DescriptionWorkoutBuilder(BufferDescriptions bufferDescriptions,
                                     DescriptionBuilder exerciseBuilder) {
        super(bufferDescriptions);
        this.exerciseBuilder = exerciseBuilder;
    }

    @Override
    public void buildObjects(String query) {
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()) {
            objects = buildList(c);
        }
        c.close();
    }

    @SuppressWarnings("unchecked")
    private ArrayList<DescriptionWorkout> buildList(Cursor c) {
        final DescriptionBuilder builder = exerciseBuilder;
        final BufferDescriptions buffer = bufferDescriptions;
        final ArrayList<DescriptionWorkout> list = new ArrayList<>(c.getCount());

        final int indexID = c.getColumnIndex("_id");
        final int indexName = c.getColumnIndex("name");
        final int indexDescription = c.getColumnIndex("description");

        builder.setDb(db);
        do {
            long id = c.getLong(indexID);
            DescriptionWorkout dw = buffer.getWorkout(id);
            if(dw == null) {
                dw = new DescriptionWorkout();
                dw.id = id;
                dw.name = c.getString(indexName);
                dw.description = c.getString(indexDescription);

                builder.buildObjects(createQueryFor(dw.id));
                dw.exercises = (ArrayList<DescriptionExercise>) builder.getObjects();

                buffer.addWorkout(dw);
            }

            list.add(dw);
        } while(c.moveToNext());
        return list;
    }

    private String createQueryFor(long id) {
        StringBuilder sb = getClearStringBuilder();
        sb.append("SELECT descriptionExercise._id, descriptionExercise.name, descriptionExercise.description, " +
                "descriptionExercise.type, descriptionExercise.measureSpec, descriptionExercise.muscleGroupSpec " +
                "FROM descriptionExercise, listDescriptionExercise " +
                "WHERE listDescriptionExercise.idWorkout = ").append(id).append(
                " AND descriptionExercise._id = listDescriptionExercise.idExercise " +
                "ORDER BY listDescriptionExercise.orderInList;");
        return sb.toString();
    }
}

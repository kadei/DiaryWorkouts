package ru.kadei.diaryworkouts.builder_models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 01.09.15.
 */
public class DescriptionWorkoutBuilder extends DescriptionBuilder {

    private DescriptionBuilder exerciseBuilder;

    public DescriptionWorkoutBuilder(SQLiteDatabase db, DescriptionBuilder exerciseBuilder) {
        super(db);
        this.exerciseBuilder = exerciseBuilder;
    }

    @Override
    public void buildDescriptionWorkout(String query) {
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()) {
            descriptions = buildList(c);
        }
        c.close();
    }

    @SuppressWarnings("unchecked")
    private ArrayList<DescriptionWorkout> buildList(Cursor c) {
        final DescriptionBuilder builder = exerciseBuilder;
        final ArrayList<DescriptionWorkout> list = new ArrayList<>(c.getCount());

        final int indexID = c.getColumnIndex("_id");
        final int indexName = c.getColumnIndex("name");
        final int indexDescription = c.getColumnIndex("description");

        do {
            DescriptionWorkout dw = new DescriptionWorkout();
            dw.id = c.getLong(indexID);
            dw.name = c.getString(indexName);
            dw.description = c.getString(indexDescription);

            builder.buildDescriptionExercise(createQueryFor(dw.id));
            dw.exercises = (ArrayList<DescriptionExercise>) builder.get();

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

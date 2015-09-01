package ru.kadei.diaryworkouts.builder_models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 30.08.15.
 */
public class DescriptionProgramBuilder extends DescriptionBuilder {

    private final DescriptionBuilder workoutBuilder;

    public DescriptionProgramBuilder(SQLiteDatabase db, DescriptionBuilder workoutBuilder) {
        super(db);
        this.workoutBuilder = workoutBuilder;
    }

    @Override
    public void buildDescriptionProgram(String query) {
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()) {
            descriptions = buildList(c);
        }
        c.close();
    }

    @SuppressWarnings(value = "unchecked")
    private ArrayList<DescriptionProgram> buildList(Cursor c) {
        final DescriptionBuilder builder = workoutBuilder;
        final ArrayList<DescriptionProgram> list = new ArrayList<>(c.getCount());

        final int indexID = c.getColumnIndex("_id");
        final int indexName = c.getColumnIndex("name");
        final int indexDescription = c.getColumnIndex("description");

        do {
            DescriptionProgram dp = new DescriptionProgram();
            dp.id = c.getLong(indexID);
            dp.name = c.getString(indexName);
            dp.description = c.getString(indexDescription);

            builder.buildDescriptionWorkout(createQueryFor(dp.id));
            dp.workouts = (ArrayList<DescriptionWorkout>) builder.get();

            list.add(dp);
        } while (c.moveToNext());
        return list;
    }

    private String createQueryFor(long id) {
        StringBuilder sb = getClearStringBuilder();
        sb.append("SELECT descriptionWorkout._id, descriptionWorkout.name, descriptionWorkout.description " +
                "FROM descriptionWorkout, listDescriptionWorkout " +
                "WHERE listDescriptionWorkout.idProgram = ").append(id).append(
                " AND descriptionWorkout._id = listDescriptionWorkout.idWorkout " +
                        "ORDER BY listDescriptionWorkout.orderInList;");
        return sb.toString();
    }
}

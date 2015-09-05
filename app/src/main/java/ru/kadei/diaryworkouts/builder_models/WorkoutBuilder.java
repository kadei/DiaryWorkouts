package ru.kadei.diaryworkouts.builder_models;

import android.database.Cursor;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.Workout;

/**
 * Created by kadei on 01.09.15.
 */
public class WorkoutBuilder extends DefaultBuilder {

    private final DescriptionBuilder programBuilder;

    public WorkoutBuilder(DescriptionBuilder programBuilder) {
        this.programBuilder = programBuilder;
    }

    @Override
    public void buildObjects(String query) {
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()) {
            objects = buildList(c);
        }
        c.close();
    }

    private ArrayList<Workout> buildList(Cursor c) {
        final ArrayList<Workout> list = new ArrayList<>(c.getCount());

        final int indexID = c.getColumnIndex("_id");
        final int indexIdPrg = c.getColumnIndex("idProgram");
        final int indexPosWorkout = c.getColumnIndex("posWorkout");
        final int indexStartDate = c.getColumnIndex("startDate");
        final int indexDuration = c.getColumnIndex("duration");
        final int indexComment = c.getColumnIndex("comment");

        do {
            long idPrg = c.getLong(indexIdPrg);
            int posWorkout = c.getInt(indexPosWorkout);
            Workout w = new Workout(getDescriptionFor(idPrg) , posWorkout);
            w.id = c.getLong(indexID);
            w.date = c.getLong(indexStartDate);
            w.duration = c.getLong(indexDuration);
            w.comment = c.getString(indexComment);

            list.add(w);
        } while (c.moveToNext());
        return list;
    }

    private DescriptionProgram getDescriptionFor(long id) {
        StringBuilder sb = getClearStringBuilder();
        sb.append("SELECT descriptionProgram._id, descriptionProgram.name, descriptionProgram.description " +
                "FROM descriptionProgram WHERE descriptionProgram._id = ").append(id);

        programBuilder.setDb(db);
        programBuilder.buildObjects(sb.toString());
        ArrayList<?> list = programBuilder.getObjects();
        return (DescriptionProgram) list.get(0);
    }
}

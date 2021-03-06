package ru.kadei.diaryworkouts.builders;

import android.database.Cursor;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 30.08.15.
 */
public class ProgramReader extends DescriptionReader {

    private final DescriptionReader workoutReader;

    public ProgramReader(BufferDescriptions bufferDescriptions) {
        this(bufferDescriptions, createStringBuilder());
    }

    public ProgramReader(BufferDescriptions bufferDescriptions, StringBuilder sb) {
        super(bufferDescriptions, sb);
        this.workoutReader = new WorkoutReader(bufferDescriptions, sb);
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public ArrayList<?> buildFromCursor(Cursor c) {
        final DescriptionReader reader = workoutReader;
        final BufferDescriptions buffer = bufferDescriptions;
        final ArrayList<DescriptionProgram> list = new ArrayList<>(c.getCount());

        final int indexID = c.getColumnIndex("_id");
        final int indexName = c.getColumnIndex("name");
        final int indexDescription = c.getColumnIndex("description");

        reader.setDb(db);
        do {
            long id = c.getLong(indexID);
            DescriptionProgram dp = buffer.getProgram(id);
            if (dp == null) {
                dp = new DescriptionProgram();
                dp.id = id;
                dp.name = c.getString(indexName);
                dp.description = c.getString(indexDescription);

                reader.setQuery(createQueryFor(dp.id));
                reader.readObjects();
                dp.workouts = (ArrayList<DescriptionWorkout>) reader.getObjects();

                buffer.addProgram(dp);
            }

            list.add(dp);
        } while (c.moveToNext());
        reader.forgetReferenceDB();

        return list;
    }

    private String createQueryFor(long id) {
        return query("SELECT descriptionWorkout._id, descriptionWorkout.name, descriptionWorkout.description " +
                "FROM descriptionWorkout, listDescriptionWorkout " +
                "WHERE listDescriptionWorkout.idProgram = ").append(id).append(
                " AND descriptionWorkout._id = listDescriptionWorkout.idWorkout " +
                        "ORDER BY listDescriptionWorkout.orderInList;").toString();
    }
}

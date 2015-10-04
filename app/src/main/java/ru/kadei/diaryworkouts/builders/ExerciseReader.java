package ru.kadei.diaryworkouts.builders;

import android.database.Cursor;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionStandardExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionSupersetExercise;

import static ru.kadei.diaryworkouts.models.workouts.DescriptionExercise.SUPERSET;

/**
 * Created by kadei on 01.09.15.
 */
public class ExerciseReader extends DescriptionReader {

    public ExerciseReader(BufferDescriptions bufferDescriptions) {
        super(bufferDescriptions);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<DescriptionExercise> buildFromCursor(Cursor c) {
        final BufferDescriptions buffer = bufferDescriptions;
        final ArrayList<DescriptionExercise> list = new ArrayList<>(c.getCount());

        final int indexID = c.getColumnIndex("_id");
        final int indexName = c.getColumnIndex("name");
        final int indexDescription = c.getColumnIndex("description");
        final int indexType = c.getColumnIndex("type");
        final int indexMeasureSpec = c.getColumnIndex("measureSpec");
        final int indexMuscleGroupSpec = c.getColumnIndex("muscleGroupSpec");

        do {
            long id = c.getLong(indexID);
            DescriptionExercise de = buffer.getExercise(id);
            if (de == null) {
                int type = c.getInt(indexType);
                if (type == SUPERSET) {
                    DescriptionSupersetExercise superExe = new DescriptionSupersetExercise();
                    superExe.id = id;
                    superExe.name = c.getString(indexName);
                    superExe.description = c.getString(indexDescription);
                    superExe.type = type;

                    this.readObjects(createQueryFor(superExe.id));
                    superExe.exercises = (ArrayList<DescriptionStandardExercise>) this.getObjects();
                    de = superExe;
                } else {
                    DescriptionStandardExercise stdExe = new DescriptionStandardExercise();
                    stdExe.id = id;
                    stdExe.name = c.getString(indexName);
                    stdExe.description = c.getString(indexDescription);
                    stdExe.type = type;
                    stdExe.setMeasureSpec(c.getInt(indexMeasureSpec));
                    stdExe.setMuscleGroupSpec(c.getInt(indexMuscleGroupSpec));
                    de = stdExe;
                }
                buffer.addExercise(de);
            }
            list.add(de);
        } while (c.moveToNext());
        return list;
    }

    private String createQueryFor(long id) {
        return query("SELECT descriptionExercise._id, descriptionExercise.name, descriptionExercise.description, " +
                "descriptionExercise.type, descriptionExercise.measureSpec, descriptionExercise.muscleGroupSpec " +
                "FROM descriptionExercise, listContentSuperset " +
                "WHERE listContentSuperset.idSuperset = ").append(id).append(
                " AND descriptionExercise._id = listContentSuperset.idExercise " +
                        "ORDER BY listContentSuperset.orderInList;").toString();
    }
}

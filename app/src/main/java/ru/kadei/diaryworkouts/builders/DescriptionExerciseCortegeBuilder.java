package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.CortegeBuilder;
import ru.kadei.diaryworkouts.database.Cortege;
import ru.kadei.diaryworkouts.database.Relation;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionStandardExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionSupersetExercise;

/**
 * Created by kadei on 06.09.15.
 */
public class DescriptionExerciseCortegeBuilder extends CortegeBuilder {
    @Override
    public void buildCortegeFor(Object object) {
        if(object instanceof DescriptionExercise)
            cortege = buildCortege((DescriptionExercise) object);
        else
            oops(object);
    }

    private Cortege buildCortege(DescriptionExercise exercise) {
        final Cortege cortege = new Cortege();
        cortege.nameTable = "descriptionExercise";

        ContentValues cv = new ContentValues(5);
        cv.put("name", exercise.name);
        cv.put("description", exercise.description);
        cv.put("type", exercise.type);
        cv.put("measureSpec", exercise.getMeasureSpec());
        cv.put("muscleGroupSpec", exercise.getMuscleGroupSpec());
        cortege.values = cv;

        if(exercise.isSuperset()) {
            DescriptionSupersetExercise superset = (DescriptionSupersetExercise) exercise;
            cortege.relations.add(buildRelationsExercisesAnd(superset));
        }

        return cortege;
    }

    private Relation buildRelationsExercisesAnd(DescriptionSupersetExercise superset) {
        final ArrayList<DescriptionStandardExercise> exercises = superset.exercises;
        final int size = exercises.size();
        final ArrayList<ContentValues> values = new ArrayList<>(size);

        final long idSuperset = superset.id;
        for(int pos = 0; pos < size; ++pos) {
            long idExercise = exercises.get(pos).id;

            ContentValues cv = new ContentValues(3);
            cv.put("idSuperset", idSuperset);
            cv.put("idExercise", idExercise);
            cv.put("orderInList", pos);

            values.add(cv);
        }

        Relation relation = new Relation();
        relation.nameTable = "listContentSuperset";
        relation.idTarget = idSuperset;
        relation.columnIdTarget = "idSuperset";
        relation.values = values;
        return relation;
    }
}

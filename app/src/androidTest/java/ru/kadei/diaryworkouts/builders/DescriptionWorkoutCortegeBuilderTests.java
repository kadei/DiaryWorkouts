package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.ApplicationTest;
import ru.kadei.diaryworkouts.database.CortegeBuilder;
import ru.kadei.diaryworkouts.database.Cortege;
import ru.kadei.diaryworkouts.database.Relation;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionStandardExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 04.09.15.
 */
public class DescriptionWorkoutCortegeBuilderTests extends ApplicationTest {

    static final long[] idsExercise = new long[] {
            11l,
            22l,
            33l,
            44l
    };

    public void testCreateCortege() throws Exception {

        DescriptionWorkout workout = createWorkout(
                55l, "my first workout", "description for workout");

        CortegeBuilder builder = new DescriptionWorkoutCortegeBuilder();
        builder.buildCortegeFor(workout);
        Cortege cortege = builder.getCortege();

        assertNameTableFor(cortege);
        assertValuesFor(cortege);
        assertRelationFor(cortege);
    }

    static void assertNameTableFor(Cortege cortege) {
        assertEquals(cortege.nameTable, "descriptionWorkout");
    }

    static void assertValuesFor(Cortege cortege) {
        assertEquals(cortege.values.size(), 2);
        assertEquals(cortege.values.get("name"), "my first workout");
        assertEquals(cortege.values.get("description"), "description for workout");
    }

    static void assertRelationFor(Cortege cortege) {
        final ArrayList<Relation> relations = cortege.relations;
        for (Relation r : relations) {
            assertEquals(r.nameTable, "listDescriptionExercise");
            assertEquals(r.idTarget, 55l);
            assertEquals(r.columnIdTarget, "idWorkout");
            assertEquals(r.values.size(), 4);
            for (int i = 0; i < r.values.size(); ++i) {
                ContentValues cv = r.values.get(i);
                assertEquals(cv.size(), 3);
                assertEquals(cv.get("idWorkout"), 55l);
                assertEquals(cv.get("idExercise"), idsExercise[i]);
                assertEquals(cv.get("orderInList"), i);
            }
        }
    }

    static DescriptionWorkout createWorkout(long id, String name, String description) {
        ArrayList<DescriptionExercise> exercises = new ArrayList<>(idsExercise.length);
        for (long anIdsWorkout : idsExercise) {
            exercises.add(createDescriptionExercise(anIdsWorkout));
        }
        DescriptionWorkout dw = new DescriptionWorkout();
        dw.id = id;
        dw.name = name;
        dw.description = description;
        dw.exercises = exercises;
        return dw;
    }

    static DescriptionExercise createDescriptionExercise(long id) {
        DescriptionExercise dw = new DescriptionStandardExercise();
        dw.id = id;
        return dw;
    }
}

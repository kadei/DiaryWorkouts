package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.ApplicationTest;
import ru.kadei.diaryworkouts.database.Cortege;
import ru.kadei.diaryworkouts.database.DatabaseWriter;
import ru.kadei.diaryworkouts.database.Relation;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

import static ru.kadei.diaryworkouts.models.workouts.DescriptionExercise.newStandardExercise;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout.newWorkout;

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

//        DescriptionWorkout workout = newWorkout(
//                55l, "my first workout", "description for workout", 4);
//
//        for (long id : idsExercise) {
//            workout.exercises.add(newStandardExercise(id, null, null, 0, 0, 0));
//        }
//
//        DatabaseWriter writer = new DescriptionWorkoutWriter();
//        writer.writeObject(workout);
//        Cortege cortege = writer.getCortege();
//
//        assertNameTableFor(cortege);
//        assertValuesFor(cortege);
//        assertRelationFor(cortege);
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
}

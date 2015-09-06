package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.ApplicationTest;
import ru.kadei.diaryworkouts.database.CortegeBuilder;
import ru.kadei.diaryworkouts.database.Cortege;
import ru.kadei.diaryworkouts.database.Relation;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionStandardExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionSupersetExercise;

import static ru.kadei.diaryworkouts.models.workouts.DescriptionExercise.BASE;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionExercise.ISOLATED;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionExercise.SUPERSET;
import static ru.kadei.diaryworkouts.models.workouts.Measure.DISTANCE;
import static ru.kadei.diaryworkouts.models.workouts.Measure.REPEAT;
import static ru.kadei.diaryworkouts.models.workouts.Measure.SPEED;

/**
 * Created by kadei on 06.09.15.
 */
public class DescriptionExerciseCortegeBuilderTests extends ApplicationTest {

    static final long[] idsExercise = new long[] {
            3l,
            4l
    };

    static final int[] measures = new int[] {
            REPEAT,
            DISTANCE | SPEED
    };

    public void testCreateCortegeForStandard() throws Exception {

        DescriptionExercise exercise = createStandardExercise(
                155l, "my first exercise", "description for exercise",
                ISOLATED, DISTANCE | SPEED, 123);

        CortegeBuilder builder = new DescriptionExerciseCortegeBuilder();
        builder.buildCortegeFor(exercise);
        Cortege cortege = builder.getCortege();

        assertNameTableFor(cortege);
        assertStandardValuesFor(cortege);
        assertEquals(cortege.relations.isEmpty(), true);
    }

    static void assertNameTableFor(Cortege cortege) {
        assertEquals(cortege.nameTable, "descriptionExercise");
    }

    static void assertStandardValuesFor(Cortege cortege) {
        assertEquals(cortege.values.size(), 5);
        assertEquals(cortege.values.get("name"), "my first exercise");
        assertEquals(cortege.values.get("description"), "description for exercise");
        assertEquals(cortege.values.get("type"), ISOLATED);
        assertEquals(cortege.values.get("measureSpec"), DISTANCE | SPEED);
        assertEquals(cortege.values.get("muscleGroupSpec"), 123);
    }

    public void testCreateCotegeForSuperset() throws Exception {
        DescriptionExercise exercise = createSupersetExercise(555l, "my first superset", "description for superset");

        CortegeBuilder builder = new DescriptionExerciseCortegeBuilder();
        builder.buildCortegeFor(exercise);
        Cortege cortege = builder.getCortege();

        assertNameTableFor(cortege);
        assertSupersetValuesFor(cortege);
        assertRelationFor(cortege);
    }

    static void assertSupersetValuesFor(Cortege cortege) {
        assertEquals(cortege.values.size(), 5);
        assertEquals(cortege.values.get("name"), "my first superset");
        assertEquals(cortege.values.get("description"), "description for superset");
        assertEquals(cortege.values.get("type"), SUPERSET);
        assertEquals(cortege.values.get("measureSpec"), REPEAT | DISTANCE | SPEED);
        assertEquals(cortege.values.get("muscleGroupSpec"), 222);
    }

    static void assertRelationFor(Cortege cortege) {
        final ArrayList<Relation> relations = cortege.relations;
        for (Relation r : relations) {
            assertEquals(r.nameTable, "listContentSuperset");
            assertEquals(r.idTarget, 555l);
            assertEquals(r.columnIdTarget, "idSuperset");
            assertEquals(r.values.size(), 2);
            for (int i = 0; i < r.values.size(); ++i) {
                ContentValues cv = r.values.get(i);
                assertEquals(cv.size(), 3);
                assertEquals(cv.get("idSuperset"), 555l);
                assertEquals(cv.get("idExercise"), idsExercise[i]);
                assertEquals(cv.get("orderInList"), i);
            }
        }
    }

    static DescriptionStandardExercise createStandardExercise(long id, String name, String description,
                                                      int type, int measureSpec, int muscleGroupSpec) {
        DescriptionStandardExercise std = new DescriptionStandardExercise();
        std.id = id;
        std.name = name;
        std.description = description;
        std.type = type;
        std.setMeasureSpec(measureSpec);
        std.setMuscleGroupSpec(muscleGroupSpec);
        return std;
    }

    static DescriptionSupersetExercise createSupersetExercise(long id, String name, String description) {
        DescriptionSupersetExercise superExe = new DescriptionSupersetExercise();
        superExe.id = id;
        superExe.name = name;
        superExe.description = description;
        superExe.type = SUPERSET;
        superExe.exercises = new ArrayList<>(idsExercise.length);
        for(int i = 0; i < idsExercise.length; ++i) {
            long idExe = idsExercise[i];
            int measure = measures[i];
            DescriptionStandardExercise std = createStandardExercise(idExe, null, null, BASE, measure, 222);
            superExe.exercises.add(std);
        }
        return superExe;
    }
}

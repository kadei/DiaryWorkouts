package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.ApplicationTest;
import ru.kadei.diaryworkouts.database.CortegeBuilder;
import ru.kadei.diaryworkouts.database.Cortege;
import ru.kadei.diaryworkouts.database.Relation;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 04.09.15.
 */
public class DescriptionProgramCortegeBuilderTests extends ApplicationTest {

    static final long[] idsWorkout = new long[] {
            2l,
            3l,
            7l
    };

    public void testCreateCortege() throws Exception {

        DescriptionProgram program = createDescriptionProgram(
                23l, "my first program", "description for program");

        CortegeBuilder builder = new DescriptionProgramCortegeBuilder();
        builder.buildCortegeFor(program);
        Cortege cortege = builder.getCortege();

        assertNameTableFor(cortege);
        assertValuesFor(cortege);
        assertRelationFor(cortege);
    }

    static void assertNameTableFor(Cortege cortege) {
        assertEquals(cortege.nameTable, "descriptionProgram");
    }

    static void assertValuesFor(Cortege cortege) {
        assertEquals(cortege.values.size(), 2);
        assertEquals(cortege.values.get("name"), "my first program");
        assertEquals(cortege.values.get("description"), "description for program");
    }

    static void assertRelationFor(Cortege cortege) {
        final ArrayList<Relation> relations = cortege.relations;
        for (Relation r : relations) {
            assertEquals(r.nameTable, "listDescriptionWorkout");
            assertEquals(r.idTarget, 23l);
            assertEquals(r.columnIdTarget, "idProgram");
            for (int i = 0; i < r.values.size(); ++i) {
                ContentValues cv = r.values.get(i);
                assertEquals(cv.size(), 3);
                assertEquals(cv.get("idProgram"), 23l);
                assertEquals(cv.get("idWorkout"), idsWorkout[i]);
                assertEquals(cv.get("orderInList"), i);
            }
        }
    }

    static DescriptionProgram createDescriptionProgram(long id, String name, String description) {
        ArrayList<DescriptionWorkout> workouts = new ArrayList<>(idsWorkout.length);
        for (long anIdsWorkout : idsWorkout) {
            workouts.add(createDescriptionWorkout(anIdsWorkout));
        }
        DescriptionProgram dp = new DescriptionProgram();
        dp.id = id;
        dp.name = name;
        dp.description = description;
        dp.workouts = workouts;
        return dp;
    }

    static DescriptionWorkout createDescriptionWorkout(long id) {
        DescriptionWorkout dw = new DescriptionWorkout();
        dw.id = id;
        return dw;
    }
}

package ru.kadei.diaryworkouts.database;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.ApplicationTest;

/**
 * Created by kadei on 23.08.15.
 */
public class ParserOfEntityTests extends ApplicationTest {

    ParserOfEntities parser;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        parser = ParserOfEntities.newParser(getContext());
    }

    public void testParseEmptyFile() throws Exception {

        try {
            ArrayList<String> list = parser.getNameEntitiesFrom("entitiesEmpty.xml");
            assertEquals(toArray(list), new String[]{});
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    public void testParseFile() throws Exception {

        try {
            ArrayList<String> list = parser.getNameEntitiesFrom("entities.xml");

            String[] names = new String[] {"ru.kadei.diaryworkouts.models.workouts.Description",
                    "ru.kadei.diaryworkouts.models.workouts.DescriptionProgram",
                    "ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout",
                    "ru.kadei.diaryworkouts.models.workouts.DescriptionStandardExercise",
                    "ru.kadei.diaryworkouts.models.workouts.DescriptionSupersetExercise"};

            assertEquals(toArray(list), names);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
}

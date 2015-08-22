package ru.kadei.diaryworkouts.database;

import ru.kadei.diaryworkouts.ApplicationTest;

/**
 * Created by kadei on 21.08.15.
 */
public class DatabaseTests extends ApplicationTest {

    DatabaseManager db;

    public void testLoadEntities() throws Exception {

        db = new DatabaseManager(getContext(), "");

        String[] names = new String[] {"ru.kadei.diaryworkouts.models.workouts.Description",
                "ru.kadei.diaryworkouts.models.workouts.DescriptionProgram",
                "ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout",
                "ru.kadei.diaryworkouts.models.workouts.DescriptionStandardExercise",
                "ru.kadei.diaryworkouts.models.workouts.DescriptionSupersetExercise"};

        assertEquals(toArray(db.getNameEntities()), names);
    }
}

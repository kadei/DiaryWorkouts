package ru.kadei.diaryworkouts.database;

import junit.framework.Assert;

import ru.kadei.diaryworkouts.ApplicationTest;

/**
 * Created by kadei on 21.08.15.
 */
public class DatabaseTests extends ApplicationTest {

    DatabaseManager db;

    public void testLoadEntitiesWithDuplicate() throws Exception {
        try {
            db = new DatabaseManager(getContext(), "entitiesWithDuplicate.xml", "my2.db");
            fail("Should throw Exception about duplicate entities");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "File [entitiesWithDuplicate.xml] contains duplicates " +
                    "[ru.kadei.diaryworkouts.models.workouts.DescriptionSupersetExercise]");
            // successful
        }
    }
}

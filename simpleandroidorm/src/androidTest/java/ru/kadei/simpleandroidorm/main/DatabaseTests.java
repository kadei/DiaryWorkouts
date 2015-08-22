package ru.kadei.simpleandroidorm.main;

import ru.kadei.simpleandroidorm.ApplicationTest;
import ru.kadei.simpleandroidorm.models.SimpleEntity;
import ru.kadei.simpleandroidorm.models.SimpleEntity2;

/**
 * Created by kadei on 20.08.15.
 */
public class DatabaseTests extends ApplicationTest {

    Database db;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        db = new Database(getContext());
    }

    public void testAnnotations() throws Exception {

        SimpleEntity se = new SimpleEntity();
        SimpleEntity2 se2 = new SimpleEntity2();

//        try {
//            db.save(se);
//        } catch (Exception e) {
//            fail("Most be not throw exception");
//        }
//
//        try {
//            db.save(se2);
//            fail("se2 most throw exception");
//        } catch (Exception e) {
//            // ignore
//        }
    }
}

package ru.kadei.diaryworkouts.database.utils;

import junit.framework.Assert;

import ru.kadei.diaryworkouts.ApplicationTest;

/**
 * Created by kadei on 23.08.15.
 */
public class ArrayUtilsTests extends ApplicationTest {

    public void testSearchDuplicate() throws Exception {
        String[] numbers = {"one", "two", "three"};
        int posDuplicate = ArrayUtils.containsDuplicate(numbers);
        Assert.assertEquals(posDuplicate, -1);

        numbers = new String[]{"one", "two", "three", "one"};
        posDuplicate = ArrayUtils.containsDuplicate(numbers);
        Assert.assertEquals(posDuplicate, 0);
    }
}

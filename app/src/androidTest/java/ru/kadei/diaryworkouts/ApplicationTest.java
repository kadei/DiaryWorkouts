package ru.kadei.diaryworkouts;

import android.app.Application;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import java.util.List;

import ru.kadei.diaryworkouts.database.utils.ArrayUtils;
import ru.kadei.diaryworkouts.database.utils.ArrayUtilsTests;

import static junit.framework.Assert.assertEquals;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testAll() {
    }

    public static String[] toArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    public static void assertEquals(int[] expected, int[] actual) {
        assertLengths(expected.length, actual.length);
        final int len = expected.length;
        for(int i = 0; i < len; ++i) {
            Assert.assertEquals("Value at index <" +i+ "> not equals", expected[i], actual[i]);
        }
    }

    public static void assertEquals(String[] expected, String[] actual) {
        assertLengths(expected.length, actual.length);
        final int len = expected.length;
        for(int i = 0; i < len; ++i) {
            Assert.assertEquals("Value at index <" +i+ "> not equals", expected[i], actual[i]);
        }
    }

    private static void assertLengths(int expectedLen, int actualLen) {
        Assert.assertEquals("Length of arrays should be equals", expectedLen, actualLen);
    }
}
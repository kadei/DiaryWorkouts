package ru.kadei.diaryworkouts;

import android.app.Application;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import static junit.framework.Assert.assertEquals;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    public static void assertEquals(int[] expected, int[] actual) {
        Assert.assertEquals("Length of arrays should be equals", expected.length, actual.length);
        final int len = expected.length;
        for(int i = 0; i < len; ++i) {
            Assert.assertEquals("Value at index <" +i+ "> not equals", expected[i], actual[i]);
        }
    }
}
package ru.kadei.diaryworkouts.utils;

import junit.framework.Assert;

import ru.kadei.diaryworkouts.ApplicationTest;

import static ru.kadei.diaryworkouts.utils.StringUtils.extractNameClass;

/**
 * Created by kadei on 22.08.15.
 */
public class StringUtilsTests extends ApplicationTest {

    public void testExtractNameClass() throws Exception {

        Class c = StringUtilsTests.class;
        String result = extractNameClass(c);
        Assert.assertEquals(result, "StringUtilsTests");

        String fullName = ".WWW";
        result = extractNameClass(fullName);
        Assert.assertEquals(result, "WWW");

        fullName = "WWW";
        result = extractNameClass(fullName);
        Assert.assertEquals(result, "WWW");

        fullName = "WWW.";
        result = extractNameClass(fullName);
        Assert.assertEquals(result, "");

        fullName = "";
        result = extractNameClass(fullName);
        Assert.assertEquals(result, "");

        fullName = "..ww..a.WWW";
        result = extractNameClass(fullName);
        Assert.assertEquals(result, "WWW");
    }
}

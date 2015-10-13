package ru.kadei.diaryworkouts.builders;

import junit.framework.Assert;

import ru.kadei.diaryworkouts.ApplicationTest;

/**
 * Created by kadei on 11.10.15.
 */
public class DateEventWriterTest extends ApplicationTest {

    public void testGetGTM() throws Exception {

        Assert.assertEquals(DateEventWriter.getGMT(), "GMT+3");
    }
}

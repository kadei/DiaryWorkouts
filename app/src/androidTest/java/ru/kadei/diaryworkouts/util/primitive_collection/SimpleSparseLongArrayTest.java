package ru.kadei.diaryworkouts.util.primitive_collection;

import junit.framework.Assert;

import ru.kadei.diaryworkouts.ApplicationTest;

/**
 * Created by kadei on 04.10.15.
 */
public class SimpleSparseLongArrayTest extends ApplicationTest {

    public void testPut() throws Exception {

        SimpleSparseLongArray arr = new SimpleSparseLongArray(0);

        arr.put(234L, 234L);
        arr.put(123L, 123L);
        arr.put(345L, 345L);

        Assert.assertEquals(123L, arr.get(123L, -1L));
        Assert.assertEquals(234L, arr.get(234L, -1L));
        Assert.assertEquals(345L, arr.get(345L, -1L));
        Assert.assertEquals(-1L, arr.get(999L, -1L));
    }

    public void testRemove() throws Exception {
        SimpleSparseLongArray arr = new SimpleSparseLongArray(0);

        arr.put(234L, 234L);
        arr.put(123L, 123L);
        arr.put(345L, 345L);

        Assert.assertEquals(3, arr.size());
        Assert.assertEquals(123L, arr.get(123L, -1L));
        Assert.assertEquals(234L, arr.get(234L, -1L));
        Assert.assertEquals(345L, arr.get(345L, -1L));

        arr.removeAt(0);
        Assert.assertEquals(2, arr.size());
        Assert.assertEquals(-1L, arr.get(123L, -1L));
        Assert.assertEquals(234L, arr.get(234L, -1L));
        Assert.assertEquals(345L, arr.get(345L, -1L));

        arr.delete(345L);
        Assert.assertEquals(1, arr.size());
        Assert.assertEquals(-1L, arr.get(123L, -1L));
        Assert.assertEquals(234L, arr.get(234L, -1L));
        Assert.assertEquals(-1L, arr.get(345L, -1L));

        arr.removeAt(0);
        Assert.assertEquals(0, arr.size());
        Assert.assertEquals(-1L, arr.get(123L, -1L));
        Assert.assertEquals(-1L, arr.get(234L, -1L));
        Assert.assertEquals(-1L, arr.get(345L, -1L));
    }

    public void testIndex() throws Exception {

        SimpleSparseLongArray arr = new SimpleSparseLongArray(0);

        arr.put(234L, 234L);
        arr.put(123L, 123L);
        arr.put(345L, 345L);

        Assert.assertEquals(0, arr.indexOfKey(123L));
        Assert.assertEquals(1, arr.indexOfKey(234L));
        Assert.assertEquals(2, arr.indexOfKey(345L));

        Assert.assertEquals(0, arr.indexOfValue(123L));
        Assert.assertEquals(1, arr.indexOfValue(234L));
        Assert.assertEquals(2, arr.indexOfValue(345L));

        arr.put(255L, 255L);

        Assert.assertEquals(0, arr.indexOfKey(123L));
        Assert.assertEquals(1, arr.indexOfKey(234L));
        Assert.assertEquals(2, arr.indexOfKey(255L));
        Assert.assertEquals(3, arr.indexOfKey(345L));

        Assert.assertEquals(0, arr.indexOfValue(123L));
        Assert.assertEquals(1, arr.indexOfValue(234L));
        Assert.assertEquals(2, arr.indexOfValue(255L));
        Assert.assertEquals(3, arr.indexOfValue(345L));
    }
}

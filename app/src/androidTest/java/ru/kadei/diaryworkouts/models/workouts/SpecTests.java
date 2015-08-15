package ru.kadei.diaryworkouts.models.workouts;

import junit.framework.Assert;

import ru.kadei.diaryworkouts.ApplicationTest;

/**
 * Created by kadei on 15.08.15.
 */
public class SpecTests extends ApplicationTest {

    public void testCountActiveFlags() throws Exception {

        Spec spec = new Spec();
        spec.spec = -1;

        int count = spec.countActiveFlags();
        Assert.assertEquals(count, 32);

        spec.spec = 0;
        count = spec.countActiveFlags();
        Assert.assertEquals(count, 0);

        spec.spec = 0xf000;
        count = spec.countActiveFlags();
        Assert.assertEquals(count, 4);
    }

    public void testExtract() throws Exception {

        Spec spec = new Spec();

        spec.spec = 0xf;
        int[] actual = new int[]{1, 2, 4, 8};
        int[] values = spec.extract();
        assertEquals(values, actual);

        spec.spec = 0;
        actual = new int[]{};
        values = spec.extract();
        assertEquals(values, actual);

        spec.spec = -1;
        actual = new int[]{1,2,4,8,16,32,64,128,256,512,1024,2048,4096,8192,16384,32768,
                65536,131072,262144,524288,1048576,2097152,4194304,8388608,16777216,33554432,67108864,134217728,
                268435456,536870912,1073741824,Integer.MIN_VALUE
        };
        values = spec.extract();
        assertEquals(values, actual);
    }

    public void testExtractInto() throws Exception {

        Spec s = new Spec();
    }
}

package ru.kadei.diaryworkouts.models.workouts;

import ru.kadei.diaryworkouts.ApplicationTest;

/**
 * Created by kadei on 10.09.15.
 */
public class DescriptionExerciseTests extends ApplicationTest {

    public void testExtractSpec() throws Exception {

        Measure measure = new Measure();

        int spec = 0;
        measure.extractSpec(spec);
        assertEquals(measure.getCount(), 0);
        assertEquals(measure.getExtractedValues(), new int[32]);

        spec = -1;
        measure.extractSpec(spec);
        assertEquals(measure.getCount(), 32);
        assertEquals(measure.getExtractedValues(), new int[]{
                1,2,4,8,16,32,64,128,256,512,
                1024,2048,4096,8192,16384,32768,
                65536,131072,262144,524288,1048576,2097152,
                4194304,8388608,16777216,33554432,67108864,
                134217728,268435456,536870912,1073741824,Integer.MIN_VALUE
        });
    }
}

package ru.kadei.diaryworkouts.models.workouts;

import junit.framework.Assert;

import ru.kadei.diaryworkouts.ApplicationTest;

import static ru.kadei.diaryworkouts.models.workouts.Measure.DISTANCE;
import static ru.kadei.diaryworkouts.models.workouts.Measure.REPEAT;
import static ru.kadei.diaryworkouts.models.workouts.Measure.SPEED;
import static ru.kadei.diaryworkouts.models.workouts.Measure.TIME;
import static ru.kadei.diaryworkouts.models.workouts.Measure.WEIGHT;

/**
 * Created by kadei on 15.08.15.
 */
public class SetTests extends ApplicationTest {

    public void testGetValueOfMeasure() throws Exception {

        Set set = new Set();

        set.setValueOfMeasure(0.1f, REPEAT);
        float expected = set.getValueOfMeasure(REPEAT);
        Assert.assertEquals(expected, 0.1f);

        set.setValueOfMeasure(0.5f, WEIGHT);
        expected = set.getValueOfMeasure(WEIGHT);
        Assert.assertEquals(expected, 0.5f);

        set.setValueOfMeasure(1f, SPEED);
        expected = set.getValueOfMeasure(SPEED);
        Assert.assertEquals(expected, 1f);

        set.setValueOfMeasure(1.5f, DISTANCE);
        expected = set.getValueOfMeasure(DISTANCE);
        Assert.assertEquals(expected, 1.5f);

        set.setValueOfMeasure(2f, TIME);
        expected = set.getValueOfMeasure(TIME);
        Assert.assertEquals(expected, 2f);
    }
}

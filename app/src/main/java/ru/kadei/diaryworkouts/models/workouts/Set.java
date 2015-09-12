package ru.kadei.diaryworkouts.models.workouts;

import ru.kadei.diaryworkouts.database.Record;

import static ru.kadei.diaryworkouts.models.workouts.Measure.MEASURE_AMOUNT;
import static ru.kadei.diaryworkouts.models.workouts.Measure.DISTANCE;
import static ru.kadei.diaryworkouts.models.workouts.Measure.REPEAT;
import static ru.kadei.diaryworkouts.models.workouts.Measure.SPEED;
import static ru.kadei.diaryworkouts.models.workouts.Measure.DURATION;
import static ru.kadei.diaryworkouts.models.workouts.Measure.WEIGHT;

/**
 * Created by kadei on 15.08.15.
 */
public class Set extends Record {

    public boolean cheat;
    public String comment;
    private float[] values;

    private static final int[] measures = new int[] {
            WEIGHT, REPEAT, SPEED, DISTANCE, DURATION
    };

    public Set() {
        cheat = false;
        comment = null;
        values = new float[MEASURE_AMOUNT];
    }

    public void setValueOfMeasure(float value, int measure) {
        values[computeIndex(measure)] = value;
    }

    public float getValueOfMeasure(int measure) {
        return values[computeIndex(measure)];
    }

    public static int computeIndex(int measure) {
        final int[] m = measures;
        for(int index = 0; index < m.length; ++index) {
            if (m[index] == measure)
                return index;
        }
        return -1;
    }

    public void resetValues() {
        final float[] v = values;
        for(int i = 0; i < v.length; ++i)
            v[i] = 0f;
        cheat = false;
    }
}

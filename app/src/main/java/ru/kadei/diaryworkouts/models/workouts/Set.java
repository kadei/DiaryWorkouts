package ru.kadei.diaryworkouts.models.workouts;

import static ru.kadei.diaryworkouts.models.workouts.MeasureSpec.MEASURE_AMOUNT;
import static ru.kadei.diaryworkouts.models.workouts.MeasureSpec.DISTANCE;
import static ru.kadei.diaryworkouts.models.workouts.MeasureSpec.REPEAT;
import static ru.kadei.diaryworkouts.models.workouts.MeasureSpec.SPEED;
import static ru.kadei.diaryworkouts.models.workouts.MeasureSpec.TIME;
import static ru.kadei.diaryworkouts.models.workouts.MeasureSpec.WEIGHT;

/**
 * Created by kadei on 15.08.15.
 */
public class Set {

    public boolean cheat;
    private float[] values;

    private static final int[] measures = new int[] {
            WEIGHT, REPEAT,
            SPEED, DISTANCE,
            TIME};

    public Set() {
        values = new float[MEASURE_AMOUNT];
        cheat = false;
    }

    public void setValueOfMeasure(float value, int measure) {
        values[computeIndex(measure)] = value;
    }

    public float getValueOfMeasure(int measure) {
        return values[computeIndex(measure)];
    }

    private int computeIndex(int measure) {
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

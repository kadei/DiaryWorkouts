package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public class Spec {

    public int spec = 0;
    private static final int[] extractedValues = new int[32];

    public Spec(int spec) {
        this.spec = spec;
    }

    public int[] extract() {
        int count = _extract();
        int[] values = new int[count];
        System.arraycopy(extractedValues, 0, values, 0, count);
        return values;
    }

    public int extractInto(int[] destination) {
        int count = _extract();
        System.arraycopy(extractedValues, 0, destination, 0, count);
        return count;
    }

    public int countActiveFlags() {
        return _extract();
    }

    private int _extract() {
        int count = 0;
        final int spec = this.spec;
        final int[] values = extractedValues;

        for(int bit = 0; bit < 32; ++bit) {
            int mask = 1 << bit;
            if((spec & mask) != 0) {
                values[count++] = mask;
            }
        }
        return count;
    }
}

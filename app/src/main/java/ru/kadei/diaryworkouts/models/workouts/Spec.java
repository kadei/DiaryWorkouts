package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public class Spec {

    private int spec = 0;
    private final int[] extractedValues = new int[32];
    private int count = 0;

    public int getSpec() {
        return spec;
    }

    public int[] getExtractedValues() {
        return extractedValues;
    }

    public int get(int index) {
        return extractedValues[index];
    }

    public void extractSpec(int spec) {
        this.spec = spec;
        final int[] dest = extractedValues;
        int count = 0;
        int mask = 1;
        for(int i = 0; i < 32; ++i) {
            int val = spec & mask;
            if(val != 0)
                dest[count++] = val;
            mask <<= 1;
        }
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}

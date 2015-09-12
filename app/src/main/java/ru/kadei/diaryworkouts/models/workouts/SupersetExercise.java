package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

/**
 * Created by kadei on 12.09.15.
 */
public class SupersetExercise extends Exercise {

    public SupersetExercise(DescriptionExercise info, ArrayList<Set> sets) {
        super(info, sets);
    }

    @Override
    public ArrayList<Set> getSet(int pos) {
        final int exerciseCount = info.getExerciseCount();
        final ArrayList<Set> src = this.sets;
        final ArrayList<Set> dest = new ArrayList<>(exerciseCount);

        int count = pos * exerciseCount;
        for (int i = 0; i < exerciseCount; ++i) {
            dest.add(src.get(count++));
        }
        return dest;
    }

    @Override
    public void setSet(int pos, ArrayList<Set> set) {

    }

    @Override
    public int getCountSet() {
        return sets.size() / info.getExerciseCount();
    }
}

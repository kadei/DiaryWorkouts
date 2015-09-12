package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

/**
 * Created by kadei on 12.09.15.
 */
public class StandardExercise extends Exercise {
    public StandardExercise(DescriptionExercise info, ArrayList<Set> sets) {
        super(info, sets);
    }

    @Override
    public ArrayList<Set> getSet(int pos) {
        ArrayList<Set> wrapper = new ArrayList<>(1);
        wrapper.add(sets.get(pos));
        return wrapper;
    }

    @Override
    public void setSet(int pos, ArrayList<Set> set) {

    }

    @Override
    public int getCountSet() {
        return sets.size();
    }
}

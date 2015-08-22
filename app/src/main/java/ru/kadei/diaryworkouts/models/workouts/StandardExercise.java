package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

/**
 * Created by kadei on 15.08.15.
 */
public class StandardExercise extends Exercise {

    private ArrayList<Set> sets;

    public StandardExercise(DescriptionExercise descriptionExercise) {
        super(descriptionExercise);
        sets = new ArrayList<>(8);
    }

    @Override
    public Exercise getExercise(int pos) {
        return this;
    }

    @Override
    public Spec getMeasureSpec(int posExercise) {
        return null;
    }

    @Override
    public int countExercises() {
        return 1;
    }

    @Override
    public int countSet() {
        return sets.size();
    }
}

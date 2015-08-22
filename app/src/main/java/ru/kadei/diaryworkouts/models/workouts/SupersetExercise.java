package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

/**
 * Created by kadei on 15.08.15.
 */
public class SupersetExercise extends Exercise {

    ArrayList<StandardExercise> exercises;

    public SupersetExercise(DescriptionExercise descriptionExercise) {
        super(descriptionExercise);
        exercises = new ArrayList<>(2);
    }

    @Override
    public Exercise getExercise(int pos) {
        return exercises.get(pos);
    }

    @Override
    public Spec getMeasureSpec(int posExercise) {
        return null;
    }

    @Override
    public int countExercises() {
        return exercises.size();
    }

    @Override
    public int countSet() {
        return exercises.isEmpty() ? 0 : exercises.get(0).countSet();
    }
}
